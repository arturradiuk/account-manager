package com.example.account_manager.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SuppressWarnings("unused")

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RUNTIME)
@Size(min = 1, max = 25)
@Pattern(regexp = Regex.SECOND_NAME, message = "{com.example.account_manager.validation.error.lastName}")
public @interface SecondName {
    String message() default "{com.example.account_manager.validation.error.lastName}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
