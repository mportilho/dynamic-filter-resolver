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

package io.github.mportilho.dfr.core.statement.annontation;

import java.lang.annotation.Annotation;
import java.util.Map;

import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Disjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.annotation.Statement;
import io.github.mportilho.dfr.core.statement.ConditionalStatement;
import io.github.mportilho.dfr.core.statement.ConditionalStatementProvider;

/**
 * A conditional statement provider that compose statements from annotations
 * 
 * @author Marcelo Portilho
 * @see Conjunction
 * @see Disjunction
 * @see Filter
 * @see Statement
 *
 */
public interface AnnotationConditionalStatementProvider extends ConditionalStatementProvider {

	/**
	 * Creates {@link ConditionalStatement} representations from types and
	 * annotations
	 * 
	 * @param <K>                  Map key type
	 * @param <V>                  Map value type
	 * @param parameterInterface   Class from which filter configuration will be
	 *                             extracted
	 * @param parameterAnnotations Annotations from which filter configuration will
	 *                             be extracted
	 * @param parametersMap        Map containing provided values for filter
	 *                             operations
	 * @return Conditional statement representation
	 */
	<K, V> ConditionalStatement createConditionalStatements(Class<?> parameterInterface, Annotation[] parameterAnnotations,
			Map<K, V[]> parametersMap);

}
