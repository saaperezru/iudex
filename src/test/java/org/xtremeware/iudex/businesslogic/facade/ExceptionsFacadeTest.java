package org.xtremeware.iudex.businesslogic.facade;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
public class ExceptionsFacadeTest {

    @Test
    public void testGetMessage() {
        String message = Config.getInstance().getFacadeFactory().getExceptionsFacade().getMessage("test");
        String expectedMessage = "Test exception message";
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testGetSpanishMessage() {
        String message = Config.getInstance().getFacadeFactory().getExceptionsFacade().getMessage("test", new Locale("es"));
        String expectedMessage = "Mensaje de excepción de prueba";
        assertEquals(expectedMessage, message);
    }
}
