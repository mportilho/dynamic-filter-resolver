package net.dfr.provider.commons;

import java.util.Objects;

public class Pair<L, R> {

	private final L left;
	private final R right;

	private Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public static final <L, R> Pair<L, R> of(L left, R right) {
		return new Pair<L, R>(left, right);
	}

	public static final <L, R> Pair<L, R> empty() {
		return new Pair<L, R>(null, null);
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
		Pair<?, ?> other = (Pair<?, ?>) obj;
		return Objects.equals(left, other.left) && Objects.equals(right, other.right);
	}

}
