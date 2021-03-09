# Dynamic Filter Resolver

## Download

Maven:

```xml
<dependency>
  <groupId>io.github.mportilho</groupId>
  <artifactId>dynamic-filter-resolver</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Description

An extensible library for creating dynamic filters for multiple data query providers, automatically from user's parameters. The library reads a set of user's inputs, converted into conditional statements that are later transformed into a target engine's query objects by the dynamic filter resolver implementation.

`User Parameters --> [Filter Parameters] --> [Conditional Statements] --> [Dynamic Resolver Resolver] --> Target Query Object`

The highlighted elements above are plug-and-play providers, facilitating changes in how those elements interact with each other, as shown below: 

```java
// Retrieve users parameters
Collection<Object, Object[]> parametersMap = retrieveUserParameters();

// Creates conditional statements from annotations
AnnotationConditionalStatementProvider provider = new DefaultAnnotationConditionalStatementProvider(null);
ConditionalStatement statement = provider.createConditionalStatements(QueryDefinitions.class, null, parametersMap);

// Creates the target query object
DynamicFilterResolver<List<Query>> resolver = new GenericDynamicFilterResolver();
List<Query> filterClauses = resolver.convertTo(condition);
queryProvider.query(filterClauses);
```

The example can be used as base to create filters from any parameter source to any target query provider. When applicable, aspect oriented or proxied call implementation can be written for a transparent query object injection.

This library includes a Spring MVC and a Spring Data JPA integration for converting HTTP request parameters to Spring's Specification instance for database query, as shown in the example bellow+

```java
@Autowired
private PersonRepository repository;

@GetMapping("/person")
public Object searchPerson(
    @Conjunction( {
      @Filter(path = "name", attributes = "clientName",operator = Like.class)
    }) Specification<Person> filters) {
  return repository.findAll(filters);
}
```

> It's possible to add more integrations in the future

## Filter Parameters

Parameters provided by users that indicate how a particular *comparison operation* should be performed for a *given value* on a *related attribute*.

The minimum required parameter's attributes are: ***path*** (to the object's attribute), comparison ***operator*** and a ***value***.

There are additional attributes that influence how the comparison operations are performed:

- ***targetType***: A target attribute type for data conversion. Potencially overriden by a dynamic filter provider implementation when necessary.
- ***negate***: Negates the result of a comparison operation
- ***ignoreCase***: When working with text values, hints that the comparison operation must try to ignore case.
- ***format***: A format pattern to be used when transforming the provided value to the target attribute's type

## Conditional Statements

They are a set of logical clauses grouped by a logic operation type, a *conjunction* or a *disjunction*. Additionally, they can have a list other conditional statements, creating a tree like structure.

Conjunctions are a set of operands that is only true when all of its operands are true, functioning like the *AND* logic gate. Disjunction are a set of operands that is true is at least one of its operands is true, like the *OR* logic gate.

So, the structure of a conditional statement is represented visually below as a YAML document:

```yaml
conditional-statement:
  logic-type: CONJUNCTION
  clauses: [A, B, C]
  sub-statements:
    - conditional-statement:
        logic-type: DISJUNCTION
        clauses: [D, E]
        sub-statements: null
    - conditional-statement:
        logic-type: CONJUNCTION
        clauses: [F, G]
        sub-statements: null
```

The resulting logic statement is:  

(**A** *and* **B** *and* **C**) *and* (**D** *or* **E**) *and* (**F** *and* **G**)

Additionaly, inverting the logic type of the conditional structure above, we get:

```yaml
conditional-statement:
  logic-type: DISJUNCTION
  clauses: [A, B, C]
  sub-statements:
    - conditional-statement:
        logic-type: CONJUNCTION
        clauses: [D, E]
        sub-statements: null
    - conditional-statement:
        logic-type: DISJUNCTION
        clauses: [F, G]
        sub-statements: null
