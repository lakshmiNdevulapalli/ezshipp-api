package com.ezshipp.api.logging;

/**
 * Generic class to format logging message.
 */
public interface EzshippLoggingFormatter {

    /**
     * Formats a message.
     *
     * @param message the message
     * @return the formatted message
     * */
    String format(String message);
}
