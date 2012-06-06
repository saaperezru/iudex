package org.xtremeware.iudex.businesslogic.service.deleteimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Delete;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneProfessorHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class ProfessorDelete implements Delete {

    private CrudDao<ProfessorEntity> dao;

    public ProfessorDelete(CrudDao<ProfessorEntity> dao) {
        this.dao = dao;
    }

    @Override
    public void delete(EntityManager entityManager, Long entityId)
            throws DataBaseException {
        LuceneProfessorHelper.getInstance().deleteElementoInTheIndex(entityId);
        getDao().delete(entityManager, entityId);
    }

    public CrudDao<ProfessorEntity> getDao() {
        return dao;
    }
}

