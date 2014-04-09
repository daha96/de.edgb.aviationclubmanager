package de.edgb.aviationclubmanager.web.controller;
import de.edgb.aviationclubmanager.domain.Bug;
import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.domain.validators.BugValidator;
import de.edgb.aviationclubmanager.web.UserAccountDetails;
import de.edgb.aviationclubmanager.web.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/bugs")
@Controller
@RooWebScaffold(path = "bugs", formBackingObject = Bug.class)
public class BugController {
	
	@Autowired
	BugValidator bugValidator;

	@PreAuthorize("hasRole('PERMISSION_BUG_CREATE')")
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@ModelAttribute Bug bug, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        
		// aktuellen Benutzer und Datum setzen
		bug.setBugDate(Util.getCurrentDateTime());
		bug.setPerson(((UserAccountDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount().getPerson());
		
		bugValidator.validate(bug, bindingResult);
		
		if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bug);
            return "bugs/create";
        }
        uiModel.asMap().clear();
        bug.persist();
        return "redirect:/bugs/" + encodeUrlPathSegment(bug.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_BUG_CREATE')")
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Bug());
     /*   List<String[]> dependencies = new ArrayList<String[]>();
        if (Person.countPeople() == 0) {
            dependencies.add(new String[] { "person", "people" });
        }
        uiModel.addAttribute("dependencies", dependencies);
       */ return "bugs/create";
    }

	@PreAuthorize("hasRole('PERMISSION_BUG')")
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("bug", Bug.findBug(id));
        uiModel.addAttribute("itemId", id);
        return "bugs/show";
    }

	@PreAuthorize("hasRole('PERMISSION_BUG')")
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bugs", Bug.findBugEntries(firstResult, sizeNo));
            float nrOfPages = (float) Bug.countBugs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bugs", Bug.findAllBugs());
        }
        addDateTimeFormatPatterns(uiModel);
        return "bugs/list";
    }

	@PreAuthorize("hasRole('PERMISSION_BUG_UPDATE')")
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@ModelAttribute Bug bug, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        
		// aktuellen Benutzer und Datum setzen
		bug.setBugDate(Util.getCurrentDateTime());
		bug.setPerson(((UserAccountDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount().getPerson());
		
		bugValidator.validate(bug, bindingResult);
		
		if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bug);
            return "bugs/update";
        }
        uiModel.asMap().clear();
        bug.merge();
        return "redirect:/bugs/" + encodeUrlPathSegment(bug.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_BUG_UPDATE')")
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Bug.findBug(id));
        return "bugs/update";
    }

	@PreAuthorize("hasRole('PERMISSION_BUG_DELETE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Bug bug = Bug.findBug(id);
        bug.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/bugs";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("bug_bugdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Bug bug) {
        uiModel.addAttribute("bug", bug);
        addDateTimeFormatPatterns(uiModel);
      //  uiModel.addAttribute("people", Person.findAllPeople());
    }
}
