package com.github.djs.apptest.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class PersonDTO {

	private String name;

	private BigDecimal height;

	private LocalDate birthday;

	PersonDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	@Override
	public int hashCode() {
		return Objects.hash(birthday, height, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PersonDTO other = (PersonDTO) obj;
		return Objects.equals(birthday, other.birthday) && Objects.equals(height, other.height) && Objects.equals(name, other.name);
	}

}
