package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.UserRole;

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

@RequestMapping("/userroles")
@Controller
@RooWebScaffold(path = "userroles", formBackingObject = UserRole.class)
public class UserRoleController {

	@PreAuthorize("hasRole('PERMISSION_USERROLE_CREATE')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid UserRole userRole, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userRole);
			return "userroles/create";
		}
		uiModel.asMap().clear();
		userRole.persist();
		return "redirect:/userroles/"
				+ encodeUrlPathSegment(userRole.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_USERROLE_CREATE')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new UserRole());

		return "userroles/create";
	}

	@PreAuthorize("hasRole('PERMISSION_USERROLE')")
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("userrole", UserRole.findUserRole(id));
		uiModel.addAttribute("itemId", id);
		return "userroles/show";
	}

	@PreAuthorize("hasRole('PERMISSION_USERROLE')")
	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("userroles",
					UserRole.findUserRoleEntries(firstResult, sizeNo));
			float nrOfPages = (float) UserRole.countUserRoles() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("userroles", UserRole.findAllUserRoles());
		}
		return "userroles/list";
	}

	@PreAuthorize("hasRole('PERMISSION_USERROLE_UPDATE')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid UserRole userRole, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userRole);
			return "userroles/update";
		}
		uiModel.asMap().clear();
		userRole.merge();
		return "redirect:/userroles/"
				+ encodeUrlPathSegment(userRole.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_USERROLE_UPDATE')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, UserRole.findUserRole(id));
		return "userroles/update";
	}

	@PreAuthorize("hasRole('PERMISSION_USERROLE_DELETE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		UserRole userRole = UserRole.findUserRole(id);

		// Abhängigkeiten entfernen (von Auflösungstabelle)
		userRole.getUserPermissions().clear();

		userRole.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/userroles";
	}
}
