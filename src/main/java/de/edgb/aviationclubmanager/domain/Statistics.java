package de.edgb.aviationclubmanager.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import de.edgb.aviationclubmanager.web.Util;

public class Statistics {
	LocalDate minDate;
	LocalDate maxDate;
	Map<Aircraft, Integer> flightsPerAircraft = new TreeMap<>(
			new Aircraft.AircraftComparator());
	Map<Aircraft, Period> flightTimePerAircraft = new TreeMap<>(
			new Aircraft.AircraftComparator());
	Map<Person, Integer> flightsPerPilot = new TreeMap<>(
			new Person.PersonComparator());
	Map<Person, Period> flightTimePerPilot = new TreeMap<>(
			new Person.PersonComparator());
	Map<FlightType, Integer> flightsPerFlightType = new TreeMap<>();
	Map<LaunchMethod, Integer> flightsPerLaunchMethod = new TreeMap<>();

	public LocalDate getMinDate() {
		return minDate;
	}

	public LocalDate getMaxDate() {
		return maxDate;
	}

	public Map<Aircraft, Integer> getFlightsPerAircraft() {
		return flightsPerAircraft;
	}

	public Map<Aircraft, String> getFlightTimePerAircraft() {
		return convertMapTimeValue(flightTimePerAircraft,
				new Aircraft.AircraftComparator());
	}

	public Map<Person, Integer> getFlightsPerPilot() {
		return flightsPerPilot;
	}

	public Map<Person, String> getFlightTimePerPilot() {
		return convertMapTimeValue(flightTimePerPilot,
				new Person.PersonComparator());
	}

	public Map<FlightType, Integer> getFlightsPerFlightType() {
		return flightsPerFlightType;
	}

	public Map<LaunchMethod, Integer> getFlightsPerLaunchMethod() {
		return flightsPerLaunchMethod;
	}

	public Statistics(LocalDate minDate, LocalDate maxDate) {
		this.minDate = minDate;
		this.maxDate = maxDate;

		List<Flight> flights = Flight.findFlightsByFlightDateBetween(minDate,
				maxDate);

		for (Flight flight : flights) {
			if (!flightsPerAircraft.containsKey(flight.getAircraft()))
				flightsPerAircraft.put(flight.getAircraft(), 1);
			else
				flightsPerAircraft.put(flight.getAircraft(),
						flightsPerAircraft.get(flight.getAircraft()) + 1);
		}

		for (Flight flight : flights) {
			if (flight.getDuration() != null) {
				if (!flightTimePerAircraft.containsKey(flight.getAircraft()))
					flightTimePerAircraft.put(flight.getAircraft(),
							flight.getDuration());
				else
					flightTimePerAircraft.put(flight.getAircraft(),
							flightTimePerAircraft.get(flight.getAircraft())
									.plus(flight.getDuration()));
			}
		}

		for (Flight flight : flights) {
			if (!flightsPerPilot.containsKey(flight.getPilot()))
				flightsPerPilot.put(flight.getPilot(), 1);
			else
				flightsPerPilot.put(flight.getPilot(),
						flightsPerPilot.get(flight.getPilot()) + 1);
		}

		for (Flight flight : flights) {
			if (flight.getDuration() != null) {
				if (!flightTimePerPilot.containsKey(flight.getPilot()))
					flightTimePerPilot.put(flight.getPilot(),
							flight.getDuration());
				else
					flightTimePerPilot.put(
							flight.getPilot(),
							flightTimePerPilot.get(flight.getPilot()).plus(
									flight.getDuration()));
			}
		}

		for (Flight flight : flights) {
			if (!flightsPerFlightType.containsKey(flight.getFlightType()))
				flightsPerFlightType.put(flight.getFlightType(), 1);
			else
				flightsPerFlightType.put(flight.getFlightType(),
						flightsPerFlightType.get(flight.getFlightType()) + 1);
		}

		for (Flight flight : flights) {
			if (!flightsPerLaunchMethod.containsKey(flight.getLaunchMethod()))
				flightsPerLaunchMethod.put(flight.getLaunchMethod(), 1);
			else
				flightsPerLaunchMethod
						.put(flight.getLaunchMethod(), flightsPerLaunchMethod
								.get(flight.getLaunchMethod()) + 1);
		}
	}

	public static <T> Map<T, String> convertMapTimeValue(Map<T, Period> map,
			Comparator<T> comparator) {
		Map<T, String> resultMap = new TreeMap<>(comparator);

		for (Map.Entry<T, Period> entry : map.entrySet()) {
			resultMap.put(entry.getKey(),
					Util.convertPeriodToString(entry.getValue()));
		}

		return resultMap;
	}
}
