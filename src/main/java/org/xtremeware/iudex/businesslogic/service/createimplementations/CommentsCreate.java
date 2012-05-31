package org.xtremeware.iudex.businesslogic.service.createimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CommentsCreate implements Create<CommentEntity> {

    private AbstractDaoBuilder daoFactory;
    public final int MAX_COMMENTS_PER_DAY;

    public CommentsCreate(AbstractDaoBuilder daoFactory) {
        this.daoFactory = daoFactory;
        MAX_COMMENTS_PER_DAY = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_COMMENTS_PER_DAY));
    }
    
    
    @Override
    public CommentEntity create(EntityManager entityManager, CommentEntity entity) 
            throws DataBaseException, DuplicityException, MaxCommentsLimitReachedException {
        if (checkUserCommentsCounter(entityManager, entity.getUser().getId()) >= MAX_COMMENTS_PER_DAY) {
            throw new MaxCommentsLimitReachedException(entity.getUser().toVo(), entity.getCourse().toVo());
        }
        getDaoFactory().getCommentDao().create(entityManager, entity);
        return entity;
    }
    
    private int checkUserCommentsCounter(EntityManager em, Long userId) throws DataBaseException {
        return getDaoFactory().getCommentDao().getUserCommentsCounter(em, userId);
    }

    private AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }
}
