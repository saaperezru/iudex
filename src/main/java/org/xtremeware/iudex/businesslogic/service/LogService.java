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

    private final static Logger logger = Logger.getLogger("org.xtremeware.iudex");

    static {
        try {
            // TODO: Use the configuration helper to obtain the log configuration
            logger.setLevel(Level.ALL);
            logger.addAppender(new DailyRollingFileAppender(new EnhancedPatternLayout(EnhancedPatternLayout.TTCC_CONVERSION_PATTERN), "log/iudex.log", "'.'yyyy-MM-dd"));
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
        logger.debug(message);
    }

    /**
     * Logs a message and a throwable with debug level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void debug(Object message, Throwable t) {
        logger.debug(message, t);
    }

    /**
     * Logs a message with error level
     *
     * @param message the message to log
     */
    public void error(Object message) {
        logger.error(message);
    }

    /**
     * Logs a message and a throwable with error level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void error(Object message, Throwable t) {
        logger.error(message, t);
    }

    /**
     * Logs a message with fatal level
     *
     * @param message the message to log
     */
    public void fatal(Object message) {
        logger.fatal(message);
    }

    /**
     * Logs a message and a throwable with fatal level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void fatal(Object message, Throwable t) {
        logger.fatal(message, t);
    }

    /**
     * Logs a message with info level
     *
     * @param message the message to log
     */
    public void info(Object message) {
        logger.info(message);
    }

    /**
     * Logs a message and a throwable with info level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void info(Object message, Throwable t) {
        logger.info(message, t);
    }

    /**
     * Logs a message with warn level
     *
     * @param message the message to log
     */
    public void warn(Object message) {
        logger.warn(message);
    }

    /**
     * Logs a message and a throwable with warn level
     *
     * @param message the message to log
     * @param t the throwable to log
     */
    public void warn(Object message, Throwable t) {
        logger.warn(message, t);
    }
}
