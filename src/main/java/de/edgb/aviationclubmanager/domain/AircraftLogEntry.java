package de.edgb.aviationclubmanager.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.springframework.roo.addon.javabean.RooJavaBean;

import de.edgb.aviationclubmanager.web.Util;

@RooJavaBean
//@RooJpaEntity
public class AircraftLogEntry {
	
	private LocalDate entryDate;
	
	private Aircraft aircraft;
	
	private Person pilot;
	
	private int minOccupants;
	
	private int maxOccupants;
	
	public String getOccupants()
	{
		if (minOccupants == maxOccupants)
			return Integer.toString(minOccupants);
		else
			return Integer.toString(minOccupants) + "-" + Integer.toString(maxOccupants);
	}
	
	private String departureLocation;
	
	private String landingLocation;
	
	private LocalTime departureTime;
	
	private LocalTime landingTime;
	
	private Period operationTime;
	
	public String getOperationTimeString() {
        return Util.convertPeriodToString(this.operationTime);
    }
	
	private int numLandings;
	
	private String comments;
	
	
	private static Boolean collectiveLogEntryPossible (Flight previousFlight, Flight flight)
	{
		if (!flight.getAircraft().equals(previousFlight.getAircraft())) return false;
		// Only allow if the previous flight and the current flight departs and lands
		// at the same place.
		if (previousFlight.getFlightMode() != FlightMode.local 	|| 	previousFlight.getFlightMode() != FlightMode.local) return false;
		if (!previousFlight.getDepartureLocation().equals(			previousFlight.getLandingLocation())) return false;
		if (!previousFlight.getLandingLocation().equals( 			flight.getDepartureLocation())) return false;
		if (!flight.getDepartureLocation().equals(					flight.getLandingLocation())) return false;

	/*	// For motor planes: only allow if the flights are towflights.
		// Unknown planes are treated like motor planes.
		if (plane && (plane->category==Plane::categoryGlider || plane->category==Plane::categoryMotorglider))
		{
			return true;
		}
		else
		{
			if (prev->isTowflight () && isTowflight ()) return true;
			return false;
		}*/
		
		return true;
	}
	
	/**
	 * flightList.size() >= 1
	 * @param flightList
	 * @return
	 */
	private static AircraftLogEntry createAircraftLogEntry(List<Flight> flightList)
	{
		AircraftLogEntry entry = new AircraftLogEntry();
		
		// Values directly determined
		Flight flight = flightList.get(0);
		
		entry.entryDate = flight.getFlightDate();
		entry.aircraft = flight.getAircraft();
	//	entry.pilot = flight.getPilot();
		entry.departureLocation = flight.getDepartureLocation();
		entry.departureTime = flight.getDepartureTime();
		
		
		flight = flightList.get(flightList.size() - 1);
		
		entry.landingLocation = flight.getLandingLocation();
		entry.landingTime = flight.getLandingTime();

		// Values determined from all flights
		entry.minOccupants = 0;
		entry.maxOccupants = 0;
		entry.operationTime = new Period(0);
		entry.numLandings = 0;
		List<String> commentList = new ArrayList<>();
		
		for (Flight f : flightList)
		{
			entry.pilot = flight.getPilot();
			
			if (entry.minOccupants==0 || f.getNumPassengers() < entry.minOccupants)
				entry.minOccupants=f.getNumPassengers();
			if (entry.maxOccupants==0 || f.getNumPassengers() > entry.maxOccupants)
				entry.maxOccupants=f.getNumPassengers();
			
			entry.numLandings += 1;
			
			if (f.getDuration() != null)
				entry.operationTime = entry.operationTime.plus(f.getDuration());
			
			if (StringUtils.isNotEmpty(f.getComment()))
				commentList.add(f.getComment());
		}
		
		entry.comments = StringUtils.join(commentList, "; ");
		
		return entry;
	}
	
	public static List<AircraftLogEntry> createAircraftLogEntriesFromFlights(List<Flight> flightList, Aircraft airc)
	{
		List<Flight> fl = new ArrayList<Flight>();
		List<AircraftLogEntry> resultList = new ArrayList<AircraftLogEntry>();
		
		// Nur fertige Flüge des Aircrafts
		for (Flight flight : flightList)
		{
			if (flight.getAircraft().equals(airc) && flight.getFinished())
				fl.add(flight);				
		}
		
		Collections.sort(fl, new Flight.FlightComparator());
		
		// Iterate over all interesting flights, generating logbook entries.
		// Sometimes, we can generate one entry from several flights. These
		// flights are in entryFlights.
		
		List<Flight> entryFlights = new ArrayList<Flight>();
		Flight previousFlight = null;
		for (Flight flight : fl)
		{
			// We accumulate in entryFlights as long as we can merge flights.
			// Then we create an entry, append it to the list and clear
			// entryFlights.
			if (previousFlight != null && !collectiveLogEntryPossible(previousFlight, flight))
			{
				// No further merging
				resultList.add(createAircraftLogEntry(entryFlights));
				entryFlights.clear();
			}
			entryFlights.add(flight);
			previousFlight = flight;
		}
		resultList.add(createAircraftLogEntry(entryFlights));

		return resultList;
	}
	
	public static List<AircraftLogEntry> createAircraftLogEntriesFromFlights(List<Flight> flightList)
	{
		List<Flight> finishedFlightList = new ArrayList<Flight>();
		List<Aircraft> aircs = new ArrayList<Aircraft>();
		List<AircraftLogEntry> resultList = new ArrayList<AircraftLogEntry>();
		
		// Nur fertige Flüge
		for (Flight flight : flightList)
		{
			if (flight.getFinished())
			{
				finishedFlightList.add(flight);
				if (!aircs.contains(flight.getAircraft()))
					aircs.add(flight.getAircraft());
			}
		}
		
		// TODO: Aircrafts sortieren
		
		for (Aircraft ac : aircs)
		{
			resultList.addAll(createAircraftLogEntriesFromFlights(finishedFlightList, ac));
		}
		
		return resultList;
	}

    private Long id;

}
