package com.ezshipp.api.exception;

/**
 * The Class InvalidEmailException.
 */
public class InvalidPhoneException extends BusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4733039890486520993L;

    /**
     * Instantiates a new invalid email exception.
     */
    public InvalidPhoneException() {
        super(BusinessExceptionCode.INVALID_PHONE);
    }

    /**
     * Instantiates a new invalid phone exception.
     *
     * @param t the throwable
     */
    public InvalidPhoneException(Throwable t) {
        super(BusinessExceptionCode.INVALID_PHONE, t);
    }

}
