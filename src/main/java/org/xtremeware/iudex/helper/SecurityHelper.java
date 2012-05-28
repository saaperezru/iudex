/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.helper;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

/**
 *
 * @author saaperezru
 */
public class SecurityHelper {

	private Policy policy;
	private AntiSamy antiSamy;
	private static SecurityHelper instance;

	private SecurityHelper(String policyFile) throws ExternalServiceConnectionException {
		try {
			policy = Policy.getInstance(getClass().getResourceAsStream(policyFile));
			antiSamy = new AntiSamy(policy);
		} catch (PolicyException ex) {
			throw new ExternalServiceConnectionException("The specified ANTISAMY policy file for the SecuirtyHelper cannot be accessed");
		}
	}

	private static SecurityHelper getInstance() throws ExternalServiceConnectionException {
		while (instance == null) {
			instance = new SecurityHelper(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.ANTISAMY_POLICY_FILE));
		}
		return instance;
	}

	public static String sanitizeHTML(String input) throws ExternalServiceConnectionException {
		if (input == null){
			System.out.println("[DEBUG] An exception occured while sanitizing");
			throw  new IllegalArgumentException("The input to sanitize cannot be null");
		}
		String result = null;
		try {
			CleanResults cr = getInstance().getAntiSamy().scan(input, getInstance().getPolicy());
			result = cr.getCleanHTML();
		} catch (Exception ex) {
			Config.getInstance().getServiceFactory().getLogService().error(ex.getMessage(), ex);
			throw new ExternalServiceConnectionException("There was a problem while sanitizing", ex);
		}
		return result;
	}

	public static String hashPassword(String password) {
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes(Charset.forName("UTF-8")));

			byte[] mdbytes = md.digest();

			//convert the byte to hex format method 1
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			///Es casi imposible que pase esto
			Config.getInstance().getServiceFactory().getLogService().error(ex.getMessage(), ex);
		}
		return hash;
	}

	public static String generateMailingKey() throws ExternalServiceConnectionException{
		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException ex) {
			throw new ExternalServiceConnectionException("There was a problem allocating the SecureRandom instance", ex);
		}
		random.setSeed(random.generateSeed(20));
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
      		return hashPassword((bytes.toString()));

		
	}

	public AntiSamy getAntiSamy() {
		return antiSamy;
	}

	public void setAntiSamy(AntiSamy antiSamy) {
		this.antiSamy = antiSamy;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
}
