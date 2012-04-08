
package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManagerFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;

public class UsersFacade extends AbstractFacade{

		public UsersFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
			super(serviceFactory, emFactory);
		}


}
