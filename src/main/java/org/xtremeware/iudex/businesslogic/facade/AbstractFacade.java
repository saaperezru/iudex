package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManagerFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceBuilder;

public abstract class AbstractFacade {

	private ServiceBuilder serviceFactory;
	private EntityManagerFactory emFactory;

	public AbstractFacade(ServiceBuilder serviceFactory, EntityManagerFactory emFactory) {
		this.emFactory = emFactory;
		this.serviceFactory = serviceFactory;
	}

	protected ServiceBuilder getServiceFactory() {
		return serviceFactory;
	}

	protected EntityManagerFactory getEntityManagerFactory(){
		return this.emFactory;
	}

}
