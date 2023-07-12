package com.infogain.rewardcalculationservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  implements CustomerExistException class to throw
 *  exception when we fetch customer based on customerId,
 *  Customer(s) not exist in DB.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class CustomerExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public CustomerExistException(String message) {
        super(message);
    }
}
