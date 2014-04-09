package de.edgb.aviationclubmanager.domain;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord
public class Aircraft {

    @NotNull
    private String registration;

    private String callsign;

    @NotNull
    @Enumerated
    private AircraftType type;

    private String model;

    private String holder;

    @Min(1L)
    @Value("1")
    private int seats;

    private String comment;
    
    
    // Finder
    
    public static TypedQuery<Aircraft> findAircraftsMinTwoSeats() {
    	EntityManager em =  Aircraft.entityManager();
        TypedQuery<Aircraft> q = em.createQuery("SELECT o FROM Aircraft AS o WHERE o.seats >= 2 ORDER BY o.registration", Aircraft.class);
        return q;
    }
    
	public static List<Aircraft> findAllAircrafts() {
        return entityManager().createQuery("SELECT o FROM Aircraft o ORDER BY o.registration", Aircraft.class).getResultList();
    }

	public static List<Aircraft> findAircraftEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Aircraft o ORDER BY o.registration", Aircraft.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	public static class AircraftComparator implements Comparator<Aircraft> {

        @Override
        public int compare(Aircraft a1, Aircraft a2) {
            return a1.registration.compareTo(a2.registration);
        }
    }
}
