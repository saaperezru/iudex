package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.dao.Remove;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class FeedbackTypesRemove implements Remove {

    private AbstractDaoBuilder daoFactory;

    public FeedbackTypesRemove(AbstractDaoBuilder daoFactory) {
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

    private AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }
}
