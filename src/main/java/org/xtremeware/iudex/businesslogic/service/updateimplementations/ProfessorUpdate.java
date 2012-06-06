package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneProfessorHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class ProfessorUpdate implements Update<ProfessorEntity> {

    private CrudDao<ProfessorEntity> dao;

    public ProfessorUpdate(CrudDao<ProfessorEntity> dao) {
        this.dao = dao;
    }

    @Override
    public ProfessorEntity update(EntityManager entityManager, ProfessorEntity entity) 
            throws DataBaseException {
        ProfessorEntity professorEntity = getDao().read(entityManager, entity.getId());
        if (professorEntity != null) {
            professorEntity =  getDao().update(entityManager, entity);
            LuceneProfessorHelper.getInstance().updateElementoInAnIndex(professorEntity.toVo());
            return professorEntity;
        } else {
            return null;
        }
    }

    public CrudDao<ProfessorEntity> getDao() {
        return dao;
    }
}