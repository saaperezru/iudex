package org.xtremeware.iudex.businesslogic.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import org.xtremeware.iudex.businesslogic.service.InactiveUserException;
import org.xtremeware.iudex.businesslogic.service.MailingService;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.MailingConfigVo;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
public class UsersFacadeIT {

    private final int MIN_USERNAME_LENGTH;
    private final int MAX_USERNAME_LENGTH;
    private final int MAX_USER_PASSWORD_LENGTH;
    private final int MIN_USER_PASSWORD_LENGTH;
    private final EntityManagerFactory emf;

    @BeforeClass
    public static void setUpClass() throws Exception {
        FacadesTestHelper.initializeDatabase();
    }

    public UsersFacadeIT() throws
            ExternalServiceConnectionException {
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
        emf = FacadesTestHelper.createEntityManagerFactory();
    }

    /**
     * Test of a successful registration
     */
    @Test
    public void test_BL_2_1() throws MultipleMessagesException,
            DuplicityException {
        UserVo user = new UserVo();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("healarconr");
        user.setPassword("123456789");
        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
        user.setRole(Role.STUDENT);
        user.setActive(true); // Should be overriden
        UserVo expectedUser = new UserVo();
        expectedUser.setFirstName("John");
        expectedUser.setLastName("Doe");
        expectedUser.setUserName("healarconr");
        expectedUser.setPassword(SecurityHelper.hashPassword("123456789"));
        expectedUser.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
        expectedUser.setRole(Role.STUDENT);
        expectedUser.setActive(false);
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        user = usersFacade.addUser(user);
        assertNotNull(user.getId());
        assertTrue(user.getId() > 0);
        // The id is OK, transfer it to expectedUser to ease the assertion
        expectedUser.setId(user.getId());
        assertEquals(expectedUser, user);

        EntityManager em = emf.createEntityManager();

        // Will throw an exception if not found
        em.createQuery(
                "SELECT u" +
                " FROM User u" +
                " WHERE u.id = :id" +
                " AND u.firstName = :firstName" +
                " AND u.lastName = :lastName" +
                " AND u.userName = :userName" +
                " AND u.password = :password" +
                " AND u.role = :role" +
                " AND u.active = :active").setParameter("id",
                expectedUser.getId()).setParameter("firstName",
                expectedUser.getFirstName()).setParameter("lastName",
                expectedUser.getLastName()).setParameter("userName",
                expectedUser.getUserName()).setParameter("password",
                expectedUser.getPassword()).setParameter("role", expectedUser.
                getRole()).
                setParameter("active", expectedUser.isActive()).getSingleResult();

        List<Long> programsId = em.createQuery(
                "SELECT p.id FROM User u JOIN u.programs p WHERE u.id = :id").
                setParameter("id", expectedUser.getId()).getResultList();
        List<Long> expectedProgramsId = expectedUser.getProgramsId();

        assertEquals(expectedProgramsId, programsId);
    }

    /**
     * Test of an attempt to register a user which already exists
     */
    @Test(expected = DuplicityException.class)
    public void test_BL_2_2() throws MultipleMessagesException, Exception {
        final String userName = "student5";

        UserVo user = new UserVo();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName(userName); // Already exists
        user.setPassword("123456789");
        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
        user.setRole(Role.STUDENT);
        user.setActive(true);
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        usersFacade.addUser(user);
    }

    /**
     * Test of a registration attempt with invalid data
     */
    @Test
    public void test_BL_2_3() throws Exception {
        String[] expectedMessages = new String[]{
            "user.null"
        };

        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        try {
            usersFacade.addUser(null);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        UserVo user = new UserVo();

        user.setFirstName(null);
        user.setLastName(null);
        user.setUserName(null);
        user.setPassword(null);
        user.setProgramsId(null);
        user.setRole(null);
        user.setActive(true);

        expectedMessages = new String[]{
            "user.firstName.null",
            "user.lastName.null",
            "user.userName.null",
            "user.password.null",
            "user.programsId.null",
            "user.role.null"
        };

        try {
            usersFacade.addUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        user.setFirstName("");
        user.setLastName("");
        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH - 1));
        user.setPassword(FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH -
                1));
        user.setProgramsId(new ArrayList<Long>());
        user.setRole(Role.STUDENT);

