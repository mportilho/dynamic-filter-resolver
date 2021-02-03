package net.apps.tests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import net.apps.apptest.TestingApplication;
import net.apps.apptest.domain.model.Person;
import net.apps.apptest.repository.PersonRepository;
import net.dfr.filter.ConditionalStatement;
import net.dfr.filter.DynamicFilterResolver;
import net.dfr.filter.FilterParameter;
import net.dfr.filter.LogicType;
import net.dfr.operator.FilterOperatorService;
import net.dfr.operator.FilterValueConverter;
import net.dfr.operator.type.Equals;
import net.dfr.operator.type.GreaterOrEquals;
import net.dfr.provider.commons.DefaultFilterValueConverter;
import net.dfr.provider.specification.filter.SpecificationDynamicFilterResolver;
import net.dfr.provider.specification.operator.SpecificationFilterOperatorService;

@DataJpaTest
@ContextConfiguration(classes = TestingApplication.class)
public class TestProgramEntries {

	@Autowired
	private PersonRepository personRepo;

	@Test
	public void test() {
		FilterOperatorService<Specification<Person>> operatorService = new SpecificationFilterOperatorService<>();
		FilterValueConverter filterValueConverter = new DefaultFilterValueConverter();

		List<FilterParameter> parameters = new ArrayList<>();
		parameters.add(new FilterParameter("name", "clientName", String.class, GreaterOrEquals.class, false, "Fulano", null));
		parameters.add(new FilterParameter("height", "clientHeight", BigDecimal.class, Equals.class, false, BigDecimal.valueOf(3), null));
		parameters.add(new FilterParameter("addresses.location.city", "birthCity", String.class, Equals.class, false, "Belem", null));
		parameters.add(new FilterParameter("addresses.street", "address", String.class, Equals.class, false, "rua", null));
		parameters.add(new FilterParameter("phones.number", "phoneNumber", String.class, Equals.class, false, "1345", null));

		ConditionalStatement statement = new ConditionalStatement(LogicType.CONJUNCTION, false, parameters);

		DynamicFilterResolver<Specification<Person>> parameterFilter = new SpecificationDynamicFilterResolver<>(operatorService, filterValueConverter);
		Specification<Person> specification = parameterFilter.convertTo(statement);
		
		List<Person> list = personRepo.findAll(specification);
		System.out.println(list);
	}

}
