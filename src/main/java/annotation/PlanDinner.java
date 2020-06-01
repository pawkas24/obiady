package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import constraint.group.PlannerValid;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PlanDinnerValidator.class})
public @interface PlanDinner {

	  String message() default "{obiady.Dinner.PlanDinner}";
	  Class<?>[] groups() default {};
	  Class<? extends Payload>[] payload() default {};
}
