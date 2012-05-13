package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.createimplementations.UsersCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.UsersRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.UsersUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ConfirmationKeyVo;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author josebermeo
 */
public class UsersService extends CrudService<UserVo, UserEntity> {

    private final int MIN_USERNAME_LENGTH;
    private final int MAX_USERNAME_LENGTH;
    private final int MAX_USER_PASSWORD_LENGTH;
    private final int MIN_USER_PASSWORD_LENGTH;

    public UsersService(AbstractDaoFactory daoFactory) throws
            ExternalServiceConnectionException {
        super(daoFactory,
                new UsersCreate(daoFactory),
                new SimpleRead<UserEntity>(daoFactory.getUserDao()),
                new UsersUpdate(daoFactory.getUserDao()),
                new UsersRemove(daoFactory));
        MIN_USERNAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.
                getVariable(ConfigurationVariablesHelper.MIN_USERNAME_LENGTH));
        MAX_USERNAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.
                getVariable(ConfigurationVariablesHelper.MAX_USERNAME_LENGTH));
        MAX_USER_PASSWORD_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAX_USER_PASSWORD_LENGTH));
        MIN_USER_PASSWORD_LENGTH =
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MIN_USER_PASSWORD_LENGTH));
    }

    @Override
    public void validateVo(EntityManager em, UserVo vo) throws
            MultipleMessagesException, ExternalServiceConnectionException,
            DataBaseException {
        MultipleMessagesException multipleMessagesException =
                new MultipleMessagesException();

        if (vo == null) {
            multipleMessagesException.addMessage(
                    "user.null");
            throw multipleMessagesException;
        }

        if (vo.getFirstName() == null) {
            multipleMessagesException.addMessage(
                    "user.firstName.null");
        } else {
            vo.setFirstName(SecurityHelper.sanitizeHTML(vo.getFirstName()));
            if (vo.getFirstName().isEmpty()) {
                multipleMessagesException.addMessage(
                        "user.firstName.empty");
            }
        }
        if (vo.getLastName() == null) {
            multipleMessagesException.addMessage(
                    "user.lastName.null");
        } else {
            vo.setLastName(SecurityHelper.sanitizeHTML(vo.getLastName()));
            if (vo.getLastName().isEmpty()) {
                multipleMessagesException.addMessage(
                        "user.lastName.empty");
            }
        }
        if (vo.getUserName() == null) {
            multipleMessagesException.addMessage(
                    "user.userName.null");
        } else {
            vo.setUserName(SecurityHelper.sanitizeHTML(vo.getUserName()));
            if (vo.getUserName().length() < MIN_USERNAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.userName.tooShort");
            } else if (vo.getUserName().length() > MAX_USERNAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.userName.tooLong");
            }
        }
        if (vo.getPassword() == null) {
            multipleMessagesException.addMessage(
                    "user.password.null");
        } else {
            vo.setUserName(SecurityHelper.sanitizeHTML(vo.getUserName()));
            if (vo.getPassword().length() < MIN_USER_PASSWORD_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.password.tooShort");
            } else if (vo.getPassword().length() > MAX_USER_PASSWORD_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.password.tooLong");
            }
        }
        if (vo.getProgramsId() == null) {
            multipleMessagesException.addMessage(
                    "user.programsId.null");
        } else if (vo.getProgramsId().isEmpty()) {
            multipleMessagesException.addMessage(
                    "user.programsId.empty");
        } else {
            for (Long programId : vo.getProgramsId()) {
                if (programId == null) {
                    multipleMessagesException.addMessage(
                            "user.programsId.element.null");
                } else if (getDaoFactory().getProgramDao().getById(em, programId) ==
                        null) {
                    multipleMessagesException.addMessage(
                            "user.programsId.element.notFound");
                }
            }
        }
        if (vo.getRole() == null) {
            multipleMessagesException.addMessage("user.role.null");
        }

        if (!multipleMessagesException.getMessages().isEmpty()) {
            throw multipleMessagesException;
        }
    }

    @Override
    public UserEntity voToEntity(EntityManager em, UserVo vo)
            throws ExternalServiceConnectionException, MultipleMessagesException,
            DataBaseException {
        validateVo(em, vo);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(vo.getId());
        userEntity.setFirstName(vo.getFirstName());
        userEntity.setLastName(vo.getLastName());
        userEntity.setUserName(vo.getUserName());
        userEntity.setPassword(vo.getPassword());
        userEntity.setRole(vo.getRole());
        userEntity.setActive(vo.isActive());

        List<Long> programsId = vo.getProgramsId();
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

    public UserVo authenticate(EntityManager em, String userName,
            String password)
            throws InactiveUserException, ExternalServiceConnectionException,
            DataBaseException, MultipleMessagesException {
        MultipleMessagesException exception = new MultipleMessagesException();
        if (userName == null) {
            exception.addMessage("user.userName.null");
        } else if (userName.isEmpty()) {
            exception.addMessage("user.userName.empty");
        }
        if (password == null) {
            exception.addMessage("user.password.null");
        } else if (password.isEmpty()) {
            exception.addMessage("user.password.empty");
        }

        if (!exception.getMessages().isEmpty()) {
            throw exception;
        }

        userName = SecurityHelper.sanitizeHTML(userName);
        password = SecurityHelper.sanitizeHTML(password);
        password = SecurityHelper.hashPassword(password);
        UserEntity user = getDaoFactory().getUserDao().getByUsernameAndPassword(
                em, userName, password);
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

    public UserVo activateAccount(EntityManager em, String confirmationKey)
            throws ExternalServiceConnectionException, DataBaseException {
        confirmationKey = SecurityHelper.sanitizeHTML(confirmationKey);
        ConfirmationKeyEntity confirmationKeyEntity =
                getDaoFactory().getConfirmationKeyDao().getByConfirmationKey(em,
                confirmationKey);
        if (confirmationKeyEntity != null) {
            UserEntity userEntity = confirmationKeyEntity.getUser();
            if (!userEntity.isActive()) {
                userEntity.setActive(true);
                em.remove(confirmationKeyEntity);
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
