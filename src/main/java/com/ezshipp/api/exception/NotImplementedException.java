package com.ezshipp.api.exception;

/**
 * The Class NotImplementedException.
 */
public class NotImplementedException extends ServiceException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2827020683670996344L;

    /**
     * Instantiates a new invalid rate type exception.
     */
    public NotImplementedException() {
        super(ServiceExceptionCode.NOT_IMPLEMENTED);
    }

}
