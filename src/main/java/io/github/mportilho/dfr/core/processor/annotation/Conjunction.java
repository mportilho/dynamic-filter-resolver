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

package io.github.mportilho.dfr.core.processor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines a set of clauses and logic statements using 'AND' as the main logic
 * operation.
 *
 * @author Marcelo Portilho
 */
@Documented
@Retention(RUNTIME)
@Target({PARAMETER, TYPE})
public @interface Conjunction {

    /**
     * @return An array of clauses joined by AND logic operation
     */
    Filter[] value() default {};

    /**
     * Defines a set of statements where clauses of each statement are joined by OR
     * operation. At the end, the statements are joined by the AND operation,
     * following this logic:
     *
     * <p>
     * (<code>clause1</code> <b>AND</b> <code>clause2</code>) <b>AND</b>
     * [disjunction1(<code>clause3</code> <b>OR</b> <code>clause4</code>) <b>AND</b>
     * disjunction2(<code>clause5</code> <b>OR</b> <code>clause6</code>) ]
     *
     * @return An array of statements joined by AND operation
     */
    Statement[] disjunctions() default {};

    /**
     * @return Indicates that the whole conjunction result is negated
     */
    String negate() default "false";

}
