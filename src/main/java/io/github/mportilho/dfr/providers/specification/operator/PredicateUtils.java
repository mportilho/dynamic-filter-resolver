package io.github.mportilho.dfr.providers.specification.operator;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

import org.springframework.data.mapping.PropertyPath;

import io.github.mportilho.dfr.core.filter.FilterParameter;

class PredicateUtils {

	/**
	 * 
	 * @param filterParameter
	 * @param root
	 * @return
	 */
	public static final <T> Path<T> computeAttributePath(FilterParameter filterParameter, Root<?> root) {
		PropertyPath propertyPath = PropertyPath.from(filterParameter.getPath(), root.getJavaType());
		From<?, ?> from = root;
		if (propertyPath == null) {
			throw new IllegalArgumentException(
					String.format("No path '%s' found for type '%s'", filterParameter.getPath(), from.getJavaType().getSimpleName()));
		}
		while (propertyPath.hasNext()) {
			from = getOrCreateJoin(from, propertyPath.getSegment(), filterParameter.findStateOrDefault(JoinType.class, JoinType.INNER));
			propertyPath = propertyPath.next();
		}
		return from.get(propertyPath.getSegment());
	}

	/**
	 * Returns an existing join for the given attribute if one already exists or
	 * creates a new one if not.
	 *
	 * @param from      the {@link From} to get the current joins from.
	 * @param attribute the {@link Attribute} to look for in the current joins.
	 * @return will never be {@literal null}.
	 */
	private static final Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute, JoinType joinType) {
		for (Join<?, ?> join : from.getJoins()) {
			boolean sameName = join.getAttribute().getName().equals(attribute);
			if (sameName && join.getJoinType().equals(joinType)) {
				return join;
			}
		}
		return from.join(attribute, joinType);
	}

}
