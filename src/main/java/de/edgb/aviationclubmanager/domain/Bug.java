package de.edgb.aviationclubmanager.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import de.edgb.aviationclubmanager.web.Util;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Bug {

	// LocalDateTime
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    @NotNull
    private Date bugDate;
    
    public LocalDateTime getBugDate() {
        return Util.convertDateToLocalDateTime(this.bugDate);
    }
    
    public void setBugDate(LocalDateTime bugDate) {
        this.bugDate = Util.convertLocalDateTimeToDate(bugDate);
    }
    
    @ManyToOne
    @NotNull
    private Person person;

    @NotNull
    private String description;

	public static List<Bug> findAllBugs() {
        return entityManager().createQuery("SELECT o FROM Bug o ORDER BY o.id", Bug.class).getResultList();
    }

	public static List<Bug> findBugEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Bug o ORDER BY o.id", Bug.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
