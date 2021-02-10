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

package io.github.mportilho.dfr.core.statement.interfaces.queries;

import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.EndsWith;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.operator.type.Like;
import io.github.mportilho.dfr.core.operator.type.NotEquals;
import io.github.mportilho.dfr.core.operator.type.NotLike;
import io.github.mportilho.dfr.core.operator.type.StartsWith;

@Disjunction(value = { 
		@Filter(path = "name", parameters = "clientName", operator = StartsWith.class, defaultValues = "A"),
		@Filter(path = "name", parameters = "clientName", operator = StartsWith.class, ignoreCase = true, defaultValues = "b"),
		
		@Filter(path = "addresses.location.city", parameters = "clientName", operator = EndsWith.class, defaultValues = "c"),
		@Filter(path = "addresses.location.city", parameters = "clientName", operator = EndsWith.class, ignoreCase = true, defaultValues = "d"),
		
		@Filter(path = "addresses.street", parameters = "clientName", operator = Equals.class, ignoreCase = true, defaultValues = "e"),
		@Filter(path = "addresses.street", parameters = "clientName", operator = NotEquals.class, ignoreCase = true, defaultValues = "f"),
		
		@Filter(path = "addresses.location.state", parameters = "clientName", operator = Like.class, defaultValues = "g"),
		@Filter(path = "addresses.location.state", parameters = "clientName", operator = Like.class, ignoreCase = true, defaultValues = "g"),
		
		@Filter(path = "addresses.number", parameters = "clientName", operator = NotLike.class, defaultValues = "i"),
		@Filter(path = "addresses.number", parameters = "clientName", operator = NotLike.class, ignoreCase = true, defaultValues = "j")
	}
)
public interface StringComparisonOperations {

}
