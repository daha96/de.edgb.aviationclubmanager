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
import flexjson.JSONSerializer;

import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.gvnix.web.report.roo.addon.GvNIXReports;

@RequestMapping("/flights")
@Controller
@RooWebScaffold(path = "flights", formBackingObject = Flight.class)
//@GvNIXReports({ "testreport|pdf,xls" })
public class FlightController {

    @Autowired
    FlightValidator flightValidator;

    @Autowired
    MessageSource messageSource;

    @PreAuthorize("hasRole('PERMISSION_FLIGHT_CREATE')")
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@ModelAttribute Flight flight, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        prepareFlight(flight);
        flightValidator.validate(flight, bindingResult);
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, flight);
            return "flights/create";
        }
        uiModel.asMap().clear();
        flight.persist();
        return "redirect:/flights/" + encodeUrlPathSegment(flight.getId().toString(), httpServletRequest);
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT_CREATE')")
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@RequestParam(value = "flightDate", required = false) @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate, Model uiModel) {
        
    	Flight flight = new Flight();
        
    	if (flightDate != null) flight.setFlightDate(flightDate);
        flight.setDepartureLocation(messageSource.getMessage("app.homeLocation", null, LocaleContextHolder.getLocale()));
        flight.setDepartureTime(Util.getCurrentTime());
        
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
    public String update(@ModelAttribute Flight flight, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        prepareFlight(flight);
        flightValidator.validate(flight, bindingResult);
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, flight);
            return "flights/update";
        }
        uiModel.asMap().clear();
        flight.merge();
        return "redirect:/flights/" + encodeUrlPathSegment(flight.getId().toString(), httpServletRequest);
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT_UPDATE')")
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Flight.findFlight(id));
        return "flights/update";
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT_DELETE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Flight flight = Flight.findFlight(id);
        flight.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/flights";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("flight_flightdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("flight_departuretime_date_format", DateTimeFormat.patternForStyle("-S", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("flight_landingtime_date_format", DateTimeFormat.patternForStyle("-S", LocaleContextHolder.getLocale()));
        // Duration
        uiModel.addAttribute("flight_duration_date_format", DateTimeFormat.patternForStyle("-S", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("flight_lastmanipulationdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, Flight flight) {
        uiModel.addAttribute("flight", flight);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("accountings", Accounting.findAllAccountings());
        uiModel.addAttribute("aircrafts", Aircraft.findAllAircrafts());
        uiModel.addAttribute("flightmodes", Arrays.asList(FlightMode.values()));
        uiModel.addAttribute("flighttypes", Arrays.asList(FlightType.values()));
        uiModel.addAttribute("launchmethods", Arrays.asList(LaunchMethod.values()));
        uiModel.addAttribute("people", Person.findAllPeople());
    }

    // Flug aufbereiten
    void prepareFlight(Flight flight) {
        if (flight.getFlightType().equals(FlightType.guest)) flight.setCopilot(null);
        if (flight.getFlightMode().equals(FlightMode.coming)) flight.setDepartureTime((LocalTime)null);
        if (flight.getFlightMode().equals(FlightMode.leaving)) flight.setLandingTime((LocalTime)null);
        flight.setLastManipulativePerson(((UserAccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount().getPerson());
        flight.setLastManipulationDate(Util.getCurrentDateTime());
    }

    // Finder
    @PreAuthorize("hasRole('PERMISSION_FLIGHT')")
    @RequestMapping(value = "/flightlist", params = { "form" }, method = RequestMethod.GET)
    public String flightlistForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "flights/flightlist";
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT')")
    @RequestMapping(value = "/flightlist", method = RequestMethod.GET)
    public String flightlist(@RequestParam(value = "flightDate", required = false) @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate, Model uiModel) {
        LocalDate date;
    	if (flightDate == null)
        	date = Util.getCurrentDate();
    	else
    		date = Util.convertDateToLocalDate(flightDate);
        uiModel.addAttribute("flights", Flight.findFlightsByFlightDateEquals(date));
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("flightlist", true);
        uiModel.addAttribute("flightlistDate", date);
        return "flights/list";
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT')")
    @RequestMapping(value = "/pilotlog", params = { "form" }, method = RequestMethod.GET)
    public String pilotlogForm(Model uiModel) {
        uiModel.addAttribute("copilotModes", Arrays.asList(CopilotMode.values()));
        addDateTimeFormatPatterns(uiModel);
        return "flights/pilotlog";
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT')")
    @RequestMapping(value = "/pilotlog", method = RequestMethod.GET)
    public String pilotlog(@RequestParam("copilotMode") CopilotMode copilotMode, @RequestParam("minFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date minFlightDate, @RequestParam("maxFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date maxFlightDate, Model uiModel) {
        uiModel.addAttribute("flights", Flight.findFlightsByPersonAndFlightDateBetweenAndCopilotMode(((UserAccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount().getPerson(), Util.convertDateToLocalDate(minFlightDate), Util.convertDateToLocalDate(maxFlightDate), copilotMode));
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("pilotlog", true);
        return "flights/list";
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT')")
    @RequestMapping(value = "/flightdb", params = { "form" }, method = RequestMethod.GET)
    public String flightdbForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "flights/flightdb";
    }

    @PreAuthorize("hasRole('PERMISSION_FLIGHT')")
    @RequestMapping(value = "/flightdb", method = RequestMethod.GET)
    public String flightdb(@RequestParam("minFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date minFlightDate, @RequestParam("maxFlightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date maxFlightDate, Model uiModel) {
        uiModel.addAttribute("flights", Flight.findFlightsByFlightDateBetween(Util.convertDateToLocalDate(minFlightDate), Util.convertDateToLocalDate(maxFlightDate)));
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("flightdb", true);
        return "flights/list";
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
    @PreAuthorize("hasRole('PERMISSION_FLIGHT')")
    @RequestMapping(value = "/reports/flightlist", method = RequestMethod.GET)
    public String generateFlightlist(@RequestParam("flightDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date flightDate, @RequestParam(value = "format", required = true) String format, Model uiModel) {
        if (null == format || format.length() <= 0) throw new InvalidParameterException();
        final String REGEX = "(pdf|xls|csv|html)";
        Pattern pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(format);
        if (!matcher.matches()) throw new InvalidParameterException();
        
        LocalDate date = Util.convertDateToLocalDate(flightDate);
        
        Collection<Flight> dataSource = Flight.findFlightsByFlightDateEquals(date);
        if (dataSource.isEmpty()) throw new EmptyResultDataAccessException(0);
        uiModel.addAttribute("format", format);
        uiModel.addAttribute("filename", "Startliste_" + DateTimeFormat.forPattern("yyyy-MM-dd").print(date));
        uiModel.addAttribute("location", messageSource.getMessage("app.homeLocation", null, LocaleContextHolder.getLocale()));
        uiModel.addAttribute("version", messageSource.getMessage("app.version", null, LocaleContextHolder.getLocale()));
        uiModel.addAttribute("numRows", dataSource.size());
        uiModel.addAttribute("reportDate", date);
        uiModel.addAttribute("enumConverter", new Converter<Enum<?>, String>() {

            @Override
            public String convert(Enum<?> value) {
                String output = value.toString();
                try {
                    output = messageSource.getMessage(value.toString(), null, LocaleContextHolder.getLocale());
                } catch (NoSuchMessageException e) {
                    System.err.println("No message resource found for " + value + " add this to the resource bundle");
                }
                return output;
            }
        });
        uiModel.addAttribute("localDateConverter", new Converter<LocalDate, String>() {
            @Override
            public String convert(LocalDate value) {
            	if (value == null)
            		return "";
            	else
            		return DateTimeFormat.mediumDate().print(value);
            }
        });
        uiModel.addAttribute("localTimeConverter", new Converter<LocalTime, String>() {
            @Override
            public String convert(LocalTime value) {
            	if (value == null)
            		return "";
            	else
            		return DateTimeFormat.shortTime().print(value);
            }
        });
        uiModel.addAttribute("flightlistList", dataSource);
        return "flight_flightlist";
    }
    
    
    // Jetzt starten/landen
    
    @PreAuthorize("hasRole('PERMISSION_FLIGHT_UPDATE')")
    @RequestMapping(value = "/{id}", params = "depart", produces = "text/html")
    public String departNow(@PathVariable("id") Long id, Model uiModel) {
    	
    	Flight flight = Flight.findFlight(id);
    /*	
    	Date d = Util.getCurrentDate();
    	Date t = Util.getCurrentTime();
    	Date dt = Util.getCurrentDateTime();
    	Date n = Util.getNullPoint();
    	*/
    	// Nur Flüge von heute!
    	// TODO: Fehlermeldung!
    	if (!flight.getFlightDate().equals(Util.getCurrentDate()))// Util.datesEquals(flight.getFlightDate(), Util.getCurrentDate()))
    		throw new InvalidParameterException();
    	
    	// evtl. Startort setzen
    	if (StringUtils.isEmpty(flight.getDepartureLocation()))
    		flight.setDepartureLocation(messageSource.getMessage("app.homeLocation", null, LocaleContextHolder.getLocale()));
    	
    	flight.setDepartureTime(Util.getCurrentTime());
    	
    	
    	// TODO: Fehlermeldung!
    	Map<String, String> map = new HashMap<String, String>();
    	MapBindingResult errors = new MapBindingResult(map, Flight.class.getName());
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
    	if (!flight.getFlightDate().equals(Util.getCurrentDate()))// Util.datesEquals(flight.getFlightDate(), Util.getCurrentDate()))
    		throw new InvalidParameterException();
    	
    	// evtl. Landeort setzen
    	if (StringUtils.isEmpty(flight.getLandingLocation()))
    		flight.setLandingLocation(messageSource.getMessage("app.homeLocation", null, LocaleContextHolder.getLocale()));
    	
    	flight.setLandingTime(Util.getCurrentTime());
    	    	
    	
    	// TODO: Fehlermeldung!
    	Map<String, String> map = new HashMap<String, String>();
    	MapBindingResult errors = new MapBindingResult(map, Flight.class.getName());
    	flightValidator.validate(flight, errors);
    	if (errors.hasErrors())
    		throw new InvalidParameterException();

    	
        uiModel.asMap().clear();
        flight.merge();
        
        return "redirect:/flights/flightlist";
    }
}
