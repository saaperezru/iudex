package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.CommentVo;

/**
 * DAO for the CommentVo. Implements additionally some useful finders by
 * associated professor id, subject id, course id and user id.
 *
 * @author josebermeo
 */
public interface CommentDao<E> extends CrudDao<CommentVo, E> {

    /**
     * Returns a list of Comments associated with the course who's professor is
     * identified by the given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param professorId Professor identifier to look for in comment table
     * associated professor .
     * @return The list of found comments.
     */
    public List<CommentVo> getByProfessorId(DataAccessAdapter<E> em, long professorId)throws DataAccessException;

    /**
     * Returns a list of Comments associated with the course who's subject is
     * identified by the given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param subjectId Subject identifier to look for in comments table
     * associated course.
     * @return The list of found comments.
     */
    public List<CommentVo> getBySubjectId(DataAccessAdapter<E> em, long subjectId)throws DataAccessException;

    /**
     * Returns a list of Comments associated with the period identified by the
     * given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param userId User identifier to look for in comments table.
     * @return The list of found comments.
     */
    public List<CommentVo> getByUserId(DataAccessAdapter<E> em, long userId)throws DataAccessException;

    /**
     * Returns a list of Comments associated with the course identified by the
     * given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param courseId Course identifier to look for in comment table.
     * @return The list of found comments.
     */
    public List<CommentVo> getByCourseId(DataAccessAdapter<E> em, long courseId)throws DataAccessException;

    /**
     * Returns the number of comments submitted by a user on the current date
     * 
     * @param em DataAccessAdapter
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    public int getUserCommentsCounter(DataAccessAdapter<E> em, long userId)throws DataAccessException;
}
