package com.company.insurance.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Slf4j
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3784516691253631702L;

    public NotFoundException() {
        super("The entity you are looking for does not exist");
        log.error("The entity you are looking for does not exist");
    }
}
