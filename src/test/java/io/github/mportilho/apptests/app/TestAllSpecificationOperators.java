package io.github.mportilho.apptests.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import io.github.mportilho.apps.apptest.TestingApplication;
import io.github.mportilho.apps.apptest.domain.model.Person;
import io.github.mportilho.apps.apptest.repository.PersonRepository;
import io.github.mportilho.apptests.app.queries.ComparisonOperationsQueryInterface;
import io.github.mportilho.apptests.app.queries.FetchingComposedPath;
import io.github.mportilho.apptests.app.queries.FetchingSimplePath;
import io.github.mportilho.apptests.app.queries.OtherOperationsQueryInterface;
import io.github.mportilho.apptests.app.queries.StringOperationsQueryInterface;
import io.github.mportilho.dfr.core.converter.DefaultFilterValueConverter;
import io.github.mportilho.dfr.core.filter.DynamicFilterResolver;
import io.github.mportilho.dfr.core.operator.FilterOperatorService;
import io.github.mportilho.dfr.core.statement.ConditionalStatement;
import io.github.mportilho.dfr.core.statement.ConditionalStatementProvider;
import io.github.mportilho.dfr.core.statement.DefaultConditionalStatementProvider;
import io.github.mportilho.dfr.providers.specification.annotation.Fetching;
import io.github.mportilho.dfr.providers.specification.filter.SpecificationDynamicFilterResolver;
import io.github.mportilho.dfr.providers.specification.operator.SpecificationFilterOperatorService;

@DataJpaTest
@ContextConfiguration(classes = TestingApplication.class)
public class TestAllSpecificationOperators {

	@Autowired
	private PersonRepository personRepo;

	@Test
	public void testComparisons() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(ComparisonOperationsQueryInterface.class, null, null);
		FilterOperatorService<Specification<?>> operatorService = new SpecificationFilterOperatorService();
		DefaultFilterValueConverter filterValueConverter = new DefaultFilterValueConverter();
		DynamicFilterResolver<Specification<?>> resolver = new SpecificationDynamicFilterResolver(operatorService, filterValueConverter);

		Specification<Person> specification = resolver.convertTo(statement);
		List<Person> list = personRepo.findAll(specification);

		assertThat(list).isEmpty();
	}

	@Test
	public void testStringComparisons() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(StringOperationsQueryInterface.class, null, null);
		FilterOperatorService<Specification<?>> operatorService = new SpecificationFilterOperatorService();
		DefaultFilterValueConverter filterValueConverter = new DefaultFilterValueConverter();
		DynamicFilterResolver<Specification<?>> resolver = new SpecificationDynamicFilterResolver(operatorService, filterValueConverter);

		Specification<Person> specification = resolver.convertTo(statement);
		List<Person> list = personRepo.findAll(specification);

		assertThat(list).isEmpty();
	}

	@Test
	public void testOtherComparisons() {
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		ConditionalStatement statement = provider.createConditionalStatements(OtherOperationsQueryInterface.class, null, null);
		FilterOperatorService<Specification<?>> operatorService = new SpecificationFilterOperatorService();
		DefaultFilterValueConverter filterValueConverter = new DefaultFilterValueConverter();
		DynamicFilterResolver<Specification<?>> resolver = new SpecificationDynamicFilterResolver(operatorService, filterValueConverter);

		Specification<Person> specification = resolver.convertTo(statement);
		List<Person> list = personRepo.findAll(specification);

		assertThat(list).isEmpty();
	}

	@Test
	public void testSimpleFetch() {
		FilterOperatorService<Specification<?>> operatorService = new SpecificationFilterOperatorService();
		DefaultFilterValueConverter filterValueConverter = new DefaultFilterValueConverter();
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		DynamicFilterResolver<Specification<?>> resolver = new SpecificationDynamicFilterResolver(operatorService, filterValueConverter);

		ConditionalStatement statement = provider.createConditionalStatements(FetchingSimplePath.class, null, null);

		Fetching[] anns = FetchingSimplePath.class.getAnnotationsByType(Fetching.class);
		Specification<Person> specification = resolver.convertTo(statement, Collections.singletonMap(Fetching.class, anns));
		List<Person> list = personRepo.findAll(specification);

		assertThat(list).isEmpty();
	}

	@Test
	public void testComposedFetch() {
		FilterOperatorService<Specification<?>> operatorService = new SpecificationFilterOperatorService();
		DefaultFilterValueConverter filterValueConverter = new DefaultFilterValueConverter();
		ConditionalStatementProvider provider = new DefaultConditionalStatementProvider(null);
		DynamicFilterResolver<Specification<?>> resolver = new SpecificationDynamicFilterResolver(operatorService, filterValueConverter);

		ConditionalStatement statement = provider.createConditionalStatements(FetchingComposedPath.class, null, null);

		Fetching[] anns = FetchingComposedPath.class.getAnnotationsByType(Fetching.class);
		Specification<Person> specification = resolver.convertTo(statement, Collections.singletonMap(Fetching.class, anns));
		List<Person> list = personRepo.findAll(specification);

		assertThat(list).isEmpty();
	}

}
