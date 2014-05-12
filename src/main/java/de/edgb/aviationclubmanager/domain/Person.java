package de.edgb.aviationclubmanager.domain;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Person {

	@NotNull
	private String lastName;

	@NotNull
	private String firstName;

	private String address;

	private String zipCode;

	private String city;

	private String landline;

	private String cellphone;

	private String email;

	private String comment;

	// Finder

	public static TypedQuery<Person> findPeopleNotStudent() {
		EntityManager em = Person.entityManager();
		TypedQuery<Person> q = em
				.createQuery(
						"SELECT o FROM Person AS o WHERE o NOT IN (SELECT c FROM ClubMember AS c WHERE c.student IS true ORDER BY o.lastName, o.firstName)",
						Person.class);
		return q;
	}

	public static TypedQuery<Person> findPeopleBeingStudent() {
		EntityManager em = Person.entityManager();
		TypedQuery<Person> q = em
				.createQuery(
						"SELECT o FROM Person AS o WHERE o IN (SELECT c FROM ClubMember AS c WHERE c.student IS true ORDER BY o.lastName, o.firstName)",
						Person.class);
		return q;
	}

	public static TypedQuery<Person> findPeopleBeingInstructor() {
		EntityManager em = Person.entityManager();
		TypedQuery<Person> q = em
				.createQuery(
						"SELECT o FROM Person AS o WHERE o IN (SELECT c FROM ClubMember AS c WHERE c.instructor IS true ORDER BY o.lastName, o.firstName)",
						Person.class);
		return q;
	}

	public static List<Person> findAllPeople() {
		return entityManager().createQuery(
				"SELECT o FROM Person o ORDER BY o.lastName, o.firstName",
				Person.class).getResultList();
	}

	public static List<Person> findPersonEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery(
						"SELECT o FROM Person o ORDER BY o.lastName, o.firstName",
						Person.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static class PersonComparator implements Comparator<Person> {

		@Override
		public int compare(Person p1, Person p2) {
			int ln = p1.lastName.compareTo(p2.lastName);
			if (ln == 0)
				return p1.firstName.compareTo(p2.firstName);
			else
				return ln;
		}
	}
}
