package net.dfr.providers.specification.web;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import net.dfr.core.annotation.Conjunction;
import net.dfr.core.annotation.Disjunction;
import net.dfr.core.statement.ConditionalStatement;
import net.dfr.core.statement.DefaultConditionalStatementProvider;
import net.dfr.core.statement.ValueExpressionResolver;
import net.dfr.providers.specification.annotation.Fetching;
import net.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;

public class SpecificationDynamicFilterArgumentResolver implements HandlerMethodArgumentResolver {

	private DefaultConditionalStatementProvider conditionalStatementProvider;
	private SpecificationDynamicFilterResolver<?> dynamicFilterResolver;

	public SpecificationDynamicFilterArgumentResolver(StringValueResolver stringValueResolver,
			SpecificationDynamicFilterResolver<?> dynamicFilterResolver) {
		ValueExpressionResolver valueExpressionResolver = stringValueResolver != null ? (value -> stringValueResolver.resolveStringValue(value))
				: null;
		this.conditionalStatementProvider = new DefaultConditionalStatementProvider(valueExpressionResolver);
		this.dynamicFilterResolver = dynamicFilterResolver;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> parameterType = parameter.getParameterType();
		if (!parameterType.isInterface() || !Specification.class.isAssignableFrom(parameterType)) {
			return false;
		}
		int countAnnotations = countAnnotationsUntilTwo(parameter);
		if (countAnnotations > 1) {
			throw new IllegalStateException(String.format(
					"Multiples annotations of Conjunction and/or Disjunction cannot be used at the same time, as seen on parameter %s of class '%s",
					parameter.getParameterName(), parameter.getDeclaringClass()));
		}
		return countAnnotations == 1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		Map<Object, Object[]> providedParameterValuesMap = createProvidedValuesMap(parameter, webRequest);

		ConditionalStatement statement = conditionalStatementProvider.createConditionalStatements(
				(Class<Specification<?>>) parameter.getParameterType(), parameter.getParameterAnnotations(), providedParameterValuesMap);

		return (statement != null && statement.hasAnyCondition()) ? dynamicFilterResolver.convertTo(statement) : Specification.where(null);
	}

	/**
	 * 
	 * @param parameter
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Object, Object[]> createProvidedValuesMap(MethodParameter parameter, NativeWebRequest webRequest) {
		Map<Object, Object[]> providedParameterValuesMap = new HashMap<>();

		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		providedParameterValuesMap.putAll(webRequest.getParameterMap());
		providedParameterValuesMap.putAll((Map<String, String[]>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));

		Annotation[] anns = parameter.getParameterAnnotations();
		if (anns != null && anns.length > 0) {
			Annotation[] filteredAnnos = Stream.of(anns).filter(ann -> Fetching.class.isInstance(ann))
					.collect(collectingAndThen(toList(), l -> l.toArray(new Annotation[l.size()])));
			if (filteredAnnos != null && filteredAnnos.length > 0) {
				providedParameterValuesMap.put(Fetching.class, filteredAnnos);
			}
		}

		return providedParameterValuesMap;
	}

	/**
	 * 
	 * @param parameter
	 */
	private int countAnnotationsUntilTwo(MethodParameter parameter) {
		int count = 0;
		for (Annotation annotation : parameter.getParameterAnnotations()) {
			if (annotation.annotationType().equals(Conjunction.class) || annotation.annotationType().equals(Disjunction.class)) {
				count++;
			}
			if (count > 1) {
				return count;
			}
		}
		return count;
	}

}
