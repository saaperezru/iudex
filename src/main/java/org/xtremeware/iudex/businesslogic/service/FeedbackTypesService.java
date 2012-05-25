package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.FeedbackTypesRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.helper.SecurityHelper;
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
    public FeedbackTypesService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<FeedbackTypeEntity>(daoFactory.
                getFeedbackTypeDao()),
                new SimpleRead<FeedbackTypeEntity>(
                daoFactory.getFeedbackTypeDao()),
                new SimpleUpdate<FeedbackTypeEntity>(daoFactory.
                getFeedbackTypeDao()),
                new FeedbackTypesRemove(daoFactory));
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
    public void validateVo(EntityManager em, FeedbackTypeVo vo)
            throws ExternalServiceConnectionException,
            MultipleMessagesException {
        
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage(
                    "feedbackType.null");
            throw multipleMessageException;
        }
        if (vo.getName() == null) {
            multipleMessageException.addMessage(
                    "feedbackType.name.null");
            throw multipleMessageException;
        }
        vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
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
    public List<FeedbackTypeVo> list(EntityManager em)
            throws DataBaseException {
        List<FeedbackTypeEntity> feedbackTypeEntitys = getDaoFactory().
                getFeedbackTypeDao().getAll(em);
        if (feedbackTypeEntitys.isEmpty()) {
            return null;
        }
        ArrayList<FeedbackTypeVo> arrayList = new ArrayList<FeedbackTypeVo>();
        for (FeedbackTypeEntity feedbackTypeEntity : feedbackTypeEntitys) {
            arrayList.add(feedbackTypeEntity.toVo());
        }
        return arrayList;
    }
}
