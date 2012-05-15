package org.xtremeware.iudex.businesslogic.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;
import org.xtremeware.iudex.vo.*;

public class CommentsFacade extends AbstractFacade {

    public CommentsFacade(ServiceFactory serviceFactory,
            EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    public CommentVo addComment(CommentVo vo) throws
            MultipleMessagesException, MaxCommentsLimitReachedException {
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
        } catch (MultipleMessagesException e) {
            throw e;
        } catch (MaxCommentsLimitReachedException e) {
            throw e;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return createdVo;

    }

    public void removeComment(long commentId) throws DataBaseException {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            getServiceFactory().createCommentsService().remove(em, commentId);
            tx.commit();
        } catch (DataBaseException e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
    }

    public List<CommentVoVwFull> getCommentsByCourseId(long courseId) throws
            DataBaseException {
        EntityManager em = null;
        EntityTransaction tx = null;
        List<CommentVoVwFull> result = new ArrayList<CommentVoVwFull>();
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            List<CommentVo> comments = getServiceFactory().createCommentsService().
                    getByCourseId(em, courseId);
            HashMap<Long, UserVoVwSmall> users =
                    new HashMap<Long, UserVoVwSmall>();

            for (CommentVo c : comments) {
                if (!users.containsKey(c.getUserId())) {
                    UserVo vo = getServiceFactory().createUsersService().getById(
                            em, c.getUserId());
                    UserVoVwSmall uservo = new UserVoVwSmall(courseId, vo.
                            getFirstName() + " " + vo.getLastName(), vo.
                            getUserName());
                    uservo = users.put(c.getUserId(), uservo);
                }
                if (c.isAnonymous()) {
                    result.add(new CommentVoVwFull(c, null,getCommentRatingSummary(c.getId())));
                } else {
                    result.add(new CommentVoVwFull(c, users.get(c.getId()),getCommentRatingSummary(c.getId())));
                }
            }

        } catch (DataBaseException e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return result;
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
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return summary;
    }

    public CommentRatingVo getCommentRatingByUserId(long commentId, long userId)
            throws DataBaseException {
        EntityManager em = null;
        CommentRatingVo rating = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            rating = getServiceFactory().createCommentRatingService().
                    getByCommentIdAndUserId(em, commentId, userId);
        } catch (DataBaseException e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            throw e;
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return rating;
    }

    public CommentRatingVo rateComment(long commentId, long userId, int value)
            throws MultipleMessagesException {
        EntityManager em = null;
        EntityTransaction tx = null;
        CommentRatingVo rating = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            rating = getServiceFactory().createCommentRatingService().
                    getByCommentIdAndUserId(em, commentId, userId);
            if (rating == null) {
                rating = new CommentRatingVo();
                rating.setEvaluetedObjectId(commentId);
                rating.setUser(userId);
                rating.setValue(value);
                rating = getServiceFactory().createCommentRatingService().create(
                        em, rating);
            } else {
                rating.setValue(value);
                getServiceFactory().createCommentRatingService().update(em,
                        rating);
            }
            tx.commit();

        } catch (MultipleMessagesException ex) {
            throw ex;
        } catch (Exception e) {
            getServiceFactory().createLogService().error(e.getMessage(), e);
            if (em != null && tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.clear();
                em.close();
            }
        }
        return rating;
    }
}
