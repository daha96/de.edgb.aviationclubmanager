package de.edgb.aviationclubmanager.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import de.edgb.aviationclubmanager.web.Util;

@RooJavaBean
@RooToString
@RooJpaActiveRecord//(finders = { "findFlightsByFlightDateBetween" })
public class Flight {

    @NotNull
    @Enumerated
    private FlightType flightType;

    @NotNull
    @ManyToOne
    private Aircraft aircraft;

    @NotNull
    @ManyToOne
    private Person pilot;

    @ManyToOne
    private Person copilot;
    
    public int getNumPassengers()
    {
    	return (copilot != null) ? 2 : 1;
    }

    @NotNull
    @ManyToOne
    private Accounting accounting;

    // ------------------------------------------
    @NotNull
    @Enumerated
    private FlightMode flightMode;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    @Past
    private Date flightDate;

    public LocalDate getFlightDate() {
        return Util.convertDateToLocalDate(this.flightDate);
    }
    
    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = Util.convertLocalDateToDate(flightDate);
    }
    
    @NotNull
    @Enumerated
    private LaunchMethod launchMethod;

    private String departureLocation;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(style = "-S")
//    @Past
    private Date departureTime;

    private String landingLocation;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(style = "-S")
//    @Past
    private Date landingTime;
    
    
    public LocalTime getDepartureTime() {
        return Util.convertDateToLocalTime(this.departureTime);
    }
    
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = Util.convertLocalTimeToDate(departureTime);
    }
    
    public LocalTime getLandingTime() {
        return Util.convertDateToLocalTime(this.landingTime);
    }
    
    public void setLandingTime(LocalTime landingTime) {
        this.landingTime = Util.convertLocalTimeToDate(landingTime);
    }
    
    public Period getDuration()
    {
    	if (departureTime != null)
    	{
    		if (landingTime != null)
    			return new Period(getDepartureTime(), getLandingTime());
    		else if (getLandsHere())
    			return new Period(getDepartureTime(), Util.getCurrentTime());    		
    	}
    	
    	return null;
    }
    
    public String getDurationString()
    {
    	Period period = getDuration();
    	
    	if (period != null)
    		return Util.convertPeriodToString(period);
    	else
    		return null;
    }
    
 /*   // Wird in FlightController.list und im Report flightlist benutzt:
    // Wenn duration verwendet wird, wird Date als Zeit interpretiert und mit der aktuellen TimeZone angezeigt
    public String getDurationString()
    {
    	Date dur = getDuration();
    	
    	if(dur == null)
    		return "";
    	
    	Long time = dur.getTime();
    	Integer diffMinutes = (int)(time / (60 * 1000) % 60);
    	Integer diffHours = (int)(time / (60 * 60 * 1000) % 24);
    	return String.format("%02d", diffHours) + ":" + String.format("%02d", diffMinutes);    	
    }
	
    public Date getDuration()
    {
    	if (departureTime != null)
    	{
    		if (landingTime != null)
    		{
    			return new Date(landingTime.getTime() - departureTime.getTime());/*
    			long diff = landingTime.getTime() - departureTime.getTime();
    			long dminutes = diff / (60 * 1000) % 60;
    			long dhours = diff / (60 * 60 * 1000) % 24;
    			Util.getNullPoint().
    			return new Date(1970, 1, 1, (int)dhours, (int)dminutes);*

    		}
    		// TODO: überprüfen!
    		else if (getLandsHere())
    		{
    			return new Date(Util.getCurrentTime().getTime() - departureTime.getTime());/*
    			long diff = Util.getCurrentTime().getTime() - departureTime.getTime();
    			long dminutes = diff / (60 * 1000) % 60;
    			long dhours = diff / (60 * 60 * 1000) % 24;    			   			
    			return new Date(1970, 1, 1, (int)dhours, (int)dminutes);*
    		}
    	}
    	/*if (departureTime != null)
    	{
    		if (landingTime != null)
    		{
    			long diff = landingTime.getTime() - departureTime.getTime();
    			int diffMinutes = (int)(diff / (60 * 1000) % 60);
    			int diffHours = (int)(diff / (60 * 60 * 1000) % 24);
    			return new Date(1970, 1, 1, diffHours, diffMinutes);
    		}
    		else if (getLandsHere())
    		{
    			long diff = new Date().getTime() - departureTime.getTime();
    			int diffMinutes = (int)(diff / (60 * 1000) % 60);
    			int diffHours = (int)(diff / (60 * 60 * 1000) % 24);
    			return new Date(1970, 1, 1, diffHours, diffMinutes);
    		}
    	}*
    	
    	return null;
    }
*/
    private String comment;

    // ------------------------------------------
    @NotNull
    @ManyToOne
    private Person lastManipulativePerson;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date lastManipulationDate;


    
    public LocalDateTime getLastManipulationDate() {
        return Util.convertDateToLocalDateTime(this.lastManipulationDate);
    }
    
    public void setLastManipulationDate(LocalDateTime lastManipulationDate) {
        this.lastManipulationDate = Util.convertLocalDateTimeToDate(lastManipulationDate);
    }
    
    
    // Finder
    public static List<String> findLocations() {
        EntityManager em = Flight.entityManager();
        // Folgendes gibt unter anderem zweimal den Leerstring aus -> JavaScript-Error im View !!!
        // return em.createQuery("SELECT departureLocation AS location FROM Flight UNION SELECT landingLocation AS location FROM Flight").getResultList();
        List<String> depLoc = em.createQuery("SELECT DISTINCT o.departureLocation FROM Flight AS o", String.class).getResultList();
        List<String> landLoc = em.createQuery("SELECT DISTINCT o.landingLocation FROM Flight AS o", String.class).getResultList();
        for (String s : landLoc) {
            if (!depLoc.contains(s)) depLoc.add(s);
        }
        
        if (depLoc.contains(null))
        	depLoc.remove(null);
        
        Collections.sort(depLoc);
        return depLoc;
    }

    public static List<Flight> findAllFlights() {
        List<Flight> resultList = entityManager().createQuery("SELECT o FROM Flight o", Flight.class).getResultList();
        Collections.sort(resultList, new Flight.FlightComparator());
        return resultList;
    }

    public static List<Flight> findFlightEntries(int firstResult, int maxResults) {
        // TODO: Implement (Sort Porblems)
        throw new NotImplementedException("Sort Promlems!");
    }

    public static List<Flight> findFlightsByFlightDateEquals(LocalDate flightDate) {
        if (flightDate == null) throw new IllegalArgumentException("The flightDate argument is required");
        EntityManager em = Flight.entityManager();
        TypedQuery<Flight> q = em.createQuery("SELECT o FROM Flight AS o WHERE o.flightDate = :flightDate", Flight.class);
        q.setParameter("flightDate", Util.convertLocalDateToDate(flightDate));
        List<Flight> resultList = q.getResultList();
        Collections.sort(resultList, new Flight.FlightComparator());
        return resultList;
    }

    public static List<Flight> findFlightsByPersonAndFlightDateBetweenAndCopilotMode(Person person, LocalDate minFlightDate, LocalDate maxFlightDate, CopilotMode copilotMode) {
    	if (minFlightDate == null) throw new IllegalArgumentException("The minFlightDate argument is required");
        if (maxFlightDate == null) throw new IllegalArgumentException("The maxFlightDate argument is required");
        if (copilotMode == null) throw new IllegalArgumentException("The copilotMode argument is required");
        EntityManager em = Flight.entityManager();
        TypedQuery<Flight> q;
        if (copilotMode == CopilotMode.pilotOnly) q = em.createQuery("SELECT o FROM Flight AS o WHERE o.flightDate BETWEEN :minFlightDate AND :maxFlightDate AND o.pilot = :person", Flight.class); else if (copilotMode == CopilotMode.flightinstructor) {
            q = em.createQuery("SELECT o FROM Flight AS o WHERE o.flightDate BETWEEN :minFlightDate AND :maxFlightDate AND (o.pilot = :person OR (o.copilot = :person AND o.flightType = :type))", Flight.class);
            q.setParameter("type", FlightType.training);
        } else q = em.createQuery("SELECT o FROM Flight AS o WHERE o.flightDate BETWEEN :minFlightDate AND :maxFlightDate AND (o.pilot = :person OR o.copilot = :person)", Flight.class);
        q.setParameter("minFlightDate", Util.convertLocalDateToDate(minFlightDate));
        q.setParameter("maxFlightDate", Util.convertLocalDateToDate(maxFlightDate));
        q.setParameter("person", person);
        List<Flight> resultList = q.getResultList();
        Collections.sort(resultList, new Flight.FlightComparator());
        return resultList;
    }

	public static List<Flight> findFlightsByFlightDateBetween(LocalDate minFlightDate, LocalDate maxFlightDate) {
        if (minFlightDate == null) throw new IllegalArgumentException("The minFlightDate argument is required");
        if (maxFlightDate == null) throw new IllegalArgumentException("The maxFlightDate argument is required");
        EntityManager em = Flight.entityManager();
        TypedQuery<Flight> q = em.createQuery("SELECT o FROM Flight AS o WHERE o.flightDate BETWEEN :minFlightDate AND :maxFlightDate", Flight.class);
        q.setParameter("minFlightDate", Util.convertLocalDateToDate(minFlightDate));
        q.setParameter("maxFlightDate", Util.convertLocalDateToDate(maxFlightDate));
        List<Flight> resultList = q.getResultList();
        Collections.sort(resultList, new Flight.FlightComparator());
        return resultList;
	}
	
	

	public boolean getDepartsHere() {
        return flightMode == FlightMode.local || flightMode == FlightMode.leaving;
    }

    public boolean getLandsHere() {
        return flightMode == FlightMode.local || flightMode == FlightMode.coming;
    }
    
    public boolean getCanDepart() {
        return getDepartsHere() && !getDeparted() && !getLanded();
    }

    public boolean getCanLand() {
        return getLandsHere() && !getLanded() && (!getDepartsHere() || getDeparted());
    }

    public boolean getDeparted() {
        return departureTime != null;
    }

    public boolean getLanded() {
        return landingTime != null;
    }

    public boolean getHappened() {
        return (getDepartsHere() && getDeparted()) || (getLandsHere() && getLanded());
    }

    public boolean getPrepared() {
        return !getHappened();
    }

    public boolean getFinished() {
        return getLandsHere() ? getLanded () : getDeparted();
    }

    private LocalTime getSortTime() {
        if (getDepartsHere() && getDeparted()) return getDepartureTime();
        if (getLandsHere() && getLanded()) return getLandingTime();
        return new LocalTime(0);
    }

    public static class FlightComparator implements Comparator<Flight> {

        @Override
        public int compare(Flight f1, Flight f2) {
            // Both prepared
            if (f1.getPrepared() && f2.getPrepared()) {
                // Incoming prepared before locally departing prepared
                if (f1.getDepartsHere() && !f2.getDepartsHere()) return 1;
                if (!f1.getDepartsHere() && f2.getDepartsHere()) return -1;
                // Flights are equal
                return 0;
            }
            // Prepared flights to the end
            if (f1.getPrepared()) return 1;
            if (f2.getPrepared()) return -1;
            // Sort by effective date
            if (f1.getFlightDate().isAfter(f2.getFlightDate())) return 1;
            if (f1.getFlightDate().isBefore(f2.getFlightDate())) return -1;
            // Sort by effective time
            if (f1.getSortTime().isAfter(f2.getSortTime())) return 1;
            if (f1.getSortTime().isBefore(f2.getSortTime())) return -1;
            return 0;
        }
    }
}
