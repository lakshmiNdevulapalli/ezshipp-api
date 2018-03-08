package com.ezshipp.api.exception;

/**
 * The Class InvalidEmailException.
 */
public class InvalidEmailException extends BusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4733039890486520993L;

    /**
     * Instantiates a new invalid email exception.
     */
    public InvalidEmailException() {
        super(BusinessExceptionCode.INVALID_EMAIL);
    }

}
