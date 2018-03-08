package com.ezshipp.api.exception;

/**
 * The Class InvalidCurrencyException.
 */
public class InvalidCurrencyException extends BusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7706574249660827109L;

    /**
     * Instantiates a new invalid currency exception.
     */
    public InvalidCurrencyException() {
        super(BusinessExceptionCode.INVALID_CURRENCY);
    }
}
