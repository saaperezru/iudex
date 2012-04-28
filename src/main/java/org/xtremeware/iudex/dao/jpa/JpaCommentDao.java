package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
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
public class JpaCommentDao extends JpaCrudDao<CommentVo,CommentEntity> implements CommentDao<EntityManager> {

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
    public List<CommentVo> getByProfessorId(DataAccessAdapter<EntityManager> em, long professorId) {
        checkDataAccessAdapter(em);
        List<CommentEntity> list = em.getDataAccess().createNamedQuery("getCommentsByProfessorId").setParameter("professorId", professorId).getResultList();
        return entitiesToVos(list);
        
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
    public List<CommentVo> getBySubjectId(DataAccessAdapter<EntityManager> em, long subjectId) {
        checkDataAccessAdapter(em);
        List<CommentEntity> list = em.getDataAccess().createNamedQuery("getCommentsBySubjectId").setParameter("subjectId", subjectId).getResultList();
        return entitiesToVos(list);
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
    public List<CommentVo> getByUserId(DataAccessAdapter<EntityManager> em, long userId) {
        checkDataAccessAdapter(em);
        List<CommentEntity> list = em.getDataAccess().createNamedQuery("getCommentsByUserId").setParameter("userId", userId).getResultList();
        return entitiesToVos(list);
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
    public List<CommentVo> getByCourseId(DataAccessAdapter<EntityManager> em, long courseId) {
        checkDataAccessAdapter(em);
        List<CommentEntity> list = em.getDataAccess().createNamedQuery("getCommentsByCourseId").setParameter("courseId", courseId).getResultList();
        return entitiesToVos(list);
    }
    
    /**
     * Returns the number of comments submitted by a user on the current date
     * 
     * @param em DataAccessAdapter
     * @param userId id of the user
     * @return number of comments submitted on the current day
     */
    @Override
    public int getUserCommentsCounter(DataAccessAdapter<EntityManager> em, long userId) {
        checkDataAccessAdapter(em);
        return (em.getDataAccess().createNamedQuery("getUserCommentsCounter",Long.class).setParameter("userId", userId).getSingleResult()).intValue();
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
    protected Class getEntityClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private List<CommentVo> entitiesToVos(List<CommentEntity> list) {
        ArrayList<CommentVo> arrayList = new ArrayList<CommentVo>();
        for (CommentEntity entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }
}