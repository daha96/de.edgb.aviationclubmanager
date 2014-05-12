package de.edgb.aviationclubmanager.web.controller;

import de.edgb.aviationclubmanager.domain.ChangePasswordModelLight;
import de.edgb.aviationclubmanager.domain.Person;
import de.edgb.aviationclubmanager.domain.UserAccount;
import de.edgb.aviationclubmanager.domain.validators.ChangePasswordValidatorLight;
import de.edgb.aviationclubmanager.domain.validators.UserAccountValidator;
import de.edgb.aviationclubmanager.web.UserAccountDetails;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/useraccounts")
@Controller
@RooWebScaffold(path = "useraccounts", formBackingObject = UserAccount.class)
public class UserAccountController {

	@Autowired
	ShaPasswordEncoder encoder;

	@Autowired
	ChangePasswordValidatorLight changePasswordValidatorLight;

	@Autowired
	UserAccountValidator userAccountValidator;

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT_CREATE')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@ModelAttribute UserAccount userAccount,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {

		userAccountValidator.validate(userAccount, bindingResult);

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userAccount);
			return "useraccounts/create";
		}
		uiModel.asMap().clear();

		// Passwort encodieren
		userAccount.setPassword(encoder.encodePassword(
				userAccount.getPassword(), null));

		userAccount.persist();
		return "redirect:/useraccounts/"
				+ encodeUrlPathSegment(userAccount.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT_CREATE')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new UserAccount());
		List<String[]> dependencies = new ArrayList<String[]>();
		if (Person.countPeople() == 0) {
			dependencies.add(new String[] { "person", "people" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "useraccounts/create";
	}

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT')")
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("useraccount", UserAccount.findUserAccount(id));
		uiModel.addAttribute("itemId", id);
		return "useraccounts/show";
	}

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT')")
	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("useraccounts",
					UserAccount.findUserAccountEntries(firstResult, sizeNo));
			float nrOfPages = (float) UserAccount.countUserAccounts() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("useraccounts",
					UserAccount.findAllUserAccounts());
		}
		return "useraccounts/list";
	}

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT_UPDATE')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@ModelAttribute UserAccount userAccount,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {

		// Passwort beibehalten
		userAccount.setPassword(UserAccount
				.findUserAccount(userAccount.getId()).getPassword());

		userAccountValidator.validate(userAccount, bindingResult);

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userAccount);
			return "useraccounts/update";
		}
		uiModel.asMap().clear();
		userAccount.merge();
		return "redirect:/useraccounts/"
				+ encodeUrlPathSegment(userAccount.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT_UPDATE')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, UserAccount.findUserAccount(id));
		return "useraccounts/update";
	}

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT_DELETE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		UserAccount userAccount = UserAccount.findUserAccount(id);

		// TODO: Fehlermeldung!
		// aktueller Benutzer kann nicht gelöscht werden!
		if (userAccount.getId().equals(
				((UserAccountDetails) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal()).getUserAccount()
						.getId()))
			throw new AccessDeniedException("Access denied!");

		// Abhängigkeiten entfernen (von Auflösungstabelle)
		userAccount.getUserRoles().clear();

		userAccount.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/useraccounts";
	}

	// update Password

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT_UPDATE')")
	@RequestMapping(value = "/{id}/password", method = RequestMethod.PUT, produces = "text/html")
	public String updatePassword(
			@PathVariable("id") Long id,
			@ModelAttribute("changePassword") ChangePasswordModelLight changePassword,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {

		changePasswordValidatorLight.validate(changePassword, bindingResult);

		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("changePassword", changePassword);

			return "useraccounts/password/form";
		}
		uiModel.asMap().clear();
		UserAccount ua = UserAccount.findUserAccount(id);
		ua.setPassword(encoder.encodePassword(changePassword.getNewPassword(),
				null));
		ua.merge();
		return "redirect:/useraccounts/"
				+ encodeUrlPathSegment(ua.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasRole('PERMISSION_USERACCOUNT_UPDATE')")
	@RequestMapping(value = "/{id}/password", params = "form", produces = "text/html")
	public String updatePasswordForm(@PathVariable("id") Long id, Model uiModel) {

		uiModel.addAttribute("changePassword", new ChangePasswordModelLight());
		uiModel.addAttribute("id", id);

		return "useraccounts/password/form";
	}
}
