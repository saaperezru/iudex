package org.xtremeware.iudex.businesslogic.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests the LogService
 *
 * @author healarconr
 */
public class LogServiceTest {

    /**
     * The log file name
     */
    private final static String fileName = "log/iudex.log";

    /**
     * Deletes log file
     */
    private void deleteFile() {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Checks the length of the log file and cleans it
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void checkAndClean() throws FileNotFoundException, IOException {
        File logFile = new File(fileName);
        assertTrue(logFile.exists() && logFile.length() != 0);
        FileOutputStream fos = new FileOutputStream(logFile);
        fos.write("".getBytes());
        fos.close();
    }

    /**
     * Tests all LogService's methods
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void testLogMethods() throws FileNotFoundException, IOException {
        deleteFile();

        LogService logService = new LogService();

        logService.debug("Debug message");
        checkAndClean();

        logService.debug("Debug message", new Exception("Test exception"));
        checkAndClean();

        logService.error("Error message");
        checkAndClean();

        logService.error("Error message", new Exception("Test exception"));
        checkAndClean();

        logService.fatal("Fatal message");
        checkAndClean();

        logService.fatal("Fatal message", new Exception("Test exception"));
        checkAndClean();

        logService.info("Info message");
        checkAndClean();

        logService.info("Info message", new Exception("Test exception"));
        checkAndClean();

        logService.warn("Warn message");
        checkAndClean();

        logService.warn("Warn message", new Exception("Test exception"));
        checkAndClean();

        deleteFile();
    }
}