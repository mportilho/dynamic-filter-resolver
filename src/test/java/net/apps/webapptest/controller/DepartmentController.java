package net.apps.webapptest.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.apps.webapptest.model.Department;
import net.dfr.annotation.Conjunction;
import net.dfr.annotation.Filter;
import net.dfr.operator.type.StartsWith;

@RestController
@RequestMapping("department")
public class DepartmentController {

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public String test(@Conjunction(value = {
			@Filter(path = "name", parameters = "name", operator = StartsWith.class) }) Specification<Department> specification) {
		return "{\"name\":\"Blanka\"}";
	}

}
