package org.xtremeware.iudex.businesslogic.helper;

import java.util.Properties;
import javax.mail.Session;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;

public class TestMailingSessionFactory implements MailingSessionFactory {

    private Properties properties;

    public TestMailingSessionFactory() {
        properties = getProperties();
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host",
                ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_SERVER));
        props.put("mail.smtp.port", ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_PORT));
        return props;
    }

    @Override
    public Session createSession() {
        return Session.getInstance(properties);
    }
}
