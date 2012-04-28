package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.FeedbackDao;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 * JpaDao for the Feedback value objects. Implements additionally a useful finder by
 * Feedback type.
 *
 * @author saaperezru
 */
public class JpaFeedbackDao extends JpaCrudDao<FeedbackVo,FeedbackEntity> implements FeedbackDao<EntityManager> {

    /**
     * Returns a Feedback entity using the information in the provided
     * Feedback value object.
     * 
     * @param em the data access adapter
     * @param vo the Feedback value object
     * @return the Feedback entity
     */
    @Override
    protected FeedbackEntity voToEntity(DataAccessAdapter<EntityManager> em, FeedbackVo vo) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        
        feedbackEntity.setId(vo.getId());
        feedbackEntity.setDate(vo.getDate());
        feedbackEntity.setContent(vo.getContent());

        feedbackEntity.setType(em.getDataAccess().getReference(FeedbackTypeEntity.class, vo.getFeedbackTypeId()));

        return feedbackEntity;
    }

    @Override
    protected Class getEntityClass() {
        return FeedbackEntity.class;
    }
    
    /**
     * Returns a list of Feedback with a type corresponding to the specified
     * feedbackTypeId
     * 
     * @param em the data access adapter
     * @param feedbackTypeId Feedback type identifier to look for in feedback
     * entities
     * @return The list of found feedbacks
     */
    @Override
    public List<FeedbackVo> getByTypeId(DataAccessAdapter<EntityManager> em, long feedbackTypeId) {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getByTypeId").setParameter("feedbackTypeId", feedbackTypeId).getResultList());
    }

    /**
     * Returns a list of Feedback with a type corresponding to the specified
     * content
     * 
     * @param em the da the data access adapterta access adapter
     * @param query the content of the feedback
     * @return The list of found feedbacks
     */
    @Override
    public List<FeedbackVo> getByContentLike(DataAccessAdapter<EntityManager> em, String query) {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getFeedbackByContentLike").setParameter("query", "%"+query+"%").getResultList());
    }
}
