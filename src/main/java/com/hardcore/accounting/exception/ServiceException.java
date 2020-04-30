package com.hardcore.accounting.exception;

import lombok.Data;

/**
 * HC Accounting Service Exception.
 */
@Data
public class ServiceException extends RuntimeException {
    private int statusCode;
    private String errorCode; // biz error code
    private ServiceException.ErrorType errorType; // Service, Client, Unkown

    public enum ErrorType {
        Client,
        Service,
        Unkown
    }

    public ServiceException(String message) {
        super(message);
    }
}
