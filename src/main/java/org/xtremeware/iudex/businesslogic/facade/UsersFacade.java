package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.*;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.*;

public class UsersFacade extends AbstractFacade {

    public UsersFacade(ServiceBuilder serviceFactory,
            EntityManagerFactory emFactory) {
        super(serviceFactory, emFactory);
    }

    /**
     * Activates a user in the database using a confirmation key
     *
     * @param confirmationKey the confirmation key
     * @return the activated user or null if the confirmation key doesn't match
     * any user
     */
    public UserVo activateUser(String confirmationKey) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        UserVo userVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            userVo = getServiceFactory().getUsersService().activateAccount(entityManager,
                    confirmationKey);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return userVo;
    }

    /**
     * Adds a new user to the database. The new user is by default inactive and
     * the password in the returned user is hashed.
     *
     * @param user the user to add
     * @return the added user
     * @throws MultipleMessagesException if there are validation problems
     */
    public UserVo createUser(UserVo user) 
            throws MultipleMessagesException,DuplicityException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        UserVo newUser = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            UsersService usersService = getServiceFactory().getUsersService();
            newUser = usersService.create(entityManager, user);

            // TODO: The confirmation email message should be configurable
            Map<String, String> data = new HashMap<String, String>();
            data.put("userName", newUser.getUserName());
            data.put("appPath",
                    ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.APP_PATH));
            data.put("confirmationKey", usersService.getConfirmationKeyByUserId(
                    entityManager, newUser.getId()).getConfirmationKey());
            getServiceFactory().getMailingService().sendMessage(data,
                    ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.MAILING_TEMPLATES_CONFIRMATION),
                    ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.MAILING_TEMPLATES_CONFIRMATION_SUBJECT),
                    newUser.getUserName() + "@unal.edu.co");
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception,
                    MultipleMessagesException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return newUser;
    }

    /**
     * Authenticates a user
     *
     * @param username the user name
     * @param password the password
     * @return the authenticated user
     * @throws InactiveUserException if the user is still inactive
     * @throws MultipleMessagesException if there are validation problems
     */
    public UserVo logIn(String username, String password) 
            throws InactiveUserException, MultipleMessagesException {
        EntityManager entityManager = null;
        UserVo userVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            userVo = getServiceFactory().getUsersService().authenticate(entityManager,
                    username, password);
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkException(exception, InactiveUserException.class);
            FacadesHelper.checkException(exception, MultipleMessagesException.class);
            throw new RuntimeException(exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return userVo;
    }

    /**
     * Edits a user. No user can change his user name or role.
     *
     * @param user the user
     * @return the updated user
     * @throws MultipleMessagesException if there are validation problems
     */
    public UserVo updateUser(UserVo userVo) throws MultipleMessagesException, DuplicityException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        UserVo updatedUserVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            updatedUserVo = getServiceFactory().getUsersService().update(entityManager,
                    userVo);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkDuplicityViolation(entityManager, transaction, exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception,
                    MultipleMessagesException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return updatedUserVo;
    }

    public void recoverPassword(String userName) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            ServiceBuilder serviceBuilder = getServiceFactory();

            ForgottenPasswordKeyVo vo = serviceBuilder.getUsersService().
                    createForgottenPasswordKey(em,
                    userName);

            if (vo != null) {
                Map<String, String> data = new HashMap<String, String>();
                data.put("userName", userName);
                data.put("appPath", ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.APP_PATH));
                data.put("key", vo.getKey());

                serviceBuilder.getMailingService().sendMessage(data,
                        ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.MAILING_TEMPLATES_RECOVER_PASSWORD),
                        ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.MAILING_TEMPLATES_RECOVER_PASSWORD_SUBJECT),
                        userName + "@unal.edu.co");
            }
            tx.commit();
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            FacadesHelper.rollbackTransaction(em, tx, ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
    }

    public UserVo validateForgottenPasswordKey(String key) {
        EntityManager em = null;
        UserVo vo = null;
        try {
            em = getEntityManagerFactory().createEntityManager();

            vo = getServiceFactory().getUsersService().
                    getUserByForgottenPasswordKey(em, key);
        } catch (Exception ex) {
            getServiceFactory().getLogService().error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return vo;
    }

    public void resetPassword(String key, String password) throws
            MultipleMessagesException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            getServiceFactory().getUsersService().resetPassword(entityManager, key,
                    password);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception,
                    MultipleMessagesException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }
    
    public void deleteUser(long userId) throws DataBaseException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            getServiceFactory().getUsersService().delete(entityManager, userId);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().getLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception, DataBaseException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, exception);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
    }
}
