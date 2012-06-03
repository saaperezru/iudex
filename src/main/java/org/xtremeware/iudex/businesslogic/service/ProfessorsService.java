package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ProfessorVo;

public class ProfessorsService extends CrudService<ProfessorVo, ProfessorEntity> {

    private final int MAX_PROFESSOR_NAME_LENGTH;
    private final int MAX_PROFESSOR_DECRIPTION_LENGTH;

    public ProfessorsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {
        super(daoFactory, create, read, update, delete);

        MAX_PROFESSOR_NAME_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_PROFESSOR_NAME_LENGTH));
        MAX_PROFESSOR_DECRIPTION_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_PROFESSOR_DECRIPTION_LENGTH));
    }

    public List<ProfessorVo> getByNameLike(EntityManager entityManager, String professorName)
            throws DataBaseException {
        professorName = SecurityHelper.sanitizeHTML(professorName);
        ArrayList<ProfessorVo> list = new ArrayList<ProfessorVo>();
        if (!professorName.isEmpty()) {
            for (ProfessorEntity professor : getDaoFactory().getProfessorDao().
                    getByNameLike(entityManager, professorName.toUpperCase())) {
                list.add(professor.toVo());
            }
        }
        return list;
    }

    public List<ProfessorVo> getBySubjectId(EntityManager entityManager, long subjectId)
            throws DataBaseException {

        ArrayList<ProfessorVo> list = new ArrayList<ProfessorVo>();
        List<ProfessorEntity> professorEntitys = getDaoFactory().getProfessorDao().
                getBySubjectId(entityManager, subjectId);
        for (ProfessorEntity professor : professorEntitys) {
            list.add(professor.toVo());
        }
        return list;
    }

    @Override
    public void validateVoForCreation(EntityManager em, ProfessorVo vo)
            throws MultipleMessagesException {
        
        String preFixHttp = "http://";
        String preFixHttps = "https://";
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
            if (!vo.getImageUrl().substring(preFixHttp.length()).equalsIgnoreCase(preFixHttp)
                    && !vo.getImageUrl().substring(preFixHttps.length()).equalsIgnoreCase(preFixHttps)) {
                imageUrl = preFixHttp + imageUrl;
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
            if (!vo.getWebsite().substring(preFixHttp.length()).equalsIgnoreCase(preFixHttp)
                    && !vo.getWebsite().substring(preFixHttps.length()).equalsIgnoreCase(preFixHttps)) {
                website = preFixHttp + website;
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
    public void validateVoForUpdate(EntityManager entityManager, ProfessorVo professorVo)
            throws MultipleMessagesException, DataBaseException {

        validateVoForCreation(entityManager, professorVo);

        MultipleMessagesException multipleMessageException =
                new MultipleMessagesException();
        if (professorVo.getId() == null) {
            multipleMessageException.addMessage("professor.id.null");
            throw multipleMessageException;
        }
    }

    @Override
    public ProfessorEntity voToEntity(EntityManager entityManager, ProfessorVo professorVo)
            throws MultipleMessagesException {


        ProfessorEntity entity = new ProfessorEntity();
        entity.setId(professorVo.getId());
        entity.setEmail(professorVo.getEmail());

        entity.setDescription(professorVo.getDescription());
        entity.setFirstName(professorVo.getFirstName());
        entity.setLastName(professorVo.getLastName());

        entity.setImageUrl(professorVo.getImageUrl());
        entity.setWebsite(professorVo.getWebsite());
        return entity;
    }
}
