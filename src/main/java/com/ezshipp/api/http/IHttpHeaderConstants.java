package com.ezshipp.api.http;

/**
 * The Interface IHttpHeaderConstants provides custom http headers for LQ.
 */
@SuppressWarnings("PMD.AvoidConstantsInterface")
public interface IHttpHeaderConstants {

    /** The Constant LQ_ERROR_CODE returns a lq_error_code string. */
    String LQ_ERROR_CODE = "lq_error_code";

    /** The lq error code description. */
    String LQ_ERROR_CODE_DESCRIPTION = "lq_error_code_description";

    /** The Constant LQ_ERROR_MESSAGE returns a lq_error_message string. */
    String LQ_ERROR_MESSAGE = "lq_error_message";

    /** The lq error type. */
    String LQ_ERROR_TYPE = "lq_error_type";

}
