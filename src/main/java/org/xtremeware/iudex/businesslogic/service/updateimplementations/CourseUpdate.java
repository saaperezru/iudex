package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneCourseHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CourseUpdate implements Update<CourseEntity> {

    private CrudDao<CourseEntity> dao;

    public CourseUpdate(CrudDao<CourseEntity> dao) {
        this.dao = dao;
    }

    @Override
    public CourseEntity update(EntityManager entityManager, CourseEntity entity) 
            throws DataBaseException {
        CourseEntity courseEntity = getDao().read(entityManager, entity.getId());
        if (courseEntity != null) {
            courseEntity =  getDao().update(entityManager, entity);
            LuceneCourseHelper.getInstance().updateElementoInAnIndex(entity.toVo(), entityManager);
            return courseEntity;
        } else {
            return null;
        }
    }

    public CrudDao<CourseEntity> getDao() {
        return dao;
    }
}
