package com.ezshipp.api.exception;

/**
 * The Class TokenizationException.
 */
public class TokenizationException extends BusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4733039890486520993L;

    /**
     * Instantiates a new tokenization exception.
     */
    public TokenizationException() {
        super(BusinessExceptionCode.TOKENIZATION_FAILED_EXCEPTION);
    }

    /**
     * Instantiates a new tokenization exception.
     *
     * @param t the throwable
     */
    public TokenizationException(Throwable t) {
        super(BusinessExceptionCode.TOKENIZATION_FAILED_EXCEPTION, t);
    }

}
