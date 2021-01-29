package com.github.dfr.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import com.github.dfr.apptest.TestingApplication;
import com.github.dfr.apptest.domain.model.Person;
import com.github.dfr.apptest.repository.PersonRepository;
import com.github.dfr.filter.ConditionalStatement;
import com.github.dfr.filter.DynamicFilterResolver;
import com.github.dfr.filter.FilterParameter;
import com.github.dfr.filter.LogicType;
import com.github.dfr.operator.FilterOperatorService;
import com.github.dfr.operator.FilterValueConverter;
import com.github.dfr.operator.type.Equals;
import com.github.dfr.operator.type.GreaterOrEquals;
import com.github.dfr.provider.commons.DefaultFilterValueConverter;
import com.github.dfr.provider.specification.filter.SpecificationDynamicFilterResolver;
import com.github.dfr.provider.specification.operator.SpecificationFilterOperatorService;

@DataJpaTest
@ContextConfiguration(classes = TestingApplication.class)
public class TestProgramEntries {

	@Autowired
	private PersonRepository personRepo;

	@Test
	public void test() {
		FilterOperatorService<Specification<Person>> decoderService = new SpecificationFilterOperatorService<>();
		FilterValueConverter filterValueConverter = new DefaultFilterValueConverter();

		List<FilterParameter> parameters = new ArrayList<>();
		parameters.add(new FilterParameter("name", "clientName", String.class, GreaterOrEquals.class, false, "Fulano", null));
		parameters.add(new FilterParameter("height", "clientHeight", BigDecimal.class, Equals.class, false, BigDecimal.valueOf(3), null));
		parameters.add(new FilterParameter("addresses.location.city", "birthCity", String.class, Equals.class, false, "Belem", null));
		parameters.add(new FilterParameter("addresses.street", "address", String.class, Equals.class, false, "rua", null));
		parameters.add(new FilterParameter("phones.number", "phoneNumber", String.class, Equals.class, false, "1345", null));

		ConditionalStatement logicWrapper = new ConditionalStatement(LogicType.CONJUNCTION, parameters);

		DynamicFilterResolver<Specification<Person>> parameterFilter = new SpecificationDynamicFilterResolver<>(decoderService,
				filterValueConverter);

		List<Person> list = personRepo.findAll(parameterFilter.convertTo(logicWrapper));
		System.out.println(list);
	}

}
