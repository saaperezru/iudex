
package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManagerFactory;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;

public class FeedbacksFacade extends AbstractFacade{

			public FeedbacksFacade(ServiceFactory serviceFactory, EntityManagerFactory emFactory) {
				super(serviceFactory, emFactory);
			}

}
