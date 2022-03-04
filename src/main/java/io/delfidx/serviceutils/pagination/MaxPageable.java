package io.delfidx.serviceutils.pagination;

import io.micronaut.core.annotation.Introspected;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Valid
@Documented
@Constraint(validatedBy = {MaxPageableValidator.class, OptionalMaxPageValidator.class})
@Target({PARAMETER, FIELD, TYPE_PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Introspected
public @interface MaxPageable {
    String message() default "Cannot exceed maximum page size: {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Min(0)
    int value() default 0;// Use the default max value defined in the validator
    String paginationKey() default "default";
}
