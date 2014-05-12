package de.edgb.aviationclubmanager.domain.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import de.edgb.aviationclubmanager.domain.ChangePasswordModelLight;
import de.edgb.aviationclubmanager.domain.validators.AbstractValidator;

@Component
public class ChangePasswordValidatorLight extends AbstractValidator {

	@Override
	protected void addExtraValidation(Object objectForm, Errors errors) {

		ChangePasswordModelLight cpm = (ChangePasswordModelLight) objectForm;

		if (!cpm.getNewPassword().equals(cpm.getConfirmNewPassword()))
			errors.reject("de_edgb_aviationclubmanager_domain_changepasswordmodellight_passwords_not_equal");
	}

}
