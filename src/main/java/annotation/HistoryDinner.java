package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import constraint.group.HistoryValid;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {HistoryDinnerValidator.class})
public @interface HistoryDinner {

	  String message() default "{}";
	  Class<?>[] groups() default {};
	  Class<? extends Payload>[] payload() default {};
}
