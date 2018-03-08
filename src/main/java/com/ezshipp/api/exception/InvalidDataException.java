package com.ezshipp.api.exception;

import java.util.List;

/**
 * The Class InvalidDataException.
 */
public class InvalidDataException extends MultiBusinessException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new multi business exception.
     *
     * @param exception the exception
     */
    public InvalidDataException(BusinessException exception) {
        super(exception);
    }

    /**
     * This constructor provides a way to encapsulate an specific error into a more generic error. For example, the INVALID_INPUT error code, the root cause of the exception could
     * be a bad id or an empty description. Therefore, the INVALID_INPUT error code is sent, but with the specific error message.
     *
     * @param exception the exception
     * @param rootCauseExceptionCode the rootCauseExceptionCode
     */
    public InvalidDataException(BusinessException exception, BusinessExceptionCode rootCauseExceptionCode) {
        super(exception);
        BusinessException rootException = new BusinessException(rootCauseExceptionCode);
        String message = rootException.getErrorMessage();
        exception.customFields = new String[] { message };
    }

    /**
     * This constructor provides a way to encapsulate an specific error into a more generic error. For example, the INVALID_INPUT error code, the root cause of the exception could
     * be a bad id or an empty description. Therefore, the INVALID_INPUT error code is sent, but with the specific error message.
     *
     * @param exception the exception
     * @param rootCauseExceptionCode the rootCauseExceptionCode
     * @param params the params
     */
    public InvalidDataException(BusinessException exception, BusinessExceptionCode rootCauseExceptionCode, String[] params) {
        super(exception);
        BusinessException rootException = new BusinessException(rootCauseExceptionCode, params);
        String message = rootException.getErrorMessage();
        exception.customFields = new String[] { message };
    }

    /**
     * Instantiates a new multi business exception.
     *
     * @param exception the exception
     * @param params the params
     */
    public InvalidDataException(BusinessException exception, String[] params) {
        super(exception, params);
    }

    /**
     * Instantiates a new multi business exception.
     *
     * @param exception the exception
     */
    public InvalidDataException(List<BusinessException> exception) {
        super(exception);
    }

}
