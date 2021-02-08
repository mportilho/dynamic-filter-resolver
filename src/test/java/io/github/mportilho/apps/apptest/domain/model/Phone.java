package io.github.mportilho.apps.apptest.domain.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Phone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String number;
	
	@ManyToOne
	@JoinColumn(name = "ID_PERSON")
	private Person person;

	public Long getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}
	
	public Person getPerson() {
		return person;
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
		Phone other = (Phone) obj;
		return id != null && Objects.equals(id, other.id);
	}

}
