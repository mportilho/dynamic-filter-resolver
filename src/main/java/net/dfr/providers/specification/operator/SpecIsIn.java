package net.dfr.providers.specification.operator;

import static net.dfr.providers.specification.operator.PredicateUtils.computeAttributePath;

import java.util.Collection;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.type.IsIn;

class SpecIsIn<T> implements IsIn<Specification<T>> {

	@Override
	public Specification<T> createFilter(FilterParameter filterParameter, FilterValueConverter filterValueConverter) {
		return (root, query, criteriaBuilder) -> {
			Path<?> path = computeAttributePath(filterParameter, root);
			Object[] rawValues = extractArrayFromParameter(filterParameter.getValues());
			Predicate predicate = null;

			if (rawValues != null) {
				Object[] value = new Object[rawValues.length];
				for (int i = 0; i < value.length; i++) {
					value[i] = filterValueConverter.convert(rawValues[i], path.getJavaType(), filterParameter.findFormat());
				}
				predicate = path.in(value);
			}
			return predicate;
		};
	}

	/**
	 * 
	 * @param paramValues
	 * @return
	 */
	private Object[] extractArrayFromParameter(Object[] paramValues) {
		if (paramValues == null || paramValues.length == 0) {
			return null;
		} else if (!paramValues[0].getClass().isArray() && !Collection.class.isAssignableFrom(paramValues[0].getClass())) {
			throw new IllegalArgumentException("Expecting parameter value to be a collection of elements for 'in' operation");
		} else if (paramValues[0].getClass().isArray()) {
			return (Object[]) paramValues[0];
		}
		return ((Collection<?>) paramValues[0]).toArray();
	}

}
