package de.edgb.aviationclubmanager.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import de.edgb.aviationclubmanager.web.Util;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class InstructorPresence {

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date presenceDate;

	public LocalDate getPresenceDate() {
		return Util.convertDateToLocalDate(this.presenceDate);
	}

	public void setPresenceDate(LocalDate presenceDate) {
		this.presenceDate = Util.convertLocalDateToDate(presenceDate);
	}

	@NotNull
	@ManyToOne
	private ClubMember instructor;

	private String comment;

	// Finder

	public static List<InstructorPresence> findAllInstructorPresences() {
		return entityManager().createQuery(
				"SELECT o FROM InstructorPresence o ORDER BY presenceDate",
				InstructorPresence.class).getResultList();
	}

	public static List<InstructorPresence> findInstructorPresenceEntries(
			int firstResult, int maxResults) {
		return entityManager()
				.createQuery(
						"SELECT o FROM InstructorPresence o ORDER BY presenceDate",
						InstructorPresence.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static TypedQuery<InstructorPresence> findInstructorPresencesByPresenceDateGreaterThanEquals(
			LocalDate presenceDate) {
		if (presenceDate == null)
			throw new IllegalArgumentException(
					"The presenceDate argument is required");
		EntityManager em = InstructorPresence.entityManager();
		TypedQuery<InstructorPresence> q = em
				.createQuery(
						"SELECT o FROM InstructorPresence AS o WHERE o.presenceDate >= :presenceDate ORDER BY presenceDate",
						InstructorPresence.class);
		q.setParameter("presenceDate",
				Util.convertLocalDateToDate(presenceDate));
		return q;
	}
}
