package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the Comment entities.
 *
 * @author josebermeo
 */
public interface CommentDaoInterface extends CrudDaoInterface<CommentEntity> {

    /**
     * Returns a list of Comments associated with the course who's professor is
     * identified by the given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param professorId Professor identifier to look for in comment table
     * associated professor .
     * @return The list of found comments.
     */
    public List<CommentEntity> getByProfessorId(EntityManager em, long professorId)
            throws DataBaseException;

    /**
     * Returns a list of Comments associated with the course who's subject is
     * identified by the given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param subjectId Subject identifier to look for in comments table
     * associated course.
     * @return The list of found comments.
     */
    public List<CommentEntity> getBySubjectId(EntityManager em, long subjectId)
            throws DataBaseException;

    /**
     * Returns a list of Comments associated with the period identified by the
     * given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param userId User identifier to look for in comments table.
     * @return The list of found comments.
     */
    public List<CommentEntity> getByUserId(EntityManager em, long userId)
            throws DataBaseException;

    /**
     * Returns a list of Comments associated with the course identified by the
     * given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param courseId Course identifier to look for in comment table.
     * @return The list of found comments.
     */
    public List<CommentEntity> getByCourseId(EntityManager em, long courseId)
            throws DataBaseException;

    /**
     * Returns the number of comments submitted by a user on the current date
     *
     * @param em DataAccessAdapter
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    public int getUserCommentsCounter(EntityManager em, long userId)
            throws DataBaseException;
}
