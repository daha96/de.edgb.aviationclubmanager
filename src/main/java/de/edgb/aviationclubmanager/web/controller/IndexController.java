package de.edgb.aviationclubmanager.web.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/")
@Controller
public class IndexController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String form(Model uiModel, Principal principal) {
		if (principal != null)
			return "index";
		else
			return "redirect:/login";
    }
	
}
