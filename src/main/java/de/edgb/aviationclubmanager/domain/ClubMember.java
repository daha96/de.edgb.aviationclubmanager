package de.edgb.aviationclubmanager.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import de.edgb.aviationclubmanager.web.Util;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findClubMembersByUsernameEquals" })
public class ClubMember extends Person {

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date birthday;

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date joiningDate;

	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date exitDate;

	public LocalDate getBirthday() {
		return Util.convertDateToLocalDate(this.birthday);
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = Util.convertLocalDateToDate(birthday);
	}

	public LocalDate getJoiningDate() {
		return Util.convertDateToLocalDate(this.joiningDate);
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = Util.convertLocalDateToDate(joiningDate);
	}

	public LocalDate getExitDate() {
		return Util.convertDateToLocalDate(this.exitDate);
	}

	public void setExitDate(LocalDate exitDate) {
		this.exitDate = Util.convertLocalDateToDate(exitDate);
	}

	@NotNull
	@Enumerated
	private ClubMemberState clubMemberState;

	@NotNull
	@Enumerated
	private Gender gender;

	@NotNull
	private Boolean glidingFixedRate;

	@NotNull
	private Boolean enginedAviationFixedRate;

	@NotNull
	private Boolean student;

	@NotNull
	private Boolean cheapPrice;

	@OneToOne
	private ClubCapacity clubCapacity;

	@NotNull
	private Boolean instructor;

	@NotNull
	private Boolean winchDriver;

	// Finder

	public static TypedQuery<ClubMember> findClubMembersBeingWinchDriver() {
		EntityManager em = ClubMember.entityManager();
		TypedQuery<ClubMember> q = em
				.createQuery(
						"SELECT o FROM ClubMember AS o WHERE o.winchDriver AND o.clubMemberState != 4 IS TRUE ORDER BY o.lastName, o.firstName",
						ClubMember.class);
		return q;
	}

	public static TypedQuery<ClubMember> findClubMembersBeingInstructor() {
		EntityManager em = ClubMember.entityManager();
		TypedQuery<ClubMember> q = em
				.createQuery(
						"SELECT o FROM ClubMember AS o WHERE o.instructor AND o.clubMemberState != 4 IS TRUE ORDER BY o.lastName, o.firstName",
						ClubMember.class);
		return q;
	}

	public static List<ClubMember> findAllClubMembers() {
		return findAllClubMembers(false);
	}

	public static List<ClubMember> findAllClubMembers(
			boolean includeDeceasedClubMember) {
		String query;
		if (includeDeceasedClubMember)
			query = "SELECT o FROM ClubMember o ORDER BY o.lastName, o.firstName";
		else
			query = "SELECT o FROM ClubMember o WHERE o.clubMemberState != 4 ORDER BY o.lastName, o.firstName";

		return entityManager().createQuery(query, ClubMember.class)
				.getResultList();
	}

	public static List<ClubMember> findClubMemberEntries(int firstResult,
			int maxResults) {
		return findClubMemberEntries(false, firstResult, maxResults);
	}

	public static List<ClubMember> findClubMemberEntries(
			boolean includeDeceasedClubMember, int firstResult, int maxResults) {
		String query;
		if (includeDeceasedClubMember)
			query = "SELECT o FROM ClubMember o ORDER BY o.lastName, o.firstName";
		else
			query = "SELECT o FROM ClubMember o WHERE o.clubMemberState != 4 ORDER BY o.lastName, o.firstName";

		return entityManager().createQuery(query, ClubMember.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}
}
