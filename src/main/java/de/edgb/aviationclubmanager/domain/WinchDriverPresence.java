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
@RooJpaActiveRecord//(finders = { "findWinchDriverPresencesByPresenceDateEquals" })
public class WinchDriverPresence {

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
    private ClubMember winchDriver;

    private String comment;

	public static List<WinchDriverPresence> findAllWinchDriverPresences() {
        return entityManager().createQuery("SELECT o FROM WinchDriverPresence o ORDER BY presenceDate", WinchDriverPresence.class).getResultList();
    }

	public static List<WinchDriverPresence> findWinchDriverPresenceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM WinchDriverPresence o ORDER BY presenceDate", WinchDriverPresence.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static TypedQuery<WinchDriverPresence> findWinchDriverPresencesByPresenceDateGreaterThanEquals(LocalDate presenceDate) {
        if (presenceDate == null) throw new IllegalArgumentException("The presenceDate argument is required");
        EntityManager em = WinchDriverPresence.entityManager();
        TypedQuery<WinchDriverPresence> q = em.createQuery("SELECT o FROM WinchDriverPresence AS o WHERE o.presenceDate >= :presenceDate ORDER BY presenceDate", WinchDriverPresence.class);
        q.setParameter("presenceDate", Util.convertLocalDateToDate(presenceDate));
        return q;
    }
}
