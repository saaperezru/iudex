package org.xtremeware.iudex.helper;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.vo.MailingConfigVo;

/**
 *
 * @author saaperezru
 */
public class Config {

	public final static String CONFIGURATION_VARIABLES_PATH =
			"/org/xtremeware/iudex/iudex.properties";
	private EntityManagerFactory persistenceUnit;
	private AbstractDaoBuilder daoFactory;
	private static Config instance;
	private ServiceBuilder serviceFactory;
	private FacadeFactory facadeFactory;

	private Config(String persistenceUnit, AbstractDaoBuilder daoFactory) throws
			ExternalServiceConnectionException {
		try {

			this.persistenceUnit = Persistence.createEntityManagerFactory(
					persistenceUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.daoFactory = daoFactory;
		MailingConfigVo mailingConf = new MailingConfigVo();
		mailingConf.setSender(ConfigurationVariablesHelper.getVariable(
				ConfigurationVariablesHelper.MAILING_SENDER_EMAIL_ADDRESS));
		mailingConf.setSmtpPassword(ConfigurationVariablesHelper.getVariable(
				ConfigurationVariablesHelper.MAILING_SMTP_PASSWORD));
		mailingConf.setSmtpServer(ConfigurationVariablesHelper.getVariable(
				ConfigurationVariablesHelper.MAILING_SMTP_SERVER));
		mailingConf.setSmtpServerPort(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PORT)));
		mailingConf.setSmtpUser(ConfigurationVariablesHelper.getVariable(
				ConfigurationVariablesHelper.MAILING_SMTP_USER));
		this.serviceFactory = new ServiceBuilder(daoFactory, mailingConf);
		facadeFactory = new FacadeFactory(serviceFactory, this.persistenceUnit);
	}

	public static synchronized Config getInstance() {
		while (instance == null) {
			try {
				instance = new Config(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.APP_PERSISTENCE_UNIT),
						(AbstractDaoBuilder) Class.forName(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.APP_DAO_BUILDER)).newInstance());
			} catch (ExternalServiceConnectionException exception) {
				throw new RuntimeException("[FATAL ERROR] Configuration Variables file could not be found, without this file the application won't work ", exception.getCause());
			} catch (ClassNotFoundException exception) {
				throw new RuntimeException("[FATAL ERROR] DaoBuilder specified : " + ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.APP_DAO_BUILDER) + " class could not be found ", exception.getCause());
			} catch (InstantiationException exception) {
				throw new RuntimeException("[FATAL ERROR] DaoBuilder could not be instantiated", exception.getCause());
			} catch (IllegalAccessException exception) {
				throw new RuntimeException("[FATAL ERROR] Illegal Access Exception while initializing application Configuration", exception.getCause());
			}
		}
		return instance;
	}

	public AbstractDaoBuilder getDaoFactory() {
		return daoFactory;
	}

	public EntityManagerFactory getPersistenceUnit() {
		return persistenceUnit;
	}

	public ServiceBuilder getServiceFactory() {
		return serviceFactory;
	}

	public FacadeFactory getFacadeFactory() {
		return facadeFactory;
	}
}
