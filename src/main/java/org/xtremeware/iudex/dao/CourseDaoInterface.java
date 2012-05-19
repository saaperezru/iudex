package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the Course entities.
 *
 * @author saaperezru
 */
public interface CourseDaoInterface extends CrudDaoInterface<CourseEntity> {

    /**
     * Returns a list of Courses associated with the professor identified by the
     * given id
     *
     * @param entityManager EntityManager with which the entities will be searched
     * @param professorId Professor identifier to look for in courses entities.
     * @return The list of found courses.
     */
    public List<CourseEntity> getByProfessorId(EntityManager entityManager, long professorId)
            throws DataBaseException;

    /**
     * Returns a list of Courses associated with the subject identified by the
     * given id
     *
     * @param entityManager EntityManager with which the entities will be searched
     * @param subjectId Subject identifier to look for in courses entities.
     * @return The list of found courses.
     */
    public List<CourseEntity> getBySubjectId(EntityManager entityManager, long subjectId)
            throws DataBaseException;

    /**
     * Returns a list of Courses associated with the period identified by the
     * given id
     *
     * @param entityManager EntityManager with which the entities will be searched
     * @param periodId Period identifier to look for in courses entities.
     * @return The list of found courses.
     */
    public List<CourseEntity> getByPeriodId(EntityManager entityManager, long periodId)
            throws DataBaseException;

    /**
     * Returns a list of Courses associated with the professor and subject
     * identified by the given ids
     *
     * @param entityManager EntityManager with which the entities will be searched
     * @param professorId Professor identifier to look for in courses entities.
     * @param subjectId Subject identifier to look for in courses entities.
     * @return The list of found courses.
     */
    public List<CourseEntity> getByProfessorIdAndSubjectId(EntityManager entityManager,
            long professorId, long subjectId) throws DataBaseException;

    /**
     * Returns a list of Courses associated with professors and subjects whose
     * names are like the ones provided in
     * <code>subjectName</code> and
     * <code>professorName</code>, and whose period corresponds to the one
     * identified by the id
     * <code>periodId</code>.
     *
     * @param entityManager EntityManager with which the entities will be searched
     * @param professorName If null will search for any professor
     * @param subjectName If null will search for any subject
     * @param periodId If null will search for any period
     * @return The list of courses related to professors and subjects with names
     * like the ones provided.
     */
    public List<CourseEntity> getCoursesByProfessorNameLikeAndSubjectNameLike(EntityManager entityManager,
            String professorName, String subjectName, Long periodId)
            throws DataBaseException;
}
