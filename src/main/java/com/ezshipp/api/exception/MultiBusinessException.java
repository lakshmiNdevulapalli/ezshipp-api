package com.ezshipp.api.exception;

import com.google.common.base.Joiner;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class represents the list of business errors.
 * */
public class MultiBusinessException extends BusinessException {

    /** The Constant NEW_LINE. */
    private static final String NEW_LINE = "\n";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The list of error codes. */
    private final List<BusinessException> exceptionList;

    /**
     * Instantiates a new multi business exception.
     *
     * @param exception the exception
     */
    public MultiBusinessException(BusinessException exception) {
        super(BusinessExceptionCode.MULTIPLE_BUSINESS_ERRORS);
        exceptionList = new ArrayList<BusinessException>();
        exceptionList.add(exception);
    }

    /**
     * Instantiates a new multi business exception.
     *
     * @param exception the exception
     * @param params the params
     */
    public MultiBusinessException(BusinessException exception, String[] params) {
        super(BusinessExceptionCode.MULTIPLE_BUSINESS_ERRORS, params);
        exceptionList = new ArrayList<BusinessException>();
        exceptionList.add(exception);
    }

    /**
     * Instantiates a new multi business exception.
     *
     * @param exception the exception
     */
    public MultiBusinessException(List<BusinessException> exception) {
        super(BusinessExceptionCode.MULTIPLE_BUSINESS_ERRORS);
        exceptionList = exception;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.laquinta.service.common.exception.BusinessException#getCodeMessage()
     */
    @Override
    public String getCodeMessage() {
        return Joiner.on(",").join(getCodeNumberList());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.laquinta.service.common.exception.AException#getErrorCodeDescription()
     */
    @Override
    public String getErrorCodeDescription() {
        List<String> businessExceptionCodes = new ArrayList<String>();
        for (BusinessException exception : exceptionList) {
            if (exception instanceof MultiBusinessException) {
                MultiBusinessException nestedMultiBusinessException = (MultiBusinessException) exception;
                businessExceptionCodes.add(nestedMultiBusinessException.getErrorCodeDescription());
            }
            else {
                businessExceptionCodes.add(exception.getCode().toString());
            }
        }
        return Joiner.on(",").join(businessExceptionCodes);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.laquinta.service.common.exception.BusinessException#getErrorMessage()
     */
    @Override
    public String getErrorMessage() {
        // grab a resource bundle
        // FIXME add locale - it seems to be getting locale from the server's locale
        ResourceBundle bundle = ResourceBundle.getBundle("exception/exceptions");
        StringBuilder stringBuilder = new StringBuilder();
        String msg = null;
        for (BusinessException e : exceptionList) {
            if (e instanceof MultiBusinessException) {
                MultiBusinessException nestedMultiBusinessException = (MultiBusinessException) e;
                msg = nestedMultiBusinessException.getErrorMessage();
            }
            else {
                // get the message from the bundle
                try {
                    msg = bundle.getString("business.exception.message." + e.getMessage());

                    // MessageFormat will strip out single quotes without this line
                    msg = msg.replace("'", "''");
                    msg = MessageFormat.format(msg, (Object[]) e.getCustomFields());
                }
                catch (MissingResourceException mre) {
                    // if we can't find the specific error message provide a general message
                    msg = bundle.getString("business.exception.message");
                }
            }
            stringBuilder.append(msg);
            stringBuilder.append(NEW_LINE);
        }
        return stringBuilder.toString();
    }

    /**
     * The list of error codes.
     *
     * @return the list of codes
     */
    @Override
    public List<BusinessException> getExceptionList() {
        List<BusinessException> finalList = new ArrayList<BusinessException>();
        for (BusinessException businessException : exceptionList) {
            finalList.addAll(businessException.getExceptionList());
        }
        return finalList;
    }

    /**
     * Gets the code list.
     *
     * @return the code list
     */
    private List<String> getCodeNumberList() {
        List<String> codeList = new ArrayList<String>();
        for (BusinessException e : getExceptionList()) {
            if (e instanceof MultiBusinessException) {
                MultiBusinessException nestedMultiBusinessException = (MultiBusinessException) e;
                codeList.addAll(nestedMultiBusinessException.getCodeNumberList());
            }
            else {
                codeList.add(e.getCode().getMessageNumber());
            }
        }
        return codeList;
    }
}
