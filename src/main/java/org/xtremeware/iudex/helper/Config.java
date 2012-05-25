package org.xtremeware.iudex.helper;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.sql.MySqlDaoBuilder;
import org.xtremeware.iudex.vo.MailingConfigVo;

/**
 *
 * @author saaperezru
 */
public class Config {

    // TODO: Do not register the MySQL driver
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public final static String CONFIGURATION_VARIABLES_PATH =
            "/org/xtremeware/iudex/iudex.properties";
    private EntityManagerFactory persistenceUnit;
    private AbstractDaoBuilder daoFactory;
    private static Config instance;
    private ServiceFactory serviceFactory;
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
        mailingConf.setSmtpServerPort(Integer.parseInt(ConfigurationVariablesHelper.
                getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PORT)));
        mailingConf.setSmtpUser(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_USER));
        this.serviceFactory = new ServiceFactory(daoFactory, mailingConf);
        facadeFactory = new FacadeFactory(serviceFactory, this.persistenceUnit);
    }

    public static synchronized Config getInstance() {
        while (instance == null) {
            try {
                instance = new Config("org.xtremeware.iudex_local",
                        MySqlDaoBuilder.getInstance());
            } catch (ExternalServiceConnectionException ex) {
                System.out.println(
                        "[FATAL ERROR] Configuration Variables file could not be found, this is a ");
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

    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }
}
