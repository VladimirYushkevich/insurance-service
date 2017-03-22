package com.company.insurance.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class ModuleValidationException extends RuntimeException {

    private static final long serialVersionUID = -9162061119958994072L;

    public ModuleValidationException(String message) {
        super(message);
        log.warn(message);
    }
}
