package org.xtremeware.iudex.businesslogic.service;

import java.io.IOException;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Logs different levels of messages and exceptions in a central dailly rolling
 * log file
 *
 * @author healarconr
 */
@SuppressWarnings("CallToThreadDumpStack")
public class LogService {

    private static final Logger LOGGER = Logger.getLogger("org.xtremeware.iudex");

    static {
        try {
            // TODO: Use the configuration helper to obtain the log configuration
            LOGGER.setLevel(Level.ALL);
            LOGGER.addAppender(new DailyRollingFileAppender(
                    new EnhancedPatternLayout("[%d] - " + EnhancedPatternLayout.TTCC_CONVERSION_PATTERN),
                    "log/iudex.log", "'.'yyyy-MM-dd"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Logs a message with debug level
     *
     * @param message the message to log
     */
    public void debug(Object message) {
        LOGGER.debug(message);
    }

    /**
     * Logs a message and a throwable with debug level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void debug(Object message, Throwable t) {
        LOGGER.debug(message, t);
    }

    /**
     * Logs a message with error level
     *
     * @param message the message to log
     */
    public void error(Object message) {
        LOGGER.error(message);
    }

    /**
     * Logs a message and a throwable with error level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void error(Object message, Throwable t) {
        LOGGER.error(message, t);
    }

    /**
     * Logs a message with fatal level
     *
     * @param message the message to log
     */
    public void fatal(Object message) {
        LOGGER.fatal(message);
    }

    /**
     * Logs a message and a throwable with fatal level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void fatal(Object message, Throwable t) {
        LOGGER.fatal(message, t);
    }

    /**
     * Logs a message with info level
     *
     * @param message the message to log
     */
    public void info(Object message) {
        LOGGER.info(message);
    }

    /**
     * Logs a message and a throwable with info level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void info(Object message, Throwable t) {
        LOGGER.info(message, t);
    }

    /**
     * Logs a message with warn level
     *
     * @param message the message to log
     */
    public void warn(Object message) {
        LOGGER.warn(message);
    }

    /**
     * Logs a message and a throwable with warn level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void warn(Object message, Throwable t) {
        LOGGER.warn(message, t);
    }
}
