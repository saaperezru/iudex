package org.xtremeware.iudex.dao.sql.deleteimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.Delete;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class PeriodDeleteBehavior implements Delete {

    private AbstractDaoBuilder daoBuilder;
    private SimpleDeleteBehavior<PeriodEntity> simpleRemove;

    public PeriodDeleteBehavior(AbstractDaoBuilder daoBuilder,
            SimpleDeleteBehavior simpleRemove) {
        this.daoBuilder = daoBuilder;
        this.simpleRemove = simpleRemove;
    }

    @Override
    public void delete(EntityManager entityManager, Entity entity)
            throws DataBaseException {
        removeCourses(entityManager, entity.getId());
        getSimpleRemove().delete(entityManager, entity);
    }

    private SimpleDeleteBehavior<PeriodEntity> getSimpleRemove() {
        return simpleRemove;
    }

    private void removeCourses(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        List<CourseEntity> courseEntitys = getDaoBuilder().
                getCourseDao().getByPeriodId(entityManager, entityId);
        for (CourseEntity courseEntity : courseEntitys) {
            getDaoBuilder().
                    getCourseDao().delete(entityManager, courseEntity.getId());
        }
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
}
