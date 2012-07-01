package org.xtremeware.iudex.businesslogic.facade;

import com.dumbster.smtp.SimpleSmtpServer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesHelper;
import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import org.xtremeware.iudex.businesslogic.service.InactiveUserException;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.UserVo;

public class UsersFacadeIT {

    private final int MIN_USERNAME_LENGTH;
    private final int MAX_USERNAME_LENGTH;
    private final int MAX_USER_PASSWORD_LENGTH;
    private final int MIN_USER_PASSWORD_LENGTH;
    private static final EntityManagerFactory entityManagerFactory;
    private static final UsersFacade usersFacade;
    private static EntityManager entityManager;
    private static List<UserEntity> users;
    private static List<ProgramEntity> programs;
    private static UserEntity existingActiveUser;
    private static UserEntity existingInactiveUser;
    private static UserEntity toActivateUser;
    private static final String existingActiveUserPassword;
    private static final String existingInactiveUserPassword;
    private static final String validConfirmationKey;

    static {
        entityManagerFactory = FacadesTestHelper.createEntityManagerFactory();
        usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
        existingActiveUserPassword = "123456789";
        existingInactiveUserPassword = existingActiveUserPassword;
        validConfirmationKey = SecurityHelper.generateMailingKey();
    }

    @BeforeClass
    public static void setUpClass() {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        insertPrograms(entityManager);
        insertUsers(entityManager);

        transaction.commit();
    }

    private static void insertPrograms(EntityManager entityManager) {
        final int programCount = 2;
        final int programCodeFactor = 10000000;
        final int programNameLength = 20;

        programs = new ArrayList<ProgramEntity>(programCount);

        ProgramEntity program;
        for (int i = 1; i <= programCount; i++) {
            program = new ProgramEntity();
            program.setCode(
                    Math.round((float) Math.random() * programCodeFactor));
            program.setName(FacadesTestHelper.randomString(programNameLength));
            entityManager.persist(program);
            programs.add(program);
        }
    }

