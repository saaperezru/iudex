package org.xtremeware.iudex.businesslogic.service.deleteimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Delete;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneCourseHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CourseDelete implements Delete {

    private CrudDao<CourseEntity> dao;

    public CourseDelete(CrudDao<CourseEntity> dao) {
        this.dao = dao;
    }

    @Override
    public void delete(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        LuceneCourseHelper.getInstance().deleteElementoInTheIndex(entityId);
        getDao().delete(entityManager, entityId);
    }

    public CrudDao<CourseEntity> getDao() {
        return dao;
    }
}
