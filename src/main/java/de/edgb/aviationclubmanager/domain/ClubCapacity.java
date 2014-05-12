package de.edgb.aviationclubmanager.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ClubCapacity {

	@NotNull
	private String name;

	private String email;

	private String comment;

	public static List<ClubCapacity> findAllClubCapacitys() {
		return entityManager().createQuery(
				"SELECT o FROM ClubCapacity o ORDER BY o.name",
				ClubCapacity.class).getResultList();
	}

	public static List<ClubCapacity> findClubCapacityEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM ClubCapacity o ORDER BY o.name",
						ClubCapacity.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}
}
