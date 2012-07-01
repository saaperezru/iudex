package org.xtremeware.iudex.businesslogic.helper;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;

public class DefaultMailingSessionFactory implements MailingSessionFactory {

    private Properties properties;
    private String userName;
    private String password;

    public DefaultMailingSessionFactory() {
        properties = getProperties();
        userName = getUserName();
        password = getPassword();
    }

    private String getPassword() {
        return ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_PASSWORD);
    }

    private String getUserName() {
        return ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_USER);
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host",
                ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_SERVER));
        props.put("mail.smtp.port", ConfigurationVariablesHelper.
                getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }

    @Override
    public Session createSession() {
        return Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
    }
}