    private static void insertUsers(EntityManager entityManager) {
        users = new ArrayList<UserEntity>();

        existingActiveUser = new UserEntity();
        existingActiveUser.setFirstName("Existing");
        existingActiveUser.setLastName("Active User");
        existingActiveUser.setUserName("existingActiveUser");
        existingActiveUser.setPassword(SecurityHelper.hashPassword(
                existingActiveUserPassword));
        existingActiveUser.setPrograms(programs);
        existingActiveUser.setRole(Role.STUDENT);
        existingActiveUser.setActive(true);
        entityManager.persist(existingActiveUser);

        users.add(existingActiveUser);

        existingInactiveUser = new UserEntity();
        existingInactiveUser.setFirstName("Existing");
        existingInactiveUser.setLastName("Inactive User");
        existingInactiveUser.setUserName("existingInactiveUser");
        existingInactiveUser.setPassword(SecurityHelper.hashPassword(
                existingInactiveUserPassword));
        existingInactiveUser.setPrograms(programs);
        existingInactiveUser.setRole(Role.STUDENT);
        existingInactiveUser.setActive(false);
        entityManager.persist(existingInactiveUser);

        users.add(existingInactiveUser);

        toActivateUser = new UserEntity();
        toActivateUser.setFirstName("Existing");
        toActivateUser.setLastName("Inactive User");
        toActivateUser.setUserName("toActivateUser");
        toActivateUser.setPassword(SecurityHelper.hashPassword(
                "123456789"));
        toActivateUser.setPrograms(programs);
        toActivateUser.setRole(Role.STUDENT);
        toActivateUser.setActive(false);
        entityManager.persist(toActivateUser);

        users.add(toActivateUser);

        ConfirmationKeyEntity confirmationKeyEntity =
                new ConfirmationKeyEntity();
        confirmationKeyEntity.setUser(toActivateUser);
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH,
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_KEYS_EXPIRATION)));
        confirmationKeyEntity.setExpirationDate(expirationDate.getTime());
        confirmationKeyEntity.setConfirmationKey(validConfirmationKey);
        entityManager.persist(confirmationKeyEntity);
    }

    @AfterClass
    public static void tearDownClass() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        deleteUsers(entityManager);
        deletePrograms(entityManager);

        transaction.commit();

        FacadesHelper.closeEntityManager(entityManager);
    }

    private static void deleteUsers(EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();
        deleteConfirmationKeys(entityManager);
        deleteUserPrograms();
        transaction.commit();
        transaction.begin();
        for (UserEntity user : users) {
            entityManager.remove(user);
        }
    }

    private static void deleteConfirmationKeys(EntityManager entityManager) {
        entityManager.createQuery("DELETE FROM ConfirmationKey").executeUpdate();
    }

    private static void deleteUserPrograms() {
        for (UserEntity user : users) {
            user.setPrograms(null);
        }
    }

    private static void deletePrograms(EntityManager entityManager) {
        for (ProgramEntity program : programs) {
            entityManager.remove(program);
        }
    }

    public UsersFacadeIT() throws ExternalServiceConnectionException {
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

    @Test
    public void createUser_validInactiveUserVo_success() throws
            MultipleMessagesException, DuplicityException {
        UserVo inputUserVo = getValidInactiveUserVoForCreateUserTest();
        createUser_validUserVo_success(inputUserVo);
    }

    @Test
    public void createUser_validActiveUserVo_success() throws
            MultipleMessagesException, DuplicityException {
        UserVo inputUserVo = getValidActiveUserVoForCreateUserTest();
        createUser_validUserVo_success(inputUserVo);
    }

    private void createUser_validUserVo_success(UserVo inputUserVo) throws
            MultipleMessagesException, DuplicityException {
        UserVo expectedUser = getExpectedUserVoForCreateUserTest(inputUserVo);

        SimpleSmtpServer smtpServer = FacadesTestHelper.startFakeSmtpServer();
        UserVo outputUserVo = usersFacade.createUser(inputUserVo);

        smtpServer.stop();

        assertNotNull(outputUserVo.getId());
        assertTrue(outputUserVo.getId() > 0);

        Long autogeneratedValidId = outputUserVo.getId();
        expectedUser.setId(autogeneratedValidId);

        assertEquals(expectedUser, outputUserVo);

        assertEquals(1, smtpServer.getReceivedEmailSize());

        assertTrue(existsUserInDb(entityManager, expectedUser));

        List<Long> userProgramsIdsInDb = getUserProgramsIdsFromDb(entityManager,
                expectedUser.getId());
        List<Long> expectedProgramsIds = expectedUser.getProgramsId();

        assertEquals(expectedProgramsIds, userProgramsIdsInDb);

        users.add(entityManager.find(UserEntity.class, outputUserVo.getId()));

        assertEquals(1, smtpServer.getReceivedEmailSize());
    }

    private UserVo getValidActiveUserVoForCreateUserTest() {
        UserVo userVo = getValidUserVoForCreateUserTest();
        userVo.setUserName(userVo.getUserName() + "active");
        userVo.setActive(true);
        return userVo;
    }

    private UserVo getValidInactiveUserVoForCreateUserTest() {
        UserVo userVo = getValidUserVoForCreateUserTest();
        userVo.setUserName(userVo.getUserName() + "inactive");
        userVo.setActive(false);
        return userVo;
    }

    private UserVo getValidUserVoForCreateUserTest() {
        UserVo userVo = new UserVo();
        userVo.setFirstName("John");
        userVo.setLastName("Doe");
        userVo.setUserName("johndoe");
        userVo.setPassword("123456789");

        List<Long> programsId = getProgramsIds();

        userVo.setProgramsId(programsId);
        userVo.setRole(Role.STUDENT);
        return userVo;
    }

    private List<Long> getProgramsIds() {
        List<Long> programsIds = new ArrayList<Long>(programs.size());
        for (ProgramEntity program : programs) {
            programsIds.add(program.getId());
        }
        return programsIds;
    }

    private UserVo getExpectedUserVoForCreateUserTest(UserVo userVo) {
        UserVo expectedUser = new UserVo();
        expectedUser.setFirstName(userVo.getFirstName());
        expectedUser.setLastName(userVo.getLastName());
        expectedUser.setUserName(userVo.getUserName());
        expectedUser.setPassword(SecurityHelper.hashPassword(
                userVo.getPassword()));
        expectedUser.setProgramsId(userVo.getProgramsId());
        expectedUser.setRole(userVo.getRole());
        boolean alwaysInactiveStatus = false;
        expectedUser.setActive(alwaysInactiveStatus);
        return expectedUser;
    }

    private boolean existsUserInDb(EntityManager entityManager,
            UserVo expectedUser) {
        entityManager.createQuery(
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
        return true;
    }

    private List<Long> getUserProgramsIdsFromDb(EntityManager entityManager,
            Long userId) {
        List<Long> programsIds = entityManager.createQuery(
                "SELECT p.id FROM User u JOIN u.programs p WHERE u.id = :id").
                setParameter("id", userId).getResultList();
        return programsIds;
    }

    @Test(expected = DuplicityException.class)
    public void createUser_existingUser_duplicityException() throws
            MultipleMessagesException, Exception {
        final String existingUserName = existingActiveUser.getUserName();

        UserVo user = new UserVo();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName(existingUserName);
        user.setPassword("123456789");
        user.setProgramsId(getProgramsIds());
        user.setRole(Role.STUDENT);
        user.setActive(true);
        usersFacade.createUser(user);
    }

    @Test
    public void createUser_validationConstraints_multipleMessagesException()
            throws Exception {
        String[] expectedMessages = new String[]{
            "user.null"
        };

        try {
            usersFacade.createUser(null);
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
            usersFacade.createUser(user);
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
            usersFacade.createUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        user.setFirstName(FacadesTestHelper.randomString(10));
        user.setLastName(FacadesTestHelper.randomString(10));
        user.setUserName(
                FacadesTestHelper.randomString(MAX_USERNAME_LENGTH + 1));
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
            usersFacade.createUser(user);
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
            usersFacade.createUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    @Test
    public void logIn_validLogin_success() throws Exception {
        String userName = existingActiveUser.getUserName();
        String password = existingActiveUserPassword;
        UserVo user = usersFacade.logIn(userName, password);
        assertNotNull(user);

        assertTrue(existsUserInDb(userName, password));
    }

    private boolean existsUserInDb(String userName, String password) {
        entityManager.createQuery("SELECT u" +
                " FROM User u" +
                " WHERE u.userName = :userName" +
                " AND u.password = :password" +
                " AND active = true").setParameter("userName", userName).
                setParameter("password", SecurityHelper.hashPassword(password)).
                getSingleResult();
        return true;
    }

    @Test
    public void logIn_invalidLogin_null() throws Exception {
        String userName;
        String password;

        // Wrong username, right password
        userName = "Invalid!";
        password = existingActiveUserPassword;
        UserVo user = usersFacade.logIn(userName, password);
        assertNull(user);

        // Right username, wrong password
        userName = existingActiveUser.getUserName();
        password = "Invalid!";
        user = usersFacade.logIn(userName, password);
        assertNull(user);

        // Wrong username, wrong password
        userName = "Invalid!";
        password = "Invalid!";
        user = usersFacade.logIn(userName, password);
        assertNull(user);
    }

    @Test(expected = InactiveUserException.class)
    public void logIn_inactiveUser_inactiveUserException() throws Exception {
        final String userName = existingInactiveUser.getUserName();
        final String password = existingInactiveUserPassword;
        usersFacade.logIn(userName, password);
    }

    @Test
    public void activateUser_validConfirmationKey_success() throws Exception {
        UserVo user = usersFacade.activateUser(validConfirmationKey);
        Long userId = user.getId();
        assertTrue(user.isActive());

        EntityManager localEntityManager = entityManagerFactory.
                createEntityManager();
        UserEntity userEntity =
                (UserEntity) localEntityManager.createQuery("SELECT u" +
                " FROM User u" +
                " WHERE u.id = :id").setParameter("id", userId).
                getSingleResult();
        assertTrue(userEntity.isActive());
        assertNull(userEntity.getConfirmationKey());

        user = usersFacade.activateUser(validConfirmationKey);
        assertNull(user);
    }

    @Test
    public void activateUser_invalidConfirmationKey_null() throws Exception {
        String invalidConfirmationKey = "Invalid!";
        UserVo user = usersFacade.activateUser(invalidConfirmationKey);
        assertNull(user);
    }

    @Test
    public void updateUser_validUserVo_success() throws
            MultipleMessagesException, Exception {
        UserVo userVo = new UserVo();
        userVo.setId(existingActiveUser.getId());
        userVo.setFirstName("New name");
        userVo.setLastName("New last name");
        userVo.setUserName("newUserName");
        userVo.setPassword("New password");
        List<Long> expectedProgramsIds = new ArrayList<Long>(1);
        expectedProgramsIds.add(programs.get(0).getId());
        userVo.setProgramsId(expectedProgramsIds);
        userVo.setRole(Role.ADMINISTRATOR);

        UserVo expectedUserVo = new UserVo();
        expectedUserVo.setId(existingActiveUser.getId());
        expectedUserVo.setFirstName("New name");
        expectedUserVo.setLastName("New last name");
        expectedUserVo.setPassword(SecurityHelper.hashPassword("New password"));
        expectedUserVo.setProgramsId(expectedProgramsIds);
        expectedUserVo.setRole(Role.STUDENT); // Shouldn't change
        expectedUserVo.setUserName(existingActiveUser.getUserName()); // Shouldn't change

        userVo = usersFacade.updateUser(userVo);
        assertEquals(expectedUserVo, userVo);

        existsUserInDb(entityManager, expectedUserVo);

        List<Long> programsIds = getUserProgramsIdsFromDb(entityManager,
                expectedUserVo.getId());

        assertEquals(expectedProgramsIds, programsIds);
    }
//    /**
//     * Test an attempt to edit an inexistent user
//     */
//    @Test 
//    public void test_BL_11_2() throws MultipleMessagesException, Exception {
//        UserVo user = new UserVo();
//        user.setId(-1L);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setPassword("123456789");
//        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        user.setRole(Role.STUDENT);
//        user.setUserName("healarconr");
//        user = Config.getInstance().getFacadeFactory().getUsersFacade().updateUser(
//                user);
//        assertNull(user);
//    }
//
//    /**
//     * Test an attempt to edit a user with invalid data
//     */
//    @Test 
//    public void test_BL_11_3() throws Exception {
//        String[] expectedMessages = new String[]{
//            "user.null"
//        };
//
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
//                getUsersFacade();
//        try {
//            usersFacade.updateUser(null);
//        } catch (MultipleMessagesException ex) {
//            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
//        }
//
//        UserVo user = new UserVo();
//
//        user.setFirstName(null);
//        user.setLastName(null);
//        user.setUserName(null);
//        user.setPassword(null);
//        user.setProgramsId(null);
//        user.setRole(null);
//        user.setActive(true);
//
//        expectedMessages = new String[]{
//            "user.firstName.null",
//            "user.lastName.null",
//            "user.userName.null",
//            "user.password.null",
//            "user.programsId.null",
//            "user.role.null"
//        };
//
//        try {
//            usersFacade.updateUser(user);
//        } catch (MultipleMessagesException ex) {
//            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
//        }
//
//        user.setFirstName("");
//        user.setLastName("");
//        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH - 1));
//        user.setPassword(FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH -
//                1));
//        user.setProgramsId(new ArrayList<Long>());
//        user.setRole(Role.STUDENT);
//
//        expectedMessages = new String[]{
//            "user.firstName.empty",
//            "user.lastName.empty",
//            "user.userName.tooShort",
//            "user.password.tooShort",
//            "user.programsId.empty"
//        };
//
//        try {
//            usersFacade.updateUser(user);
//        } catch (MultipleMessagesException ex) {
//            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
//        }
//
//        user.setFirstName(FacadesTestHelper.randomString(10));
//        user.setLastName(FacadesTestHelper.randomString(10));
//        user.setUserName(FacadesTestHelper.randomString(MAX_USERNAME_LENGTH + 10));
//        user.setPassword(FacadesTestHelper.randomString(MAX_USER_PASSWORD_LENGTH +10));
//        List<Long> programsId = user.getProgramsId();
//        programsId.add(null);
//
//        expectedMessages = new String[]{
//            "user.userName.tooLong",
//            "user.password.tooLong",
//            "user.programsId.element.null"
//        };
//
//        try {
//            usersFacade.updateUser(user);
//        } catch (MultipleMessagesException ex) {
//            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
//        }
//
//        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH));
//        user.setPassword(
//                FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH));
//        programsId.set(0, -1L);
//
//        expectedMessages = new String[]{
//            "user.programsId.element.notFound"
//        };
//
//        try {
//            usersFacade.updateUser(user);
//        } catch (MultipleMessagesException ex) {
//            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
//        }
//    }
//    
//    @Test 
//    public void testDoubleRaitingCommentDeleted() throws MultipleMessagesException, Exception {
//        CommentVo commentVo = new CommentVo();
//        commentVo.setAnonymous(false);
//        commentVo.setContent("MUY MAL CURSO");
//        commentVo.setCourseId(5L);
//        commentVo.setDate(new Date());
//        commentVo.setRating(1F);
//        commentVo.setUserId(6L);
//        CommentVo addComment = Config.getInstance().getFacadeFactory().getCommentsFacade().createComment(commentVo);
//        
//        Config.getInstance().getFacadeFactory().getCommentsFacade().rateComment(addComment.getId(), 6L, 1);
//        
//        Config.getInstance().getFacadeFactory().getUsersFacade().deleteUser(6L);
//    }
}
