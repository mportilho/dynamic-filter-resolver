package net.dfr.core.modules.springdocs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Disjunction;
import net.dfr.core.annotation.Filter;
import net.dfr.core.annotation.Statement;

public class DynamicFilterOperationCustomizer implements OperationCustomizer {

	protected static Set<String> NOT_NULL_ANNOTATIONS = new HashSet<>(Arrays.asList("NotNull", "NonNull", "NotBlank", "NotEmpty"));

	private DynamicFilterOperationCustomizer() {
	}

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
			String parameterName = getParameterName(methodParameter);
			List<Filter> parameterAnnotations = retrieveSpecificationParameterAnnotations(methodParameter);
			if (!parameterAnnotations.isEmpty()) {
				operation.getParameters().removeIf(p -> p.getName().equals(parameterName));
				for (Filter spec : parameterAnnotations) {
					try {
						customizeParameter(operation, methodParameter, spec);
					} catch (Exception e) {
						throw new IllegalStateException("Cannot build custom OpenAPI parameters from specification-args-resolver", e);
					}
				}
			}
		}
		return operation;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void customizeParameter(Operation operation, MethodParameter methodParameter, Filter spec) throws Exception {
		ParameterizedType parameterizedType = (ParameterizedType) methodParameter.getParameter().getParameterizedType();
		Class<?> parameterizedClassType = Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName());

		Field field = getPropertyField(parameterizedClassType, spec.path());
		Class<?> fieldClass = field.getType();

		JsonView jsonView = getJsonViewFromMethod(methodParameter);
		Schema tempSchema = AnnotationsUtils.resolveSchemaFromType(fieldClass, null, jsonView);

		for (String parameterName : spec.parameters()) {
			Optional<io.swagger.v3.oas.models.parameters.Parameter> optParameter = operation.getParameters().stream()
					.filter(p -> p.getName().equals(parameterName)).findFirst();
			boolean parameterExists = optParameter.isPresent();

			io.swagger.v3.oas.models.parameters.Parameter parameter = optParameter.orElse(new io.swagger.v3.oas.models.parameters.Parameter());
			parameter.setName(parameterName);

			Optional<Schema> optSchema = Optional.ofNullable(parameter.getSchema());
			boolean schemaExists = optSchema.isPresent();
			Schema schema = optSchema.orElse(new Schema<>());

			if (schemaExists) {
				schema.setType(tempSchema.getType());
				schema.setEnum(tempSchema.getEnum());
			} else {
				schema = tempSchema;
				parameter.setSchema(schema);
			}
			applyBeanValidatorAnnotations(schema, field.getAnnotations(), null);

			if (parameterExists) {
				if (parameter.getIn() == null || ParameterIn.DEFAULT.toString().equals(parameter.getIn())) {
					parameter.setIn(ParameterIn.QUERY.toString());
				}
			} else {
				parameter.setIn(ParameterIn.QUERY.toString());
				parameter.setRequired(false);
				operation.getParameters().add(parameter);
			}
		}
	}

	private static JsonView getJsonViewFromMethod(MethodParameter methodParameter) {
		JsonView[] jsonViews = methodParameter.getMethod().getAnnotationsByType(JsonView.class);
		JsonView jsonView = null;
		if (jsonViews != null && jsonViews.length > 0) {
			jsonView = jsonViews[0];
		}
		return jsonView;
	}

	private static final String getParameterName(MethodParameter methodParameter) {
		String name = null;
		Parameter parameter = methodParameter.getParameter();
		io.swagger.v3.oas.annotations.Parameter parameterAnnotation = parameter.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class);
		if (parameterAnnotation != null) {
			name = parameterAnnotation.name();
		} else {
			name = parameter.getName();
		}
		return name;
	}

	private static final List<Filter> retrieveSpecificationParameterAnnotations(MethodParameter methodParameter) {
		if (!Specification.class.isAssignableFrom(methodParameter.getParameterType())) {
			return Collections.emptyList();
		}
		Annotation[] annotations = methodParameter.getParameterAnnotations();
		List<Filter> specs = new ArrayList<>();
		if (annotations != null) {
			for (Annotation annotation : annotations) {
				specs.addAll(composeSpecsFromParameterConfiguration(annotation));
			}
		}
		return specs;
	}

	private static final List<Filter> composeSpecsFromParameterConfiguration(Annotation annotation) {
		List<Filter> specsList = new ArrayList<>();
		if (Conjunction.class.isAssignableFrom(annotation.getClass())) {
			Conjunction ann = (Conjunction) annotation;
			specsList.addAll(Arrays.asList(ann.value()));
			specsList.addAll(Stream.of(ann.disjunctions()).flatMap(v -> Stream.of(v.value())).collect(Collectors.toList()));
		} else if (Disjunction.class.isAssignableFrom(annotation.getClass())) {
			Disjunction ann = (Disjunction) annotation;
			specsList.addAll(Arrays.asList(ann.value()));
			specsList.addAll(Stream.of(ann.conjunctions()).flatMap(v -> Stream.of(v.value())).collect(Collectors.toList()));
		} else if (Statement.class.isAssignableFrom(annotation.getClass())) {
			Statement ann = (Statement) annotation;
			specsList.addAll(Arrays.asList(ann.value()));
		}
		return specsList;
	}

	private static void applyBeanValidatorAnnotations(Schema<?> property, Annotation[] annotations, Schema<?> parent) {
		Map<String, Annotation> annos = new HashMap<>();
		for (Annotation anno : annotations) {
			annos.put(anno.annotationType().getName(), anno);
		}
		if (parent != null && annotations != null) {
			boolean requiredItem = Arrays.stream(annotations)
					.anyMatch(annotation -> NOT_NULL_ANNOTATIONS.contains(annotation.annotationType().getSimpleName()));
			if (requiredItem) {
				setAsRequiredItem(parent, property.getName());
			}
		}
		if (annos.containsKey("javax.validation.constraints.Min")) {
			if ("integer".equals(property.getType()) || "number".equals(property.getType())) {
				Min min = (Min) annos.get("javax.validation.constraints.Min");
				property.setMinimum(new BigDecimal(min.value()));
			}
		}
		if (annos.containsKey("javax.validation.constraints.Max")) {
			if ("integer".equals(property.getType()) || "number".equals(property.getType())) {
				Max max = (Max) annos.get("javax.validation.constraints.Max");
				property.setMaximum(new BigDecimal(max.value()));
			}
		}
		if (annos.containsKey("javax.validation.constraints.Size")) {
			Size size = (Size) annos.get("javax.validation.constraints.Size");
			if ("integer".equals(property.getType()) || "number".equals(property.getType())) {
				property.setMinimum(new BigDecimal(size.min()));
				property.setMaximum(new BigDecimal(size.max()));
			} else if (property instanceof StringSchema) {
				StringSchema sp = (StringSchema) property;
				sp.minLength(Integer.valueOf(size.min()));
				sp.maxLength(Integer.valueOf(size.max()));
			} else if (property instanceof ArraySchema) {
				ArraySchema sp = (ArraySchema) property;
				sp.setMinItems(size.min());
				sp.setMaxItems(size.max());
			}
		}
		if (annos.containsKey("javax.validation.constraints.DecimalMin")) {
			DecimalMin min = (DecimalMin) annos.get("javax.validation.constraints.DecimalMin");
			if (property instanceof NumberSchema) {
				NumberSchema ap = (NumberSchema) property;
				ap.setMinimum(new BigDecimal(min.value()));
				ap.setExclusiveMinimum(!min.inclusive());
			}
		}
		if (annos.containsKey("javax.validation.constraints.DecimalMax")) {
			DecimalMax max = (DecimalMax) annos.get("javax.validation.constraints.DecimalMax");
			if (property instanceof NumberSchema) {
				NumberSchema ap = (NumberSchema) property;
				ap.setMaximum(new BigDecimal(max.value()));
				ap.setExclusiveMaximum(!max.inclusive());
			}
		}
		if (annos.containsKey("javax.validation.constraints.Pattern")) {
			Pattern pattern = (Pattern) annos.get("javax.validation.constraints.Pattern");
			if (property instanceof StringSchema) {
				property.setPattern(pattern.regexp());
			}
		}
	}

	private static void setAsRequiredItem(Schema<?> model, String propName) {
		if (model == null || propName == null || StringUtils.isBlank(propName)) {
			return;
		}
		if (model.getRequired() == null || model.getRequired().isEmpty()) {
			model.addRequiredItem(propName);
		}
		if (model.getRequired().stream().noneMatch(propName::equals)) {
			model.addRequiredItem(propName);
		}
	}

	private static Field getPropertyField(Class<?> clazz, String fieldName) {
		final String[] fieldNames = fieldName.split("\\.", -1);
		// if using dot notation to navigate for classes
		if (fieldNames.length > 1) {
			final String firstProperty = fieldNames[0];
			final String otherProperties = StringUtils.join(fieldNames, '.', 1, fieldNames.length);
			final Field firstPropertyType = getPropertyField(clazz, firstProperty);
			return getPropertyField(firstPropertyType.getType(), otherProperties);
		}

		try {
			return clazz.getDeclaredField(fieldName);
		} catch (final NoSuchFieldException e) {
			if (!clazz.equals(Object.class)) {
				return getPropertyField(clazz.getSuperclass(), fieldName);
			}
			throw new IllegalStateException(e);
		}
	}

}
