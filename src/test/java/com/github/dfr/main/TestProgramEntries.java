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
import com.github.dfr.decoder.ParameterConverter;
import com.github.dfr.decoder.type.Equals;
import com.github.dfr.decoder.type.GreaterOrEquals;
import com.github.dfr.filter.ParameterFilterMetadata;
import com.github.dfr.provider.specification.decoder.SpecificationFilterDecoderService;
import com.github.dfr.provider.specification.decoder.SpecificationParameterConverter;
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
		SpecificationFilterDecoderService<Person> decoderService = new DefaultSpecificationDecoderService<>();
		ParameterConverter parameterConverter = new SpecificationParameterConverter();

		List<ParameterFilterMetadata> parameters = new ArrayList<>();
		parameters.add(new ParameterFilterMetadata("name", new String[0], "name", String.class, GreaterOrEquals.class, "Fulano", null));
		parameters.add(new ParameterFilterMetadata("height", new String[0], "height", BigDecimal.class, Equals.class, BigDecimal.valueOf(3), null));
		parameters.add(new ParameterFilterMetadata("city", new String[0], "addresses.location.city", String.class, Equals.class, "Belem", null));
		parameters.add(new ParameterFilterMetadata("streetName", new String[0], "addresses.street", String.class, Equals.class, "rua", null));

		SpecificationParameterFilter<Person> parameterFilter = new DefaultSpecificationParameterFilter<>(decoderService, parameterConverter);

		List<Person> list = personRepo.findAll(parameterFilter.convertTo(parameters));
		System.out.println(list);
	}

}
