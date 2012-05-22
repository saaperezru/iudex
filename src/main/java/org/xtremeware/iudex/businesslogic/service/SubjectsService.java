package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SubjectsRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public class SubjectsService extends CrudService<SubjectVo, SubjectEntity> {

    public final int MAX_SUBJECT_NAME_LENGTH;
    public final int MAX_SUBJECT_DESCRIPTION_LENGTH;

    /**
     * SubjectsService constructor
     *
     * @param daoFactory
     */
    public SubjectsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<SubjectEntity>(daoFactory.getSubjectDao()),
                new SimpleRead<SubjectEntity>(daoFactory.getSubjectDao()),
                new SimpleUpdate<SubjectEntity>(daoFactory.getSubjectDao()),
                new SubjectsRemove(daoFactory));
        MAX_SUBJECT_NAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH));
        MAX_SUBJECT_DESCRIPTION_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH));
    }

    /**
     * Validate the provided SubjectVo, if the SubjectVo is not correct the
     * method throws an exception
     *
     * @param em EntityManager
     * @param vo SubjectVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVoForCreation(EntityManager em, SubjectVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage("subject.null");
            throw multipleMessageException;
        }
        if (vo.getId() == null) {
            multipleMessageException.addMessage("subject.id.null");
        } else {
            vo.setId(Math.abs(vo.getId()));
        }

        if (vo.getDescription() == null) {
            vo.setDescription("");
        }

        vo.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
        if (vo.getDescription().length() > MAX_SUBJECT_DESCRIPTION_LENGTH) {
            multipleMessageException.addMessage("subject.description.tooLong");
        }

        if (vo.getName() == null || vo.getName().equals("")) {
            multipleMessageException.addMessage("subject.name.null");
        } else {
            vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
            if (vo.getName().length() > MAX_SUBJECT_NAME_LENGTH) {
                multipleMessageException.addMessage("subject.name.tooLong");
            }
        }

        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }
    
    @Override
    public void validateVoForUpdate(EntityManager entityManager, SubjectVo valueObject) throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage("subject.id.null");
            throw multipleMessageException;
        }
    }

    /**
     * Returns a SubjectEntity using the information in the provided SubjectVo.
     *
     * @param em EntityManager
     * @param vo SubjectVo
     * @return SubjectEntity
     * @throws InvalidVoException
     */
    @Override
    public SubjectEntity voToEntity(EntityManager em, SubjectVo valueObject)
            throws ExternalServiceConnectionException, MultipleMessagesException {

        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(valueObject.getId());
        subjectEntity.setName(valueObject.getName());
        subjectEntity.setDescription(valueObject.getDescription());
        subjectEntity.setCode(valueObject.getCode());
        
        return subjectEntity;
    }

    /**
     * Returns a list of SubjectVo according with the search query
     *
     * @param em EntityManager
     * @param query String with the search parameter
     * @return A list of SubjectVo
     */
    public List<SubjectVo> search(EntityManager em, String query)
            throws ExternalServiceConnectionException, DataBaseException {
        query = SecurityHelper.sanitizeHTML(query);
        List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().
                getByName(em, query.toUpperCase());
        if (subjectEntitys.isEmpty()) {
            return null;
        }
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }

    /**
     * Returns a list of SubjectVo according with the search name
     *
     * @param em EntityManager
     * @param name String with the name of the SubjectVo
     * @return A list if SubjectVo
     */
    public List<SubjectVo> getByNameLike(EntityManager em, String name)
            throws ExternalServiceConnectionException, DataBaseException {
        name = SecurityHelper.sanitizeHTML(name);
        List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().
                getByName(em, name.toUpperCase());
        if (subjectEntitys.isEmpty()) {
            return new ArrayList<SubjectVo>();
        }
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }

    /**
     * Returns a list of SubjectVos that had been taught by the specified
     * professor.
     *
     * @param em EntityManager
     * @param professorId Professor's id to look for subjects.
     * @return A list of SubjectVo
     */
    public List<SubjectVo> getByProfessorId(EntityManager em, long professorId)
            throws DataBaseException {
        List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().
                getByProfessorId(em, professorId);
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }
}
