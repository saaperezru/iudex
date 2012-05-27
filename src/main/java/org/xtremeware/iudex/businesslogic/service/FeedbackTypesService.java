package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.FeedbackTypeVo;

/**
 *
 * @author josebermeo
 */
public class FeedbackTypesService extends CrudService<FeedbackTypeVo, FeedbackTypeEntity> {

    /**
     * FeedbackTypesService constructor
     *
     * @param daoFactory
     */
    public FeedbackTypesService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Remove remove) {

        super(daoFactory, create, read, update, remove);
        
    }

    /**
     * Validate the provided FeedbackTypeVo, if the FeedbackTypeVo is not
     * correct the methods throws an exception
     *
     * @param entityManager EntityManager
     * @param feedbackTypeVo FeedbackTypeVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVoForCreation(EntityManager entityManager, FeedbackTypeVo feedbackTypeVo)
            throws ExternalServiceConnectionException,
            MultipleMessagesException {
        
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (feedbackTypeVo == null) {
            multipleMessageException.addMessage(
                    "feedbackType.null");
            throw multipleMessageException;
        }
        if (feedbackTypeVo.getName() == null) {
            multipleMessageException.addMessage(
                    "feedbackType.name.null");
            throw multipleMessageException;
        }
        feedbackTypeVo.setName(SecurityHelper.sanitizeHTML(feedbackTypeVo.getName()));
    }
    
    @Override
    public void validateVoForUpdate(EntityManager entityManager, FeedbackTypeVo feedbackTypeVo) 
            throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        
        validateVoForCreation(entityManager, feedbackTypeVo);
        
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (feedbackTypeVo.getId() == null) {
            multipleMessageException.addMessage(
                    "feedbackType.id.null");
            throw multipleMessageException;
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
    public FeedbackTypeEntity voToEntity(EntityManager em, FeedbackTypeVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {

        FeedbackTypeEntity feedbackTypeEntity = new FeedbackTypeEntity();
        feedbackTypeEntity.setId(vo.getId());
        feedbackTypeEntity.setName(vo.getName());

        return feedbackTypeEntity;
    }

    /**
     * return a list of all different FeedbackTypeVo
     *
     * @param entityManager EntityManager
     * @return A list of all different FeedbackTypeVo
     */
    public List<FeedbackTypeVo> list(EntityManager entityManager)
            throws DataBaseException {
        
        List<FeedbackTypeEntity> feedbackTypeEntitys = getDaoFactory().
                getFeedbackTypeDao().getAll(entityManager);
        
        ArrayList<FeedbackTypeVo> arrayList = new ArrayList<FeedbackTypeVo>();
        for (FeedbackTypeEntity feedbackTypeEntity : feedbackTypeEntitys) {
            arrayList.add(feedbackTypeEntity.toVo());
        }
        return arrayList;
    }
}
