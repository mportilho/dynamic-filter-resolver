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

package io.github.mportilho.dfr.mocks.interfaces.queries;

import io.github.mportilho.dfr.core.processor.annotation.Conjunction;
import io.github.mportilho.dfr.core.processor.annotation.Filter;
import io.github.mportilho.dfr.core.operation.type.*;

@Conjunction(value = {
        @Filter(path = "addresses.number", parameters = "houseNumber", operation = Equals.class, defaultValues = "123"),
        @Filter(path = "name", parameters = "clientName", operation = NotEquals.class, defaultValues = "Anonymous"),
        @Filter(path = "height", parameters = "height", operation = Greater.class, defaultValues = "170"),
        @Filter(path = "addresses.number", parameters = "houseNumber", operation = GreaterOrEquals.class, defaultValues = "1010"),
        @Filter(path = "weight", parameters = "weight", operation = Less.class, defaultValues = "70"),
        @Filter(path = "registerDate", parameters = "registerDate", operation = LessOrEquals.class, defaultValues = "05/03/2018"),
        @Filter(path = "birthday", parameters = {"startDate", "endDate"}, operation = Between.class, defaultValues = {"01/01/1980", "01/01/2000"})
})
public interface ComparisonOperations {

}
