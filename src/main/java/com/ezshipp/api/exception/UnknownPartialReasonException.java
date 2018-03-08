package com.ezshipp.api.exception;

import java.util.List;

/**
 * The Class UnknownPartialReasonException.
 * 
 */
public class UnknownPartialReasonException extends PartialSuccessServiceException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 160279971919243L;

    /**
     * The unknown partial reason service exception.
     * 
     * @param code the service exception code
     * @param reservations the reservation dt os
     */
    public UnknownPartialReasonException(ServiceExceptionCode code, List reservations) {
        super(code, reservations);
    }

    /**
     * The unknown partial reason service exception.
     * 
     * @param code the service exception code
     * @param t the t
     * @param reservations the reservation dt os
     */
    public UnknownPartialReasonException(ServiceExceptionCode code, Throwable t, List reservations) {
        super(code, t, reservations);
    }

}
