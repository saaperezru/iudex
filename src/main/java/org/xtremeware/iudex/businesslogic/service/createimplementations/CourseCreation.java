package org.xtremeware.iudex.businesslogic.service.createimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneCourseHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CourseCreation implements Create<CourseEntity> {

    private CrudDao<CourseEntity> crudDao;

    public CourseCreation(CrudDao<CourseEntity> dao) {
        this.crudDao = dao;
    }

    @Override
    public CourseEntity create(EntityManager entityManager, CourseEntity entity)
            throws DataBaseException {
        getCrudDao().create(entityManager, entity);
        LuceneCourseHelper.getInstance().addElementToAnIndex(entity.toVo(), entityManager);

        return entity;
    }

    private CrudDao<CourseEntity> getCrudDao() {
        return crudDao;
    }
    
}
