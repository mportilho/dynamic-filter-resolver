package com.github.dfr.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.github.dfr.apptest.TestingApplication;
import com.github.dfr.apptest.domain.model.Person;
import com.github.dfr.apptest.repository.PersonRepository;
import com.github.dfr.decoder.ValueConverter;
import com.github.dfr.decoder.type.Equals;
import com.github.dfr.decoder.type.GreaterOrEquals;
import com.github.dfr.filter.ParameterFilterMetadata;
import com.github.dfr.provider.specification.decoder.SpecificationDecoderService;
import com.github.dfr.provider.specification.decoder.SpecificationValueConverter;
import com.github.dfr.provider.specification.decoder.type.DefaultSpecificationDecoderService;
import com.github.dfr.provider.specification.filter.DefaultSpecificationParameterFilter;
import com.github.dfr.provider.specification.filter.SpecificationParameterFilter;

@DataJpaTest
@ContextConfiguration(classes = TestingApplication.class)
public class TestProgramEntries {

	@Autowired
	private PersonRepository personRepo;

	@Test
	public void test() {
		SpecificationDecoderService<Person> decoderService = new DefaultSpecificationDecoderService<>();
		ValueConverter valueConverter = new SpecificationValueConverter();

		List<ParameterFilterMetadata> parameters = new ArrayList<>();
		parameters.add(new ParameterFilterMetadata("name", new String[0], "name", String.class, GreaterOrEquals.class, "Fulano"));
		parameters.add(new ParameterFilterMetadata("height", new String[0], "height", BigDecimal.class, Equals.class, BigDecimal.valueOf(3)));
		parameters.add(new ParameterFilterMetadata("city", new String[0], "addresses.location.city", String.class, Equals.class, "Belem"));
		parameters.add(new ParameterFilterMetadata("streetName", new String[0], "addresses.street", String.class, Equals.class, "rua"));

		SpecificationParameterFilter<Person> parameterFilter = new DefaultSpecificationParameterFilter<>(decoderService, valueConverter);

		List<Person> list = personRepo.findAll(parameterFilter.convertTo(parameters));
		System.out.println(list);
	}

}
