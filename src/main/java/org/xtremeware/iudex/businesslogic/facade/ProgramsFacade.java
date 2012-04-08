
package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManagerFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;

public class ProgramsFacade extends AbstractFacade{
		public ProgramsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
			super(serviceFactory, emFactory);
		}


}
