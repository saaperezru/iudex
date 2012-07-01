package org.xtremeware.iudex.helper;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.helper.MailingSessionFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;

public final class Config {

    public static final String CONFIGURATION_VARIABLES_PATH =
            "/org/xtremeware/iudex/iudex.properties";
    private EntityManagerFactory persistenceUnit;
    private AbstractDaoBuilder daoFactory;
    private static Config instance;
    private ServiceBuilder serviceFactory;
    private FacadeFactory facadeFactory;

    private Config(String persistenceUnit, AbstractDaoBuilder daoFactory) {
        try {

            this.persistenceUnit = Persistence.createEntityManagerFactory(
                    persistenceUnit);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.daoFactory = daoFactory;

        createIndex();
        MailingSessionFactory mailingSessionFactory = getMailingSessionFactory();
        serviceFactory = new ServiceBuilder(daoFactory, mailingSessionFactory);
        facadeFactory = new FacadeFactory(serviceFactory, this.persistenceUnit);
    }

    private void createIndex() {
        if (ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.CREATE_LUCENE_INDEX).equals("true")) {
            ConfigLucene.indexDataBase(persistenceUnit.createEntityManager());
        }
    }

    private MailingSessionFactory getMailingSessionFactory() {
        Class mailingSessionFactoryClass = getMailingSessionFactoryClass();
        return getMailingSessionInstance(mailingSessionFactoryClass);
    }

    private Class<MailingSessionFactory> getMailingSessionFactoryClass() {
        String sessionFactoryClassName = ConfigurationVariablesHelper.
                getVariable(
                ConfigurationVariablesHelper.MAILING_SESSION_FACTORY);
        try {
            return (Class<MailingSessionFactory>) Class.forName(
                    sessionFactoryClassName);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private MailingSessionFactory getMailingSessionInstance(
            Class<MailingSessionFactory> mailingSessionFactoryClass) {
        try {
            return mailingSessionFactoryClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static synchronized Config getInstance() {
        while (instance == null) {
            try {
                instance =
                        new Config(ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.APP_PERSISTENCE_UNIT),
                        (AbstractDaoBuilder) Class.forName(
                        ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.APP_DAO_BUILDER)).
                        newInstance());
            } catch (ExternalServiceConnectionException exception) {
                throw new RuntimeException(
                        "[FATAL ERROR] Configuration Variables file could not be found, without this file the application won't work ",
                        exception.getCause());
            } catch (ClassNotFoundException exception) {
                throw new RuntimeException("[FATAL ERROR] DaoBuilder specified : " +
                        ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.APP_DAO_BUILDER) +
                        " class could not be found ", exception.getCause());
            } catch (InstantiationException exception) {
                throw new RuntimeException(
                        "[FATAL ERROR] DaoBuilder could not be instantiated",
                        exception.getCause());
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(
                        "[FATAL ERROR] Illegal Access Exception while initializing application Configuration",
                        exception.getCause());
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
