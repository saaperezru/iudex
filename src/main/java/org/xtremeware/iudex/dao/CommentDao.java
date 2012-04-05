
package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CourseEntity;
/**
 * DAO for the Comment entities. Implements additionally some useful finders by
 * associated professor id, subject id, course id and user id.
 *
 * @author saaperezru
 */
public class CommentDao extends Dao<CommentEntity> {

	/**
	 * Returns a list of Comments associated with the course who's professor is identified by the given id 
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param professorId Professor identifier to look for in comment entities' associated professor .
	 * @return The list of found comments.
	 */
	public List<CommentEntity> getByProfessorId(EntityManager em, long professorId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCommentsByProfessorId").setParameter("professorId", professorId).getResultList();

	}

	
	/**
	 * Returns a list of Comments associated with the course who's subject is identified by the given id 
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param subjectId Subject identifier to look for in comments entities' associated course.
	 * @return The list of found comments.
	 */
	public List<CommentEntity> getBySubjectId(EntityManager em, long subjectId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCommentsBySubjectId").setParameter("subjectId", subjectId).getResultList();

	}

	/**
	 * Returns a list of Comments associated with the period identified by the given id 
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param userId User identifier to look for in comments entities.
	 * @return The list of found comments.
	 */
	public List<CourseEntity> getByUserId(EntityManager em, long userId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCommentsByUserId").setParameter("userId", userId).getResultList();

	}


	/**
	 * Returns a list of Courses associated with the course identified by the given id
	 *
	 * @param em EntityManager with which the entities will be searched
	 * @param courseId Course identifier to look for in comment entities.
	 * @return The list of found comments.
	 */
	public List<CourseEntity> getByCourseId(EntityManager em, long courseId)	{
	if (em == null) {
        	    throw new IllegalArgumentException("EntityManager em cannot be null");
	        }
	return em.createNamedQuery("getCommentsByCourseId").setParameter("courseId", courseId).getResultList();

	}

}
