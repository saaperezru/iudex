package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.*;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.*;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

public class CommentsFacade extends AbstractFacade {

    public CommentsFacade(ServiceFactory serviceFactory,
            EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public CommentVo addComment(CommentVo vo) throws
            MultipleMessagesException, MaxCommentsLimitReachedException, DuplicityException {
        CommentVo createdVo = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            createdVo = getServiceFactory().createCommentsService().create(em,
                    vo);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkException(e, MaxCommentsLimitReachedException.class);
            FacadesHelper.checkExceptionAndRollback(em, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return createdVo;

    }

    public void removeComment(long commentId) throws Exception {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            getServiceFactory().createCommentsService().remove(em, commentId);
            tx.commit();
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.rollbackTransaction(em, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
    }

    public List<CommentVoFull> getCommentsByCourseId(long courseId) {
        EntityManager em = null;
        List<CommentVoFull> commentVoFulls = new ArrayList<CommentVoFull>();
        try {
            em = getEntityManagerFactory().createEntityManager();

            List<CommentVo> comments = getServiceFactory().createCommentsService().
                    getByCourseId(em, courseId);

            HashMap<Long, UserVoSmall> users =
                    new HashMap<Long, UserVoSmall>();

            for (CommentVo commentVo : comments) {
                if (!users.containsKey(commentVo.getUserId())) {
                    users.put(commentVo.getUserId(),
                            new UserVoSmall(getServiceFactory().
                            createUsersService().getById(em,
                            commentVo.getUserId())));
                }
                if (commentVo.isAnonymous()) {
                    commentVoFulls.add(new CommentVoFull(commentVo,
                            null, getCommentRatingSummary(commentVo.getId())));
                } else {
                    commentVoFulls.add(new CommentVoFull(commentVo, users.get(commentVo.getUserId()), getCommentRatingSummary(commentVo.getId())));

                }
            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return commentVoFulls;
    }

    public RatingSummaryVo getCommentRatingSummary(long commentId) {
        EntityManager em = null;
        RatingSummaryVo summary = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            summary = getServiceFactory().createCommentRatingService().
                    getSummary(em, commentId);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return summary;
    }

    public BinaryRatingVo getCommentRatingByUserId(long commentId, long userId) {
        EntityManager em = null;
        BinaryRatingVo rating = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            rating = getServiceFactory().createCommentRatingService().
                    getByEvaluatedObjectAndUserId(em, commentId, userId);
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return rating;
    }

    public BinaryRatingVo rateComment(long commentId, long userId, int value)
            throws MultipleMessagesException, Exception {
        EntityManager entityManager = null;
        EntityTransaction tx = null;
        BinaryRatingVo rating = null;
        try {
            BinaryRatingVo vo = new BinaryRatingVo();
            vo.setEvaluatedObjectId(commentId);
            vo.setUserId(userId);
            vo.setValue(value);

            entityManager = getEntityManagerFactory().createEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            rating = getServiceFactory().createCommentRatingService().create(entityManager, vo);
            tx.commit();

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            FacadesHelper.checkException(e, MultipleMessagesException.class);
            FacadesHelper.checkExceptionAndRollback(entityManager, tx, e, DuplicityException.class);
            FacadesHelper.rollbackTransaction(entityManager, tx, e);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return rating;
    }
}
