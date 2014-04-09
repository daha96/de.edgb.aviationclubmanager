package de.edgb.aviationclubmanager.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.edgb.aviationclubmanager.domain.ChangePasswordModel;
import de.edgb.aviationclubmanager.domain.Flight;
import de.edgb.aviationclubmanager.domain.Statistics;
import de.edgb.aviationclubmanager.domain.UserAccount;
import de.edgb.aviationclubmanager.web.UserAccountDetails;
import de.edgb.aviationclubmanager.web.Util;

@RequestMapping("/statistics")
@Controller
public class StatisticsController {
	
	@PreAuthorize("hasRole('PERMISSION_STATISTICS')")
    @RequestMapping(params = { "form" }, method = RequestMethod.GET)
    public String form(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "statistics/find";
    }

    @PreAuthorize("hasRole('PERMISSION_STATISTICS')")
    @RequestMapping(method = RequestMethod.GET)
    public String show(@RequestParam("minDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date minDate, @RequestParam("maxDate") @org.springframework.format.annotation.DateTimeFormat(style = "M-") Date maxDate, Model uiModel) {
        
    	Statistics stats = new Statistics(Util.convertDateToLocalDate(minDate), Util.convertDateToLocalDate(maxDate));
    	
    	uiModel.addAttribute("statistics", stats);
    	uiModel.addAttribute("flightsPerAircraft", stats.getFlightsPerAircraft().entrySet());
        
        
        
        addDateTimeFormatPatterns(uiModel);
        return "statistics/show";
    }
    
    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("statistics_date_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("statistics_time_date_format", DateTimeFormat.patternForStyle("-S", LocaleContextHolder.getLocale()));
       /* uiModel.addAttribute("flight_landingtime_date_format", DateTimeFormat.patternForStyle("-S", LocaleContextHolder.getLocale()));
        // Duration
        uiModel.addAttribute("flight_duration_date_format", DateTimeFormat.patternForStyle("-S", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("flight_lastmanipulationdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    */}	
}