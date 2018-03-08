package com.ezshipp.api.exception;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Class PartialSuccessBusinessException.
 *
 */
public class PartialSuccessBusinessException extends AException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3200306944846581404L;

    /**
     * Instantiates a new partial success business exception.
     *
     * @param code the error code from BusinessExceptionCode
     * @param t the throwable
     * @param body the exception body
     */
    public PartialSuccessBusinessException(BusinessExceptionCode code, Throwable t, Object body) {
        super(code.getMessageNumber(), t);
        this.body = body;
    }

    /**
     * Instantiates a new partial success business exception.
     *
     * @param code the error code from ServiceExceptionCode
     * @param t the throwable
     * @param body the exception body
     */
    public PartialSuccessBusinessException(ServiceExceptionCode code, Throwable t, Object body) {
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
        BusinessExceptionCode code = BusinessExceptionCode.forMessageNumber(getMessage());
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
            msg = bundle.getString("business.exception.message." + getMessage());

            // MessageFormat will strip out single quotes without this line
            msg = msg.replace("'", "''");
            msg = MessageFormat.format(msg, (Object[]) getCustomFields());
        }
        catch (MissingResourceException e) {
            // if we can't find the specific error message provide a general message
            msg = bundle.getString("business.exception.message");
        }
        return msg;
    }

    /**
     * With params.
     *
     * @param values the values
     * @return the partial success business exception
     */
    public PartialSuccessBusinessException withParams(String[] values) {
        customFields = values.clone();
        return this;
    }

}
