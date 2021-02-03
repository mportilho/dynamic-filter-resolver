package net.dfr.operator;

public interface FilterOperatorService<T> {

	FilterOperator<T> getOperatorFor(Class<? extends FilterOperator<?>> operator);

}