        expectedMessages = new String[]{
            "user.firstName.empty",
            "user.lastName.empty",
            "user.userName.tooShort",
            "user.password.tooShort",
            "user.programsId.empty"
        };

        try {
            usersFacade.addUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        user.setFirstName(FacadesTestHelper.randomString(10));
        user.setLastName(FacadesTestHelper.randomString(10));
        user.setUserName(FacadesTestHelper.randomString(MAX_USERNAME_LENGTH + 1));
        user.setPassword(FacadesTestHelper.randomString(MAX_USER_PASSWORD_LENGTH +
                1));
        List<Long> programsId = user.getProgramsId();
        programsId.add(null);

        expectedMessages = new String[]{
            "user.userName.tooLong",
            "user.password.tooLong",
            "user.programsId.element.null"
        };

        try {
            usersFacade.addUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH));
        user.setPassword(
                FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH));
        programsId.set(0, -1L);

        expectedMessages = new String[]{
            "user.programsId.element.notFound"
        };

        try {
            usersFacade.addUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    /**
     * Test of a failed registration due to email problems
     */
    @Test
    public void test_BL_2_4() throws MultipleMessagesException,
            DuplicityException {
        UserVo user = new UserVo();
        user.setFirstName("Diane");
        user.setLastName("Doe");
        user.setUserName("someone");
        user.setPassword("123456789");
        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
        user.setRole(Role.STUDENT);

        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        MailingService mailingService = Config.getInstance().getServiceFactory().
                createMailingService();

        MailingConfigVo originalMailingConfig = new MailingConfigVo();
        originalMailingConfig.setSender(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_SENDER_EMAIL_ADDRESS));
        originalMailingConfig.setSmtpPassword(ConfigurationVariablesHelper.
                getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_PASSWORD));
        originalMailingConfig.setSmtpServer(ConfigurationVariablesHelper.
                getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_SERVER));
        originalMailingConfig.setSmtpServerPort(Integer.parseInt(ConfigurationVariablesHelper.
                getVariable(ConfigurationVariablesHelper.MAILING_SMTP_PORT)));
        originalMailingConfig.setSmtpUser(ConfigurationVariablesHelper.
                getVariable(
                ConfigurationVariablesHelper.MAILING_SMTP_USER));

        try {
            MailingConfigVo mailingConfig = new MailingConfigVo();
            mailingConfig.setSender("Invalid!");
            mailingConfig.setSmtpPassword(
                    originalMailingConfig.getSmtpPassword());
            mailingConfig.setSmtpUser(originalMailingConfig.getSmtpUser());
            mailingConfig.setSmtpServer(originalMailingConfig.getSmtpServer());
            mailingConfig.setSmtpServerPort(
                    originalMailingConfig.getSmtpServerPort());
            testMailingConfig(mailingService, mailingConfig);

            mailingConfig.setSender(originalMailingConfig.getSender());
            mailingConfig.setSmtpPassword(null);
            testMailingConfig(mailingService, mailingConfig);

            mailingConfig.setSmtpPassword(
                    originalMailingConfig.getSmtpPassword());
            mailingConfig.setSmtpUser(null);
            testMailingConfig(mailingService, mailingConfig);

            mailingConfig.setSmtpUser(originalMailingConfig.getSmtpUser());
            mailingConfig.setSmtpServer("Invalid!");
            testMailingConfig(mailingService, mailingConfig);

            mailingConfig.setSmtpServer(originalMailingConfig.getSmtpServer());
            mailingConfig.setSmtpServerPort(-1);
            testMailingConfig(mailingService, mailingConfig);

            mailingConfig.setSmtpServerPort(65536);
            testMailingConfig(mailingService, mailingConfig);

            mailingConfig.setSmtpServerPort(
                    originalMailingConfig.getSmtpServerPort());
            mailingConfig.setSmtpUser("someone@example.com");
            testMailingConnection(usersFacade, user, mailingService,
                    mailingConfig);

            mailingConfig.setSmtpUser(originalMailingConfig.getSmtpUser());
            mailingConfig.setSmtpPassword("Invalid!");
            testMailingConnection(usersFacade, user, mailingService,
                    mailingConfig);
        } catch (RuntimeException ex) {
            mailingService.setConfig(originalMailingConfig);
            throw ex;
        }
    }

    private void testMailingConfig(MailingService mailingService,
            MailingConfigVo mailingConfig) {
        boolean exception = false;
        try {
            mailingService.setConfig(mailingConfig);
        } catch (IllegalArgumentException ex) {
            exception = true;
        }
        assertTrue(exception);
    }

    private void testMailingConnection(UsersFacade usersFacade, UserVo user,
            MailingService mailingService, MailingConfigVo mailingConfig) throws
            MultipleMessagesException, DuplicityException {
        mailingService.setConfig(mailingConfig);
        boolean exception = false;
        try {
            usersFacade.addUser(user);
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof ExternalServiceConnectionException) {
                exception = true;
            }
        }
        assertTrue(exception);
    }

    /**
     * Test of a successful login
     */
    @Test
    public void test_BL_3_1() throws Exception {
        String userName = "student1";
        String password = "123456789";
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        UserVo user = usersFacade.logIn(userName, password);
        assertNotNull(user);

        EntityManager em = emf.createEntityManager();
        em.createQuery("SELECT u" +
                " FROM User u" +
                " WHERE u.userName = :userName" +
                " AND u.password = :password" +
                " AND active = true").setParameter("userName", userName).
                setParameter("password", SecurityHelper.hashPassword(password)).
                getSingleResult();
    }

    /**
     * Test of an invalid login
     */
    @Test
    public void test_BL_3_2() throws Exception {
        String userName;
        String password;
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        // Wrong username, right password
        userName = "Invalid!";
        password = "123456789";
        UserVo user = usersFacade.logIn(userName, password);
        assertNull(user);
        // Right username, wrong password
        userName = "student1";
        password = "Invalid!";
        user = usersFacade.logIn(userName, password);
        assertNull(user);
        // Wrong username, wrong password
        userName = "Invalid!";
        password = "Invalid!";
        user = usersFacade.logIn(userName, password);
        assertNull(user);
    }

    /**
     * Test of a login attempt with an inactive account
     */
    @Test(expected = InactiveUserException.class)
    public void test_BL_3_3() throws Exception {
        final String userName = "student3";
        final String password = "123456789";
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        usersFacade.logIn(userName, password);
    }

    /**
     * Test of a successful user activation
     */
    @Test
    public void test_BL_10_1() throws Exception {
        final String confirmationKey =
                "1d141671f909bb21d3658372a7dbb87af521bc8d8a92088fbdada64604bf1cf1";

        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        UserVo user = usersFacade.activateUser(confirmationKey);
        Long id = user.getId();
        assertTrue(user.isActive());
        user = usersFacade.activateUser(confirmationKey);
        assertNull(user);

        EntityManager em = emf.createEntityManager();
        UserEntity userEntity = (UserEntity) em.createQuery("SELECT u" +
                " FROM User u" +
                " WHERE u.id = :id").setParameter("id", id).
                getSingleResult();
        assertTrue(userEntity.isActive());
        assertNull(userEntity.getConfirmationKey());
    }

    /**
     * Test of an invalid confirmation key
     */
    @Test
    public void test_BL_10_2() throws Exception {
        String confirmationKey = "Invalid!";
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        UserVo user = usersFacade.activateUser(confirmationKey);
        assertNull(user);
    }

    /**
     * Test a successful user account update
     */
    @Test
    public void test_BL_11_1() throws MultipleMessagesException {
        UserVo user = new UserVo();
        user.setId(5L);
        user.setFirstName("New name");
        user.setLastName("New last name");
        user.setUserName("newUserName");
        user.setPassword("New password");
        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
        user.setRole(Role.ADMINISTRATOR);
        UserVo expectedUser = new UserVo();
        expectedUser.setId(5L);
        expectedUser.setFirstName("New name");
        expectedUser.setLastName("New last name");
        expectedUser.setPassword(SecurityHelper.hashPassword("New password"));
        expectedUser.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
        expectedUser.setRole(Role.STUDENT); // Shouldn't change
        expectedUser.setUserName("student4"); // Shouldn't change
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        user = usersFacade.editUser(user);
        assertEquals(expectedUser, user);

        EntityManager em = emf.createEntityManager();
        em.createQuery("SELECT u" +
                " FROM User u" +
                " WHERE u.id = :id" +
                " AND u.firstName = :firstName" +
                " AND u.lastName = :lastName" +
                " AND u.userName = :userName" +
                " AND u.password = :password" +
                " AND u.role = :role").setParameter("id", expectedUser.getId()).
                setParameter("firstName", expectedUser.getFirstName()).
                setParameter("lastName",
                expectedUser.getLastName()).setParameter("userName",
                expectedUser.getUserName()).setParameter("password",
                expectedUser.getPassword()).
                setParameter("role", expectedUser.getRole()).getSingleResult();

        List<Long> programsId = em.createQuery(
                "SELECT p.id FROM User u JOIN u.programs p WHERE u.id = :id").
                setParameter("id", expectedUser.getId()).getResultList();
        List<Long> expectedProgramsId = expectedUser.getProgramsId();

        assertEquals(expectedProgramsId, programsId);
    }

    /**
     * Test an attempt to edit an inexistent user
     */
    @Test
    public void test_BL_11_2() throws MultipleMessagesException {
        UserVo user = new UserVo();
        user.setId(-1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("123456789");
        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
        user.setRole(Role.STUDENT);
        user.setUserName("healarconr");
        user = Config.getInstance().getFacadeFactory().getUsersFacade().editUser(
                user);
        assertNull(user);
    }
    
    /**
     * Test an attempt to edit a user with invalid data
     */
    @Test
    public void test_BL_11_3() throws Exception {
        String[] expectedMessages = new String[]{
            "user.null"
        };

        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
                getUsersFacade();
        try {
            usersFacade.editUser(null);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        UserVo user = new UserVo();

        user.setFirstName(null);
        user.setLastName(null);
        user.setUserName(null);
        user.setPassword(null);
        user.setProgramsId(null);
        user.setRole(null);
        user.setActive(true);

        expectedMessages = new String[]{
            "user.firstName.null",
            "user.lastName.null",
            "user.userName.null",
            "user.password.null",
            "user.programsId.null",
            "user.role.null"
        };

        try {
            usersFacade.editUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        user.setFirstName("");
        user.setLastName("");
        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH - 1));
        user.setPassword(FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH -
                1));
        user.setProgramsId(new ArrayList<Long>());
        user.setRole(Role.STUDENT);

        expectedMessages = new String[]{
            "user.firstName.empty",
            "user.lastName.empty",
            "user.userName.tooShort",
            "user.password.tooShort",
            "user.programsId.empty"
        };

        try {
            usersFacade.editUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        user.setFirstName(FacadesTestHelper.randomString(10));
        user.setLastName(FacadesTestHelper.randomString(10));
        user.setUserName(FacadesTestHelper.randomString(MAX_USERNAME_LENGTH + 1));
        user.setPassword(FacadesTestHelper.randomString(MAX_USER_PASSWORD_LENGTH +
                1));
        List<Long> programsId = user.getProgramsId();
        programsId.add(null);

        expectedMessages = new String[]{
            "user.userName.tooLong",
            "user.password.tooLong",
            "user.programsId.element.null"
        };

        try {
            usersFacade.editUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH));
        user.setPassword(
                FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH));
        programsId.set(0, -1L);

        expectedMessages = new String[]{
            "user.programsId.element.notFound"
        };

        try {
            usersFacade.editUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }
}
