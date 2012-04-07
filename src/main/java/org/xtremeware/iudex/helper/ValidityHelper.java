package org.xtremeware.iudex.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

public class ValidityHelper {

//	private static final String EMAIL_PATTERN =
//		"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@ [A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//	private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//	private static Matcher matcher;

	/**
	 * Validate an email address (only syntax)
	 *
	 * @param email email address to validate
	 * @return true if email is a valid email address, false otherwise
	 */
	public static boolean isValidEmail(final String email) {

		return EmailValidator.getInstance().isValid(email);

//		matcher = pattern.matcher(hex);
//		return matcher.matches();
	}

	public static boolean isValidDomain(String domain) {
		return DomainValidator.getInstance().isValid(domain);
	}

	/**
	 * Validate an URL validity (only syntax)
	 *
	 * @param url URL to validate
	 * @return true if url is a valid URL , false otherwise
	 */
	public static boolean isValidUrl(String url) {

		return UrlValidator.getInstance().isValid(url);

	}
}
