package net.apps.tests.app;

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
import net.dfr.core.converter.DefaultFilterValueConverter;
import net.dfr.core.converter.FilterValueConverter;
import net.dfr.core.filter.DynamicFilterResolver;
import net.dfr.core.filter.FilterParameter;
import net.dfr.core.operator.FilterOperatorService;
import net.dfr.core.operator.type.Between;
import net.dfr.core.operator.type.Equals;
import net.dfr.core.operator.type.GreaterOrEquals;
import net.dfr.core.statement.ConditionalStatement;
import net.dfr.core.statement.LogicType;
import net.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;
import net.dfr.providers.specification.operator.SpecificationFilterOperatorService;

@DataJpaTest
@ContextConfiguration(classes = TestingApplication.class)
public class TestProgramEntries {

	@Autowired
	private PersonRepository personRepo;

	@Test
	public void test() {
		FilterOperatorService<Specification<?>> operatorService = new SpecificationFilterOperatorService();
		FilterValueConverter filterValueConverter = new DefaultFilterValueConverter();

		List<FilterParameter> parameters = new ArrayList<>();
		parameters.add(new FilterParameter("name", "name", "clientName", String.class, GreaterOrEquals.class, false, "Fulano", null));
		parameters.add(new FilterParameter("height", "height", new String[] { "clientHeight", "clientHeight" }, BigDecimal.class, Between.class,
				false, new Object[] { 3, 5 }, null));
		parameters.add(new FilterParameter("city", "addresses.location.city", "birthCity", String.class, Equals.class, false, "Belem", null));
		parameters.add(new FilterParameter("streetName", "addresses.street", "address", String.class, Equals.class, false, "rua", null));
		parameters.add(new FilterParameter("phonesNumber", "phones.number", "phoneNumber", String.class, Equals.class, false, "1345", null));

		ConditionalStatement statement = new ConditionalStatement(LogicType.CONJUNCTION, false, parameters);

		DynamicFilterResolver<Specification<?>> parameterFilter = new SpecificationDynamicFilterResolver(operatorService, filterValueConverter);
		Specification<Person> specification = parameterFilter.convertTo(statement);

		List<Person> list = personRepo.findAll(specification);
		System.out.println(list);
	}

}
