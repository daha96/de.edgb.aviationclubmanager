package de.edgb.aviationclubmanager.web.controller;
import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.web.UserAccountDetails;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/people")
@Controller
@RooWebScaffold(path = "people", formBackingObject = Person.class)
public class PersonController {

	@PreAuthorize("hasRole('PERMISSION_PERSON_CREATE')")
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Person person, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, person);
            return "people/create";
        }
        uiModel.asMap().clear();
        person.persist();
        return "redirect:/people/" + encodeUrlPathSegment(person.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_PERSON_CREATE')")
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Person());
        return "people/create";
    }

	@PreAuthorize("hasRole('PERMISSION_PERSON')")
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("person", Person.findPerson(id));
        uiModel.addAttribute("itemId", id);
        return "people/show";
    }

	@PreAuthorize("hasRole('PERMISSION_PERSON')")
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("people", Person.findPersonEntries(firstResult, sizeNo));
            float nrOfPages = (float) Person.countPeople() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("people", Person.findAllPeople());
        }
        return "people/list";
    }

	@PreAuthorize("hasRole('PERMISSION_PERSON_UPDATE')")
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Person person, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, person);
            return "people/update";
        }
        uiModel.asMap().clear();
        person.merge();
        return "redirect:/people/" + encodeUrlPathSegment(person.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_PERSON_UPDATE')")
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel, HttpServletRequest httpServletRequest) {
		
		Person person = Person.findPerson(id);		
		
		// wenn Vereinsmitglied verändert werden soll, weiterleiten!
		if (person instanceof ClubMember)
			return "redirect:/clubmembers/" + encodeUrlPathSegment(person.getId().toString(), httpServletRequest) + "?form";
		
        populateEditForm(uiModel, person);
        return "people/update";
    }

	@PreAuthorize("hasRole('PERMISSION_PERSON_DELETE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Person person = Person.findPerson(id);
        
     // TODO: Fehlermeldung!
        // aktueller Benutzer kann nicht gelöscht werden!
        if (person.getId().equals(((UserAccountDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount().getPerson().getId()))
        	throw new AccessDeniedException("Access denied!");
        
        person.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/people";
    }

	void populateEditForm(Model uiModel, Person person) {
        uiModel.addAttribute("person", person);
    }
}
