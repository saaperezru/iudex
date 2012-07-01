package org.xtremeware.iudex.businesslogic.facade;

import com.dumbster.smtp.SimpleSmtpServer;
import java.util.ArrayList;
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
    private static List<ProgramEntity> programs;
    private static UserEntity existingActiveUser;
    private static String existingActiveUserPassword;

    static {
        entityManagerFactory = FacadesTestHelper.createEntityManagerFactory();
        usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
        existingActiveUserPassword = "123456789";
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
        existingActiveUser = new UserEntity();
        existingActiveUser.setFirstName("Existing");
        existingActiveUser.setLastName("User");
        existingActiveUser.setUserName("existingUser");
        existingActiveUser.setPassword(SecurityHelper.hashPassword(
                existingActiveUserPassword));
        existingActiveUser.setPrograms(programs);
        existingActiveUser.setRole(Role.STUDENT);
        existingActiveUser.setActive(true);
        entityManager.persist(existingActiveUser);
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
        deleteConfirmationKeys(entityManager);
        EntityTransaction transaction = entityManager.getTransaction();
        deleteUserPrograms(entityManager);
        transaction.commit();
        transaction.begin();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }

    private static void deleteConfirmationKeys(EntityManager entityManager) {
        entityManager.createQuery("DELETE FROM ConfirmationKey").executeUpdate();
    }

    private static void deleteUserPrograms(EntityManager entityManager) {
        List<UserEntity> users = (List<UserEntity>) entityManager.createQuery(
                "SELECT u FROM User u").getResultList();
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

        entityManager.createQuery("SELECT u" +
                " FROM User u" +
                " WHERE u.userName = :userName" +
                " AND u.password = :password" +
                " AND active = true").setParameter("userName", userName).
                setParameter("password", SecurityHelper.hashPassword(password)).
                getSingleResult();
    }
//
//    /**
//     * Test of an invalid login
//     */
//    @Test
//    public void test_BL_3_2() throws Exception {
//        String userName;
//        String password;
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
//                getUsersFacade();
//        // Wrong username, right password
//        userName = "Invalid!";
//        password = "123456789";
//        UserVo user = usersFacade.logIn(userName, password);
//        assertNull(user);
//        // Right username, wrong password
//        userName = "student1";
//        password = "Invalid!";
//        user = usersFacade.logIn(userName, password);
//        assertNull(user);
//        // Wrong username, wrong password
//        userName = "Invalid!";
//        password = "Invalid!";
//        user = usersFacade.logIn(userName, password);
//        assertNull(user);
//    }
//
//    /**
//     * Test of a login attempt with an inactive account
//     */
//    @Test(expected = InactiveUserException.class)
//    public void test_BL_3_3() throws Exception {
//        final String userName = "student3";
//        final String password = "123456789";
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
//                getUsersFacade();
//        usersFacade.logIn(userName, password);
//    }
//
//    /**
//     * Test of a successful user activation
//     */
//    @Test
//    public void test_BL_10_1() throws Exception {
//        final String confirmationKey =
//                "1d141671f909bb21d3658372a7dbb87af521bc8d8a92088fbdada64604bf1cf1";
//
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
//                getUsersFacade();
//        UserVo user = usersFacade.activateUser(confirmationKey);
//        Long id = user.getId();
//        assertTrue(user.isActive());
//        user = usersFacade.activateUser(confirmationKey);
//        assertNull(user);
//
//        EntityManager em = entityManagerFactory.createEntityManager();
//        UserEntity userEntity = (UserEntity) em.createQuery("SELECT u" +
//                " FROM User u" +
//                " WHERE u.id = :id").setParameter("id", id).
//                getSingleResult();
//        assertTrue(userEntity.isActive());
//        assertNull(userEntity.getConfirmationKey());
//    }
//
//    /**
//     * Test of an invalid confirmation key
//     */
//    @Test
//    public void test_BL_10_2() throws Exception {
//        String confirmationKey = "Invalid!";
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
//                getUsersFacade();
//        UserVo user = usersFacade.activateUser(confirmationKey);
//        assertNull(user);
//    }
//
//    /**
//     * Test a successful user account update
//     */
//    @Test
//    public void test_BL_11_1() throws MultipleMessagesException, Exception {
//        UserVo user = new UserVo();
//        user.setId(5L);
//        user.setFirstName("New name");
//        user.setLastName("New last name");
//        user.setUserName("newUserName");
//        user.setPassword("New password");
//        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        user.setRole(Role.ADMINISTRATOR);
//        
//        UserVo expectedUser = new UserVo();
//        expectedUser.setId(5L);
//        expectedUser.setFirstName("New name");
//        expectedUser.setLastName("New last name");
//        expectedUser.setPassword(SecurityHelper.hashPassword("New password"));
//        expectedUser.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        expectedUser.setRole(Role.STUDENT); // Shouldn't change
//        expectedUser.setUserName("student4"); // Shouldn't change
//        
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().
//                getUsersFacade();
//        user = usersFacade.updateUser(user);
//        assertEquals(expectedUser, user);
//
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.createQuery("SELECT u" +
//                " FROM User u" +
//                " WHERE u.id = :id" +
//                " AND u.firstName = :firstName" +
//                " AND u.lastName = :lastName" +
//                " AND u.userName = :userName" +
//                " AND u.password = :password" +
//                " AND u.role = :role").setParameter("id", expectedUser.getId()).
//                setParameter("firstName", expectedUser.getFirstName()).
//                setParameter("lastName",
//                expectedUser.getLastName()).setParameter("userName",
//                expectedUser.getUserName()).setParameter("password",
//                expectedUser.getPassword()).
//                setParameter("role", expectedUser.getRole()).getSingleResult();
//
//        List<Long> programsId = em.createQuery(
//                "SELECT p.id FROM User u JOIN u.programs p WHERE u.id = :id").
//                setParameter("id", expectedUser.getId()).getResultList();
//        List<Long> expectedProgramsId = expectedUser.getProgramsId();
//
//        assertEquals(expectedProgramsId, programsId);
//    }
//
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
