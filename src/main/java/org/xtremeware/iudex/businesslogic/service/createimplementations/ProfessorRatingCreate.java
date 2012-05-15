package org.xtremeware.iudex.businesslogic.service.createimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.CreateInterface;
import org.xtremeware.iudex.dao.ProfessorRatingDaoInterface;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class ProfessorRatingCreate implements CreateInterface<ProfessorRatingEntity> {

    private ProfessorRatingDaoInterface dao;

    public ProfessorRatingCreate(ProfessorRatingDaoInterface dao) {
        this.dao = dao;
    }

    @Override
    public ProfessorRatingEntity create(EntityManager em, ProfessorRatingEntity entity)
            throws DataBaseException, DuplicityException {
        ProfessorRatingEntity pre = getDao().getByProfessorIdAndUserId(em, entity.getProfessor().getId(),
                entity.getUser().getId());
        if (pre == null) {
            getDao().persist(em, entity);
            return entity;
        } else {
            pre.setValue(entity.getValue());
            return pre;
        }
    }

    private ProfessorRatingDaoInterface getDao() {
        return this.dao;
    }
}
