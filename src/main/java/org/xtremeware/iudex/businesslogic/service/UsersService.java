package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.ConfirmationKeyDao;
import org.xtremeware.iudex.dao.ForgottenPasswordKeyDao;
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
            throws MultipleMessagesException, DataBaseException {

        MultipleMessagesException multipleMessagesException =
                new MultipleMessagesException();

        if (userVo == null) {
            multipleMessagesException.addMessage("user.null");
            throw multipleMessagesException;
        }

        checkFirstName(multipleMessagesException, userVo.getFirstName());
        checkLastName(multipleMessagesException, userVo.getLastName());
        checkUserName(multipleMessagesException, userVo.getUserName());
        checkPassword(multipleMessagesException, userVo.getPassword());
        checkPrograms(multipleMessagesException, userVo.getProgramsId(), entityManager);

        if (userVo.getRole() == null) {
            multipleMessagesException.addMessage("user.role.null");
        }

        if (!multipleMessagesException.getMessages().isEmpty()) {
            throw multipleMessagesException;
        }
        userVo.setFirstName(SecurityHelper.sanitizeHTML(userVo.getFirstName()));
        userVo.setLastName(SecurityHelper.sanitizeHTML(userVo.getLastName()));
        userVo.setUserName(SecurityHelper.sanitizeHTML(userVo.getUserName()));
        userVo.setPassword(SecurityHelper.sanitizeHTML(userVo.getPassword()));
    }

    private void checkFirstName(MultipleMessagesException multipleMessagesException,
            String firstName) {
        if (firstName == null) {
            multipleMessagesException.addMessage(
                    "user.firstName.null");
        } else {
            if (firstName.isEmpty()) {
                multipleMessagesException.addMessage(
                        "user.firstName.empty");
            }
        }
    }

    private void checkLastName(MultipleMessagesException multipleMessagesException,
            String lastName) {
        if (lastName == null) {
            multipleMessagesException.addMessage(
                    "user.lastName.null");
        } else {
            if (lastName.isEmpty()) {
                multipleMessagesException.addMessage(
                        "user.lastName.empty");
            }
        }
    }

    private void checkUserName(MultipleMessagesException multipleMessagesException,
            String UserName) {
        if (UserName == null) {
            multipleMessagesException.addMessage(
                    "user.userName.null");
        } else {
            if (UserName.length() < MIN_USERNAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.userName.tooShort");
            } else if (UserName.length() > MAX_USERNAME_LENGTH) {
                multipleMessagesException.addMessage(
                        "user.userName.tooLong");
            }
        }
    }

    private void checkPrograms(MultipleMessagesException multipleMessagesException,
            List<Long> programsId, EntityManager entityManager) throws DataBaseException {
        if (programsId == null) {
            multipleMessagesException.addMessage(
                    "user.programsId.null");
        } else if (programsId.isEmpty()) {
            multipleMessagesException.addMessage(
                    "user.programsId.empty");
        } else {
            for (Long programId : programsId) {
                if (programId == null) {
                    multipleMessagesException.addMessage(
                            "user.programsId.element.null");
                } else if (getDaoFactory().getProgramDao().read(entityManager, programId) == null) {
                    multipleMessagesException.addMessage(
                            "user.programsId.element.notFound");
                }
            }
        }
    }
    
    private void checkPassword(MultipleMessagesException multipleMessagesException,
            String password) {
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

    @Override
    public void validateVoForUpdate(EntityManager entityManager, UserVo userVo)
            throws MultipleMessagesException, DataBaseException {
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
            throws MultipleMessagesException, DataBaseException {

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
            throws InactiveUserException, DataBaseException, MultipleMessagesException {

        MultipleMessagesException exceptions = new MultipleMessagesException();

        checkUserName(exceptions, userName);
        checkPassword(exceptions, password);
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
            throws DataBaseException {

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
        checkPassword(multipleMessagesException, password);

        if (!multipleMessagesException.getMessages().isEmpty()) {
            throw multipleMessagesException;
        }

        forgottenPasswordKey.getUser().setPassword(SecurityHelper.hashPassword(SecurityHelper.sanitizeHTML(password)));
        dao.delete(em, forgottenPasswordKey.getId());
    }
}
