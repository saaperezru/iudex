/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.FeedbackTypeDao;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.FeedbackTypeVo;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author josebermeo
 */
public class FeedbackTypesService extends SimpleCrudService<FeedbackTypeVo> {

    /**
     * FeedbackTypesService constructor
     *
     * @param daoFactory
     */
    public FeedbackTypesService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the JpaFeedbackTypeDao to be used.
     *
     * @return
     */
    @Override
    protected CrudDao<FeedbackTypeVo, FeedbackTypeEntity> getDao() {
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
    public void validateVo(DataAccessAdapter em, FeedbackTypeVo vo) throws InvalidVoException, DataAccessException, ExternalServiceConnectionException {
        if (vo == null) {
            throw new InvalidVoException("Null FeedbackTypeVo");
        }
        if (vo.getName() == null) {
            throw new InvalidVoException("Null name in the provided FeedbackTypeVo");
        }
        
        vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
        
        
    }

    /**
     * return a list of all different FeedbackTypeVo
     *
     * @param em EntityManager
     * @return A list of all different FeedbackTypeVo
     */
    public List<FeedbackTypeVo> list(DataAccessAdapter em) throws DataAccessException {
        return ((FeedbackTypeDao) this.getDao()).getAll(em);
    }

    /**
     * Remove the FeedBack Type and all the Feedback Comments associated to it.
     *
     * @param em entity manager
     * @param id id of the FeedBackType
     */
    @Override
    public void remove(DataAccessAdapter em, long id) throws DataAccessException {
        List<FeedbackVo> feedBacks = getDaoFactory().getFeedbackDao().getByTypeId(em, id);
        for (FeedbackVo feedBack : feedBacks) {
            getDaoFactory().getFeedbackDao().remove(em, feedBack.getId());
        }

        getDao().remove(em, id);
    }
}
