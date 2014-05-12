package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.ClubCapacity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/clubcapacitys")
@Controller
@RooWebScaffold(path = "clubcapacitys", formBackingObject = ClubCapacity.class)
public class ClubCapacityController {

	@PreAuthorize("hasRole('PERMISSION_CLUBCAPACITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ClubCapacity clubCapacity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, clubCapacity);
            return "clubcapacitys/create";
        }
        uiModel.asMap().clear();
        clubCapacity.persist();
        return "redirect:/clubcapacitys/" + encodeUrlPathSegment(clubCapacity.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_CLUBCAPACITY_CREATE')")
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ClubCapacity());
        return "clubcapacitys/create";
    }

	@PreAuthorize("hasRole('PERMISSION_CLUBCAPACITY')")
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("clubcapacity", ClubCapacity.findClubCapacity(id));
        uiModel.addAttribute("itemId", id);
        return "clubcapacitys/show";
    }

	@PreAuthorize("hasRole('PERMISSION_CLUBCAPACITY')")
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("clubcapacitys", ClubCapacity.findClubCapacityEntries(firstResult, sizeNo));
            float nrOfPages = (float) ClubCapacity.countClubCapacitys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("clubcapacitys", ClubCapacity.findAllClubCapacitys());
        }
        return "clubcapacitys/list";
    }

	@PreAuthorize("hasRole('PERMISSION_CLUBCAPACITY_UPDATE')")
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ClubCapacity clubCapacity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, clubCapacity);
            return "clubcapacitys/update";
        }
        uiModel.asMap().clear();
        clubCapacity.merge();
        return "redirect:/clubcapacitys/" + encodeUrlPathSegment(clubCapacity.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasRole('PERMISSION_CLUBCAPACITY_UPDATE')")
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ClubCapacity.findClubCapacity(id));
        return "clubcapacitys/update";
    }

	@PreAuthorize("hasRole('PERMISSION_CLUBCAPACITY_DELETE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ClubCapacity clubCapacity = ClubCapacity.findClubCapacity(id);
        clubCapacity.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/clubcapacitys";
    }

	void populateEditForm(Model uiModel, ClubCapacity clubCapacity) {
        uiModel.addAttribute("clubCapacity", clubCapacity);
    }
}
