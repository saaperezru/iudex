package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.RemoveInterface;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class FeedbackTypesRemove implements RemoveInterface {

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
    public void remove(EntityManager em, Long id) throws DataBaseException {
        List<FeedbackEntity> feedBacks = getDaoFactory().getFeedbackDao().getByTypeId(em, id);
        for (FeedbackEntity feedBack : feedBacks) {
            getDaoFactory().getFeedbackDao().remove(em, feedBack.getId());
        }

        getDaoFactory().getFeedbackTypeDao().remove(em, id);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
