/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.helper;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author saaperezru
 */
public class ConfigurationVariablesHelper {

    private static ConfigurationVariablesHelper instance;
    private PropertiesConfiguration config;
    public static final String APP_PATH = "app.path";
    public static final String MIN_USERNAME_LENGTH = "user.minUserNameLength";
    public static final String MAX_USERNAME_LENGTH = "user.maxUserNameLength";
   
    public static final String MAX_USER_PASSWORD_LENGTH = "user.maxUserPasswordLength";
    public static final String MIN_USER_PASSWORD_LENGTH = "user.minUserPasswordLength";
    public static final String MAX_NUMBER_OF_AUTHENTICATION_FAILURES = "user.maxNumberOfAuthenticationFailures";
    public static final String MAX_COMMENT_LENGTH = "comment.maxCommentLength";
    public static final String MAX_COMMENTS_PER_DAY = "comment.maxCommentsPerDay";
    public static final String MAX_PROFESSOR_NAME_LENGTH = "professor.maxProfessorNameLength";
    public static final String MAX_PROFESSOR_DECRIPTION_LENGTH = "professor.maxProfessorDescriptionLength";
    public static final String MAX_SUBJECT_NAME_LENGTH = "subject.maxNameLength";
    public static final String MAX_SUBJECT_DESCRIPTION_LENGTH = "subject.maxDescriptionLength";
    public static final String RECAPTCHA_PRIVATE_KEY = "security.reCaptchaPrivateKey";
    public static final String RECAPTCHA_PUBLIC_KEY = "security.reCaptchaPublicKey";
    public static final String MAILING_SMTP_SERVER = "mailing.smtpServer";
    public static final String MAILING_SENDER_EMAIL_ADDRESS = "mailing.senderEMailAddress";
    public static final String MAILING_SMTP_USER = "mailing.smtpServerUser";
    public static final String MAILING_SMTP_PASSWORD = "mailing.smtpServerPassword";
    public static final String MAILING_SMTP_PORT = "mailing.smtpServerPort";
    public static final String MAILING_TEMPLATES_PATH = "mailing.templates.path";
    public static final String MAILING_TEMPLATES_CONFIRMATION = "mailing.templates.confirmation";
    public static final String MAILING_TEMPLATES_CONFIRMATION_SUBJECT = "mailing.templates.confirmation.subject";
    public static final String PERIOD_MIN_YEAR = "period.minYear";
    public static final String PERIOD_MAX_YEAR = "period.maxYear";
    public static final String PERIOD_MAX_SEMESTER = "period.maxSemester";
    public static final String PERIOD_MIN_SEMESTER = "period.minSemester";
    public static final String ACCOUNT_MAX_DAY_TERM = "user.maxDaysTerm";
    public static final String ANTISAMY_POLICY_FILE = "security.antiSamyPolicyFile";
    public static final String EXCEPTIONS_BUNDLE_BASE_NAME = "exceptions.bundleBaseName";

    private ConfigurationVariablesHelper(String configurationFilePath) throws ExternalServiceConnectionException {
        try {
            config = new PropertiesConfiguration(getClass().getResource(configurationFilePath));
        } catch (Exception ex) {
            throw new ExternalServiceConnectionException("The specified configuration file for the COnfigurationVariablesHelper does not exists");
        }

    }

    private static ConfigurationVariablesHelper getInstance() throws ExternalServiceConnectionException {
        if (instance == null) {
            instance = new ConfigurationVariablesHelper(Config.CONFIGURATION_VARIABLES_PATH);
        }
        return instance;
    }

    public static String getVariable(String variableName) throws ExternalServiceConnectionException {
        return getInstance().getConfig().getString(variableName);

    }

    public static void setVariable(String variableName, String value) throws ExternalServiceConnectionException {
        getInstance().getConfig().setProperty(variableName, value);
    }

    private PropertiesConfiguration getConfig() {
        return config;
    }

    private void setConfig(PropertiesConfiguration config) {
        this.config = config;
    }
}
