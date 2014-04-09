package de.edgb.aviationclubmanager.domain.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserAccountValidator extends AbstractValidator {

	@Override
	protected void addExtraValidation(Object objectForm, Errors errors) {
	}

}
