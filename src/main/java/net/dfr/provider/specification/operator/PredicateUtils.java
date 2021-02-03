package net.dfr.provider.specification.operator;

import java.util.Map;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

import org.springframework.data.mapping.PropertyPath;

import net.dfr.filter.FilterParameter;
import net.dfr.provider.specification.annotation.Fetches;
import net.dfr.provider.specification.annotation.FetchingMode;

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
		boolean graphFromFetch = false;

		Map<String, FetchingMode> fetchingMap = filterParameter.findState(Fetches.class);

		if (propertyPath.isCollection()) {
			do {
				Fetch<?, ?> fetchAttribute = getFetchAttribute(from, propertyPath.getSegment());
				if (fetchAttribute != null) {
					from = (From<?, ?>) fetchAttribute;
					graphFromFetch = true;
				} else if (graphFromFetch) {
					from = (From<?, ?>) from.fetch(propertyPath.getSegment(), filterParameter.findStateOrDefault(JoinType.class, JoinType.LEFT));
				} else {
					from = getOrCreateJoin(from, propertyPath.getSegment(), filterParameter.findStateOrDefault(JoinType.class, JoinType.INNER));
				}
				propertyPath = propertyPath.next();
			} while (propertyPath.hasNext());
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
	public static final Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute, JoinType joinType) {
		for (Join<?, ?> join : from.getJoins()) {
			boolean sameName = join.getAttribute().getName().equals(attribute);
			if (sameName && join.getJoinType().equals(joinType)) {
				return join;
			}
		}
		return from.join(attribute, joinType);
	}

	/**
	 * Return whether the given {@link From} contains a fetch declaration for the
	 * attribute with the given name.
	 *
	 * @param from      the {@link From} to check for fetches.
	 * @param attribute the attribute name to check.
	 * @return
	 */
	private static Fetch<?, ?> getFetchAttribute(From<?, ?> from, String attribute) {
		for (Fetch<?, ?> fetch : from.getFetches()) {
			boolean sameName = fetch.getAttribute().getName().equals(attribute);
			if (sameName && fetch.getJoinType().equals(JoinType.LEFT)) {
				return fetch;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param from
	 * @param attribute
	 * @param joinType
	 * @return
	 */
	public static final Join<?, ?> getJoinByAttribute(From<?, ?> from, String attribute, JoinType joinType) {
		for (Join<?, ?> join : from.getJoins()) {
			boolean sameName = join.getAttribute().getName().equals(attribute);
			if (sameName && join.getJoinType().equals(joinType)) {
				return join;
			}
		}
		return null;
	}

}
