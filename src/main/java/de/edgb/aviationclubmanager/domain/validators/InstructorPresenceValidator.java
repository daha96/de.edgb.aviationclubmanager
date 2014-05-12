package de.edgb.aviationclubmanager.domain.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import de.edgb.aviationclubmanager.domain.InstructorPresence;

@Component
public class InstructorPresenceValidator extends AbstractValidator {

	@Override
	protected void addExtraValidation(Object objectForm, Errors errors) {

		InstructorPresence instructorPresence = (InstructorPresence) objectForm;

		if (!instructorPresence.getInstructor().getInstructor())
			errors.rejectValue("instructor",
					"de_edgb_aviationclubmanager_domain_instructor_clubmember_is_not_instructor");

	}

}
