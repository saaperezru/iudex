package org.xtremeware.iudex.businesslogic.service.deleteimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Delete;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneSubjectHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SubjectDelete implements Delete {

    private CrudDao<SubjectEntity> dao;

    public SubjectDelete(CrudDao<SubjectEntity> dao) {
        this.dao = dao;
    }

    @Override
    public void delete(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        LuceneSubjectHelper.getInstance().deleteElementoInTheIndex(entityId);
        getDao().delete(entityManager, entityId);
    }

    public CrudDao<SubjectEntity> getDao() {
        return dao;
    }
}

