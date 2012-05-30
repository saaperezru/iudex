package org.xtremeware.iudex.dao.sql;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for the Comment entities. Implements additionally some useful finders by
 * associated professor id, subject id, course id and user id.
 *
 * @author saaperezru
 */
public class SqlCommentDao extends SqlCrudDao<CommentEntity> implements CommentDao {

    public SqlCommentDao(Delete delete) {
        super(delete);
    }

    /**
     * Returns a list of Comments associated with the course who's professor is
     * identified by the given id
     *
     * @param entityManager EntityManager with which the entities will be
     * searched
     * @param professorId Professor identifier to look for in comment entities
     * associated professor .
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getByProfessorId(EntityManager entityManager, long professorId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getCommentsByProfessorId", CommentEntity.class).setParameter("professorId", professorId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of Comments associated with the course who's subject is
     * identified by the given id
     *
     * @param entityManager EntityManager with which the entities will be
     * searched
     * @param subjectId Subject identifier to look for in comments entities.
     * associated course.
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getBySubjectId(EntityManager entityManager, long subjectId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getCommentsBySubjectId", CommentEntity.class).
                    setParameter("subjectId", subjectId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of Comments associated with the period identified by the
     * given id
     *
     * @param entityManager EntityManager with which the entities will be
     * searched
     * @param userId User identifier to look for in comments entities.
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getByUserId(EntityManager entityManager, long userId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getCommentsByUserId", CommentEntity.class).
                    setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of Comments associated with the course identified by the
     * given id
     *
     * @param entityManager EntityManager with which the entities will be
     * searched
     * @param courseId Course identifier to look for in comment entities.
     * @return The list of found comments.
     */
    @Override
    public List<CommentEntity> getByCourseId(EntityManager entityManager, long courseId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getCommentsByCourseId", CommentEntity.class).
                    setParameter("courseId", courseId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns the number of comments submitted by a user on the current date
     *
     * @param entityManager entity manager
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    @Override
    public int getUserCommentsCounter(EntityManager entityManager, long userId)
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getUserCommentsCounter", Long.class).
                    setParameter("userId", userId).getSingleResult().intValue();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return CommentEntity.class;
    }
}
