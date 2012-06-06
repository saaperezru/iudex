package org.xtremeware.iudex.businesslogic.service.createimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneSubjectHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SubjectCreation implements Create<SubjectEntity> {

    private CrudDao<SubjectEntity> crudDao;

    public SubjectCreation(CrudDao<SubjectEntity> dao) {
        this.crudDao = dao;
    }

    @Override
    public SubjectEntity create(EntityManager entityManager, SubjectEntity entity)
            throws DataBaseException, DuplicityException {
        getCrudDao().create(entityManager, entity);
        LuceneSubjectHelper.getInstance().addElementToAnIndex(entity.toVo());

        return entity;
    }

    private CrudDao<SubjectEntity> getCrudDao() {
        return crudDao;
    }
}
