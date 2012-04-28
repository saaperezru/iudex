package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.FeedbackTypeDao;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.vo.FeedbackTypeVo;

/**
 * JpaDao for feedback type value objects. Implements additionally an useful finder by
 * name.
 *
 * @author healarconr
 */
public class JpaFeedbackTypeDao extends JpaCrudDao<FeedbackTypeVo,FeedbackTypeEntity> implements FeedbackTypeDao<EntityManager> {

    /**
     * Returns a FeedbackType entity using the information in the provided
     * FeedbackType value object.
     * 
     * @param em the data access adapter
     * @param vo the FeedbackType value object
     * @return the FeedbackType entity
     */
    @Override
    protected FeedbackTypeEntity voToEntity(DataAccessAdapter<EntityManager> em, FeedbackTypeVo vo) {
        
        FeedbackTypeEntity feedbackTypeEntity = new FeedbackTypeEntity();
        
        feedbackTypeEntity.setId(vo.getId());
        feedbackTypeEntity.setName(vo.getName());

        return feedbackTypeEntity;
    }

    @Override
    protected Class getEntityClass() {
        return FeedbackTypeEntity.class;
    }
    
    /**
     * Returns a feedback type value object which name matches the given name
     * @param em the data access adapter
     * @param name the name
     * @return a feedback type value object
     */
    @Override
    public FeedbackTypeVo getByName(DataAccessAdapter<EntityManager> em, String name) {
        checkDataAccessAdapter(em);
        try {
            return ((FeedbackTypeEntity)em.getDataAccess().createNamedQuery("getFeedbackTypeByName", FeedbackTypeEntity.class).setParameter("name", name).getSingleResult()).toVo();
        } catch (NoResultException ex) {
            return null;
        }
    }

    /**
     * Returns a list of all the feedback type value objects
     * @param em the data access adapter
     * @return a list with all feedback types
     */
    @Override
    public List<FeedbackTypeVo> getAll(DataAccessAdapter<EntityManager> em) {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getAllFeedbackType").getResultList());
    }
}
