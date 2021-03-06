package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.Accounting;
import de.edgb.aviationclubmanager.domain.Aircraft;
import de.edgb.aviationclubmanager.domain.CopilotMode;
import de.edgb.aviationclubmanager.domain.Flight;
import de.edgb.aviationclubmanager.domain.FlightMode;
import de.edgb.aviationclubmanager.domain.FlightType;
import de.edgb.aviationclubmanager.domain.LaunchMethod;
import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.domain.validators.FlightValidator;
import de.edgb.aviationclubmanager.web.UserAccountDetails;
import de.edgb.aviationclubmanager.web.Util;
import de.edgb.aviationclubmanager.web.report.FlightListReport;
import flexjson.JSONSerializer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/flights")
@Controller
@RooWebScaffold(path = "flights", formBackingObject = Flight.class)
public class FlightController {

	@Autowired
	FlightValidator flightValidator;

	@Autowired
	MessageSource messageSource;

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_CREATE')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@ModelAttribute Flight flight,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		prepareFlight(flight);
		flightValidator.validate(flight, bindingResult);
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, flight);
			return "flights/create";
		}
		uiModel.asMap().clear();
		flight.persist();
		return "redirect:/flights/flightlist?flightDate="
				+ encodeUrlPathSegment(
						flight.getFlightDate().toString(
								DateTimeFormat.patternForStyle("M-",
										LocaleContextHolder.getLocale())),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_CREATE')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(
			@RequestParam(value = "flightDate", required = false) @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate,
			Model uiModel) {

		Flight flight = new Flight();

		if (flightDate != null)
			flight.setFlightDate(flightDate);

		populateEditForm(uiModel, flight);
		List<String[]> dependencies = new ArrayList<String[]>();
		if (Aircraft.countAircrafts() == 0) {
			dependencies.add(new String[] { "aircraft", "aircrafts" });
		}
		if (Person.countPeople() == 0) {
			dependencies.add(new String[] { "person", "people" });
		}
		if (Accounting.countAccountings() == 0) {
			dependencies.add(new String[] { "accounting", "accountings" });
		}
		if (Person.countPeople() == 0) {
			dependencies.add(new String[] { "person", "people" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "flights/create";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_CREATE')")
	@RequestMapping(params = "prepare", produces = "text/html")
	public String prepareForm(
			@RequestParam(value = "flightDate", required = false) @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate,
			Model uiModel) {

		Flight flight = new Flight();

		if (flightDate != null)
			flight.setFlightDate(flightDate);

		populateEditForm(uiModel, flight);
		List<String[]> dependencies = new ArrayList<String[]>();
		if (Aircraft.countAircrafts() == 0) {
			dependencies.add(new String[] { "aircraft", "aircrafts" });
		}
		if (Person.countPeople() == 0) {
			dependencies.add(new String[] { "person", "people" });
		}
		if (Accounting.countAccountings() == 0) {
			dependencies.add(new String[] { "accounting", "accountings" });
		}
		if (Person.countPeople() == 0) {
			dependencies.add(new String[] { "person", "people" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "flights/prepare";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(produces = "text/html")
	public String list(Model uiModel) {
		return "redirect:/flights/flightlist";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("flight", Flight.findFlight(id));
		uiModel.addAttribute("itemId", id);
		return "flights/show";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_UPDATE')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@ModelAttribute Flight flight,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		prepareFlight(flight);
		flightValidator.validate(flight, bindingResult);
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, flight);
			return "flights/update";
		}
		uiModel.asMap().clear();
		flight.merge();
		return "redirect:/flights/flightlist?flightDate="
				+ encodeUrlPathSegment(
						flight.getFlightDate().toString(
								DateTimeFormat.patternForStyle("M-",
										LocaleContextHolder.getLocale())),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_UPDATE')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, Flight.findFlight(id));
		return "flights/update";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_CREATE')")
	@RequestMapping(value = "/{id}", params = "repeat", produces = "text/html")
	public String repeat(@PathVariable("id") Long id, Model uiModel) {

		Flight flight = Flight.findFlight(id);

		Flight newFlight = Flight.repeatFlight(flight);

		populateEditForm(uiModel, newFlight);

		return "flights/create";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_DELETE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Flight flight = Flight.findFlight(id);
		flight.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/flights";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"flight_flightdate_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"flight_departuretime_date_format",
				DateTimeFormat.patternForStyle("-S",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"flight_landingtime_date_format",
				DateTimeFormat.patternForStyle("-S",
						LocaleContextHolder.getLocale()));
		// Duration
		uiModel.addAttribute(
				"flight_duration_date_format",
				DateTimeFormat.patternForStyle("-S",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"flight_lastmanipulationdate_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, Flight flight) {
		uiModel.addAttribute("flight", flight);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("accountings", Accounting.findAllAccountings());
		uiModel.addAttribute("aircrafts", Aircraft.findAllAircrafts());
		uiModel.addAttribute("flightmodes", Arrays.asList(FlightMode.values()));
		uiModel.addAttribute("flighttypes", Arrays.asList(FlightType.values()));
		uiModel.addAttribute("launchmethods",
				Arrays.asList(LaunchMethod.values()));
		uiModel.addAttribute("people", Person.findAllPeople());
	}

	// Flug aufbereiten
	void prepareFlight(Flight flight) {
		if (flight.getFlightType().equals(FlightType.guest))
			flight.setCopilot(null);
		if (flight.getFlightMode().equals(FlightMode.coming))
			flight.setDepartureTime((LocalTime) null);
		if (flight.getFlightMode().equals(FlightMode.leaving))
			flight.setLandingTime((LocalTime) null);
		flight.setLastManipulativePerson(((UserAccountDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal())
				.getUserAccount().getPerson());
		flight.setLastManipulationDate(Util.getCurrentDateTime());
	}

	// Finder

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/flightlist", params = { "form" }, method = RequestMethod.GET)
	public String flightlistForm(Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		return "flights/flightlist/form";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/flightlist", method = RequestMethod.GET)
	public String flightlist(
			@RequestParam(value = "flightDate", required = false) @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate,
			Model uiModel) {
		LocalDate date;
		if (flightDate == null)
			date = Util.getCurrentDate();
		else
			date = Util.convertDateToLocalDate(flightDate);
		uiModel.addAttribute("flights",
				Flight.findFlightsByFlightDateEquals(date));
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("flightlistDate", date);
		return "flights/flightlist/list";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/pilotlog", params = { "form" }, method = RequestMethod.GET)
	public String pilotlogForm(Model uiModel) {
		uiModel.addAttribute("copilotModes",
				Arrays.asList(CopilotMode.values()));
		addDateTimeFormatPatterns(uiModel);
		return "flights/pilotlog/form";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/pilotlog", params = { "today" }, method = RequestMethod.GET)
	public String pilotlogToday(Model uiModel) {
		uiModel.addAttribute("flights", Flight
				.findFlightsByPersonAndFlightDateBetweenAndCopilotMode(
						((UserAccountDetails) SecurityContextHolder
								.getContext().getAuthentication()
								.getPrincipal()).getUserAccount().getPerson(),
						Util.getCurrentDate(), Util.getCurrentDate(),
						CopilotMode.all));
		addDateTimeFormatPatterns(uiModel);
		return "flights/pilotlog/list";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/pilotlog", params = { "yesterday" }, method = RequestMethod.GET)
	public String pilotlogYesterday(Model uiModel) {
		uiModel.addAttribute(
				"flights",
				Flight.findFlightsByPersonAndFlightDateBetweenAndCopilotMode(
						((UserAccountDetails) SecurityContextHolder
								.getContext().getAuthentication()
								.getPrincipal()).getUserAccount().getPerson(),
						Util.getCurrentDate().minusDays(1), Util
								.getCurrentDate().minusDays(1), CopilotMode.all));
		addDateTimeFormatPatterns(uiModel);
		return "flights/pilotlog/list";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/pilotlog", method = RequestMethod.GET)
	public String pilotlog(
			@RequestParam("copilotMode") CopilotMode copilotMode,
			@RequestParam("minFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date minFlightDate,
			@RequestParam("maxFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date maxFlightDate,
			Model uiModel) {
		uiModel.addAttribute(
				"flights",
				Flight.findFlightsByPersonAndFlightDateBetweenAndCopilotMode(
						((UserAccountDetails) SecurityContextHolder
								.getContext().getAuthentication()
								.getPrincipal()).getUserAccount().getPerson(),
						Util.convertDateToLocalDate(minFlightDate),
						Util.convertDateToLocalDate(maxFlightDate), copilotMode));
		addDateTimeFormatPatterns(uiModel);
		return "flights/pilotlog/list";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/flightdb", params = { "form" }, method = RequestMethod.GET)
	public String flightdbForm(Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		return "flights/flightdb/form";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/flightdb", method = RequestMethod.GET)
	public String flightdb(
			@RequestParam("minFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date minFlightDate,
			@RequestParam("maxFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date maxFlightDate,
			Model uiModel) {
		uiModel.addAttribute(
				"flights",
				Flight.findFlightsByFlightDateBetween(
						Util.convertDateToLocalDate(minFlightDate),
						Util.convertDateToLocalDate(maxFlightDate)));
		addDateTimeFormatPatterns(uiModel);
		return "flights/flightdb/list";
	}

	// AJAX

	// Locations

	@PreAuthorize("hasRole('PERMISSION_FLIGHT')")
	@RequestMapping(value = "/ajax/locations", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getLocations() {
		List<String> locations = Flight.findLocations();
		List<Map<String, String>> resultList = new ArrayList<>();
		for (String s : locations) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", s);
			resultList.add(map);
		}
		return new JSONSerializer().exclude("*.class").serialize(resultList);
	}

	// Reports

	@PreAuthorize("hasRole('PERMISSION_CLUBMEMBER')")
	@RequestMapping(value = "/reports/flightlist", method = RequestMethod.GET)
	public void generateFlightlist(
			@RequestParam(value = "flightDate", required = true) @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate,
			@RequestParam(value = "format", required = true) String format,
			HttpServletResponse response) throws IOException {

		LocalDate date = Util.convertDateToLocalDate(flightDate);

		FlightListReport report = new FlightListReport(messageSource, format,
				date);
		report.writeToHttpServletResponse(response);
	}

	// Jetzt starten / landen

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_UPDATE')")
	@RequestMapping(value = "/{id}", params = "depart", produces = "text/html")
	public String departNow(@PathVariable("id") Long id, Model uiModel) {

		Flight flight = Flight.findFlight(id);

		// Nur Flüge von heute!
		// TODO: Fehlermeldung!
		if (!flight.getFlightDate().equals(Util.getCurrentDate()))
			throw new InvalidParameterException();

		// evtl. Startort setzen
		if (StringUtils.isEmpty(flight.getDepartureLocation()))
			flight.setDepartureLocation(messageSource.getMessage(
					"app.homeLocation", null, LocaleContextHolder.getLocale()));

		flight.setDepartureTime(Util.getCurrentTime());

		// TODO: Fehlermeldung!
		Map<String, String> map = new HashMap<String, String>();
		MapBindingResult errors = new MapBindingResult(map,
				Flight.class.getName());
		flightValidator.validate(flight, errors);
		if (errors.hasErrors())
			throw new InvalidParameterException();

		uiModel.asMap().clear();
		flight.merge();

		return "redirect:/flights/flightlist";
	}

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_UPDATE')")
	@RequestMapping(value = "/{id}", params = "land", produces = "text/html")
	public String landNow(@PathVariable("id") Long id, Model uiModel) {

		Flight flight = Flight.findFlight(id);

		// Nur Flüge von heute!
		// TODO: Fehlermeldung!
		if (!flight.getFlightDate().equals(Util.getCurrentDate()))
			throw new InvalidParameterException();

		// evtl. Landeort setzen
		if (StringUtils.isEmpty(flight.getLandingLocation()))
			flight.setLandingLocation(messageSource.getMessage(
					"app.homeLocation", null, LocaleContextHolder.getLocale()));

		flight.setLandingTime(Util.getCurrentTime());

		// TODO: Fehlermeldung!
		Map<String, String> map = new HashMap<String, String>();
		MapBindingResult errors = new MapBindingResult(map,
				Flight.class.getName());
		flightValidator.validate(flight, errors);
		if (errors.hasErrors())
			throw new InvalidParameterException();

		uiModel.asMap().clear();
		flight.merge();

		return "redirect:/flights/flightlist";
	}

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	ConversionService applicationConversionService;

	String[] headerCodes = new String[] {
			"label_de_edgb_aviationclubmanager_domain_flight_flightdate",
			"label_de_edgb_aviationclubmanager_domain_flight_flighttype",
			"label_de_edgb_aviationclubmanager_domain_flight_aircraft",
			"label_de_edgb_aviationclubmanager_domain_flight_pilot",
			"label_de_edgb_aviationclubmanager_domain_flight_copilot",
			"label_de_edgb_aviationclubmanager_domain_flight_launchmethod",
			"label_de_edgb_aviationclubmanager_domain_flight_departuretime",
			"label_de_edgb_aviationclubmanager_domain_flight_landingtime",
			"label_de_edgb_aviationclubmanager_domain_flight_duration",
			"label_de_edgb_aviationclubmanager_domain_flight_departurelocation",
			"label_de_edgb_aviationclubmanager_domain_flight_landinglocation",
			"label_de_edgb_aviationclubmanager_domain_flight_comment",
			"label_de_edgb_aviationclubmanager_domain_flight_lastmanipulativeperson",
			"label_de_edgb_aviationclubmanager_domain_flight_lastmanipulationdate" };

	@PreAuthorize("hasRole('PERMISSION_FLIGHT_SENDEMAIL')")
	@RequestMapping(value = "/pilotlog/send", produces = "text/html")
	public String sendPilotlogEmails(
			@RequestParam("flightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate,
			HttpServletRequest httpServletRequest, Model uiModel)
			throws UnsupportedEncodingException, NoSuchMessageException,
			MessagingException {

		LocalDate date = Util.convertDateToLocalDate(flightDate);

		String dateString = date.toString(DateTimeFormat.patternForStyle("M-",
				LocaleContextHolder.getLocale()));

		List<Flight> flights = Flight.findFlightsByFlightDateEquals(date);

		Set<Person> persons = new HashSet<Person>();
		for (Flight flight : flights) {
			persons.add(flight.getPilot());
			persons.add(flight.getCopilot());
		}

		for (Person person : persons) {
			if (person != null && StringUtils.isNotBlank(person.getEmail())) {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true,
						"utf-8");
				helper.setFrom(messageSource.getMessage("app.email", null,
						LocaleContextHolder.getLocale()), messageSource
						.getMessage("flightemail_from_label",
								new String[] { messageSource.getMessage(
										"app.homeLocation", null,
										LocaleContextHolder.getLocale()) },
								LocaleContextHolder.getLocale()));
				helper.setTo(person.getEmail());
				helper.setSubject(messageSource.getMessage(
						"flightemail_subject", new String[] { dateString },
						LocaleContextHolder.getLocale()));

				String text = "<html><body><p>"
						+ messageSource.getMessage("flightemail_text",
								new String[] { person.getFirstName(),
										dateString },
								LocaleContextHolder.getLocale()) + "</p>"
						+ "<p><table border=\"1\"><tr>";
				for (String string : headerCodes) {
					text = text
							+ "<th>"
							+ messageSource.getMessage(string, null,
									LocaleContextHolder.getLocale()) + "</th>";
				}
				text = text + "</tr>";

				for (Flight flight : flights) {
					if ((flight.getPilot() != null
							&& (flight.getPilot().equals(person)) || (flight
							.getCopilot() != null && flight.getCopilot()
							.equals(person)))) {
						text = text
								+ "<tr>"
								+ addCell(convertLocalDate(flight
										.getFlightDate()))
								+ addCell(convertValue(flight.getFlightType()))
								+ addCell(convertValue(flight.getAircraft()))
								+ addCell(convertValue(flight.getPilot()))
								+ addCell(convertValue(flight.getCopilot()))
								+ addCell(convertValue(flight.getLaunchMethod()))
								+ addCell(convertLocalTime(flight
										.getDepartureTime()))
								+ addCell(convertLocalTime(flight
										.getLandingTime()))
								+ addCell(convertValue(flight
										.getDurationString()))
								+ addCell(convertValue(flight
										.getDepartureLocation()))
								+ addCell(convertValue(flight
										.getLandingLocation()))
								+ addCell(convertValue(flight.getComment()))
								+ addCell(convertValue(flight
										.getLastManipulativePerson()))
								+ addCell(convertLocalDateTime(flight
										.getLastManipulationDate())) + "</tr>";
					}
				}

				text = text + "</table></p></body></html>";
				helper.setText(text, true);

				mailSender.send(message);
			}
		}

		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("date", date);

		return "flights/pilotlog/mail_success";
	}

	String addCell(String text) {
		return "<td>" + text + "</td>";
	}

	private String convertValue(Object value) {
		if (value == null)
			return "";
		else
			return applicationConversionService.convert(value, String.class);
	}

	private String convertLocalTime(LocalTime value) {
		if (value == null)
			return "";
		else
			return DateTimeFormat.shortTime()
					.withLocale(LocaleContextHolder.getLocale()).print(value);
	}

	private String convertLocalDate(LocalDate value) {
		if (value == null)
			return "";
		else
			return DateTimeFormat.mediumDate()
					.withLocale(LocaleContextHolder.getLocale()).print(value);
	}

	private String convertLocalDateTime(LocalDateTime value) {
		if (value == null)
			return "";
		else
			return DateTimeFormat.mediumDateTime()
					.withLocale(LocaleContextHolder.getLocale()).print(value);
	}
}
