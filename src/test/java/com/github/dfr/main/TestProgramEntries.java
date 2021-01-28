package com.github.dfr.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import com.github.dfr.apptest.TestingApplication;
import com.github.dfr.apptest.domain.model.Person;
import com.github.dfr.apptest.repository.PersonRepository;
import com.github.dfr.decoder.FilterDecoderService;
import com.github.dfr.decoder.ParameterValueConverter;
import com.github.dfr.decoder.type.Equals;
import com.github.dfr.decoder.type.GreaterOrEquals;
import com.github.dfr.filter.CorrelatedFilterParameter;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.FilterParameterResolver;
import com.github.dfr.filter.LogicType;
import com.github.dfr.filter.LogicalContext;
import com.github.dfr.provider.specification.decoder.DefaultParameterValueConverter;
import com.github.dfr.provider.specification.decoder.type.DefaultSpecificationDecoderService;
import com.github.dfr.provider.specification.filter.SpecificationFilterParameterResolver;

@DataJpaTest
@ContextConfiguration(classes = TestingApplication.class)
public class TestProgramEntries {

	@Autowired
	private PersonRepository personRepo;

	@Test
	public void test() {
		FilterDecoderService<Specification<Person>> decoderService = new DefaultSpecificationDecoderService<>();
		ParameterValueConverter parameterValueConverter = new DefaultParameterValueConverter();

		List<FilterParameter> parameters = new ArrayList<>();
		parameters.add(new FilterParameter("name", "clientName", String.class, GreaterOrEquals.class, "Fulano", null));
		parameters.add(new FilterParameter("height", "clientHeight", BigDecimal.class, Equals.class, BigDecimal.valueOf(3), null));
		parameters.add(new FilterParameter("addresses.location.city", "birthCity", String.class, Equals.class, "Belem", null));
		parameters.add(new FilterParameter("addresses.street", "address", String.class, Equals.class, "rua", null));
		parameters.add(new FilterParameter("phones.number", "phoneNumber", String.class, Equals.class, "1345", null));

		LogicalContext logicWrapper = new LogicalContext(LogicType.CONJUNCTION, new CorrelatedFilterParameter(LogicType.CONJUNCTION, parameters),
				Collections.emptyList());

		FilterParameterResolver<Specification<Person>> parameterFilter = new SpecificationFilterParameterResolver<>(decoderService,
				parameterValueConverter);

		List<Person> list = personRepo.findAll(parameterFilter.convertTo(logicWrapper));
		System.out.println(list);
	}

}
