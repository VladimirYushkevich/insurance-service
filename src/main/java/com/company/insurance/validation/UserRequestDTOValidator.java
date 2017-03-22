package com.company.insurance.validation;

import com.company.insurance.dto.UserRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Set;

import static java.util.Objects.isNull;

@Component
public class UserRequestDTOValidator extends AbstractDTOValidator<UserRequestDTO> {

    private static final String FIELD_FIRST_NAME = "firstName";
    private static final String FIELD_LAST_NAME = "lastName";
    private static final String FIELD_RISK_FACTOR = "riskFactor";

    @Override
    protected void validate(UserRequestDTO dto, Errors errors, Set<Class<?>> hints) {

        final Double riskFactor = dto.getRiskFactor();
        if (isNull(errors.getFieldValue(FIELD_RISK_FACTOR))) {
            errors.rejectValue(FIELD_RISK_FACTOR, CODE_FIELD_REQUIRED);
        } else if (riskFactor == 0 || riskFactor >= 1 || riskFactor < 0) {
            errors.rejectValue(FIELD_RISK_FACTOR, CODE_FIELD_OUT_OF_RANGE);
        }

        if (isNull(errors.getFieldValue(FIELD_FIRST_NAME))) {
            errors.rejectValue(FIELD_FIRST_NAME, CODE_FIELD_REQUIRED);
        }

        if (isNull(errors.getFieldValue(FIELD_LAST_NAME))) {
            errors.rejectValue(FIELD_LAST_NAME, CODE_FIELD_REQUIRED);
        }
    }
}
