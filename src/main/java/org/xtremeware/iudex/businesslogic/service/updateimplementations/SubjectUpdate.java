package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneSubjectHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SubjectUpdate implements Update<SubjectEntity> {

    private CrudDao<SubjectEntity> dao;

    public SubjectUpdate(CrudDao<SubjectEntity> dao) {
        this.dao = dao;
    }

    @Override
    public SubjectEntity update(EntityManager entityManager, SubjectEntity entity)
            throws DataBaseException {
        SubjectEntity subjectEntity = getDao().read(entityManager, entity.getId());
        if (subjectEntity != null) {
            subjectEntity = getDao().update(entityManager, entity);
            LuceneSubjectHelper.getInstance().updateElementoInAnIndex(subjectEntity.toVo());
            return subjectEntity;
        } else {
            return null;
        }
    }

    public CrudDao<SubjectEntity> getDao() {
        return dao;
    }
}
