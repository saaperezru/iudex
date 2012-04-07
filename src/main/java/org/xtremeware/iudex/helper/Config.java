/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.helper;

import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.MySqlDaoFactory;
import org.xtremeware.iudex.vo.MailingConfigVo;

/**
 *
 * @author saaperezru
 */
public class Config {

	private String persistenceUnit;
	private AbstractDaoFactory daoFactory;
	private String configurationVariablesPath;
	private static Config instance;

	private Config(String persistenceUnit, AbstractDaoFactory daoFactory, String configurationVariablesPath ) {
		this.daoFactory = daoFactory;
		this.configurationVariablesPath = configurationVariablesPath;
	}

	public static Config getInstance() {
		while (instance == null) {
			instance = new Config("persistenceUnit", new MySqlDaoFactory(), "iudex.properties");
		}
		return instance;
	}

	public String getConfigurationVariablesPath() {
		return configurationVariablesPath;
	}

	public void setConfigurationVariablesPath(String configurationVariablesPath) {
		this.configurationVariablesPath = configurationVariablesPath;
	}

	public AbstractDaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(AbstractDaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public void setPersistenceUnit(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}
}
