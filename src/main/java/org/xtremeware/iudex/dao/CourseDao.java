
package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CourseEntity;

/**
 * DAO for the Course entities. Implements additionally some useful finders by
 * associated professor id, subject id and period id.
 *
 * @author saaperezru
 */


public class CourseDao extends Dao<CourseEntity> {


	/**
	 * Returns a list of Courses associated with the professor identified by the given id 
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param professorId Professor identifier to look for in courses entities.
	 * @return The list of found courses.
	 */
	public List<CourseEntity> getByProfessorId(EntityManager em, long professorId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCourseByProfessorId").setParameter("professorId", professorId).getResultList();

	}

	/**
	 * Returns a list of Courses associated with the subject identified by the given id 
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param subjectId Subject identifier to look for in courses entities.
	 * @return The list of found courses.
	 */
	public List<CourseEntity> getBySubjectId(EntityManager em, long subjectId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCourseBySubjectId").setParameter("subjectId", subjectId).getResultList();

	}

	/**
	 * Returns a list of Courses associated with the period identified by the given id 
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param periodId Period identifier to look for in courses entities.
	 * @return The list of found courses.
	 */
	public List<CourseEntity> getByPeriodId(EntityManager em, long periodId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCourseByPeriodId").setParameter("periodId", periodId).getResultList();

	}


	/**
	 * Returns a list of Courses associated with the professor and subject identified by the given ids
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param professorId Professor identifier to look for in courses entities.
	 * @param subjectId Subject identifier to look for in courses entities.
	 * @return The list of found courses.
	 */
	public List<CourseEntity> getByProfessorIdAndSubjectId(EntityManager em, long professorId, long subjectId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCourseByProfessorIdAndSubjectId").setParameter("professorId", professorId).setParameter("subjectId", subjectId).getResultList();

	}


}
