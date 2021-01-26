package com.github.dfr.provider.specification.decoder.type;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

import org.springframework.data.mapping.PropertyPath;

import com.github.dfr.filter.ParameterFilterMetadata;

class PredicateUtils {

	/**
	 * 
	 * @param metadata
	 * @param root
	 * @return
	 */
	public static final Path<?> computeAttributePath(ParameterFilterMetadata metadata, Root<?> root) {
		String pathToField = getPropertyPathFromMetadata(metadata);
		PropertyPath propertyPath = PropertyPath.from(pathToField, root.getJavaType());
		From<?, ?> from = root;
		boolean graphFromFetch = false;
		if (propertyPath.isCollection()) {
			do {
				Fetch<?, ?> fetchAttribute = getFetchAttribute(from, propertyPath.getSegment());
				if (fetchAttribute != null) {
					from = (From<?, ?>) fetchAttribute;
					graphFromFetch = true;
				} else if (graphFromFetch) {
					from = (From<?, ?>) from.fetch(propertyPath.getSegment(), JoinType.LEFT);
				} else {
					from = getOrCreateJoin(from, propertyPath.getSegment(), metadata.findStateOrDefault(JoinType.class, JoinType.INNER));
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

	/**
	 * 
	 * @param metadata
	 * @return
	 */
	public static final String getPropertyPathFromMetadata(ParameterFilterMetadata metadata) {
		return metadata.getPath() != null && !metadata.getPath().isEmpty() ? metadata.getPath() : metadata.getName();
	}

}
