package de.edgb.aviationclubmanager.domain.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import de.edgb.aviationclubmanager.domain.WinchDriverPresence;

@Component
public class WinchdriverPresenceValidator extends AbstractValidator {

	@Override
	protected void addExtraValidation(Object objectForm, Errors errors) {

		WinchDriverPresence winchDriverPresence = (WinchDriverPresence) objectForm;

		if (!winchDriverPresence.getWinchDriver().getWinchDriver())
			errors.rejectValue(
					"winchDriver",
					"de_edgb_aviationclubmanager_domain_winchdriverpresence_clubmember_is_not_winchdriver");

	}

}
