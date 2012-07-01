package org.xtremeware.iudex.businesslogic.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;

public class LogServiceTest {

    private static String logFileName;
    private static LogService logService;

    static {
        String classesRoot = LogService.class.getResource("/").getFile();
        logFileName = ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.LOGGER_LOG_FILE);

        logFileName = classesRoot + logFileName;
    }

    @BeforeClass
    public static void setUpClass() {
        deleteLogFile();

        logService = new LogService();
    }

    private static void deleteLogFile() {
        File file = new File(logFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        deleteLogFile();
    }

    @After
    public void tearDown() throws Exception {
        checkLogFileLengthAndClearIt();
    }

    private void checkLogFileLengthAndClearIt() throws FileNotFoundException,
            IOException {
        File logFile = new File(logFileName);
        assertTrue(logFile.exists() && logFile.length() != 0);
        FileOutputStream fos = new FileOutputStream(logFile);
        fos.write("".getBytes());
        fos.close();
    }

    @Test
    public void debug_message_success() {
        logService.debug("Debug message");
    }

    @Test
    public void debug_messageAndThrowable_success() {
        logService.debug("Debug message", new Exception("Test exception"));
    }

    @Test
    public void error_message_success() {
        logService.error("Error message");
    }

    @Test
    public void error_messageAndThrowable_success() {
        logService.error("Error message", new Exception("Test exception"));
    }

    @Test
    public void fatal_message_success() {
        logService.fatal("Fatal message");
    }

    @Test
    public void fatal_messageAndThrowable_success() {
        logService.fatal("Fatal message", new Exception("Test exception"));
    }

    @Test
    public void info_message_success() {
        logService.info("Info message");
    }

    @Test
    public void info_messageAndThrowable_success() {
        logService.info("Info message", new Exception("Test exception"));
    }

    @Test
    public void warn_message_success() {
        logService.warn("Warn message");
    }

    @Test
    public void warn_messageAndThrowable_success() {
        logService.warn("Warn message", new Exception("Test exception"));
    }
}