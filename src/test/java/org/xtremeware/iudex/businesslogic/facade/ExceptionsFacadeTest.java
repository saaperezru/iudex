package org.xtremeware.iudex.businesslogic.facade;

import java.util.Locale;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
public class ExceptionsFacadeTest {
    
//    public ExceptionsFacadeTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }

    @Test
    public void testGetMessage() {
        String message = Config.getInstance().getFacadeFactory().getExceptionsFacade().getMessage("test");
        String expectedMessage = "Test exception message";
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testGetSpanishMessage() {
        String message = Config.getInstance().getFacadeFactory().getExceptionsFacade().getMessage("test", Locale.forLanguageTag("es"));
        String expectedMessage = "Mensaje de excepción de prueba";
        assertEquals(expectedMessage, message);
    }
}
