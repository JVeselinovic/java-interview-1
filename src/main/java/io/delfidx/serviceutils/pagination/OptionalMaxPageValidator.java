package io.delfidx.serviceutils.pagination;

import io.micronaut.context.BeanContext;
import io.micronaut.data.model.Pageable;
import io.micronaut.inject.qualifiers.Qualifiers;
import jakarta.inject.Inject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class OptionalMaxPageValidator implements ConstraintValidator<MaxPageable, Optional<Pageable>> {


    private final BeanContext beanContext;
    private String paginationKey;
    private int overrideMax;

    @Inject
    public OptionalMaxPageValidator(BeanContext beanContext) {
        this.beanContext = beanContext;
    }

    @Override
    public void initialize(MaxPageable constraintAnnotation) {
        paginationKey = constraintAnnotation.paginationKey();
        overrideMax = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Optional<Pageable> value, ConstraintValidatorContext context) {
        if (value.isEmpty()) {
            return true;
        }
        PaginationConfiguration paginationConfig = beanContext.getBean(PaginationConfiguration.class, Qualifiers.byName(paginationKey));
        int maxValue = overrideMax == 0 ? paginationConfig.getMaxAllowedPageSize() : overrideMax;
        boolean isValid = value.get().getSize() >= 0
                && value.get().getSize() <= maxValue;
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Cannot exceed maximum page size: %s".formatted(maxValue))
                    .addConstraintViolation();
        }
        return isValid;
    }
}
