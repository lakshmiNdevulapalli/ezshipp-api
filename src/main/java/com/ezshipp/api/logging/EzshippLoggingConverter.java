package com.ezshipp.api.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * EzshippLoggingConverter will grab all the implementations of EzshippLoggingFormatter and chain the formats.
 */
@Named("EzshippLoggingConverter")
public class EzshippLoggingConverter extends ClassicConverter {

    /**
     * List of formatters.
     */
    private static final List<EzshippLoggingFormatter> LOGGING_FORMATTER;

    /**
     * Initialization of the list of formatters, since I can't find a way to inject them by Spring.
     * */
    static {
        LOGGING_FORMATTER = new ArrayList<EzshippLoggingFormatter>();
        //LOGGING_FORMATTER.add(new CreditCardMaskFormatter());
    }

    /**
     * The convert method is responsible for extracting data from the event and storing it for later use by the write method.
     *
     * @param event the event
     * @return the formatted message
     */
    @Override
    public String convert(ILoggingEvent event) {
        String msg = event.getFormattedMessage();
        for (EzshippLoggingFormatter formatter : LOGGING_FORMATTER) {
            msg = formatter.format(msg);
        }
        return msg;
    }

}
