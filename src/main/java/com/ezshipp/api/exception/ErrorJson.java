package com.ezshipp.api.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class ErrorJson.
 */
public class ErrorJson {

    /** The Constant ARRAY_END. */
    private static final String ARRAY_END = "]";

    /** The Constant ARRAY_START. */
    private static final String ARRAY_START = "[";

    /** The Constant COMMA. */
    private static final String COMMA = ",";

    /** The error. */
    private List<String> error = new ArrayList<String>();

    /**
     * Instantiates a new error json.
     */
    public ErrorJson() {
        // default construct
    }

    /**
     * Instantiates a new error json.
     *
     * @param errorMsgs the error
     */
    public ErrorJson(List<String> errorMsgs) {
        error = errorMsgs;
    }

    /**
     * Instantiates a new error json.
     *
     * @param errorMsg the error
     */
    public ErrorJson(String errorMsg) {
        addError(errorMsg);
    }

    /**
     * Adds the error.
     *
     * @param errorMsg the error
     */
    public void addError(String errorMsg) {
        error.add(errorMsg);
    }

    /**
     * Gets the errors as single json string.
     *
     * @return the errors as single json string
     */
    public String errorsAsSingleJsonString() {
        return error.stream().collect(Collectors.joining(COMMA, ARRAY_START, ARRAY_END));

    }

    /**
     * Gets the error.
     *
     * @return the error
     */
    public List<String> getError() {
        return error;
    }

    /**
     * Sets the error.
     *
     * @param errors the new errors
     */
    public void setError(List<String> errors) {
        error = errors;
    }
}
