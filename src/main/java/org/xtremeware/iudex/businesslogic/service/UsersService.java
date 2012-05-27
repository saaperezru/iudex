package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.*;

/**
 *
 * @author josebermeo
 */
public class UsersService extends CrudService<UserVo, UserEntity> {

    private final int MIN_USERNAME_LENGTH;
    private final int MAX_USERNAME_LENGTH;
    private final int MAX_USER_PASSWORD_LENGTH;
    private final int MIN_USER_PASSWORD_LENGTH;

    public UsersService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Remove remove) {

        super(daoFactory, create, read, update, remove);

        MIN_USERNAME_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MIN_USERNAME_LENGTH));
        MAX_USERNAME_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_USERNAME_LENGTH));
        MAX_USER_PASSWORD_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_USER_PASSWORD_LENGTH));
        MIN_USER_PASSWORD_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MIN_USER_PASSWORD_LENGTH));
    }

    @Override
    public void validateVoForCreation(EntityManager entityManager, UserVo userVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException {

        MultipleMessagesException multipleMessagesException =
                new MultipleMessagesException();

        if (userVo == null) {
            multipleMessagesException.addMessage("user.null");
            throw multipleMessagesException;
        }

        if (userVo.getFirstName() == null) {
            multipleMessagesException.addMessage(
                    "user.firstName.null");
        } else {
            userVo.setFirstName(SecurityHelper.sanitizeHTML(userVo.getFirstName()));
            if (userVo.getFirstName().isEmpty()) {
                multipleMessagesException.addMessage(
                        "user.firstName.empty");
            }
        }
        if (userVo.getLastName() == null) {
            multipleMessagesException.addMessage(
                    "user.lastName.null");
        } else {
            userVo.setLastName(SecurityHelper.sanitizeHTML(userVo.getLastName()));
            if (userVo.getLastName().isEmpty()) {
                multipleMessagesException.addMessage(
                        "user.lastName.empty");
            }
        }
        if (userVo.getUserName() == null) {
            multipleMessagesException.addMessage(
                    "user.userName.null");
        } else {
            userVo.setUserName(SecurityHelper.sanitizeHTML(userVo.getUserName()));
            if (userVo.getUserName().length() < MIN_USERNAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.userName.tooShort");
            } else if (userVo.getUserName().length() > MAX_USERNAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.userName.tooLong");
            }
        }
        if (userVo.getPassword() == null) {
            multipleMessagesException.addMessage(
                    "user.password.null");
        } else {
            userVo.setPassword(SecurityHelper.sanitizeHTML(userVo.getPassword()));
            if (userVo.getPassword().length() < MIN_USER_PASSWORD_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.password.tooShort");
            } else if (userVo.getPassword().length() > MAX_USER_PASSWORD_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.password.tooLong");
            }
        }
        if (userVo.getProgramsId() == null) {
            multipleMessagesException.addMessage(
                    "user.programsId.null");
        } else if (userVo.getProgramsId().isEmpty()) {
            multipleMessagesException.addMessage(
                    "user.programsId.empty");
        } else {
            for (Long programId : userVo.getProgramsId()) {
                if (programId == null) {
                    multipleMessagesException.addMessage(
                            "user.programsId.element.null");
                } else if (getDaoFactory().getProgramDao().getById(entityManager, programId) == null) {
                    multipleMessagesException.addMessage(
                            "user.programsId.element.notFound");
                }
            }
        }
        if (userVo.getRole() == null) {
            multipleMessagesException.addMessage("user.role.null");
        }

        if (!multipleMessagesException.getMessages().isEmpty()) {
            throw multipleMessagesException;
        }
    }

    @Override
    public void validateVoForUpdate(EntityManager entityManager, UserVo userVo)
            throws MultipleMessagesException,
            ExternalServiceConnectionException, DataBaseException {
        validateVoForCreation(entityManager, userVo);
        MultipleMessagesException multipleMessagesException =
                new MultipleMessagesException();
        if (userVo.getId() == null) {
            multipleMessagesException.addMessage("user.id.null");
            throw multipleMessagesException;
        }
    }

    @Override
    public UserEntity voToEntity(EntityManager em, UserVo userVo)
            throws ExternalServiceConnectionException, MultipleMessagesException,
            DataBaseException {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userVo.getId());
        userEntity.setFirstName(userVo.getFirstName());
        userEntity.setLastName(userVo.getLastName());
        userEntity.setUserName(userVo.getUserName());
        userEntity.setPassword(userVo.getPassword());
        userEntity.setRole(userVo.getRole());
        userEntity.setActive(userVo.isActive());

        List<Long> programsId = userVo.getProgramsId();
        if (programsId != null) {
            List<ProgramEntity> arrayList = new ArrayList<ProgramEntity>();
            for (Long programId : programsId) {
                arrayList.add(this.getDaoFactory().getProgramDao().getById(em,
                        programId));
            }
            userEntity.setPrograms(arrayList);
        }

        return userEntity;
    }

    public UserVo authenticate(EntityManager entityManager, String userName,
            String password)
            throws InactiveUserException, ExternalServiceConnectionException,
            DataBaseException, MultipleMessagesException {

        MultipleMessagesException exceptions = new MultipleMessagesException();

        if (userName == null) {
            exceptions.addMessage("user.userName.null");
        } else if (userName.length() < MIN_USERNAME_LENGTH) {
            exceptions.addMessage(
                    "user.userName.tooShort");
        } else if (userName.length() > MAX_USERNAME_LENGTH) {
            exceptions.addMessage(
                    "user.userName.tooLong");
        }
        if (password == null) {
            exceptions.addMessage("user.password.null");
        } else if (password.length() < MIN_USER_PASSWORD_LENGTH) {
            exceptions.addMessage("user.password.tooShort");
        } else if (password.length() > MAX_USER_PASSWORD_LENGTH) {
            exceptions.addMessage(
                    "user.password.tooLong");
        }

        if (!exceptions.getMessages().isEmpty()) {
            throw exceptions;
        }

        userName = SecurityHelper.sanitizeHTML(userName);
        password = SecurityHelper.sanitizeHTML(password);
        password = SecurityHelper.hashPassword(password);

        UserEntity user = getDaoFactory().getUserDao().getByUsernameAndPassword(
                entityManager, userName, password);
        if (user == null) {
            return null;
        } else {
            if (!user.isActive()) {
                throw new InactiveUserException("user.inactive");
            } else {
                return user.toVo();
            }
        }
    }

    public UserVo activateAccount(EntityManager entityManager, String confirmationKey)
            throws ExternalServiceConnectionException, DataBaseException {

        confirmationKey = SecurityHelper.sanitizeHTML(confirmationKey);
        ConfirmationKeyEntity confirmationKeyEntity =
                getDaoFactory().getConfirmationKeyDao().getByConfirmationKey(entityManager,
                confirmationKey);
        if (confirmationKeyEntity != null) {
            UserEntity userEntity = confirmationKeyEntity.getUser();
            if (!userEntity.isActive()) {
                userEntity.setActive(true);
                entityManager.remove(confirmationKeyEntity);
                return userEntity.toVo();
            }
        }
        return null;
    }

    public ConfirmationKeyVo getConfirmationKeyByUserId(EntityManager em,
            long id)
            throws DataBaseException {
        return getDaoFactory().getUserDao().getById(em, id).getConfirmationKey().
                toVo();
    }
}