```

(**A** *or* **B** *or* **C**) *or* (**D** *and* **E**) *or* (**F** *or* **G**)

## Filter Annotations

The library includes annotations for implementing filter parameter capturing more easily:

### ***@Filter***

Representation of a Filter Parameter. It's attributes are:

*Required Attributes:*

- ***path***: A notation value from which the target attribute can be found on a specified root type, like `Person.addresses.streetName`
- ***parameters***: Parameters needed to be supplied by the caller, exposed as input data requirements
- ***operator***: Operation to be used as a query filter

*Optional Attributes:*

- ***targetType***: Target attribute type for convertion
- ***negate***: Indicates that the logic of this filter must be negated
- ***ignoreCase***: Indication to try to ignore case while processing creating dynamic filters on text like types
- ***defaultValues***: Default values for parameters if none is provided by the user
- ***constantValues***: Constant values for parameters. If used, no value will not be requested from the user
- ***format***: A format pattern to assist data conversion to the corresponding target type
- ***attributePath***: A path for another object type's attribute. Useful for providing metadata for creating filter parameters and metadata for the target type's attribute

### ***@Statement***

A set of related filters. It's attributes are:

- ***value***: An array of @Filter annotations
- ***negate***: If true, negates the whole conditional statement result

### ***@Conjunction***

Aggregates filters into AND logic operations. It's attributes are:

- ***value***: An array of @Filter annotations
- ***disjunctions***: A set of @Filter that operates like OR logic gates
- ***negate***: If true, negates the whole conjunction result

```java
@Conjunction(
  value = {
    @Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
    @Filter(path = "status", parameters = "status", operator = Equals.class),
  disjunctions = {
    @Statement({
      @Filter(path = "status", parameters = "status", operator = Equals.class, targetType = StatusEnum.class)
    })
})
```

### ***@Disjunction***

Aggregates filter parameters into OR logic operations. It's attributes are:

- ***value***: An array of @Filter annotations
- ***disjunctions***: A set of @Filter that operates like OR logic gates
- ***negate***: If true, negates the whole disjunction result

```java
@Disjunction(
  value = {
    @Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, defaultValues = "12/12/2012", targetType = LocalDate.class),
    @Filter(path = "height", parameters = "height", operator = Greater.class),
  conjunctions = {
    @Statement({
      @Filter(path = "status", parameters = "status", operator = Equals.class, targetType = StatusEnum.class)
    })
})
```

## Composing Interfaces and Annotations

The library's annotations can be used on *types* and *method's parameters*, where they are extracted and converted to `ConditionalStatement` from `AnnotationConditionalStatementProvider` interface implementations.

Additionally, they can composed through the type's hierarchy, which annotations can be searched recursively, as shown below:

- *Supertype **NoDelete***

```java
@Conjunction(value = {
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class)
})
public interface NoDelete {
}
```

- *Subtype **NoDeleteExtendedStatusOK***

```java
@Conjunction(value = {
  @Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})
