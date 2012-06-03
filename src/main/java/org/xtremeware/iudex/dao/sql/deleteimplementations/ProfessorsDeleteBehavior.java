package org.xtremeware.iudex.dao.sql.deleteimplementations;


import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class ProfessorsDeleteBehavior implements Delete {

    private AbstractDaoBuilder daoBuilder;
    private SimpleDeleteBehavior<ProfessorEntity> simpleDelete;

    public ProfessorsDeleteBehavior(AbstractDaoBuilder daoBuilder,
            SimpleDeleteBehavior simpleDelete) {
        this.daoBuilder = daoBuilder;
        this.simpleDelete = simpleDelete;
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
    
    @Override
    public void delete(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        deleteProfessorRatings(entityManager, entity.getId());
        deleteCourses(entityManager, entity.getId());
        getSimpleDelete().delete(entityManager, entity);
    }

    private SimpleDeleteBehavior<ProfessorEntity> getSimpleDelete() {
        return simpleDelete;
    }
    
    private void deleteProfessorRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<ProfessorRatingEntity> professorRatings = getDaoBuilder().
                getProfessorRatingDao().getByEvaluatedObjectId(entityManager, entityId);
        for (ProfessorRatingEntity rating : professorRatings) {
            getDaoBuilder().getProfessorRatingDao().delete(entityManager, rating.getId());
        }
    }
    
    private void deleteCourses(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<CourseEntity> courseEntitys = getDaoBuilder().
                getCourseDao().getByProfessorId(entityManager, entityId);
        for (CourseEntity courseEntity : courseEntitys) {
            getDaoBuilder().
                getCourseDao().delete(entityManager, courseEntity.getId());
        }
    }
}
