package org.xtremeware.iudex.dao.sql.removeimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.Remove;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class PeriodRemoveBehavior implements Remove {

    private AbstractDaoBuilder daoBuilder;
    private SimpleRemoveBehavior<PeriodEntity> simpleRemove;

    public PeriodRemoveBehavior(AbstractDaoBuilder daoBuilder,
            SimpleRemoveBehavior simpleRemove) {
        this.daoBuilder = daoBuilder;
        this.simpleRemove = simpleRemove;
    }

    @Override
    public void remove(EntityManager entityManager, Entity entity)
            throws DataBaseException {
        removeCourses(entityManager, entity.getId());
        getSimpleRemove().remove(entityManager, entity);
    }

    private SimpleRemoveBehavior<PeriodEntity> getSimpleRemove() {
        return simpleRemove;
    }

    private void removeCourses(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<CourseEntity> courseEntitys = getDaoBuilder().
                getCourseDao().getByPeriodId(entityManager, entityId);
        for (CourseEntity courseEntity : courseEntitys) {
            getDaoBuilder().
                    getCourseDao().remove(entityManager, courseEntity.getId());
        }
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
}
