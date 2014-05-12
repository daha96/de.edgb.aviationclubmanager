package de.edgb.aviationclubmanager.web.controller;
import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.domain.InstructorPresence;
import de.edgb.aviationclubmanager.domain.validators.InstructorPresenceValidator;
import de.edgb.aviationclubmanager.web.Util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/instructorpresences")
@Controller
@RooWebScaffold(path = "instructorpresences", formBackingObject = InstructorPresence.class)
public class InstructorPresenceController {
	
	@Autowired
	InstructorPresenceValidator instructorPresenceValidator;

	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE_CREATE')")
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@ModelAttribute InstructorPresence instructorPresence, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        
		instructorPresenceValidator.validate(instructorPresence, bindingResult);
		
		if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, instructorPresence);
            return "instructorpresences/create";
        }
        uiModel.asMap().clear();
        instructorPresence.persist();
        return "redirect:/instructorpresences/" + encodeUrlPathSegment(instructorPresence.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE_CREATE')")
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new InstructorPresence());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (ClubMember.findClubMembersBeingInstructor().getResultList().size() == 0) {
            dependencies.add(new String[] { "clubmember", "clubmembers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "instructorpresences/create";
    }

	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE')")
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("instructorpresence", InstructorPresence.findInstructorPresence(id));
        uiModel.addAttribute("itemId", id);
        return "instructorpresences/show";
    }

	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE')")
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("instructorpresences", InstructorPresence.findInstructorPresenceEntries(firstResult, sizeNo));
            float nrOfPages = (float) InstructorPresence.countInstructorPresences() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("instructorpresences", InstructorPresence.findAllInstructorPresences());
        }
        addDateTimeFormatPatterns(uiModel);
        return "instructorpresences/list";
    }

	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE_UPDATE')")
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@ModelAttribute InstructorPresence instructorPresence, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        
		instructorPresenceValidator.validate(instructorPresence, bindingResult);
		
		if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, instructorPresence);
            return "instructorpresences/update";
        }
        uiModel.asMap().clear();
        instructorPresence.merge();
        return "redirect:/instructorpresences/" + encodeUrlPathSegment(instructorPresence.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE_UPDATE')")
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, InstructorPresence.findInstructorPresence(id));
        return "instructorpresences/update";
    }

	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE_DELETE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        InstructorPresence instructorPresence = InstructorPresence.findInstructorPresence(id);
        instructorPresence.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/instructorpresences";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("instructorPresence_presencedate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, InstructorPresence instructorPresence) {
        uiModel.addAttribute("instructorPresence", instructorPresence);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("clubmembers", ClubMember.findClubMembersBeingInstructor().getResultList());
    }
	
	// Finder
	
	@PreAuthorize("hasRole('PERMISSION_INSTRUCTORPRESENCE')")
    @RequestMapping(params = "latest", method = RequestMethod.GET)
    public String findLatestInstructorPresences(Model uiModel) {
        uiModel.addAttribute("instructorpresences", InstructorPresence.findInstructorPresencesByPresenceDateGreaterThanEquals(Util.getCurrentDate()).getResultList());
        uiModel.addAttribute("latest", true);
        addDateTimeFormatPatterns(uiModel);
        return "instructorpresences/list";
    }
}
