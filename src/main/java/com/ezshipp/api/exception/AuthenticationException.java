package com.ezshipp.api.exception;

/**
 * The Authentication Exception.
 */
public class AuthenticationException extends BusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4846523495970718365L;

    /**
     * The Authentication Exception constructor.
     */
    public AuthenticationException() {
        super(BusinessExceptionCode.AUTHENTICATION_FAILED_SECURITY_TOKEN);
    }

    /**
     * The Authentication Exception constructor.
     *
     * @param t the throwable
     */
    public AuthenticationException(Throwable t) {
        super(BusinessExceptionCode.AUTHENTICATION_FAILED_SECURITY_TOKEN, t);
    }

}
