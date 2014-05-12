package de.edgb.aviationclubmanager.domain;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserPermission {

	@NotNull
	private String name;

	public GrantedAuthority getAuthority() {
		return new SimpleGrantedAuthority(name);
	}

	// Finder

	public static List<UserPermission> findAllUserPermissions() {
		return entityManager().createQuery(
				"SELECT o FROM UserPermission o ORDER BY o.id",
				UserPermission.class).getResultList();
	}

	public static List<UserPermission> findUserPermissionEntries(
			int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM UserPermission o ORDER BY o.id",
						UserPermission.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}
}
