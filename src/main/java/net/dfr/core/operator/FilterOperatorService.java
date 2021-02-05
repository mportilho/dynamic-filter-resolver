package net.dfr.core.operator;

public interface FilterOperatorService<T> {

	<R extends FilterOperator<T>> R getOperatorFor(Class<?> operator);

}
