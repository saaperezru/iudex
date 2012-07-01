package org.xtremeware.iudex.businesslogic.helper;

import com.dumbster.smtp.SimpleSmtpServer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import org.h2.tools.RunScript;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.MultipleMessagesException;

public class FacadesTestHelper {

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
                "jdbc:h2:mem:db1;", connectionProps), reader);
        init = true;
    }

    public static void checkExceptionMessages(
            MultipleMessagesException exception, String[] expectedMessages) {
        List<String> messages = exception.getMessages();
        for (String expectedMessage : expectedMessages) {
            assertTrue("not message " + expectedMessage, messages.contains(
                    expectedMessage));
        }
        assertEquals("Size not equal", messages.size(), expectedMessages.length);
    }

    public static String randomString(int length) {
        return new BigInteger(length * 5, new Random()).toString(32);
    }

    public static int randomInt(int length) {
        return new Random().nextInt((int) Math.pow(10.0, length + 0.0));
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        return javax.persistence.Persistence.createEntityManagerFactory(
                "org.xtremeware.iudex_local");
    }

    public static SimpleSmtpServer startFakeSmtpServer() {
        int serverPort = Integer.parseInt(ConfigurationVariablesHelper.
                getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PORT));
        SimpleSmtpServer smtpServer = SimpleSmtpServer.start(serverPort);
        return smtpServer;
    }
}
