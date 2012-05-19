package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.CourseDaoInterface;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for the Course entities. Implements additionally some useful finders by
 * associated professor id, subject id and period id.
 *
 * @author saaperezru
 */
public class CourseDao extends CrudDao<CourseEntity> implements CourseDaoInterface {

    /**
     * Returns a list of Courses associated with the professor identified by the
     * given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param professorId Professor identifier to look for in courses entities.
     * @return The list of found courses.
     */
    @Override
    public List<CourseEntity> getByProfessorId(EntityManager em, long professorId)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCourseByProfessorId", CourseEntity.class).setParameter("professorId", professorId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of Courses associated with the subject identified by the
     * given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param subjectId Subject identifier to look for in courses entities.
     * @return The list of found courses.
     */
    @Override
    public List<CourseEntity> getBySubjectId(EntityManager em, long subjectId)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCourseBySubjectId", CourseEntity.class).setParameter("subjectId", subjectId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of Courses associated with the period identified by the
     * given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param periodId Period identifier to look for in courses entities.
     * @return The list of found courses.
     */
    @Override
    public List<CourseEntity> getByPeriodId(EntityManager em, long periodId)
            throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCourseByPeriodId", CourseEntity.class).setParameter("periodId", periodId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of Courses associated with the professor and subject
     * identified by the given ids
     *
     * @param em EntityManager with which the entities will be searched
     * @param professorId Professor identifier to look for in courses entities.
     * @param subjectId Subject identifier to look for in courses entities.
     * @return The list of found courses.
     */
    @Override
    public List<CourseEntity> getByProfessorIdAndSubjectId(EntityManager em,
            long professorId, long subjectId) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getCourseByProfessorIdAndSubjectId", CourseEntity.class).
                    setParameter("professorId", professorId).setParameter("subjectId", subjectId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of Courses associated with professors and subjects whose
     * names are like the ones provided in
     * <code>subjectName</code> and
     * <code>professorName</code>, and whose period corresponds to the one
     * identified by the id
     * <code>periodId</code>.
     *
     * @param em EntityManager with which the entities will be searched
     * @param professorName If null will search for any professor
     * @param subjectName If null will search for any subject
     * @param periodId If null will search for any period
     * @return The list of courses related to professors and subjects with names
     * like the ones provided.
     */
    @Override
    public List<CourseEntity> getCoursesByProfessorNameLikeAndSubjectNameLike(EntityManager em,
            String professorName, String subjectName, Long periodId) throws DataBaseException {
        checkEntityManager(em);
        try {
            if (periodId == null) {
                return em.createNamedQuery("getCoursesByProfessorNameLikeAndSubjectNameLike", CourseEntity.class).
                        setParameter("professorName", "%" + professorName + "%").setParameter("subjectName", "%" + subjectName + "%").
                        getResultList();
            } else {
                return em.createNamedQuery("getCoursesByProfessorNameLikeAndSubjectNameLikeAndPeriodId", CourseEntity.class).
                        setParameter("professorName", "%" + professorName + "%").setParameter("subjectName", "%" + subjectName + "%").
                        setParameter("periodId", periodId).getResultList();
            }
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

    }

    @Override
    protected Class getEntityClass() {
        return CourseEntity.class;
    }
}
