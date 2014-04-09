package de.edgb.aviationclubmanager.domain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class UserPermission {

    /**
     */
    @NotNull
    private String name;

    /**
     */
  /*  @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<UserRole>();
    */
    
    public GrantedAuthority getAuthority()
    {
    	return new SimpleGrantedAuthority(name);
    }
    
    // TODO: DEBUG
    public static List<GrantedAuthority> getAllAuthorities()
    {
    	List<GrantedAuthority> res = new ArrayList<>();
    	for (UserPermission perm : UserPermission.findAllUserPermissions()) {
			res.add(perm.getAuthority());
		}
    	return res;
    }

	public static List<UserPermission> findAllUserPermissions() {
        return entityManager().createQuery("SELECT o FROM UserPermission o ORDER BY o.id", UserPermission.class).getResultList();
    }

	public static List<UserPermission> findUserPermissionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserPermission o ORDER BY o.id", UserPermission.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
