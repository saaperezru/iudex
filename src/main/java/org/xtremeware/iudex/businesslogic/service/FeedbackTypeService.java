/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.FeedbackTypeDao;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.vo.FeedbackTypeVo;

/**
 *
 * @author josebermeo
 */
public class FeedbackTypeService extends SimpleCrudService<FeedbackTypeVo, FeedbackTypeEntity> {

    /**
     * FeedbackTypeService constructor
     *
     * @param daoFactory
     */
    public FeedbackTypeService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the FeedbackTypeDao to be used.
     *
     * @return
     */
    @Override
    protected Dao<FeedbackTypeEntity> getDao() {
        return this.getDaoFactory().getFeedbackTypeDao();
    }

    /**
     * Validate the provided FeedbackTypeVo, if the FeedbackTypeVo is not
     * correct the methods throws an exception
     *
     * @param em EntityManager
     * @param vo FeedbackTypeVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVo(EntityManager em, FeedbackTypeVo vo) throws InvalidVoException {
        if (vo == null) {
            throw new InvalidVoException("Null FeedbackTypeVo");
        }
        if (vo.getName() == null) {
            throw new InvalidVoException("Null name in the provided FeedbackTypeVo");
        }
    }

    /**
     * Returns a FeedbackTypeEntity using the information in the provided
     * FeedbackTypeVo.
     *
     * @param em EntityManager
     * @param vo FeedbackTypeVo
     * @return FeedbackTypeEntity
     * @throws InvalidVoException
     */
    @Override
    public FeedbackTypeEntity voToEntity(EntityManager em, FeedbackTypeVo vo) throws InvalidVoException {

        validateVo(em, vo);
        FeedbackTypeEntity feedbackTypeEntity = new FeedbackTypeEntity();
        feedbackTypeEntity.setId(vo.getId());
        feedbackTypeEntity.setName(vo.getName());

        return feedbackTypeEntity;
    }

    /**
     * return a list of all different FeedbackTypeVo
     *
     * @param em EntityManager
     * @return A list of all different FeedbackTypeVo
     */
    public List<FeedbackTypeVo> list(EntityManager em) {
        List<FeedbackTypeEntity> feedbackTypeEntitys = ((FeedbackTypeDao)this.getDao()).getAll(em);
        if(feedbackTypeEntitys.isEmpty())
            return null;
        ArrayList<FeedbackTypeVo> arrayList = new ArrayList<FeedbackTypeVo>();
        for (FeedbackTypeEntity feedbackTypeEntity : feedbackTypeEntitys) {
            arrayList.add(feedbackTypeEntity.toVo());
        }
        return arrayList;
    }
}
