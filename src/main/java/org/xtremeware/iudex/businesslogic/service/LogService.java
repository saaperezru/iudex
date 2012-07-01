package org.xtremeware.iudex.businesslogic.service;

import java.io.IOException;
import org.apache.log4j.*;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;

@SuppressWarnings("CallToThreadDumpStack")
public class LogService {

    private static Logger LOGGER;

    static {
        try {
            String loggerName = getLoggerName();
            LOGGER = Logger.getLogger(loggerName);

            Level level = getLevel();
            LOGGER.setLevel(level);

            Appender appender = getAppender();
            LOGGER.addAppender(appender);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String getLoggerName() {
        return ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.LOGGER_NAME);
    }

    private static Level getLevel() {
        String levelName = ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.LOGGER_LEVEL);
        Level level = Level.toLevel(levelName);
        return level;
    }

    private static Appender getAppender() throws IOException {
        final String pattern = getAppenderPattern();
        Layout layout = new EnhancedPatternLayout(pattern);
        final String fileName = getLogFileName();
        final String datePattern = "'.'yyyy-MM-dd";
        Appender appender = new DailyRollingFileAppender(layout, fileName,
                datePattern);
        return appender;
    }

    private static String getLogFileName() {
        String classesRoot = LogService.class.getResource("/").getFile();
        String logFileName = ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.LOGGER_LOG_FILE);

        return classesRoot + logFileName;
    }

    private static String getAppenderPattern() {
        return "[%d] - " +
                EnhancedPatternLayout.TTCC_CONVERSION_PATTERN;
    }

    public void debug(Object message) {
        LOGGER.debug(message);
    }

    public void debug(Object message, Throwable throwable) {
        LOGGER.debug(message, throwable);
    }

    public void error(Object message) {
        LOGGER.error(message);
    }

    public void error(Object message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }

    public void fatal(Object message) {
        LOGGER.fatal(message);
    }

    public void fatal(Object message, Throwable throwable) {
        LOGGER.fatal(message, throwable);
    }

    public void info(Object message) {
        LOGGER.info(message);
    }

    public void info(Object message, Throwable throwable) {
        LOGGER.info(message, throwable);
    }

    public void warn(Object message) {
        LOGGER.warn(message);
    }

    public void warn(Object message, Throwable throwable) {
        LOGGER.warn(message, throwable);
    }
}
