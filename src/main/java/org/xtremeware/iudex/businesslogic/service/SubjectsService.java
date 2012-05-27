package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
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
    public SubjectsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Remove remove) {
        super(daoFactory, create, read, update, remove);
        MAX_SUBJECT_NAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH));
        MAX_SUBJECT_DESCRIPTION_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH));
    }

    /**
     * Validate the provided SubjectVo, if the SubjectVo is not correct the
     * method throws an exception
     *
     * @param entityManager EntityManager
     * @param subjectVo SubjectVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVoForCreation(EntityManager entityManager, SubjectVo subjectVo)
            throws ExternalServiceConnectionException, MultipleMessagesException {
        MultipleMessagesException multipleMessageException = new MultipleMessagesException();
        if (subjectVo == null) {
            multipleMessageException.addMessage("subject.null");
            throw multipleMessageException;
        }

        if (subjectVo.getDescription() == null) {
            subjectVo.setDescription("");
        }

        subjectVo.setDescription(SecurityHelper.sanitizeHTML(subjectVo.getDescription()));
        if (subjectVo.getDescription().length() > MAX_SUBJECT_DESCRIPTION_LENGTH) {
            multipleMessageException.addMessage("subject.description.tooLong");
        }

        if (subjectVo.getName() == null || subjectVo.getName().equals("")) {
            multipleMessageException.addMessage("subject.name.null");
        } else {
            subjectVo.setName(SecurityHelper.sanitizeHTML(subjectVo.getName()));
            if (subjectVo.getName().length() > MAX_SUBJECT_NAME_LENGTH) {
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
     * @param entityManager EntityManager
     * @param vo SubjectVo
     * @return SubjectEntity
     * @throws InvalidVoException
     */
    @Override
    public SubjectEntity voToEntity(EntityManager entityManager, SubjectVo valueObject)
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
     * @param entityManager EntityManager
     * @param query String with the search parameter
     * @return A list of SubjectVo
     */
    public List<SubjectVo> search(EntityManager entityManager, String query)
            throws ExternalServiceConnectionException, DataBaseException {

        query = SecurityHelper.sanitizeHTML(query);

        List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().
                getByName(entityManager, query.toUpperCase());

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

        ArrayList<SubjectVo> subjectVos = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            subjectVos.add(subjectEntity.toVo());
        }
        return subjectVos;
    }

    /**
     * Returns a list of SubjectVos that had been taught by the specified
     * professor.
     *
     * @param entityManager EntityManager
     * @param professorId Professor's id to look for subjects.
     * @return A list of SubjectVo
     */
    public List<SubjectVo> getByProfessorId(EntityManager entityManager, long professorId)
            throws DataBaseException {
        List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().
                getByProfessorId(entityManager, professorId);

        ArrayList<SubjectVo> subjectVos = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            subjectVos.add(subjectEntity.toVo());
        }
        return subjectVos;
    }
}
