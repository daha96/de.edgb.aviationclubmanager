package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.Aircraft;
import de.edgb.aviationclubmanager.domain.AircraftType;

import java.util.Arrays;

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

@RequestMapping("/aircrafts")
@Controller
@RooWebScaffold(path = "aircrafts", formBackingObject = Aircraft.class)
public class AircraftController {

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFT_CREATE')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Aircraft aircraft, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, aircraft);
			return "aircrafts/create";
		}
		uiModel.asMap().clear();
		aircraft.persist();
		return "redirect:/aircrafts/"
				+ encodeUrlPathSegment(aircraft.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFT_CREATE')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Aircraft());
		return "aircrafts/create";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFT')")
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("aircraft", Aircraft.findAircraft(id));
		uiModel.addAttribute("itemId", id);
		return "aircrafts/show";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFT')")
	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("aircrafts",
					Aircraft.findAircraftEntries(firstResult, sizeNo));
			float nrOfPages = (float) Aircraft.countAircrafts() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("aircrafts", Aircraft.findAllAircrafts());
		}
		return "aircrafts/list";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFT_UPDATE')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Aircraft aircraft, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, aircraft);
			return "aircrafts/update";
		}
		uiModel.asMap().clear();
		aircraft.merge();
		return "redirect:/aircrafts/"
				+ encodeUrlPathSegment(aircraft.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFT_UPDATE')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, Aircraft.findAircraft(id));
		return "aircrafts/update";
	}

	@PreAuthorize("hasRole('PERMISSION_AIRCRAFT_DELETE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Aircraft aircraft = Aircraft.findAircraft(id);
		aircraft.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/aircrafts";
	}

	void populateEditForm(Model uiModel, Aircraft aircraft) {
		uiModel.addAttribute("aircraft", aircraft);
		uiModel.addAttribute("aircrafttypes",
				Arrays.asList(AircraftType.values()));
	}
}
