package io.github.mportilho.apps.webapptest.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.mportilho.apps.webapptest.model.Department;
import io.github.mportilho.apps.webapptest.repository.NoDeletionSpecification;
import io.github.mportilho.dfr.core.annotation.Conjunction;
import io.github.mportilho.dfr.core.annotation.Filter;
import io.github.mportilho.dfr.core.operator.type.StartsWith;

@RestController
@RequestMapping("department")
public class DepartmentController {

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public String test(
			@Conjunction({ @Filter(path = "name", parameters = "name", operator = StartsWith.class) }) Specification<Department> specification)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("name", "Blanka");
		node.put("foundSpec", specification != null);
		return mapper.writeValueAsString(node);
	}

	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String test2(@PathVariable Long id,
			@Conjunction({
					@Filter(path = "name", parameters = "name", operator = StartsWith.class) }) NoDeletionSpecification<Department> specification)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("name", "Blanka");
		node.put("foundSpec", specification != null);
		return mapper.writeValueAsString(node);
	}

}
