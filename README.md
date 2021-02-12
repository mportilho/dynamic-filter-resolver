# Dynamic Filter Resolver (DOC WIP)

An extensible library for creating dynamic filters for multiple data query providers. It has a built-in filter provider for Spring Data JPA's Specification, but more providers can be delivered in the future or extended by users for their project's needs.

The library read a set of inputs that are converted into conditional statements and later transformed into the target engine's query objects.

Filter Parameters -> Conditional Statements -> Operators -> Query Objects

Flexibility for creating plugable conditional statements providers and plugable dynamic filter resolvers ...


## Filter Parameters

Parameters provided by users that indicate how a particular *comparison operation* should be performed for a *given value* and *related attribute*.

The minimum required parameter's attributes are: ***path*** (to the object's attribute), comparison ***operator*** and a ***value***.

There are additional attributes that influence how the comparison operations are performed:

- ***targetType***: A target attribute type for data conversion. Potencially overriden by a dynamic filter provider implementation when necessary.
- ***negate***: Negates the result of a comparison operation
- ***ignoreCase***: When working with text values, hints that the comparison operation must try to ignore case.
- ***format***: A format pattern to be used when transforming the provided value to the target attribute's type

## Conditional Statements

They are a set of logical clauses grouped by a logic operation type, a *conjunction* or a *disjunction*. Additionally, they can have a list other conditional statements, creating a tree like structure.

Conjunctions are a set of operands that is only true when all of its operands are true, functioning like the *AND* logic gate. Disjunction are a set of operands that is true is at least one of its operands is true, like the *OR* logic gate.

So, the structure of a conditional statement on this library is:

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

## Filter Annotations

The library includes annotations for implementing filter parameter capturing more easily and have attributes corresponding to the filter parameters. The annotations are:

#### ***@Filter***

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
- ***constantValues***: Constant values for parameters. Having any value, the corresponding filter value will not be requested from the user
- ***format***: A format pattern to assist data conversion for the corresponding target type
- ***attributePath***: A path for another object type's attribute. Useful for providing metadata for creating filter parameters and metadata for the target type's attribute

#### ***@Statement***

A set of related filters. It's attributes are:

- ***value***: An array of @Filter annotations
- ***negate***: If true, negates the whole conditional statement result

#### ***@Conjunction***

Aggregates filters into AND logic operations. It's attributes are:

- ***value***: An array of @Filter annotations
- ***disjunctions***: A set of @Filter that operates like OR logic gates
- ***negate***: If true, negates the whole conjunction result

```java
@Conjunction(value = {
  @Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class),
  @Filter(path = "status", parameters = "status", operator = Equals.class)
})
```

#### ***@Disjunction***

Aggregates filter parameters into OR logic operations. It's attributes are:

- ***value***: An array of @Filter annotations
- ***disjunctions***: A set of @Filter that operates like OR logic gates
- ***negate***: If true, negates the whole disjunction result

```java
@Disjunction(value = {
  @Filter(path = "birthday", parameters = "birthday", operator = GreaterOrEquals.class, defaultValues = "12/12/2012", targetType = LocalDate.class),
  @Filter(path = "height", parameters = "height", operator = Greater.class) 
})
```

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

The possible comparison types and corresponding constant values are: 

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

## Composing Interfaces and Annotations

```java
@Conjunction(value = {
  @Filter(path = "status", parameters = "status", operator = Equals.class, constantValues = "OK", targetType = StatusEnum.class)
})
public interface NoDeleteExtendedStatusOK extends NoDelete, Serializable {
}
```

```java
@Conjunction(value = {
	@Filter(path = "deleted", parameters = "delete", operator = Equals.class, constantValues = "false", targetType = Boolean.class)
})
public interface NoDelete {
}
```

## Spring Data JPA's Specification

This library currently provides an default implementation of a Dynamic Filter Provider for Spring Data JPA's Specification.


Spring data jpa starter ...

EnableMvcSpecificationFilterResolver autoconfiguration ...

@Fetching ...

Annotating method controllers ...

## Optional Modules

### Springdocs Swagger

SpringdocsDynamicFilterOperationCustomizer ...

## Converters

Explain generic DateTimeFormatter converters ... 
FormattedConversionService ...
FormattedConverter