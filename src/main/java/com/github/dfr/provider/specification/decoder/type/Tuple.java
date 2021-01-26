package com.github.dfr.provider.specification.decoder.type;

import java.util.Objects;

class Tuple<L, R> {

	private final L left;
	private final R right;

	private Tuple(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public static final <L, R> Tuple<L, R> of(L left, R right) {
		return new Tuple<L, R>(left, right);
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tuple<?, ?> other = (Tuple<?, ?>) obj;
		return Objects.equals(left, other.left) && Objects.equals(right, other.right);
	}

}
