package org.xtremeware.iudex.businesslogic.facade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.InactiveUserException;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.UserVo;

public class UsersFacade extends AbstractFacade {

    public UsersFacade(ServiceFactory serviceFactory,
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
            userVo = getServiceFactory().createUsersService().activateAccount(entityManager,
                    confirmationKey);
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
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
    public UserVo addUser(UserVo user) throws MultipleMessagesException,
            DuplicityException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        UserVo newUser = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            newUser = getServiceFactory().createUsersService().create(entityManager, user);
            // TODO: The confirmation email message should be configurable
            getServiceFactory().createMailingService().sendMessage("<a href='http://iudex.j.rsnx.ru/confirm.xhtml?key=" +
                    getServiceFactory().createUsersService().
                    getConfirmationKeyByUserId(entityManager, newUser.getId()).
                    getConfirmationKey() + "'>Confirmar registro</a>",
                    "Confirmar registro", user.getUserName() + "@unal.edu.co");
            transaction.commit();
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, exception,
                    DuplicityException.class);
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
    public UserVo logIn(String username, String password) throws
            InactiveUserException, MultipleMessagesException {
        EntityManager entityManager = null;
        UserVo userVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            userVo = getServiceFactory().createUsersService().authenticate(entityManager,
                    username, password);
        } catch (Exception exception) {
            getServiceFactory().createLogService().error(exception.getMessage(), exception);
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
    public UserVo editUser(UserVo userVo) throws MultipleMessagesException {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        UserVo updatedUserVo = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            updatedUserVo = getServiceFactory().createUsersService().update(entityManager,
                    userVo);
            transaction.commit();
        } catch (Exception ex) {
            getServiceFactory().createLogService().error(ex.getMessage(), ex);
            FacadesHelper.checkExceptionAndRollback(entityManager, transaction, ex,
                    MultipleMessagesException.class);
            FacadesHelper.rollbackTransaction(entityManager, transaction, ex);
        } finally {
            FacadesHelper.closeEntityManager(entityManager);
        }
        return updatedUserVo;
    }
}
