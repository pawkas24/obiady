package annotation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FutureOrPresentOrNullValidator implements ConstraintValidator<FutureOrPresentOrNull, LocalDate> {

	@Override
	public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
		if(date == null) {
			return true;
		}
		return date.isAfter(LocalDate.now().minusDays(1));
	}

}
