package org.xtremeware.iudex.businesslogic.service;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.xtremeware.iudex.businesslogic.helper.MailingSessionFactory;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;

public final class MailingService {

    private final String MESSAGE_CONTENT_TYPE = "text/html";
    private MailingSessionFactory sessionFactory;
    private Configuration templatesConfiguration;

    public MailingService(MailingSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void sendMessage(Map<String, String> data, String templateName,
            String subject, String receiver) {
        try {
            Session session = sessionFactory.createSession();

            Message message = new MimeMessage(session);

            message.setSubject(subject);
            message.setSentDate(new Date());
            message = setReceiver(message, receiver);
            message = setContent(message, data, templateName);
            message.saveChanges();

            Transport.send(message);
        } catch (MessagingException ex) {
            throw new ExternalServiceConnectionException(
                    "There was a problem while sending the email", ex);
        }
    }

    private Message setReceiver(Message message, String receiver) throws
            IllegalArgumentException,
            MessagingException {
        try {
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiver, false));
            return message;
        } catch (AddressException ex) {
            throw new IllegalArgumentException(
                    "There was a problem while translating receiver email address",
                    ex);
        }
    }

    private Message setContent(Message message, Map<String, String> data,
            String templateName) throws IllegalArgumentException,
            MessagingException {
        Configuration templatesConfig = getTemplatesConfiguration();

        Template template = getTemplate(templatesConfig, templateName);

        String messageContent = parseTemplate(template, data);

        message.setContent(messageContent, MESSAGE_CONTENT_TYPE);

        return message;
    }

    private Configuration getTemplatesConfiguration() throws
            IllegalArgumentException {
        if (templatesConfiguration == null) {
            initializeTemplatesConfiguration();
        }
        return templatesConfiguration;
    }

    private void initializeTemplatesConfiguration() throws
            IllegalArgumentException {
        templatesConfiguration = new Configuration();
        try {
            templatesConfiguration.setDirectoryForTemplateLoading(
                    getTemplatesDirectory());
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "There was a problem while getting the email templates directory",
                    ex);
        }
        templatesConfiguration.setObjectWrapper(new DefaultObjectWrapper());
    }

    private File getTemplatesDirectory() {
        return new File(getClass().
                getResource(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_TEMPLATES_PATH)).
                getFile());
    }

    private Template getTemplate(Configuration templates, String templateName)
            throws
            IllegalArgumentException {
        try {
            return templates.getTemplate(templateName);
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "There was a problem while getting the email template",
                    ex);
        }
    }

    private String parseTemplate(Template template,
            Map<String, String> data) throws IllegalArgumentException {
        StringWriter writer = new StringWriter();
        try {
            template.process(data, writer);
        } catch (TemplateException ex) {
            handleParseException(ex);
        } catch (IOException ex) {
            handleParseException(ex);
        }
        return writer.getBuffer().toString();
    }

    private void handleParseException(Exception ex) throws
            IllegalArgumentException {
        throw new IllegalArgumentException(
                "There was a problem while processing the template",
                ex);
    }
}
