package com.ezshipp.api.exception;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Class PartialSuccessServiceException.
 *
 */
public class PartialSuccessServiceException extends AException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -146692020713032028L;

    /**
     * Instantiates a new partial success service exception.
     *
     * @param code the error code from ServiceExceptionCode
     * @param body the exception body
     */
    public PartialSuccessServiceException(ServiceExceptionCode code, Object body) {
        super(code.getMessageNumber());
        this.body = body;
    }

    /**
     * Instantiates a new partial success service exception.
     *
     * @param code the error code from ServiceExceptionCode
     * @param t the throwable
     * @param body the exception body
     */
    public PartialSuccessServiceException(ServiceExceptionCode code, Throwable t, Object body) {
        super(code.getMessageNumber(), t);
        this.body = body;
    }

    /**
     * Gets the code message.
     *
     * @return the code message
     */
    public String getCodeMessage() {
        return getMessage();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.laquinta.service.common.exception.AException#getErrorCodeDescription()
     */
    @Override
    public String getErrorCodeDescription() {
        ServiceExceptionCode code = ServiceExceptionCode.forMessageNumber(getMessage());
        if (code != null) {
            return code.toString();
        }
        return null;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        // grab a resource bundle
        // FIXME add locale - it seems to be getting locale from the server's locale
        ResourceBundle bundle = ResourceBundle.getBundle("exception/exceptions");

        // get the message from the bundle
        String msg = null;
        try {
            msg = bundle.getString("service.exception.message." + getMessage());

            // MessageFormat will strip out single quotes without this line
            msg = msg.replace("'", "''");
            msg = MessageFormat.format(msg, (Object[]) getCustomFields());
        }
        catch (MissingResourceException e) {
            // if we can't find the specific error message provide a general message
            msg = bundle.getString("service.exception.message");
        }
        return msg;
    }

    /**
     * With params.
     *
     * @param values the values
     * @return the partial success business exception
     */
    public PartialSuccessServiceException withParams(String[] values) {
        customFields = values.clone();
        return this;
    }

}
