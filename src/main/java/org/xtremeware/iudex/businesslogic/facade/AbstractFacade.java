package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManagerFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;

public abstract class AbstractFacade {

	private ServiceFactory serviceFactory;
	private EntityManagerFactory emFactory;

	public AbstractFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
		this.emFactory = emFactory;
		this.serviceFactory = serviceFactory;
	}

	protected ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	protected EntityManagerFactory getEntityManagerFactory(){
		return this.emFactory;
	}

}
