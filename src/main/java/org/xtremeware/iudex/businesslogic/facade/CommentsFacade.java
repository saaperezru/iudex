package org.xtremeware.iudex.businesslogic.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.VoVwFactory;
import org.xtremeware.iudex.vo.BinaryRatingVo;
import org.xtremeware.iudex.vo.CommentVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

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

    public List<CommentVoVwFull> getCommentsByCourseId(long courseId) {
        EntityManager em = null;
        List<CommentVoVwFull> commentVoVwFulls = new ArrayList<CommentVoVwFull>();
        try {
            em = getEntityManagerFactory().createEntityManager();

            List<CommentVo> comments = getServiceFactory().createCommentsService().
                    getByCourseId(em, courseId);

            HashMap<Long, UserVoVwSmall> users =
                    new HashMap<Long, UserVoVwSmall>();

            for (CommentVo commentVo : comments) {
                if (!users.containsKey(commentVo.getUserId())) {
                    users.put(commentVo.getUserId(), VoVwFactory.getUserVoVwSmall(courseId,
                            getServiceFactory().createUsersService().getById(em,
                            commentVo.getUserId())));
                }
                if (commentVo.isAnonymous()) {
                    commentVoVwFulls.add(VoVwFactory.getCommentVoVwFull(commentVo,
                            null, getCommentRatingSummary(commentVo.getId())));
                } else {
                    commentVoVwFulls.add(VoVwFactory.getCommentVoVwFull(commentVo,
                            users.get(commentVo.getId()), getCommentRatingSummary(commentVo.getId())));
                }
            }

        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return commentVoVwFulls;
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
