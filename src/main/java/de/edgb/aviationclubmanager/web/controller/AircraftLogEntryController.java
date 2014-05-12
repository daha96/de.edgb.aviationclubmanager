package de.edgb.aviationclubmanager.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import de.edgb.aviationclubmanager.domain.Aircraft;
import de.edgb.aviationclubmanager.domain.AircraftLogEntry;
import de.edgb.aviationclubmanager.domain.Flight;
import de.edgb.aviationclubmanager.web.Util;

@RequestMapping("/aircraftlog")
@Controller
public class AircraftLogEntryController {

	@Autowired
	MessageSource messageSource;

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFTLOG')")
	@RequestMapping(params = { "form" }, method = RequestMethod.GET)
	public String aircraftlogForm(Model uiModel) {
		addDateTimeFormatPatterns(uiModel);

		uiModel.addAttribute("aircrafts", Aircraft.findAllAircrafts());
		return "aircraftlog/find";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFTLOG')")
	@RequestMapping(params = { "form" }, value = "/clubaircraft", method = RequestMethod.GET)
	public String clubaircraftlogForm(Model uiModel) {
		addDateTimeFormatPatterns(uiModel);

		return "aircraftlog/find_clubaircraftlog";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFTLOG')")
	@RequestMapping(produces = "text/html", params = { "today" }, value = "/clubaircraft")
	public String listClubaircraftToday(Model uiModel) {

		List<Flight> fl = Flight.findFlightsByFlightDateEqualsAndClubaircraft(
				Util.getCurrentDate(),
				messageSource.getMessage("app.club", null,
						LocaleContextHolder.getLocale()));

		if (fl.size() > 0)
			uiModel.addAttribute("aircraftLogEntries",
					AircraftLogEntry.createAircraftLogEntriesFromFlights(fl));
		else
			uiModel.addAttribute("aircraftLogEntries", null);

		addDateTimeFormatPatterns(uiModel);
		return "aircraftlog/list";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFTLOG')")
	@RequestMapping(produces = "text/html", params = { "yesterday" }, value = "/clubaircraft")
	public String listClubaircraftYesterday(Model uiModel) {

		List<Flight> fl = Flight.findFlightsByFlightDateEqualsAndClubaircraft(
				Util.getCurrentDate().minusDays(1),
				messageSource.getMessage("app.club", null,
						LocaleContextHolder.getLocale()));

		if (fl.size() > 0)
			uiModel.addAttribute("aircraftLogEntries",
					AircraftLogEntry.createAircraftLogEntriesFromFlights(fl));
		else
			uiModel.addAttribute("aircraftLogEntries", null);

		addDateTimeFormatPatterns(uiModel);
		return "aircraftlog/list";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFTLOG')")
	@RequestMapping(produces = "text/html", value = "/clubaircraft")
	public String listClubaircraft(
			@RequestParam @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate,
			Model uiModel) {

		List<Flight> fl = Flight.findFlightsByFlightDateEqualsAndClubaircraft(
				Util.convertDateToLocalDate(flightDate),
				messageSource.getMessage("app.club", null,
						LocaleContextHolder.getLocale()));

		if (fl.size() > 0)
			uiModel.addAttribute("aircraftLogEntries",
					AircraftLogEntry.createAircraftLogEntriesFromFlights(fl));
		else
			uiModel.addAttribute("aircraftLogEntries", null);

		addDateTimeFormatPatterns(uiModel);
		return "aircraftlog/list";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFTLOG')")
	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate,
			@RequestParam(required = false) Aircraft aircraft, Model uiModel) {

		List<Flight> fl;

		if (aircraft != null)
			fl = Flight.findFlightsByFlightDateEqualsAndAircraft(
					Util.convertDateToLocalDate(flightDate), aircraft);
		else
			fl = Flight.findFlightsByFlightDateEquals(Util
					.convertDateToLocalDate(flightDate));

		if (fl.size() > 0)
			uiModel.addAttribute("aircraftLogEntries",
					AircraftLogEntry.createAircraftLogEntriesFromFlights(fl));
		else
			uiModel.addAttribute("aircraftLogEntries", null);

		addDateTimeFormatPatterns(uiModel);
		return "aircraftlog/list";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"aircraftlogentry_flightdate_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"aircraftlogentry_departuretime_date_format",
				DateTimeFormat.patternForStyle("-S",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"aircraftlogentry_landingtime_date_format",
				DateTimeFormat.patternForStyle("-S",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"aircraftlogentry_operationtime_date_format",
				DateTimeFormat.patternForStyle("-S",
						LocaleContextHolder.getLocale()));
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
}
