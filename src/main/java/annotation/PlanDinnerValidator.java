package annotation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import obiady.Dinner;

public class PlanDinnerValidator implements ConstraintValidator<PlanDinner, Dinner> {

	@Override
	public boolean isValid(Dinner dinner, ConstraintValidatorContext context) {
		if(dinner.getDinnerDetail().getDinnerName().isBlank()) {
			return false;
		}
		if(dinner.getAteAt() == null) {
			return true;
		}
		return dinner.getAteAt().isAfter(LocalDate.now().minusDays(1));
		
	}

}
