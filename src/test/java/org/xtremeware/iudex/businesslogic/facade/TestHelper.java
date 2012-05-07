package org.xtremeware.iudex.businesslogic.facade;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.h2.tools.RunScript;

public class TestHelper {

	public static void initializeDatabase() throws FileNotFoundException, SQLException {
		Properties connectionProps = new Properties();
		connectionProps.put("user", "sa");
		connectionProps.put("password", "");
		FileReader reader = new FileReader("src/test/init.sql");
		RunScript.execute(DriverManager.getConnection("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;", connectionProps), reader);
	}
}
