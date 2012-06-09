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
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.ValidityHelper;
import org.xtremeware.iudex.vo.MailingConfigVo;

public final class MailingService {

    private Properties props;
    private MailingConfigVo config;
    private Configuration templates;
    private static final int MAX_SMTPSERVERPORT = 65535;
    private static final int MIN_SMTPSERVERPORT = 0;

    public MailingService(MailingConfigVo config) {
        setConfig(config);
    }

    public MailingConfigVo getConfig() {
        return config;
    }

    public void setConfig(MailingConfigVo config) {
        checkConfig(config);
        if (props == null) {
            props = new Properties();
        }
        props.put("mail.smtp.host", config.getSmtpServer());
        props.put("mail.smtp.port", config.getSmtpServerPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        this.config = config;
    }

    private void checkConfig(MailingConfigVo config) {
        if (config == null) {
            throw new IllegalArgumentException(
                    "MailConfigVo config cannot be null");
        }
        if (!ValidityHelper.isValidDomain(config.getSmtpServer())) {
            throw new IllegalArgumentException(
                    "String smtpServer in the MailingConfigVo provided must be a valid domain.");
        }
        if (!ValidityHelper.isValidEmail(config.getSender())) {
            throw new IllegalArgumentException(
                    "String sender in the MailingConfigVo provided must be a valid email address");
        }
        if (config.getSmtpServerPort() > MAX_SMTPSERVERPORT || config.getSmtpServerPort() < MIN_SMTPSERVERPORT) {
            throw new IllegalArgumentException(
                    "int smtpServerPort in the MailingConfigVo provided must be in between "+MIN_SMTPSERVERPORT+" and "+MAX_SMTPSERVERPORT);
        }
        if (config.getSmtpUser() == null) {
            throw new IllegalArgumentException(
                    "String smtpServerUser in the MailingConfigVo provided cannot be null");
        }
        if (config.getSmtpPassword() == null) {
            throw new IllegalArgumentException(
                    "String smtpServerPassword in the MailingConfigVo provided cannot be null");
        }
    }

    public void sendMessage(Map<String, String> data, String templateName,
            String subject, String receiver) {
        try {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {

                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    config.getSmtpUser(),
                                    config.getSmtpPassword());
                        }
                    });
            // Create a new message
            Message msg = new MimeMessage(session);
            try {
                // Set the FROM field
                msg.setFrom(new InternetAddress(config.getSender()));
            } catch (AddressException ex) {
                throw new IllegalArgumentException(
                        "There was a problem while translating sender email address",
                        ex);
            }
            try {
                // Set the TO field
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(receiver, false));
            } catch (AddressException ex) {
                throw new IllegalArgumentException(
                        "There was a problem while translating receiver email address",
                        ex);
            }
            // Set the subject
            msg.setSubject(subject);

            // Set the content
            if (templates == null) {
                templates = new Configuration();
                try {
                    templates.setDirectoryForTemplateLoading(new File(getClass().
                            getResource(ConfigurationVariablesHelper.getVariable(
                            ConfigurationVariablesHelper.MAILING_TEMPLATES_PATH)).
                            getFile()));
                } catch (IOException ex) {
                    throw new IllegalArgumentException(
                            "There was a problem while getting the email templates directory",
                            ex);
                }
                templates.setObjectWrapper(new DefaultObjectWrapper());
            }

            Template template;
            try {
                template = templates.getTemplate(templateName);
            } catch (IOException ex) {
                throw new IllegalArgumentException(
                        "There was a problem while getting the email template",
                        ex);
            }

            StringWriter writer = new StringWriter();
            try {
                template.process(data, writer);
            } catch (TemplateException ex) {
                throw new IllegalArgumentException(
                        "There was a problem while processing the template",
                        ex);
            } catch (IOException ex) {
                throw new IllegalArgumentException(
                        "There was a problem while processing the template",
                        ex);
            }

            msg.setContent(writer.getBuffer().toString(), "text/html");
            msg.setSentDate(new Date());
            // Send the message
            msg.saveChanges();

            // Use Transport to deliver the message
            Transport.send(msg);
        } catch (MessagingException ex) {
            throw new ExternalServiceConnectionException(
                    "There was a problem while sending the email", ex);
        }
    }
}
