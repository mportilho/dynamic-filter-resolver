/*MIT License

Copyright (c) 2021 Marcelo Portilho

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

package io.github.mportilho.dfr.providers.specification.operator;

import java.util.function.BiFunction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;

import org.springframework.data.mapping.PropertyPath;

import io.github.mportilho.dfr.core.filter.FilterParameter;

/**
 * Helper class for manipulating JPA Criteria's objects
 * 
 * @author Marcelo Portilho
 *
 */
class PredicateUtils {

	/**
	 * Helps calling comparison methods of {@link CriteriaBuilder} on generic type
	 * objects
	 * 
	 * @param path
	 * @param value
	 * @param comparablePredicateFunction
	 * @param numberPredicateFunction
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static final Predicate toComparablePredicate(Expression<?> path, Object value,
			BiFunction<Expression<? extends Comparable<Object>>, Comparable<Object>, Predicate> comparablePredicateFunction,
			BiFunction<Expression<Number>, Number, Predicate> numberPredicateFunction) {
		if (value != null && Number.class.isAssignableFrom(value.getClass())) {
			return numberPredicateFunction.apply((Expression<Number>) path, (Number) value);
		}
		return comparablePredicateFunction.apply((Expression<? extends Comparable<Object>>) path, (Comparable<Object>) value);
	}

	/**
	 * Obtains the actual JPA Critetia's {@link Path} object to the desired path
	 * defined on {@link FilterParameter} object. Creates joins automatically when
	 * navigating through entities.
	 * 
	 * @param <T>
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
