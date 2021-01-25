package com.github.djs.apptest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.djs.apptest.domain.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

}
