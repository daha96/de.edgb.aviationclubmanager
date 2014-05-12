package de.edgb.aviationclubmanager.domain;

import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserRole {

	@NotNull
	private String name;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>();

	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> resultList = new ArrayList<>();

		for (UserPermission perm : userPermissions) {
			resultList.add(perm.getAuthority());
		}

		return resultList;
	}

	// Finder

	public static List<UserRole> findAllUserRoles() {
		return entityManager().createQuery(
				"SELECT o FROM UserRole o ORDER BY o.name", UserRole.class)
				.getResultList();
	}

	public static List<UserRole> findUserRoleEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM UserRole o ORDER BY o.name",
						UserRole.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}
}
