package annotation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import obiady.Dinner;

public class HistoryDinnerValidator implements ConstraintValidator<HistoryDinner, Dinner> {

	@Override
	public boolean isValid(Dinner dinner, ConstraintValidatorContext context) {

		if(dinner.getDinnerDetail().getDinnerName().isBlank()) {
			return false;
		}
		return dinner.getAteAt().isBefore(LocalDate.now().plusDays(1));
	}

}
