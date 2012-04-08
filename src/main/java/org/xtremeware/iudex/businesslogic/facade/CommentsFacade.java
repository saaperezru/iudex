
package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManagerFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;

public class CommentsFacade extends AbstractFacade {
		public CommentsFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
			super(serviceFactory, emFactory);
		}

	

}
