package org.xtremeware.iudex.businesslogic.service.createimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.businesslogic.service.lucene.LuceneProfessorHelper;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class ProfessorCreation implements Create<ProfessorEntity>{
    
    private CrudDao<ProfessorEntity> crudDao;

    public ProfessorCreation(CrudDao<ProfessorEntity> dao) {
        this.crudDao = dao;
    }

    @Override
    public ProfessorEntity create(EntityManager entityManager, ProfessorEntity entity)
            throws DataBaseException, DuplicityException {
        getCrudDao().create(entityManager, entity);    
        LuceneProfessorHelper.getInstance().addElementToAnIndex(entity.toVo());
        
        return entity;
    }

    private CrudDao<ProfessorEntity> getCrudDao(){
        return crudDao;
    }    
}
