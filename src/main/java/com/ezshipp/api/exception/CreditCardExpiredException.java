package com.ezshipp.api.exception;

/**
 * The Class CreditCardExpiredException.
 */
public class CreditCardExpiredException extends BusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8137219380444901457L;

    /**
     * Instantiates a new credit card expired exception.
     */
    public CreditCardExpiredException() {
        super(BusinessExceptionCode.CREDIT_CARD_EXPIRED);
    }
}
