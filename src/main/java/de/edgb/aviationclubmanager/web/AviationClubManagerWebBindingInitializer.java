package de.edgb.aviationclubmanager.web;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AviationClubManagerWebBindingInitializer {
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, NumberFormat.getInstance(request.getLocale()), true));
		
		// ohne diesen findet kein ausgehendes Form-Binding statt:
		// http://stackoverflow.com/questions/6575542/spring-checkbox-jsp-tag-is-broken-when-using-converter-for-type-boolean
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(false));
	}
}
