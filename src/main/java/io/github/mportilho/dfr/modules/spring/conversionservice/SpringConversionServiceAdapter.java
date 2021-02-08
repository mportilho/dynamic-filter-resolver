package io.github.mportilho.dfr.modules.spring.conversionservice;

import java.util.Objects;

import org.springframework.core.convert.ConversionService;

import io.github.mportilho.dfr.core.converter.FormattedConversionService;

public class SpringConversionServiceAdapter implements FormattedConversionService {

	private final ConversionService conversionService;

	public SpringConversionServiceAdapter(ConversionService conversionService) {
		this.conversionService = Objects.requireNonNull(conversionService, "Spring ConversionService is required");
	}

	@Override
	public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
		return conversionService.canConvert(sourceType, targetType);
	}

	@Override
	public <T> T convert(Object source, Class<T> targetType) {
		return conversionService.convert(source, targetType);
	}

}
