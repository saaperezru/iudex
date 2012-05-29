package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.*;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.*;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class CommentsFacade extends AbstractFacade {

    public CommentsFacade(ServiceBuilder serviceFactory,
            EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public CommentVo addComment(CommentVo vo) throws
            MultipleMessagesException, MaxCommentsLimitReachedException, DuplicityException {
        CommentVo createdVo = null;
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            createdVo = getServiceFactory().createCommentsService().create(entityManager,
                    vo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkException(exception, MaxCommentsLimitReachedException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return createdVo;

    }

    public void removeComment(long commentId) throws Exception {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().createCommentsService().remove(entityManager, commentId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }

    public List<CommentVo> getCommentsByCourseId(long courseId) {
        EntityManager entityManager = null;
        List<CommentVo> commentVos = new ArrayList<CommentVo>();
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            commentVos = getServiceFactory().createCommentsService().
                    getByCourseId(entityManager, courseId);       
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        
        return commentVos;
    }

    public RatingSummaryVo getCommentRatingSummary(long commentId) {
        EntityManager entityManager = null;
        RatingSummaryVo summary = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            summary = getServiceFactory().getCommentRatingService().
                    getSummary(entityManager, commentId);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return summary;
    }

    public BinaryRatingVo getCommentRatingByUserId(long commentId, long userId) {
        EntityManager entityManager = null;
        BinaryRatingVo rating = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            rating = getServiceFactory().getCommentRatingService().
                    getByEvaluatedObjectAndUserId(entityManager, commentId, userId);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return rating;
    }

    public BinaryRatingVo rateComment(long commentId, long userId, int value) throws MultipleMessagesException, DuplicityException {
        
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        BinaryRatingVo rating = null;
        try {
            BinaryRatingVo vo = new BinaryRatingVo();
            vo.setEvaluatedObjectId(commentId);
            vo.setUserId(userId);
            vo.setValue(value);

            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            rating = getServiceFactory().getCommentRatingService().create(entityManager, vo);
            transaction.commit();

        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return rating;
    }
}
