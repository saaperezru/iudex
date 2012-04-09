/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.helper;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.MySqlDaoFactory;
import org.xtremeware.iudex.vo.MailingConfigVo;

/**
 *
 * @author saaperezru
 */
public class Config {

	public final static String CONFIGURATION_VARIABLES_PATH = "iudex.properties";

	private String persistenceUnit;
	private AbstractDaoFactory daoFactory;
	private static Config instance;
	private ServiceFactory serviceFactory;
        private FacadeFactory facadeFactory;

	private Config(String persistenceUnit, AbstractDaoFactory daoFactory) throws ExternalServiceConnectionException {
		this.daoFactory = daoFactory;
		MailingConfigVo mailingConf = new MailingConfigVo();
		mailingConf.setSender(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SENDER_EMAIL_ADDRESS));
		mailingConf.setSmtpPassword(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PASSWORD));
		mailingConf.setSmtpServer(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_SERVER));
		mailingConf.setSmtpServerPort(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PORT)));
		mailingConf.setSmtpUser(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_USER));
		this.serviceFactory = new ServiceFactory(daoFactory, mailingConf);
                // TODO: initialize facadeFactory
	}

	public static Config getInstance() {
		while (instance == null) {
			try {
				instance = new Config("persistenceUnit", new MySqlDaoFactory());
			} catch (ExternalServiceConnectionException ex) {
				System.out.println("[FATAL ERROR] Configuration Variables file could not be found, this is a ");
			}
		}
		return instance;
	}

	public AbstractDaoFactory getDaoFactory() {
		return daoFactory;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}
        
        public FacadeFactory getFacadeFactory() {
        	return facadeFactory;
        }
}
