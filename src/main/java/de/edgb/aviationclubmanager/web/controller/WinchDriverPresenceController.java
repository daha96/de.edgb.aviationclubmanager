package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.ClubMember;
import de.edgb.aviationclubmanager.domain.WinchDriverPresence;
import de.edgb.aviationclubmanager.domain.validators.WinchdriverPresenceValidator;
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

@RequestMapping("/winchdriverpresences")
@Controller
@RooWebScaffold(path = "winchdriverpresences", formBackingObject = WinchDriverPresence.class)
public class WinchDriverPresenceController {

	@Autowired
	WinchdriverPresenceValidator winchdriverPresenceValidator;

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE_CREATE')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(
			@ModelAttribute WinchDriverPresence winchDriverPresence,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {

		winchdriverPresenceValidator.validate(winchDriverPresence,
				bindingResult);

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, winchDriverPresence);
			return "winchdriverpresences/create";
		}
		uiModel.asMap().clear();
		winchDriverPresence.persist();
		return "redirect:/winchdriverpresences/"
				+ encodeUrlPathSegment(winchDriverPresence.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE_CREATE')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new WinchDriverPresence());
		List<String[]> dependencies = new ArrayList<String[]>();
		if (ClubMember.findClubMembersBeingWinchDriver().getResultList().size() == 0) {
			dependencies.add(new String[] { "clubmember", "clubmembers" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "winchdriverpresences/create";
	}

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE')")
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("winchdriverpresence",
				WinchDriverPresence.findWinchDriverPresence(id));
		uiModel.addAttribute("itemId", id);
		return "winchdriverpresences/show";
	}

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE')")
	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("winchdriverpresences", WinchDriverPresence
					.findWinchDriverPresenceEntries(firstResult, sizeNo));
			float nrOfPages = (float) WinchDriverPresence
					.countWinchDriverPresences() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("winchdriverpresences",
					WinchDriverPresence.findAllWinchDriverPresences());
		}
		addDateTimeFormatPatterns(uiModel);
		return "winchdriverpresences/list";
	}

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE_UPDATE')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(
			@ModelAttribute WinchDriverPresence winchDriverPresence,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {

		winchdriverPresenceValidator.validate(winchDriverPresence,
				bindingResult);

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, winchDriverPresence);
			return "winchdriverpresences/update";
		}
		uiModel.asMap().clear();
		winchDriverPresence.merge();
		return "redirect:/winchdriverpresences/"
				+ encodeUrlPathSegment(winchDriverPresence.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE_UPDATE')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel,
				WinchDriverPresence.findWinchDriverPresence(id));
		return "winchdriverpresences/update";
	}

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE_DELETE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		WinchDriverPresence winchDriverPresence = WinchDriverPresence
				.findWinchDriverPresence(id);
		winchDriverPresence.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/winchdriverpresences";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"winchDriverPresence_presencedate_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, WinchDriverPresence winchDriverPresence) {
		uiModel.addAttribute("winchDriverPresence", winchDriverPresence);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("clubmembers", ClubMember
				.findClubMembersBeingWinchDriver().getResultList());
	}

	// Finder

	@PreAuthorize("hasRole('PERMISSION_WINCHDRIVERPRESENCE')")
	@RequestMapping(params = "latest", method = RequestMethod.GET)
	public String findLatestWinchDriverPresences(Model uiModel) {
		uiModel.addAttribute(
				"winchdriverpresences",
				WinchDriverPresence
						.findWinchDriverPresencesByPresenceDateGreaterThanEquals(
								Util.getCurrentDate()).getResultList());
		uiModel.addAttribute("latest", true);
		addDateTimeFormatPatterns(uiModel);
		return "winchdriverpresences/list";
	}
}
