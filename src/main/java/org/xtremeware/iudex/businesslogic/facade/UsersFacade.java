package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.service.InactiveUserException;
import org.xtremeware.iudex.businesslogic.service.ServiceFactory;
import org.xtremeware.iudex.businesslogic.service.UsersService;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
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
        EntityManager em = null;
        EntityTransaction tx = null;
        UserVo user = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            user = getServiceFactory().createUsersService().activateAccount(em,
                    confirmationKey);
            tx.commit();
        } catch (Exception ex) {
            getServiceFactory().createLogService().error(ex.getMessage(), ex);
            FacadesHelper.rollbackTransaction(em, tx, ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return user;
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
        EntityManager em = null;
        EntityTransaction tx = null;
        UserVo newUser = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            UsersService usersService = getServiceFactory().createUsersService();
            newUser = usersService.create(em, user);
            // TODO: The confirmation email message should be configurable
            Map<String, String> data = new HashMap<String, String>();
            data.put("userName", newUser.getUserName());
            data.put("appPath",
                    ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.APP_PATH));
            data.put("confirmationKey", usersService.getConfirmationKeyByUserId(
                    em, newUser.getId()).getConfirmationKey());
            getServiceFactory().createMailingService().sendMessage(data,
                    ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.MAILING_TEMPLATES_CONFIRMATION),
                    ConfigurationVariablesHelper.getVariable(
                    ConfigurationVariablesHelper.MAILING_TEMPLATES_CONFIRMATION_SUBJECT),
                    newUser.getUserName() + "@unal.edu.co");
            tx.commit();
        } catch (Exception ex) {
            getServiceFactory().createLogService().error(ex.getMessage(), ex);
            FacadesHelper.checkExceptionAndRollback(em, tx, ex,
                    DuplicityException.class);
            FacadesHelper.checkExceptionAndRollback(em, tx, ex,
                    MultipleMessagesException.class);
            FacadesHelper.rollbackTransaction(em, tx, ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
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
        EntityManager em = null;
        UserVo user = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            user = getServiceFactory().createUsersService().authenticate(em,
                    username, password);
        } catch (Exception ex) {
            getServiceFactory().createLogService().error(ex.getMessage(), ex);
            FacadesHelper.checkException(ex, InactiveUserException.class);
            FacadesHelper.checkException(ex, MultipleMessagesException.class);
            throw new RuntimeException(ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return user;
    }

    /**
     * Edits a user. No user can change his user name or role.
     *
     * @param user the user
     * @return the updated user
     * @throws MultipleMessagesException if there are validation problems
     */
    public UserVo editUser(UserVo user) throws MultipleMessagesException {
        EntityManager em = null;
        EntityTransaction tx = null;
        UserVo updatedUser = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            updatedUser = getServiceFactory().createUsersService().update(em,
                    user);
            tx.commit();
        } catch (Exception ex) {
            getServiceFactory().createLogService().error(ex.getMessage(), ex);
            FacadesHelper.checkExceptionAndRollback(em, tx, ex,
                    MultipleMessagesException.class);
            FacadesHelper.rollbackTransaction(em, tx, ex);
        } finally {
            FacadesHelper.closeEntityManager(em);
        }
        return updatedUser;
    }
}
