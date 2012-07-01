package org.xtremeware.iudex.businesslogic.facade;

import com.dumbster.smtp.SimpleSmtpServer;
import java.util.*;
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
import org.xtremeware.iudex.entity.*;
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
    private static PeriodEntity period;
    private static SubjectEntity subject;
    private static ProfessorEntity professor;
    private static CourseEntity course;
    private static CommentEntity comment;
    private static CommentRatingEntity commentRating;
    private static UserEntity existingActiveUser;
    private static UserEntity existingInactiveUser;
    private static UserEntity toActivateUser;
    private static UserEntity toDeleteUser;
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

        createPrograms(entityManager);
        createUsers(entityManager);
        createPeriod(entityManager);
        createSubject(entityManager);
        createProfessor(entityManager);
        createCourse(entityManager);
        createComment(entityManager);
        createCommentRating(entityManager);

        transaction.commit();
    }

    private static void createPrograms(EntityManager entityManager) {
        final int programCount = 2;
        final int programCodeLength = 4;
        final int programNameLength = 20;

        programs = new ArrayList<ProgramEntity>(programCount);

        ProgramEntity program;
        for (int i = 1; i <= programCount; i++) {
            program = new ProgramEntity();

            program.setCode(FacadesTestHelper.randomInt(programCodeLength));
            program.setName(FacadesTestHelper.randomString(programNameLength));
            entityManager.persist(program);
            programs.add(program);
        }
    }

    private static void createUsers(EntityManager entityManager) {
        users = new ArrayList<UserEntity>();

        createExistingActiveUser(entityManager);
        createExistingInactiveUser(entityManager);
        createToActivateUser(entityManager);
        createToDeleteUser(entityManager);
    }

    private static void createExistingActiveUser(EntityManager entityManager) {
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
    }

    private static void createExistingInactiveUser(EntityManager entityManager) {
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
    }

    private static void createToActivateUser(EntityManager entityManager) throws
            NumberFormatException {
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

        createConfirmationKey(entityManager);
    }

    private static void createConfirmationKey(EntityManager entityManager)
            throws NumberFormatException {
        ConfirmationKeyEntity confirmationKeyEntity =
                new ConfirmationKeyEntity();
        confirmationKeyEntity.setUser(toActivateUser);
        Date expirationDate = getConfirmationKeyExpirationDate();
        confirmationKeyEntity.setExpirationDate(expirationDate);
        confirmationKeyEntity.setConfirmationKey(validConfirmationKey);
        entityManager.persist(confirmationKeyEntity);
    }

    private static Date getConfirmationKeyExpirationDate() throws
            NumberFormatException {
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH,
                Integer.parseInt(ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.MAILING_KEYS_EXPIRATION)));
        return expirationDate.getTime();
    }

    private static void createToDeleteUser(EntityManager entityManager) {
        toDeleteUser = new UserEntity();
        toDeleteUser.setFirstName("Existing");
        toDeleteUser.setLastName("To Delete User");
        toDeleteUser.setUserName("toDeleteUser");
        toDeleteUser.setPassword(SecurityHelper.hashPassword(
                "123456789"));
        toDeleteUser.setPrograms(programs);
        toDeleteUser.setRole(Role.STUDENT);
        toDeleteUser.setActive(true);
        entityManager.persist(toDeleteUser);
    }

    private static void createPeriod(EntityManager entityManager) {
        final int semester = 1;

        period = new PeriodEntity();
        period.setSemester(semester);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        period.setYear(year);

        entityManager.persist(period);
    }

    private static void createSubject(EntityManager entityManager) {
        final int codeLength = 7;
        final int nameLength = 35;
        final int descriptionLength = 400;

        subject = new SubjectEntity();

        int code = FacadesTestHelper.randomInt(codeLength);
        subject.setCode(code);

        String description = FacadesTestHelper.randomString(descriptionLength);
        subject.setDescription(description);

        String name = FacadesTestHelper.randomString(nameLength);
        subject.setName(name);

        entityManager.persist(subject);
    }

    private static void createProfessor(EntityManager entityManager) {
        professor = new ProfessorEntity();
        professor.setFirstName("Diane");
        professor.setLastName("Doe");
        entityManager.persist(professor);
    }

    private static void createCourse(EntityManager entityManager) {
        final double ratingAverage = 0;
        final long ratingCount = 0;

        course = new CourseEntity();
        course.setPeriod(period);
        course.setSubject(subject);
        course.setProfessor(professor);
        course.setRatingAverage(ratingAverage);
        course.setRatingCount(ratingCount);
        entityManager.persist(course);
    }

    private static void createComment(EntityManager entityManager) {
        final int contentLength = 500;
        final float rating = 1F;

        comment = new CommentEntity();

        String content = FacadesTestHelper.randomString(contentLength);
        comment.setContent(content);

        Date now = new Date();
        comment.setDate(now);

        comment.setUser(toDeleteUser);
        comment.setCourse(course);
        comment.setAnonymous(false);
        comment.setRating(rating);

        entityManager.persist(comment);
    }

    private static void createCommentRating(EntityManager entityManager) {
        commentRating = new CommentRatingEntity();
        commentRating.setUser(toDeleteUser);
        commentRating.setComment(comment);

        int rating = (int) comment.getRating().floatValue();
        commentRating.setValue(rating);

        entityManager.persist(commentRating);
    }

    @AfterClass
    public static void tearDownClass() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        removeCourse(entityManager);
        removeProfessor(entityManager);
        removeSubject(entityManager);
        removePeriod(entityManager);
        removeUsers(entityManager);
        removePrograms(entityManager);

        transaction.commit();

        FacadesHelper.closeEntityManager(entityManager);
    }

    private static void removeCourse(EntityManager entityManager) {
        entityManager.remove(course);
    }

    private static void removeProfessor(EntityManager entityManager) {
        entityManager.remove(professor);
    }

    private static void removeSubject(EntityManager entityManager) {
        entityManager.remove(subject);
    }

    private static void removePeriod(EntityManager entityManager) {
        entityManager.remove(period);
    }

    private static void removeUsers(EntityManager entityManager) {
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

    private static void removePrograms(EntityManager entityManager) {
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

        checkCreateUser(user, expectedMessages);

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

        checkCreateUser(user, expectedMessages);

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

        checkCreateUser(user, expectedMessages);

        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH));
        user.setPassword(
                FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH));
        programsId.set(0, -1L);

        expectedMessages = new String[]{
            "user.programsId.element.notFound"
        };

        checkCreateUser(user, expectedMessages);
    }

    private void checkCreateUser(UserVo user, String[] expectedMessages)
            throws
            DuplicityException {
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
        checkWrongUserNameAndRightPassword();
        checkRightUserNameAndWrongPassword();
        checkWrongUserNameAndWrongPassword();
    }

    private void checkRightUserNameAndWrongPassword() throws Exception {
        String userName = existingActiveUser.getUserName();
        String password = "Invalid!";
        checkLogIn(userName, password);
    }

    private void checkLogIn(String userName, String password) throws
            InactiveUserException,
            MultipleMessagesException {
        UserVo user = usersFacade.logIn(userName, password);
        assertNull(user);
    }

    private void checkWrongUserNameAndRightPassword() throws Exception {
        String userName = "Invalid!";
        String password = existingActiveUserPassword;
        checkLogIn(userName, password);
    }

    private void checkWrongUserNameAndWrongPassword() throws Exception {
        String userName = "Invalid!";
        String password = "Invalid!";
        checkLogIn(userName, password);
    }

    @Test(expected = InactiveUserException.class)
    public void logIn_inactiveUser_inactiveUserException() throws Exception {
        final String userName = existingInactiveUser.getUserName();
        final String password = existingInactiveUserPassword;
        usersFacade.logIn(userName, password);
    }

    @Test
    public void activateUser_validConfirmationKey_success() throws Exception {
        UserVo user = checkFirstActivation();
        checkActivationInDb(user);
        checkSecondActivation();
    }

    private UserVo checkFirstActivation() {
        UserVo user = usersFacade.activateUser(validConfirmationKey);
        assertTrue(user.isActive());
        return user;
    }

    private void checkActivationInDb(UserVo user) {
        EntityManager localEntityManager = entityManagerFactory.
                createEntityManager();
        Long userId = user.getId();
        UserEntity userEntity =
                (UserEntity) localEntityManager.createQuery("SELECT u" +
                " FROM User u" +
                " WHERE u.id = :id").setParameter("id", userId).
                getSingleResult();
        assertTrue(userEntity.isActive());
        assertNull(userEntity.getConfirmationKey());
    }

    private void checkSecondActivation() {
        UserVo user;
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
        final String userNameWhichShouldNotChange = "newUserName";
        final Role roleWhichShouldNotChange = Role.ADMINISTRATOR;

        UserVo userVo = new UserVo();
        userVo.setId(existingActiveUser.getId());
        userVo.setFirstName("New name");
        userVo.setLastName("New last name");
        userVo.setUserName(userNameWhichShouldNotChange);
        userVo.setPassword("New password");
        List<Long> programsIds = new ArrayList<Long>(1);
        programsIds.add(programs.get(0).getId());
        userVo.setProgramsId(programsIds);
        userVo.setRole(roleWhichShouldNotChange);

        UserVo expectedUserVo = getExpectedUserVoForUpdateUserTest(userVo);

        userVo = usersFacade.updateUser(userVo);
        assertEquals(expectedUserVo, userVo);

        existsUserInDb(entityManager, expectedUserVo);

        List<Long> programsIdsInDb = getUserProgramsIdsFromDb(entityManager,
                expectedUserVo.getId());

        assertEquals(programsIds, programsIdsInDb);
    }

    private UserVo getExpectedUserVoForUpdateUserTest(UserVo userVo) {
        UserVo expectedUserVo = new UserVo();

        final Long originalId = existingActiveUser.getId();
        expectedUserVo.setId(originalId);

        expectedUserVo.setFirstName(userVo.getFirstName());
        expectedUserVo.setLastName(userVo.getLastName());

        String hashedPassword =
                SecurityHelper.hashPassword(userVo.getPassword());
        expectedUserVo.setPassword(hashedPassword);

        final String originalUserName = existingActiveUser.getUserName();
        expectedUserVo.setUserName(originalUserName);

        expectedUserVo.setProgramsId(userVo.getProgramsId());

        final Role originalRole = existingActiveUser.getRole();
        expectedUserVo.setRole(originalRole);

        return expectedUserVo;
    }

    @Test
    public void updateUser_nonexistentUser_null() throws
            MultipleMessagesException, Exception {
        UserVo user = new UserVo();
        user.setId(-1L);
        user.setFirstName("Non-existent");
        user.setLastName("User");
        user.setPassword("123456789");
        List<Long> programsIds = new ArrayList<Long>(1);
        programsIds.add(programs.get(0).getId());
        user.setProgramsId(programsIds);
        user.setRole(Role.STUDENT);
        user.setUserName("nonexistent");
        user = Config.getInstance().getFacadeFactory().getUsersFacade().
                updateUser(
                user);
        assertNull(user);
    }

    @Test
    public void updateUser_invalidData_multipleMessagesException() throws
            Exception {
        String[] expectedMessages = new String[]{
            "user.null"
        };

        checkUpdateUser(null, expectedMessages);

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

        checkUpdateUser(user, expectedMessages);

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

        checkUpdateUser(user, expectedMessages);

        user.setFirstName(FacadesTestHelper.randomString(10));
        user.setLastName(FacadesTestHelper.randomString(10));
        user.setUserName(
                FacadesTestHelper.randomString(MAX_USERNAME_LENGTH + 10));
        user.setPassword(FacadesTestHelper.randomString(MAX_USER_PASSWORD_LENGTH +
                10));
        List<Long> programsId = user.getProgramsId();
        programsId.add(null);

        expectedMessages = new String[]{
            "user.userName.tooLong",
            "user.password.tooLong",
            "user.programsId.element.null"
        };

        checkUpdateUser(user, expectedMessages);

        user.setUserName(FacadesTestHelper.randomString(MIN_USERNAME_LENGTH));
        user.setPassword(
                FacadesTestHelper.randomString(MIN_USER_PASSWORD_LENGTH));
        programsId.set(0, -1L);

        expectedMessages = new String[]{
            "user.programsId.element.notFound"
        };

        checkUpdateUser(user, expectedMessages);
    }

    private void checkUpdateUser(UserVo user, String[] expectedMessages) throws
            DuplicityException {
        try {
            usersFacade.updateUser(user);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    @Test
    public void deleteUser_autoRatedComment_success() throws
            MultipleMessagesException, Exception {
        long userId = toDeleteUser.getId();
        usersFacade.deleteUser(userId);

        EntityManager localEntityManager = entityManagerFactory.
                createEntityManager();

        long id = commentRating.getId();
        CommentRatingEntity foundCommentRating =
                localEntityManager.find(CommentRatingEntity.class, id);
        assertNull(foundCommentRating);

        id = comment.getId();
        CommentEntity foundComment = localEntityManager.find(CommentEntity.class,
                id);
        assertNull(foundComment);
    }
}
