package de.edgb.aviationclubmanager.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findUserAccountsByUsernameEquals" })
public class UserAccount {

	@NotNull
	@Column(unique = true)
	private String username;

	@NotNull
	private String password;

	@NotNull
	private Boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	@OneToOne
	@NotNull
	private Person person;

	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> resultList = new ArrayList<>();
		for (UserRole role : userRoles) {
			resultList.addAll(role.getAuthorities());
		}
		return resultList;
	}

	// Finder

	public static List<UserAccount> findAllUserAccounts() {
		return entityManager().createQuery(
				"SELECT o FROM UserAccount o ORDER BY o.username",
				UserAccount.class).getResultList();
	}

	public static List<UserAccount> findUserAccountEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM UserAccount o ORDER BY o.username",
						UserAccount.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}
}
