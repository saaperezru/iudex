package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Read;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Delete;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.ConfirmationKeyDao;

import org.xtremeware.iudex.dao.ForgottenPasswordKeyDao;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.ForgottenPasswordKeyEntity;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ConfirmationKeyVo;
import org.xtremeware.iudex.vo.ForgottenPasswordKeyVo;
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

    public UsersService(AbstractDaoBuilder daoFactory,
            Create create, Read read, Update update, Delete delete) {

        super(daoFactory, create, read, update, delete);

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
        validatePassword(userVo.getPassword(), multipleMessagesException);
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
                } else if (getDaoFactory().getProgramDao().read(entityManager, programId) == null) {
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
                arrayList.add(this.getDaoFactory().getProgramDao().read(em,
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

        UserEntity user = getDaoFactory().getUserDao().getByUsernameAndPassword(
                entityManager, 
                SecurityHelper.sanitizeHTML(userName), 
                SecurityHelper.hashPassword(SecurityHelper.sanitizeHTML(password)));
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

        ConfirmationKeyDao dao =
                getDaoFactory().getConfirmationKeyDao();
        ConfirmationKeyEntity confirmationKeyEntity =
                dao.getByConfirmationKey(entityManager,
                SecurityHelper.sanitizeHTML(confirmationKey));
        if (confirmationKeyEntity != null) {
            UserEntity userEntity = confirmationKeyEntity.getUser();
            if (!userEntity.isActive()) {
                userEntity.setActive(true);
                dao.delete(entityManager, confirmationKeyEntity.getId());
                return userEntity.toVo();
            }
        }
        return null;
    }

    public ConfirmationKeyVo getConfirmationKeyByUserId(EntityManager em,
            long id)
            throws DataBaseException {
        return getDaoFactory().getUserDao().read(em, id).getConfirmationKey().
                toVo();
    }

    public ForgottenPasswordKeyVo createForgottenPasswordKey(EntityManager em,
            String userName) throws DataBaseException {
        ForgottenPasswordKeyVo vo = null;

        UserEntity user = getDaoFactory().getUserDao().getByUserName(em,
                userName);

        if (user != null && user.isActive()) {
            ForgottenPasswordKeyDao dao = getDaoFactory().
                    getForgottenPasswordKeyDao();

            ForgottenPasswordKeyEntity entity = dao.getByUserName(em, userName);
            boolean newKey = entity == null;

            if (newKey) {
                entity = new ForgottenPasswordKeyEntity();
                entity.setUser(user);
            }

            Calendar expiration = new GregorianCalendar();
            expiration.add(Calendar.DAY_OF_MONTH,
                    Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.MAILING_KEYS_EXPIRATION)));
            entity.setExpirationDate(expiration.getTime());
            entity.setKey(SecurityHelper.generateMailingKey());

            if (newKey) {
                dao.create(em, entity);
            } else {
                entity = dao.update(em, entity);
            }

            vo = entity.toVo();
        }

        return vo;
    }

    public UserVo getUserByForgottenPasswordKey(EntityManager em, String key)
            throws DataBaseException {
        ForgottenPasswordKeyEntity entity = getDaoFactory().
                getForgottenPasswordKeyDao().getByKey(em, key);
        if (entity != null) {
            return entity.getUser().toVo();
        }
        return null;
    }

    public void resetPassword(EntityManager em,
            String key, String password) throws DataBaseException,
            MultipleMessagesException {
        ForgottenPasswordKeyDao dao = getDaoFactory().
                getForgottenPasswordKeyDao();
        ForgottenPasswordKeyEntity forgottenPasswordKey = dao.getByKey(em, key);

        MultipleMessagesException multipleMessagesException =
                new MultipleMessagesException();
        validatePassword(password, multipleMessagesException);

        if (!multipleMessagesException.getMessages().isEmpty()) {
            throw multipleMessagesException;
        }

        forgottenPasswordKey.getUser().setPassword(SecurityHelper.hashPassword(
                password));
        dao.delete(em, forgottenPasswordKey.getId());
    }

    private void validatePassword(String password,
            MultipleMessagesException multipleMessagesException) {
        if (password == null) {
            multipleMessagesException.addMessage(
                    "user.password.null");
        } else {
            if (password.length() < MIN_USER_PASSWORD_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.password.tooShort");
            } else if (password.length() > MAX_USER_PASSWORD_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.password.tooLong");
            }
        }
    }
}
