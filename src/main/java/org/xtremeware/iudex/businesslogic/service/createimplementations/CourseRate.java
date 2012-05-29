package org.xtremeware.iudex.businesslogic.service.createimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CourseRate implements Create<CourseRatingEntity> {

    private AbstractDaoBuilder daoFactory;

    public CourseRate(AbstractDaoBuilder daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public CourseRatingEntity create(EntityManager entityManager, CourseRatingEntity entity) 
            throws DuplicityException, DataBaseException {
        try {
            CourseRatingEntity courseRatingEntity = getDaoFactory().getCourseRatingDao().
                    getByCourseIdAndUserId(entityManager, entity.getCourse().getId(), entity.getUser().getId());
            if (courseRatingEntity == null) {
                return getDaoFactory().getCourseRatingDao().persist(entityManager, entity);
            } else {
                courseRatingEntity.setValue(entity.getValue());
                return courseRatingEntity;
            }
        } catch (DataBaseException ex) {
            if (ex.getMessage().equals("entity.exists")) {
                throw new DuplicityException("entity.exists", ex.getCause());
            } else {
                throw ex;
            }
        }
    }

    private AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }
}
