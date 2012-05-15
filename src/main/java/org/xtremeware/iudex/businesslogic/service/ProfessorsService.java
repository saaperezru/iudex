package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.ProfessorsRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ProfessorVo;

public class ProfessorsService extends CrudService<ProfessorVo, ProfessorEntity> {

    private final int MAX_PROFESSOR_NAME_LENGTH;

    public ProfessorsService(AbstractDaoFactory daoFactory) throws
            ExternalServiceConnectionException {
        super(daoFactory, new SimpleCreate<ProfessorEntity>(daoFactory.
                getProfessorDao()),
                new SimpleRead<ProfessorEntity>(daoFactory.getProfessorDao()),
                new SimpleUpdate<ProfessorEntity>(daoFactory.getProfessorDao()),
                new ProfessorsRemove(daoFactory));
        MAX_PROFESSOR_NAME_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_PROFESSOR_NAME_LENGTH));
    }

    public List<ProfessorVo> getByNameLike(EntityManager em, String name)
            throws ExternalServiceConnectionException, DataBaseException {
        name = SecurityHelper.sanitizeHTML(name);
        ArrayList<ProfessorVo> list = new ArrayList<ProfessorVo>();
        for (ProfessorEntity professor : getDaoFactory().getProfessorDao().
                getByNameLike(em, name.toUpperCase())) {
            list.add(professor.toVo());
        }
        return list;
    }

    public List<ProfessorVo> getBySubjectId(EntityManager em, long subjectId)
            throws DataBaseException {
        ArrayList<ProfessorVo> list = new ArrayList<ProfessorVo>();
        for (ProfessorEntity professor : getDaoFactory().getProfessorDao().
                getBySubjectId(em, subjectId)) {
            list.add(professor.toVo());
        }
        return list;
    }

    @Override
    public void validateVo(EntityManager em, ProfessorVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage("Null ProfessorVo");
            throw multipleMessageException;
        }
        if (vo.getFirstName() == null) {
            multipleMessageException.addMessage(
                    "String firstName in the provided ProgramVo cannot be null");
        } else {
            vo.setFirstName(SecurityHelper.sanitizeHTML(vo.getFirstName()));
            if (vo.getFirstName().length() > MAX_PROFESSOR_NAME_LENGTH) {
                multipleMessageException.addMessage(
                        "String firstName length must be less than " +
                         String.valueOf(MAX_PROFESSOR_NAME_LENGTH));
            }
        }
        if (vo.getLastName() == null) {
            multipleMessageException.addMessage(
                    "String lastName in the provided ProgramVo cannot be null");
        } else {
            vo.setLastName(SecurityHelper.sanitizeHTML(vo.getLastName()));
            if (vo.getLastName().length() > MAX_PROFESSOR_NAME_LENGTH) {
                multipleMessageException.addMessage(
                        "String lastName length must be less than " +
                         String.valueOf(MAX_PROFESSOR_NAME_LENGTH));
            }
        }
        if (vo.getDescription() == null) {
            multipleMessageException.addMessage(
                    "String description in the provided ProgramVo cannot be null");
        } else {
            vo.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
        }

        if (vo.getEmail() == null) {
            multipleMessageException.addMessage(
                    "String email in the provided ProgramVo cannot be null");
        } else if (vo.getEmail().length() > 0 &&
                !ValidityHelper.isValidEmail(vo.getEmail())) {
            multipleMessageException.addMessage(
                    "Strng email in the provided ProgramVo must be a valid email address");
        }
        if (vo.getImageUrl() == null) {
            multipleMessageException.addMessage(
                    "String imageUrl in the provided ProgramVo cannot be null");
        } else if (vo.getImageUrl().length() > 0 &&
                !ValidityHelper.isValidUrl(vo.getImageUrl())) {
            multipleMessageException.addMessage(
                    "String imageUrl in the provided ProgamVo must be a valid URL");
        }
        if (vo.getWebsite() == null) {
            multipleMessageException.addMessage(
                    "String website in the provided ProgramVo cannot be null");
        } else if (vo.getWebsite().length() > 0 &&
                !ValidityHelper.isValidUrl(vo.getWebsite())) {
            multipleMessageException.addMessage(
                    "String website in provided ProgramVo must be a valid URL");
        }
        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }

    @Override
    public ProfessorEntity voToEntity(EntityManager em, ProfessorVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {
        validateVo(em, vo);
        ProfessorEntity entity = new ProfessorEntity();
        entity.setId(vo.getId());
        entity.setEmail(vo.getEmail());

        entity.setDescription(vo.getDescription());
        entity.setFirstName(vo.getFirstName());
        entity.setLastName(vo.getLastName());

        entity.setImageUrl(vo.getImageUrl());
        entity.setWebsite(vo.getWebsite());
        return entity;
    }
}
