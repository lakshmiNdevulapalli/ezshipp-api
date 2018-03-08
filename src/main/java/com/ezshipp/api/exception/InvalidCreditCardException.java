package com.ezshipp.api.exception;

/**
 * The Class InvalidCreditCardException.
 * 
 */
public class InvalidCreditCardException extends BusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new invalid credit card exception.
     */
    public InvalidCreditCardException() {
        super(BusinessExceptionCode.INVALID_CREDIT_CARD);
    }
}
