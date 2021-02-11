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

package io.github.mportilho.dfr.core.operator;

import io.github.mportilho.dfr.core.operator.type.EndsWith;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.operator.type.Greater;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.operator.type.Less;
import io.github.mportilho.dfr.core.operator.type.LessOrEquals;
import io.github.mportilho.dfr.core.operator.type.Like;
import io.github.mportilho.dfr.core.operator.type.NotEquals;
import io.github.mportilho.dfr.core.operator.type.NotLike;
import io.github.mportilho.dfr.core.operator.type.StartsWith;

/**
 * Comparison operations used on dynamic filter operator
 * 
 * @author Marcelo Portilho
 *
 */
public enum ComparisonOperator {

	EQ(Equals.class), //
	NE(NotEquals.class), //
	LT(Less.class), //
	LE(LessOrEquals.class), //
	GT(Greater.class), //
	GE(GreaterOrEquals.class), //
	LK(Like.class), //
	NL(NotLike.class), //
	SW(StartsWith.class), //
	EW(EndsWith.class)//
	;

	@SuppressWarnings("rawtypes")
	private Class<? extends FilterOperator> operator;

	@SuppressWarnings("rawtypes")
	private ComparisonOperator(Class<? extends FilterOperator> operator) {
		this.operator = operator;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends FilterOperator<T>> getOperator() {
		return (Class<? extends FilterOperator<T>>) operator;
	}

}
