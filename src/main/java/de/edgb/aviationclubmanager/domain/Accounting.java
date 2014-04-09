package de.edgb.aviationclubmanager.domain;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Accounting {

    @NotNull
    private String name;

	public static List<Accounting> findAllAccountings() {
        return entityManager().createQuery("SELECT o FROM Accounting o ORDER BY o.id", Accounting.class).getResultList();
    }

	public static List<Accounting> findAccountingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Accounting o ORDER BY o.id", Accounting.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
