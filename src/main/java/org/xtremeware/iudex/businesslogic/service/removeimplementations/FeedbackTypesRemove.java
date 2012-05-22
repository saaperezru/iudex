package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Remove;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class FeedbackTypesRemove implements Remove {

    private AbstractDaoFactory daoFactory;

    public FeedbackTypesRemove(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Remove the FeedBack Type and all the Feedback Comments associated to it.
     *
     * @param em entity manager
     * @param id id of the FeedBackType
     */
    @Override
    public void remove(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<FeedbackEntity> feedbackEntitys = getDaoFactory().
                getFeedbackDao().getByTypeId(entityManager, entityId);
        for (FeedbackEntity feedbackEntity : feedbackEntitys) {
            getDaoFactory().getFeedbackDao().remove(entityManager, feedbackEntity.getId());
        }

        getDaoFactory().getFeedbackTypeDao().remove(entityManager, entityId);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
