package org.xtremeware.iudex.dao.sql.removeimplementations;


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
public class ProfessorsRemoveBehavior implements Remove {

    private AbstractDaoBuilder daoBuilder;
    private SimpleRemoveBehavior<ProfessorEntity> simpleRemove;

    public ProfessorsRemoveBehavior(AbstractDaoBuilder daoBuilder,
            SimpleRemoveBehavior simpleRemove) {
        this.daoBuilder = daoBuilder;
        this.simpleRemove = simpleRemove;
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
    
    @Override
    public void remove(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        removeProfessorRatings(entityManager, entity.getId());
        removeCourses(entityManager, entity.getId());
        getSimpleRemove().remove(entityManager, entity);
    }

    private SimpleRemoveBehavior<ProfessorEntity> getSimpleRemove() {
        return simpleRemove;
    }
    
    private void removeProfessorRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<ProfessorRatingEntity> professorRatings = getDaoBuilder().
                getProfessorRatingDao().getByEvaluatedObjectId(entityManager, entityId);
        for (ProfessorRatingEntity rating : professorRatings) {
            getDaoBuilder().getProfessorRatingDao().remove(entityManager, rating.getId());
        }
    }
    
    private void removeCourses(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<CourseEntity> courseEntitys = getDaoBuilder().
                getCourseDao().getByProfessorId(entityManager, entityId);
        for (CourseEntity courseEntity : courseEntitys) {
            getDaoBuilder().
                getCourseDao().remove(entityManager, courseEntity.getId());
        }
    }
}
