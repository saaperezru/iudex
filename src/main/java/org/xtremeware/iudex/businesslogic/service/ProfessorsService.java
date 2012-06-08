package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.businesslogic.service.search.Search;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ProfessorVo;

public class ProfessorsService extends CrudService<ProfessorVo, ProfessorEntity> {

    private final int MAX_PROFESSOR_NAME_LENGTH;
    private final int MAX_PROFESSOR_DECRIPTION_LENGTH;
    private Search search;

    public ProfessorsService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete,
            Search search) {
        super(daoFactory, create, read, update, delete);
        this.search = search;
        MAX_PROFESSOR_NAME_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_PROFESSOR_NAME_LENGTH));
        MAX_PROFESSOR_DECRIPTION_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_PROFESSOR_DECRIPTION_LENGTH));
    }

    public List<Long> getByNameLike(EntityManager entityManager, String professorName)
            throws DataBaseException {
        professorName = SecurityHelper.sanitizeHTML(professorName);
        return getDaoFactory().getProfessorDao().getByNameLike(entityManager, professorName.toUpperCase());
    }

    public List<Long> search(String query) {
        return search.search(SecurityHelper.sanitizeHTML(query));
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
    public void validateVoForCreation(EntityManager em, ProfessorVo professorVo)
            throws MultipleMessagesException {


        MultipleMessagesException multipleMessagesException =
                new MultipleMessagesException();
        if (professorVo == null) {
            multipleMessagesException.addMessage("professor.null");
            throw multipleMessagesException;
        }
        checkFirstName(multipleMessagesException, professorVo.getFirstName());
        checkLastName(multipleMessagesException, professorVo.getLastName());
        checkDescription(multipleMessagesException, professorVo.getDescription());
        checkEmail(multipleMessagesException, professorVo.getEmail());
        checkImageUrl(multipleMessagesException, professorVo.getImageUrl());
        checkWebSide(multipleMessagesException, null);

        if (!multipleMessagesException.getMessages().isEmpty()) {
            throw multipleMessagesException;
        }
        professorVo.setFirstName(SecurityHelper.sanitizeHTML(professorVo.getFirstName()));
        professorVo.setLastName(SecurityHelper.sanitizeHTML(professorVo.getLastName()));
        professorVo.setDescription(SecurityHelper.sanitizeHTML(professorVo.getDescription()));
    }

    private void checkFirstName(MultipleMessagesException multipleMessagesException,
            String firstName) {
        if (firstName == null) {
            multipleMessagesException.addMessage(
                    "professor.firstName.null");
        } else {
            if (firstName.length() > MAX_PROFESSOR_NAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "professor.firstName.tooLong");
            }
            if (firstName.isEmpty()) {
                multipleMessagesException.addMessage(
                        "professor.firstName.empty");
            }
        }
    }

    private void checkLastName(MultipleMessagesException multipleMessagesException,
            String lastName) {
        if (lastName == null) {
            multipleMessagesException.addMessage(
                    "professor.lastName.null");
        } else {
            if (lastName.length() > MAX_PROFESSOR_NAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "professor.lastName.tooLong");
            }
            if (lastName.isEmpty()) {
                multipleMessagesException.addMessage(
                        "professor.lastName.empty");
            }
        }
    }

    private void checkDescription(MultipleMessagesException multipleMessagesException,
            String description) {
        if (description == null) {
            multipleMessagesException.addMessage(
                    "professor.description.null");
        } else {
            if (description.length() > MAX_PROFESSOR_DECRIPTION_LENGTH) {
                multipleMessagesException.addMessage(
                        "professor.description.tooLong");
            }
            if (description.isEmpty()) {
                multipleMessagesException.addMessage(
                        "professor.description.empty");
            }
        }
    }

    private void checkEmail(MultipleMessagesException multipleMessagesException,
            String email) {
        if (email == null) {
            multipleMessagesException.addMessage(
                    "professor.email.null");
        } else if (email.isEmpty()) {
            multipleMessagesException.addMessage(
                    "professor.email.empty");
        } else if (!ValidityHelper.isValidEmail(email)) {
            multipleMessagesException.addMessage(
                    "professor.email.invalidEmail");
        }
    }

    private void checkImageUrl(MultipleMessagesException multipleMessagesException,
            String imageUrl) {
        String preFixHttp = "http://";
        String preFixHttps = "https://";
        if (imageUrl == null) {
            multipleMessagesException.addMessage(
                    "professor.imageUrl.null");
        } else if (!imageUrl.isEmpty()) {

            if (!imageUrl.substring(preFixHttp.length()).equalsIgnoreCase(preFixHttp)
                    && !imageUrl.substring(preFixHttps.length()).equalsIgnoreCase(preFixHttps)) {
                imageUrl = preFixHttp + imageUrl;
            }
            if (!ValidityHelper.isValidUrl(imageUrl)) {
                multipleMessagesException.addMessage(
                        "professor.imageUrl.invalidImage");
            }
        }
    }

    private void checkWebSide(MultipleMessagesException multipleMessagesException,
            String webSide) {
        String preFixHttp = "http://";
        String preFixHttps = "https://";
        if (webSide == null) {
            multipleMessagesException.addMessage(
                    "professor.website.null");
        } else if (webSide.isEmpty()) {
            multipleMessagesException.addMessage(
                    "professor.website.empty");
        } else {
            if (!webSide.substring(preFixHttp.length()).equalsIgnoreCase(preFixHttp)
                    && !webSide.substring(preFixHttps.length()).equalsIgnoreCase(preFixHttps)) {
                webSide = preFixHttp + webSide;
            }
            if (!ValidityHelper.isValidUrl(webSide)) {
                multipleMessagesException.addMessage(
                        "professor.website.invalidWebsite");
            }
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
