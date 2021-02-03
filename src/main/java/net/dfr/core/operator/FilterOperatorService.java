package net.dfr.core.operator;

public interface FilterOperatorService<T> {

	FilterOperator<T> getOperatorFor(Class<? extends FilterOperator<?>> operator);

}
