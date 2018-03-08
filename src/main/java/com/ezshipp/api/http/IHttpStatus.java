package com.ezshipp.api.http;

/**
 * The Interface IHttpStatus provides custom http response codes for LQ.
 */
@SuppressWarnings("PMD.AvoidConstantsInterface")
public interface IHttpStatus {

    /** The Constant BUSINESS_EXCEPTION_REPONSE_CODE returns a 422. */
    int BUSINESS_EXCEPTION_REPONSE_CODE = 422;

    /** The Constant FORBIDDEN_CODE returns a 403. */
    int FORBIDDEN_CODE = 403;

    /** The Constant PARTIAL_SUCCESS_EXCEPTION_REPONSE_CODE returns a 427. */
    int PARTIAL_SUCCESS_EXCEPTION_REPONSE_CODE = 427;

    /** The Constant SERVICE_EXCEPTION_REPONSE_CODE returns a 567. */
    int SERVICE_EXCEPTION_REPONSE_CODE = 567;

    /** The Constant SUCCESS returns a 200. */
    int SUCCESS = 200;

    /** The Constant UNCHECHKED_EXCEPTION_REPONSE_CODE returns a 567. */
    int UNCHECKED_EXCEPTION_REPONSE_CODE = 567;

}