public interface NoDeleteAndStatusOK extends NoDelete, Serializable {
}
```

And then, used as a method parameter's type:

```java
public Object searchPerson(
    @Conjunction( {
      @Filter(path = "name", attributes = "clientName", operator = Like.class)
    }) NoDeleteAndStatusOK filters) {
  return searchEngine.search(filters);
}
```

When composing filters from different locations, *conjunction logic* will always be applied

## Operations

The library provides a predefined number of operators that indicate how filtering parameters are turned into queries, but only the ones **having non null** values. It's a important feature that allows flexibility when a conditional statement is to be applied.

These are the current operations this library provides:

### Equals

An operator that searches data which value is equals to a given parameter

***Example:***  
Parameters: { 'username' }  
Values: { 'foobar' }  

### NotEquals

An operator that searches data which value is equals to a given parameter

***Example:***
Parameters: { 'username' }  
Values: { 'xpto' }  

### Greater

An operator that searches data which value is greater than a given parameter.

***Example:***
Parameters: { 'salary' }  
Values: { '2000' }  

### GreaterOrEquals

An operator that searches data which value is greater or equals than a given parameter.

***Example:***
Parameters: { 'salary' }  
Values: { '3000' }  

### Less

An operator that searches data which value is less than a given parameter.

***Example:***
Parameters: { 'salary' }  
Values: { '2000' }  

### LessOrEquals

An operator that searches data which value is less or equals than a given parameter.

***Example:***
Parameters: { 'salary' }  
Values: { '2000' }  

### Between

An operator that verifies if a given value is between a lower and upper limit. Needs two parameters to create the conditional statement.

***Example:***
Parameters: { 'lowerDate', 'upperDate' }  
Values: { '20/12/2015', '20/12/2020' }  


### Like

An operator that searches texts which value contains a given parameter.

***Example:***
Parameters: { 'message' }  
Values: { 'success' }  

### NotLike

An operator that searches texts which value does not contains a given parameter.

***Example:***
Parameters: { 'message' }  
Values: { 'NullpointerException' }  

### StartsWith

An operator that searches texts which value starts with a given parameter.

***Example:***
Parameters: { 'message' }  
Values: { 'Object found: ' }  

### EndsWith

An operator that searches texts which value ends with a given parameter.

***Example:***
Parameters: { 'message' }  
Values: { 'not found' }  

### IsIn

An operator that searches data which value contains a given number of parameters.

***Example:***
Parameters: { 'workType' }  
Values: { 'FULL_TIME', 'PART_TIME' }  

### IsNotIn

An operator that searches data which value does not contains a given number of parameters.

***Example:***
Parameters: { 'workType' }  
Values: { 'OVER_TIME', 'NIGHT_TIME' }  

### IsNull

An operator that searches attributes which value is null. It receives a boolean value where 'true' indicates that null attributes must be queried and 'false' indicates that non null attributes must be queried. 

***Example:***
Parameters: { 'status' }  
Values: { 'true' }  

### IsNotNull

A similar operator as 'IsNull', but inverted logic. Searches attributes which value is not null. It receives a boolean value where 'true' indicates that non null attributes must be queried and 'false' indicates that null attributes must be queried. 

***Example:***
Parameters: { 'status' }  
Values: { 'true' }  

### Dynamic

An operator that performs comparison types required by the user. It receives two parameters: an user defined operation type and a given value.

The possible comparison types and corresponding constant values (not case sensitive) are: 

- *Equals*: `EQ`
- *NotEquals*: `NE`
- *Greater*: `GT`
- *GreaterOrEquals*: `GE`
- *Less*: `LT`
- *LessOrEquals*: `LE`
- *Like*: `LK`
- *NotLike*: `NL`
- *StartsWith*: `SW`
- *EndsWith*: `EW`

***Example:***
Parameters: { 'salary' }  
Values: { 'LE', '2500' }  

> There's no restriction about the values order 

## Spring Data JPA's Specification

This library currently provides an default implementation of a Dynamic Filter Provider for Spring Data JPA's Specification through the `SpecificationDynamicFilterResolver` class.

### Web Environment Configuration

For a web environment, `SpecificationDynamicFilterArgumentResolver` class can be configured on Spring's *WebMvcConfigurer* to enable auto-injection of `Specification` instances on controllers. Interfaces that extends *Specification*, defining their own filter annotations configuration, can also be used as method parameter type.

```java
@GetMapping("/person")
public Object searchPerson(
    @Conjunction( {
      @Filter(path = "name", attributes = "clientName",operator = Like.class)
    }) Specification<Person> filters) {
  return searchEngine.search(filters);
}
```


The `@EnableMvcSpecificationFilterResolver` annotation should be used on a Spring Configuration bean to enable *SpecificationDynamicFilterArgumentResolver* auto-configuration in a Spring Boot project. *Spring Data JPA* and *Spring Boot Web Starter* dependencies must be included manually as project's dependency.

The `SpecificationDynamicFilterArgumentResolver` obtains user provided filter values from *HTTP Request* parameters.

```bash
curl http://localhost:8080/person?clientName=Foobar
```

When a HTTP Request parameter is absent, it's corresponding filter parameter will not be included as a filtering condition.

### Attribute Path Navigation

This dynamic filter provider adds *sql ***inner*** join operations* automatically then a path navigation is detected.

Consider the following entities:

***Person***
```java
@Entity
@Table
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private BigDecimal height;

  private BigDecimal weight;

  private LocalDate birthday;

  private LocalDateTime registerDate;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "person")
  private List<Address> addresses;

  ...
}
```

***Address***

```java
@Entity
@Table
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String street;

  private String number;

  @ManyToOne
  @JoinColumn(name = "ID_PERSON")
  private Person person;

  @OneToOne
  @JoinColumn(name = "ID_LOCATION")
  private Location location;

  ...
}
```

***Location***

```java
@Entity
@Table
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String city;

  private String state;

  ...
}
```


The developer can adds a filter that searches people through their street's name by indicating `path` as:

```java
@Filter(path = "addresses.street", parameters = "streetName", operator = StartsWith.class)
```

or searches people through their city by indicating `path` as:

```java
@Filter(path = "addresses.location.city", parameters = "clientCity", operator = Equals.class)
```

### Earger Fetches

The repeatable annotation `@Fetching` enables earger fetch of entites when used in addition with *@Conjunction* or *@Disjunction*. By default, it uses *left outer join operation* but it can be changed by using the `joinType` annotation attribute.

```java
public Object searchPerson(
    @Fetching("addresses.location")
    @Conjunction( {
      @Filter(path = "name", attributes = "clientName",operator = Like.class)
    }) Specification<Person> filters) {
  return searchEngine.search(filters);
}
```

> Beware of the [*MultipleBagFetchException*](https://docs.jboss.org/hibernate/stable/core/javadocs/org/hibernate/loader/MultipleBagFetchException.html)


## Optional Modules

### Springdocs OpenAPI 3

When using the library [Springdocs](https://springdoc.org/), for adding HTTP Request parameter descriptions automatically on OpenAPI 3 documentation, based on ***filtering annotations***, include a instance of `SpringdocsDynamicFilterOperationCustomizer` as an application bean, as stated in the [Springdocs' documentation](https://springdoc.org/#can-i-customize-openapi-object-programmatically):

```java
@Bean
public OperationCustomizer operationCustomizer() {
  return new SpringdocsDynamicFilterOperationCustomizer();
}
```

This module reads the attributes' metadata, as *Class*, *Enums* or *Bean Validation API* annotations, to augment the resulting OpenAPI 3 documentation. Because of some restrictions encountered on array type, this library cannot apply type or validation metadata from the original source attribute.
