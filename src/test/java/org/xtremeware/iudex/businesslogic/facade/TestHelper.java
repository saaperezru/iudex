package org.xtremeware.iudex.businesslogic.facade;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.h2.tools.RunScript;
import static org.junit.Assert.assertTrue;
import org.xtremeware.iudex.helper.MultipleMessagesException;

public class TestHelper {

    private static boolean init = false;

    public static void initializeDatabase() throws FileNotFoundException,
            SQLException {
        if (init) {
            return;
        }
        Properties connectionProps = new Properties();
        connectionProps.put("user", "sa");
        connectionProps.put("password", "");
        FileReader reader = new FileReader("src/test/init.sql");
        RunScript.execute(DriverManager.getConnection(
                "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;", connectionProps), reader);
        init = true;
    }

    public static void checkExceptionMessages(
            MultipleMessagesException exception, String[] expectedMessages) {
        List<String> messages = exception.getMessages();
        for (String expectedMessage : expectedMessages) {
            assertTrue(messages.contains(expectedMessage));
        }
    }
    
    public static String randomString(int length){
        return new BigInteger(length * 5, new Random()).toString(32);
    }
}