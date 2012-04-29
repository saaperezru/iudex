package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.CommentDao;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.CommentVo;

/**
 * DAO for the CommentVo. Implements additionally some useful finders by
 * associated professor id, subject id, course id and user id.
 *
 * @author saaperezru
 */
public class JpaCommentDao extends JpaCrudDao<CommentVo, CommentEntity> implements CommentDao<EntityManager> {

    /**
     * Returns a list of Comments associated with the course who's professor is
     * identified by the given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param professorId Professor identifier to look for in comment table
     * associated professor .
     * @return The list of found comments.
     */
    @Override
    public List<CommentVo> getByProfessorId(DataAccessAdapter<EntityManager> em, long professorId) throws DataAccessException{
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCommentsByProfessorId", getEntityClass()).setParameter("professorId", professorId).getResultList());

    }

    /**
     * Returns a list of Comments associated with the course who's subject is
     * identified by the given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param subjectId Subject identifier to look for in comments table
     * associated course.
     * @return The list of found comments.
     */
    @Override
    public List<CommentVo> getBySubjectId(DataAccessAdapter<EntityManager> em, long subjectId) throws DataAccessException{
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCommentsBySubjectId", getEntityClass()).setParameter("subjectId", subjectId).getResultList());
    }

    /**
     * Returns a list of Comments associated with the period identified by the
     * given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param userId User identifier to look for in comments table.
     * @return The list of found comments.
     */
    @Override
    public List<CommentVo> getByUserId(DataAccessAdapter<EntityManager> em, long userId) throws DataAccessException{
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCommentsByUserId", getEntityClass()).setParameter("userId", userId).getResultList());
    }

    /**
     * Returns a list of Comments associated with the course identified by the
     * given id
     *
     * @param em DataAccessAdapter with which the entities will be searched
     * @param courseId Course identifier to look for in comment table.
     * @return The list of found comments.
     */
    @Override
    public List<CommentVo> getByCourseId(DataAccessAdapter<EntityManager> em, long courseId) throws DataAccessException{
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCommentsByCourseId" , getEntityClass()).setParameter("courseId", courseId).getResultList());
    }

    /**
     * Returns the number of comments submitted by a user on the current date
     *
     * @param em DataAccessAdapter
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    @Override
    public int getUserCommentsCounter(DataAccessAdapter<EntityManager> em, long userId) throws DataAccessException{
        checkDataAccessAdapter(em);
        return (em.getDataAccess().createNamedQuery("getUserCommentsCounter", Long.class).setParameter("userId", userId).getSingleResult()).intValue();
    }

    @Override
    protected CommentEntity voToEntity(DataAccessAdapter<EntityManager> em, CommentVo vo) {
        CommentEntity entity = new CommentEntity();
        entity.setAnonymous(vo.isAnonymous());
        entity.setContent(vo.getContent());
        entity.setDate(vo.getDate());
        entity.setId(vo.getId());
        entity.setRating(vo.getRating());
        entity.setCourse(em.getDataAccess().getReference(CourseEntity.class, vo.getCourseId()));
        entity.setUser(em.getDataAccess().getReference(UserEntity.class, vo.getUserId()));

        return entity;
    }

    @Override
    protected Class<CommentEntity> getEntityClass() {
        return CommentEntity.class;
    }
}