package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CommentEntity;

/**
 * DAO for the Comment entities. Implements additionally some useful finders by
 * associated professor id, subject id, course id and user id.
 *
 * @author saaperezru
 */
public class CommentDao extends CrudDao<CommentEntity> implements CommentDaoInterface {

    /**
     * Returns a list of Comments associated with the course who's professor is
     * identified by the given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param professorId Professor identifier to look for in comment entities'
     * associated professor .
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getByProfessorId(EntityManager em, long professorId) {
        checkEntityManager(em);
        return em.createNamedQuery("getCommentsByProfessorId", CommentEntity.class).setParameter("professorId", professorId).getResultList();

    }

    /**
     * Returns a list of Comments associated with the course who's subject is
     * identified by the given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param subjectId Subject identifier to look for in comments entities.
     * associated course.
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getBySubjectId(EntityManager em, long subjectId) {
        checkEntityManager(em);
        return em.createNamedQuery("getCommentsBySubjectId", CommentEntity.class).setParameter("subjectId", subjectId).getResultList();

    }

    /**
     * Returns a list of Comments associated with the period identified by the
     * given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param userId User identifier to look for in comments entities.
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getByUserId(EntityManager em, long userId) {
        checkEntityManager(em);
        return em.createNamedQuery("getCommentsByUserId", CommentEntity.class).setParameter("userId", userId).getResultList();

    }

    /**
     * Returns a list of Comments associated with the course identified by the
     * given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param courseId Course identifier to look for in comment entities.
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getByCourseId(EntityManager em, long courseId) {
        checkEntityManager(em);
        return em.createNamedQuery("getCommentsByCourseId", CommentEntity.class).setParameter("courseId", courseId).getResultList();
    }

    /**
     * Returns the number of comments submitted by a user on the current date
     *
     * @param em entity manager
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    @Override
    public int getUserCommentsCounter(EntityManager em, long userId) {
        checkEntityManager(em);
        return em.createNamedQuery("getUserCommentsCounter", Long.class).setParameter("userId", userId).getSingleResult().intValue();
    }
}