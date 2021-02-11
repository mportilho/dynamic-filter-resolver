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

package io.github.mportilho.dfr.core.converter;

import java.util.Objects;

/**
 * Represents a pair of values
 * 
 * @author Marcelo Portilho
 *
 * @param <L> The type of the left object
 * @param <R> The type of the right object
 */
class Pair<L, R> {

	private final L left;
	private final R right;

	private Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Creats a new pair
	 * 
	 * @param <L>   The type of the left object
	 * @param <R>   The type of the right object
	 * @param left  The value for the left position
	 * @param right The value for the right position
	 * @return A new Pair object with the provided values
	 */
	public static final <L, R> Pair<L, R> of(L left, R right) {
		return new Pair<L, R>(left, right);
	}

	/**
	 * Returns an empty pair, initialized with null values
	 * 
	 * @param <L> The type of the left object
	 * @param <R> The type of the right object
	 * @return An empty Pair object
	 */
	public static final <L, R> Pair<L, R> empty() {
		return new Pair<L, R>(null, null);
	}

	/**
	 * @return the left value of the pair
	 */
	public L getLeft() {
		return left;
	}

	/**
	 * @return the right value of the pair
	 */
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
