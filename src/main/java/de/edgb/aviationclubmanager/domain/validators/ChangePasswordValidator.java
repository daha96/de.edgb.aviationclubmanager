package de.edgb.aviationclubmanager.domain.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import de.edgb.aviationclubmanager.domain.ChangePasswordModel;
import de.edgb.aviationclubmanager.domain.validators.AbstractValidator;
import de.edgb.aviationclubmanager.web.UserAccountDetails;

@Component
public class ChangePasswordValidator extends AbstractValidator {

	@Autowired
	ShaPasswordEncoder encoder;

	@Override
	protected void addExtraValidation(Object objectForm, Errors errors) {

		ChangePasswordModel cpm = (ChangePasswordModel) objectForm;
		String originalPassword = ((UserAccountDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal())
				.getUserAccount().getPassword();

		if (!originalPassword.equals(encoder.encodePassword(
				cpm.getOldPassword(), null)))
			errors.rejectValue("oldPassword",
					"de_edgb_aviationclubmanager_domain_changepasswordmodel_oldpassword_invalid");

		if (!cpm.getNewPassword().equals(cpm.getConfirmNewPassword()))
			errors.reject("de_edgb_aviationclubmanager_domain_changepasswordmodel_newpassword_not_equal");

	}

}
