package de.edgb.aviationclubmanager.web.controller;

import java.security.Principal;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.edgb.aviationclubmanager.web.Util;

@RequestMapping("/")
@Controller
public class IndexController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String form(Model uiModel, Principal principal) {
		
		// TODO: DEBUG
		
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
	/*	LocalDate d = Util.getCurrentDate();
		LocalTime t = Util.getCurrentTime();
		Date normal = Util.getCurrentDateTime().toDate();
		Date utc = Util.getCurrentDateTime().toDate(TimeZone.getTimeZone("UTC"));
	
		LocalDateTime dt = Util.getCurrentDateTime();
	//	Date n = Util.getNullPoint();
	*/	
		if (principal != null)
			return "index";
		else
			return "redirect:/login";
    }
	
}
