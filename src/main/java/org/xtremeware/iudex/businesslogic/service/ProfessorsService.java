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
    private final int MAX_PROFESSOR_DECRIPTION_LENGTH;

    public ProfessorsService(AbstractDaoFactory daoFactory) throws
            ExternalServiceConnectionException {
        super(daoFactory, new SimpleCreate<ProfessorEntity>(daoFactory.getProfessorDao()),
                new SimpleRead<ProfessorEntity>(daoFactory.getProfessorDao()),
                new SimpleUpdate<ProfessorEntity>(daoFactory.getProfessorDao()),
                new ProfessorsRemove(daoFactory));
        MAX_PROFESSOR_NAME_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_PROFESSOR_NAME_LENGTH));
        MAX_PROFESSOR_DECRIPTION_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_PROFESSOR_DECRIPTION_LENGTH));
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
    public void validateVoForCreation(EntityManager em, ProfessorVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (vo == null) {
            multipleMessageException.addMessage("professor.null");
            throw multipleMessageException;
        }
        if (vo.getFirstName() == null) {
            multipleMessageException.addMessage(
                    "professor.firstName.null");
        } else {
            vo.setFirstName(SecurityHelper.sanitizeHTML(vo.getFirstName()));
            if (vo.getFirstName().length() > MAX_PROFESSOR_NAME_LENGTH) {
                multipleMessageException.addMessage(
                        "professor.firstName.tooLong");
            }
            if (vo.getFirstName().isEmpty()) {
                multipleMessageException.addMessage(
                        "professor.firstName.empty");
            }
        }
        if (vo.getLastName() == null) {
            multipleMessageException.addMessage(
                    "professor.lastName.null");
        } else {
            vo.setLastName(SecurityHelper.sanitizeHTML(vo.getLastName()));
            if (vo.getLastName().length() > MAX_PROFESSOR_NAME_LENGTH) {
                multipleMessageException.addMessage(
                        "professor.lastName.tooLong");
            }
            if (vo.getLastName().isEmpty()) {
                multipleMessageException.addMessage(
                        "professor.lastName.empty");
            }
        }
        if (vo.getDescription() == null) {
            multipleMessageException.addMessage(
                    "professor.description.null");
        } else {
            vo.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
            if (vo.getDescription().length() > MAX_PROFESSOR_DECRIPTION_LENGTH) {
                multipleMessageException.addMessage(
                        "professor.description.tooLong");
            }
            if (vo.getDescription().isEmpty()) {
                multipleMessageException.addMessage(
                        "professor.description.empty");
            }
        }

        if (vo.getEmail() == null) {
            multipleMessageException.addMessage(
                    "professor.email.null");
        } else if (vo.getEmail().isEmpty()) {
            multipleMessageException.addMessage(
                    "professor.email.empty");
        } else if (!ValidityHelper.isValidEmail(vo.getEmail())) {
            multipleMessageException.addMessage(
                    "professor.email.invalidEmail");
            }
        if (vo.getImageUrl() == null) {
            multipleMessageException.addMessage(
                    "professor.imageUrl.null");
        } else if (!vo.getImageUrl().isEmpty()) {
            String imageUrl = vo.getImageUrl();
            if (!vo.getImageUrl().substring(0, 7).equalsIgnoreCase("http://")&& 
                    !vo.getImageUrl().substring(0, 8).equalsIgnoreCase("https://")){
                imageUrl = "http://"+imageUrl;
            }
            if (!ValidityHelper.isValidUrl(imageUrl)) {
                multipleMessageException.addMessage(
                        "professor.imageUrl.invalidImage");
            }
        }
        if (vo.getWebsite() == null) {
            multipleMessageException.addMessage(
                    "professor.website.null");
        } else if (vo.getWebsite().isEmpty()) {
            multipleMessageException.addMessage(
                    "professor.website.empty");
        } else {
            String website = vo.getWebsite();
            if (!vo.getWebsite().substring(0, 7).equalsIgnoreCase("http://")&& 
                    !vo.getWebsite().substring(0, 8).equalsIgnoreCase("https://")){
                website = "http://"+website;
            }
            if (!ValidityHelper.isValidUrl(website)) {
                multipleMessageException.addMessage(
                        "professor.website.invalidWebsite");
            }
        }

        if (!multipleMessageException.getMessages().isEmpty()) {
            throw multipleMessageException;
        }
    }
    
    @Override
    public void validateVoForUpdate(EntityManager entityManager, ProfessorVo valueObject) throws MultipleMessagesException, ExternalServiceConnectionException, DataBaseException {
        validateVoForCreation(entityManager, valueObject);
        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (valueObject.getId() == null) {
            multipleMessageException.addMessage("professor.id.null");
            throw multipleMessageException;
        }
    }

    @Override
    public ProfessorEntity voToEntity(EntityManager em, ProfessorVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException {
        
        
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
