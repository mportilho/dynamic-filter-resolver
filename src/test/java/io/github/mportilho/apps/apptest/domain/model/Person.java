package io.github.mportilho.apps.apptest.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "person")
	private List<Phone> phones;

	Person() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		Person other = (Person) obj;
		return getId() != null && Objects.equals(id, other.id);
	}

}
