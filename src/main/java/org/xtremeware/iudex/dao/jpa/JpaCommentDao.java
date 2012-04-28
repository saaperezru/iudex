package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.CommentDao;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.vo.CommentVo;

/**
 * DAO for the Comment entities. Implements additionally some useful finders by
 * associated professor id, subject id, course id and user id.
 *
 * @author saaperezru
 */
public class JpaCommentDao extends JpaCrudDao<CommentVo,CommentEntity> implements CommentDao<EntityManager> {

    /**
     * Returns a list of Comments associated with the course who's professor is
     * identified by the given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param professorId Professor identifier to look for in comment entities'
     * associated professor .
     * @return The list of found comments.
     */
    public List<CommentEntity> getByProfessorId(EntityManager em, long professorId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getCommentsByProfessorId").setParameter("professorId", professorId).getResultList();

    }

    /**
     * Returns a list of Comments associated with the course who's subject is
     * identified by the given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param subjectId Subject identifier to look for in comments entities'
     * associated course.
     * @return The list of found comments.
     */
    public List<CommentEntity> getBySubjectId(EntityManager em, long subjectId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getCommentsBySubjectId").setParameter("subjectId", subjectId).getResultList();

    }

    /**
     * Returns a list of Comments associated with the period identified by the
     * given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param userId User identifier to look for in comments entities.
     * @return The list of found comments.
     */
    public List<CommentEntity> getByUserId(EntityManager em, long userId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getCommentsByUserId").setParameter("userId", userId).getResultList();

    }

    /**
     * Returns a list of Comments associated with the course identified by the
     * given id
     *
     * @param em EntityManager with which the entities will be searched
     * @param courseId Course identifier to look for in comment entities.
     * @return The list of found comments.
     */
    public List<CommentEntity> getByCourseId(EntityManager em, long courseId) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getCommentsByCourseId").setParameter("courseId", courseId).getResultList();
    }
    
    /**
     * Returns the number of comments submitted by a user on the current date
     * 
     * @param em entity manager
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    public int getUserCommentsCounter(EntityManager em, long userId){
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return ((Integer) em.createNamedQuery("getUserCommentsCounter").setParameter("userId", userId).getSingleResult()).intValue();
    }

    @Override
    protected CommentEntity voToEntity(DataAccessAdapter<EntityManager> em, CommentVo vo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Class getEntityClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommentVo> getByProfessorId(DataAccessAdapter<EntityManager> em, long professorId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommentVo> getBySubjectId(DataAccessAdapter<EntityManager> em, long subjectId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommentVo> getByUserId(DataAccessAdapter<EntityManager> em, long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommentVo> getByCourseId(DataAccessAdapter<EntityManager> em, long courseId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getUserCommentsCounter(DataAccessAdapter<EntityManager> em, long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}