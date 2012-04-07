/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.helper;

/**
 *
 * @author tuareg
 */
public class ConfigurationVariablesHelper {
	
	public static final String MIN_USER_LENGTH = "user.minUserNameLength";
	public static final String MAX_USERNAME_LENGTH = "user.maxUserNameLength";
	public static final String MAX_USER_PASSWORD_LENGTH = "user.minUserPasswordLength";
	public static final String MIN_USER_PASSWORD_LENGTH = "user.minUserPasswordLength";
	public static final String MAX_NUMBER_OF_AUTHENTICATION_FAILURES = "user.maxNumberOfAuthenticationFailures";
	public static final String MAX_COMMENT_LENGTH = "comment.maxCommentLength";
	public static final String MAX_COMMENTS_PER_DAY = "comment.maxCommentsPerDay";
	public static final String MAX_PROFESSOR_NAME_LENGTH = "professor.maxProfessorNameLength";
	public static final String RECAPTCHA_PRIVATE_KEY = "security.reCaptchaPrivateKey";
	public static final String RECAPTCHA_PUBLIC_KEY = "security.reCaptchaPublicKey";
	public static final String MAILING_SMTP_SERVER = "mailing.smtpServer";
	public static final String MAILING_SENDER_EMAIL_ADDRESS = "mailing.senderEMailAddress";
	public static final String PERIOD_MIN_YEAR = "period.minYear";
	public static final String PERIOD_MAX_YEAR = "period.maxYear";
	public static final String PERIOD_MAX_SEMESTER = "period.maxSemester";
	public static final String PERIOD_MIN_SEMESTER = "period.minSemester";
	public static final String ACCOUNT_MAX_DAY_TERM = "user.maxDaysTerm";

	
	public ConfigurationVariablesHelper(String configurationFilePath){
		
	}

	public String getVariable(String variableName) {
		throw new UnsupportedOperationException("Not supported yet.");

	}

	public void setVariable(String variableName, String value){
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
