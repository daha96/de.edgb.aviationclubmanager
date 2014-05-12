package de.edgb.aviationclubmanager.domain.validators;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import de.edgb.aviationclubmanager.domain.Aircraft;
import de.edgb.aviationclubmanager.domain.AircraftType;
import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.domain.Flight;
import de.edgb.aviationclubmanager.domain.FlightMode;
import de.edgb.aviationclubmanager.domain.FlightType;
import de.edgb.aviationclubmanager.domain.LaunchMethod;
import de.edgb.aviationclubmanager.domain.Person;

@Component
public class FlightValidator extends AbstractValidator {

	@Override
	protected void addExtraValidation(Object objectForm, Errors errors) {

		Flight flight = (Flight) objectForm;

		// --------------------------------------

		FlightType flightType = flight.getFlightType();
		Aircraft aircraft = flight.getAircraft();
		Person pilot = flight.getPilot();
		Person copilot = flight.getCopilot();

		// Copilot im Einsitzer
		if (aircraft.getSeats() <= 1 && copilot != null)
			errors.rejectValue("copilot",
					"de_edgb_aviationclubmanager_domain_flight_copilot_in_single_seater");

		// Gastflug im Einsitzer
		if (aircraft.getSeats() <= 1 && flightType.equals(FlightType.guest))
			errors.rejectValue("aircraft",
					"de_edgb_aviationclubmanager_domain_flight_guestflight_in_single_seater");

		// Pilot gleich Copilot
		if (copilot != null && pilot.equals(copilot))
			errors.rejectValue("copilot",
					"de_edgb_aviationclubmanager_domain_flight_pilot_equals_copilot");

		// Normalflug mit Flugschüler als Pilot
		if (flightType.equals(FlightType.normal) && pilot instanceof ClubMember
				&& ((ClubMember) pilot).getStudent())
			errors.rejectValue("pilot",
					"de_edgb_aviationclubmanager_domain_flight_pilot_is_student");

		// Gastflug mit Flugschüler als Pilot
		if (flightType.equals(FlightType.guest) && pilot instanceof ClubMember
				&& ((ClubMember) pilot).getStudent())
			errors.rejectValue("pilot",
					"de_edgb_aviationclubmanager_domain_flight_pilot_is_student");

		// Schulungsflug ohne Flugschüler als Pilot
		if (flightType.equals(FlightType.training)
				&& !(pilot instanceof ClubMember && ((ClubMember) pilot)
						.getStudent()))
			errors.rejectValue("pilot",
					"de_edgb_aviationclubmanager_domain_flight_pilot_is_not_student");

		// Schulungsflug ohne Flugschüler als Pilot
		if (flightType.equals(FlightType.training)
				&& copilot != null
				&& !(copilot instanceof ClubMember && ((ClubMember) copilot)
						.getInstructor()))
			errors.rejectValue("copilot",
					"de_edgb_aviationclubmanager_domain_flight_copilot_is_not_instructor");

		// --------------------------------------

		FlightMode flightMode = flight.getFlightMode();
		LaunchMethod launchMethod = flight.getLaunchMethod();
		String departureLocation = flight.getDepartureLocation();
		LocalTime departureTime = flight.getDepartureTime();
		String landingLocation = flight.getLandingLocation();
		LocalTime landingTime = flight.getLandingTime();

		// Eigenstart mit Segelflugzeug
		if (launchMethod.equals(LaunchMethod.self)
				&& aircraft.getType().equals(AircraftType.glider))
			errors.rejectValue("launchMethod",
					"de_edgb_aviationclubmanager_domain_flight_selflaunch_with_glider");

		// Kein Eigenstart mit einem Motorflugzeug, Ultraleichtflugzeug, oder
		// Helikopter
		if (!launchMethod.equals(LaunchMethod.self)
				&& (aircraft.getType().equals(AircraftType.plane)
						|| aircraft.getType().equals(AircraftType.lightPlane) || aircraft
						.getType().equals(AircraftType.helicopter)))
			errors.rejectValue(
					"launchMethod",
					"de_edgb_aviationclubmanager_domain_flight_not_selflaunch_with_plane_or_lightPlane_or_helicopter");

		// Startzeit ohne Startort
		if (departureTime != null && StringUtils.isBlank(departureLocation))
			errors.rejectValue(
					"departureLocation",
					"de_edgb_aviationclubmanager_domain_flight_departureTime_without_departureLocation");

		// Landezeit ohne Landeort
		if (landingTime != null && StringUtils.isBlank(landingLocation))
			errors.rejectValue("landingLocation",
					"de_edgb_aviationclubmanager_domain_flight_landingTime_without_landingLocation");

		// Landezeit ohne Startzeit
		if (flightMode.equals(FlightMode.local) && landingTime != null
				&& departureTime == null)
			errors.rejectValue("departureTime",
					"de_edgb_aviationclubmanager_domain_flight_landingTime_without_departureTime");

		// Landezeit vor Startzeit
		if (landingTime != null && departureTime != null
				&& landingTime.isBefore(departureTime))
			errors.rejectValue("landingTime",
					"de_edgb_aviationclubmanager_domain_flight_landingTime_before_departureTime");
	}

}
