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

package io.github.mportilho.apptests.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import io.github.mportilho.apps.apptest.TestingApplication;
import io.github.mportilho.apps.apptest.domain.model.Person;
import io.github.mportilho.apps.apptest.repository.PersonRepository;
import io.github.mportilho.dfr.core.converter.DefaultFilterValueConverter;
import io.github.mportilho.dfr.core.converter.FilterValueConverter;
import io.github.mportilho.dfr.core.filter.DynamicFilterResolver;
import io.github.mportilho.dfr.core.filter.FilterParameter;
import io.github.mportilho.dfr.core.operator.FilterOperatorService;
import io.github.mportilho.dfr.core.operator.type.Between;
import io.github.mportilho.dfr.core.operator.type.Equals;
import io.github.mportilho.dfr.core.operator.type.GreaterOrEquals;
import io.github.mportilho.dfr.core.statement.ConditionalStatement;
import io.github.mportilho.dfr.core.statement.LogicType;
import io.github.mportilho.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;
import io.github.mportilho.dfr.providers.specification.operator.SpecificationFilterOperatorService;

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
		parameters.add(new FilterParameter("name", "name", "clientName", String.class, GreaterOrEquals.class, false, false, "Fulano", null));
		parameters.add(new FilterParameter("height", "height", new String[] { "clientHeight", "clientHeight" }, BigDecimal.class, Between.class,
				false, false, new Object[] { 3, 5 }, null));
		parameters.add(new FilterParameter("city", "addresses.location.city", "birthCity", String.class, Equals.class, false, false, "Belem", null));
		parameters.add(new FilterParameter("streetName", "addresses.street", "address", String.class, Equals.class, false, false, "rua", null));
		parameters.add(new FilterParameter("phonesNumber", "phones.number", "phoneNumber", String.class, Equals.class, false, false, "1345", null));

		ConditionalStatement statement = new ConditionalStatement(LogicType.CONJUNCTION, false, parameters);

		DynamicFilterResolver<Specification<?>> parameterFilter = new SpecificationDynamicFilterResolver(operatorService, filterValueConverter);
		Specification<Person> specification = parameterFilter.convertTo(statement);

		List<Person> list = personRepo.findAll(specification);
		System.out.println(list);
	}

}
