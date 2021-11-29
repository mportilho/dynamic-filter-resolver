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

package io.github.mportilho.dfr.modules.springjpa.webautoconfigure;

import io.github.mportilho.dfr.converters.FormattedConversionService;
import org.springframework.core.convert.ConversionService;

import java.util.Objects;

/**
 * Adapts a {@link FormattedConversionService} to the Spring Framework's
 * {@link ConversionService}
 *
 * @author Marcelo Portilho
 */
public class SpringFormattedConversionServiceAdapter implements FormattedConversionService {

    private final ConversionService conversionService;
    private final FormattedConversionService delegate;

    public SpringFormattedConversionServiceAdapter(ConversionService conversionService, FormattedConversionService delegate) {
        this.conversionService = Objects.requireNonNull(conversionService, "Spring ConversionService is required");
        this.delegate = delegate;
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        boolean hasConversionOp = conversionService.canConvert(sourceType, targetType);
        if (!hasConversionOp && delegate != null) {
            return delegate.canConvert(sourceType, targetType);
        }
        return hasConversionOp;
    }

    @Override
    public <S, T, F> T convert(S source, Class<T> targetType, F format) {
        if (conversionService.canConvert(source.getClass(), targetType)) {
            return conversionService.convert(source, targetType);
        }
        return delegate.convert(source, targetType, format);
    }

}
