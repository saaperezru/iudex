package org.xtremeware.iudex.businesslogic.facade;

import java.util.Locale;
import java.util.ResourceBundle;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;

/**
 *
 * @author healarconr
 */
public class ExceptionsFacade {
    
    private String bundleBaseName;
    
    public ExceptionsFacade() {
        try {
            bundleBaseName =
                    ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.EXCEPTIONS_BUNDLE_BASE_NAME);
        } catch (ExternalServiceConnectionException ex) {
            Config.getInstance().getServiceFactory().createLogService().error(ex.
                    getMessage(), ex);
        }
    }
    
    public String getMessage(String key) {
        return getMessage(key, Locale.ENGLISH);
    }
    
    public String getMessage(String key, Locale locale) {
        try {
            return ResourceBundle.getBundle(bundleBaseName, locale).getString(
                    key);
        } catch (Exception ex) {
            Config.getInstance().getServiceFactory().createLogService().debug(
                    ex.getMessage(), ex);
            return key;
        }
    }
}
