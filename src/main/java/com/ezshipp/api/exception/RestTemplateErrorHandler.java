package com.ezshipp.api.exception;


import com.ezshipp.api.http.IHttpStatus;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class RestTemplateErrorHandler.
 *
 * This Class is responsible for properly propagating the exceptions from services. In addition, it handles the 567 code, which the RestTemplate DefaultResponseErrorHandler does
 * not recognize and therefore throws an UnknownHttpStatusCodeException, which we don't want.
 */
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    /** The Constant ERROR_CODE_HEADER. */
    protected static final String ERROR_CODE_HEADER = "lq_error_code";

    /** The Constant ERROR_TYPE_HEADER. */
    protected static final String ERROR_TYPE_HEADER = "lq_error_type";

    /** The log. */
    protected static final Logger LOG = LoggerFactory.getLogger(RestTemplateErrorHandler.class);

    /** /** The default error handler. */
    private final ResponseErrorHandler defaultErrorHandler = new DefaultResponseErrorHandler();

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.client.ResponseErrorHandler#handleError(org.springframework.http.client.ClientHttpResponse)
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // This code is responsible for properly propagating the exceptions from services

        String errorCode = getErrorCode(response);
        String errorType = getErrorType(response);

        if (errorCode == null && errorType == null) {
            InputStream inputStream = response.getBody();
            String bodyMessage = IOUtils.toString(inputStream);
            raiseForBusinessException(bodyMessage);
        }

        switch (response.getRawStatusCode()) {
        case IHttpStatus.BUSINESS_EXCEPTION_REPONSE_CODE:
            raiseForBusinessException(errorCode, errorType);
            break;
        case IHttpStatus.SERVICE_EXCEPTION_REPONSE_CODE:
            // Special case for unchecked exceptions from services.
            // If an unchecked exception comes back from services, it will have a 567 error code in the header AND http status code.
            if ("567".equals(errorCode)) {
                raiseForUncheckedException(errorType);
            }
            raiseForServiceException(errorCode, errorType);
            break;
        default:
            defaultErrorHandler.handleError(response);
            break;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.client.ResponseErrorHandler#hasError(org.springframework.http.client.ClientHttpResponse)
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        // Special checks for LQ's made-up status codes.
        if (response.getRawStatusCode() == IHttpStatus.SERVICE_EXCEPTION_REPONSE_CODE || response.getRawStatusCode() == IHttpStatus.PARTIAL_SUCCESS_EXCEPTION_REPONSE_CODE) {
            return true;
        }
        else {
            HttpStatus statusCode = response.getStatusCode();
            HttpStatus.Series series = statusCode.series();
            return !HttpStatus.Series.SUCCESSFUL.equals(series);
        }
    }

    /**
     * Gets the error code.
     *
     * @param response the response
     * @return the error code
     */
    private String getErrorCode(ClientHttpResponse response) {
        List<String> errorCodes = response.getHeaders().get(ERROR_CODE_HEADER);
        return errorCodes != null && errorCodes.size() == 1 ? errorCodes.get(0) : null;
    }

    /**
     * Gets the error type.
     *
     * @param response the response
     * @return the error type
     */
    private String getErrorType(ClientHttpResponse response) {
        List<String> errorTypes = response.getHeaders().get(ERROR_TYPE_HEADER);
        return errorTypes != null && errorTypes.size() == 1 ? errorTypes.get(0) : null;
    }

    /**
     * Raise for business exception.
     *
     * @param bodyMessage the body message
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void raiseForBusinessException(String bodyMessage) throws IOException {
        // TODO: parse body message and construct the exception list with the errors
        throw new IOException(bodyMessage);
    }

    /**
     * Raise for business exception.
     *
     * @param headerErrorCode the header error code
     * @param headerErrorType the header error type
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void raiseForBusinessException(String headerErrorCode, String headerErrorType) throws IOException {
        LOG.warn("Business exception occurred in services with type: " + headerErrorType + " and code: " + headerErrorCode);
        if (headerErrorCode.contains(",")) {
            String[] codesArray = headerErrorCode.split(",");
            List<BusinessException> exceptionsList = new ArrayList<BusinessException>();

            for (String codeString : codesArray) {
                exceptionsList.add(new BusinessException(BusinessExceptionCode.forMessageNumber(codeString)));
            }

            InvalidDataException invalidDataException = new InvalidDataException(exceptionsList);
            throw new IOException(invalidDataException);
        }
        else {
            BusinessException be = new BusinessException(BusinessExceptionCode.forMessageNumber(headerErrorCode));
            throw new IOException(be);
        }
    }

    /**
     * Raise for service exception.
     *
     * @param headerErrorCode the header error code
     * @param headerErrorType the header error type
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void raiseForServiceException(String headerErrorCode, String headerErrorType) throws IOException {
        LOG.error("Service exception occurred in services with type: " + headerErrorType + " and code: " + headerErrorCode);
        ServiceException se = new ServiceException(ServiceExceptionCode.forMessageNumber(headerErrorCode));
        throw new IOException(se);
    }

    /**
     * Raise for unchecked exception.
     *
     * @param headerErrorType the header error type
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void raiseForUncheckedException(String headerErrorType) throws IOException {
        LOG.error("Unchecked exception occurred in services with type: " + headerErrorType);
        ServiceException se = new ServiceException(ServiceExceptionCode.UNKNOWN_REASON);
        throw new IOException(se);
    }

}
