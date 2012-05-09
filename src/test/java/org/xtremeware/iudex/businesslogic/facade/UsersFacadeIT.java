package org.xtremeware.iudex.businesslogic.facade;

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.InactiveUserException;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.Role;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
public class UsersFacadeIT {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestHelper.initializeDatabase();
    }

    public UsersFacadeIT() {
    }

//    /**
//     * Test of a successful registration
//     */
//    @Test
//    public void test_BL_2_1() throws InvalidVoException {
//        UserVo user = new UserVo();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setPassword("123456789");
//        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        user.setRole(Role.STUDENT);
//        user.setUserName("healarconr");
//        user.setActive(true); // Should be overriden
//        UserVo expectedUser = new UserVo();
//        expectedUser.setFirstName("John");
//        expectedUser.setLastName("Doe");
//        expectedUser.setPassword(SecurityHelper.hashPassword("123456789"));
//        expectedUser.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        expectedUser.setRole(Role.STUDENT);
//        expectedUser.setUserName("healarconr");
//        expectedUser.setActive(false);
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
//        user = usersFacade.addUser(user);
//        assertNotNull(user.getId());
//        assertTrue(user.getId() > 0);
//        // The id is OK, transfer it to expectedUser to ease the assertion
//        expectedUser.setId(user.getId());
//        assertEquals(expectedUser, user);
//    }
//
//    /**
//     * Test of a successful login
//     */
//    @Test
//    public void test_BL_3_1() throws Exception {
//        String userName = "student1";
//        String password = "123456789";
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
//        UserVo user = usersFacade.logIn(userName, password);
//        assertNotNull(user);
//    }
//
//    /**
//     * Test of an invalid login
//     */
//    @Test
//    public void test_BL_3_2() throws Exception {
//        String userName;
//        String password;
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
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
//        String userName = "student3";
//        String password = "123456789";
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
//        usersFacade.logIn(userName, password);
//    }
//
//    /**
//     * Test of a successful user activation
//     */
//    @Test
//    public void test_BL_10_1() throws Exception {
//        String confirmationKey = "1d141671f909bb21d3658372a7dbb87af521bc8d8a92088fbdada64604bf1cf1";
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
//        UserVo user = usersFacade.activateUser(confirmationKey);
//        assertEquals(true, user.isActive());
//        user = usersFacade.activateUser(confirmationKey);
//        assertNull(user);
//    }
//
//    /**
//     * Test of an invalid confirmation key
//     */
//    @Test
//    public void test_BL_10_2() throws Exception {
//        String confirmationKey = "Invalid!";
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
//        UserVo user = usersFacade.activateUser(confirmationKey);
//        assertNull(user);
//    }
//
//    /**
//     * Test a successful user account update
//     */
//    @Test
//    public void test_BL_11_1() throws InvalidVoException {
//        UserVo user = new UserVo();
//        user.setId(5L);
//        user.setFirstName("New name");
//        user.setLastName("New last name");
//        user.setPassword("New password");
//        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        user.setRole(Role.ADMINISTRATOR);
//        user.setUserName("newUserName");
//        UserVo expectedUser = new UserVo();
//        expectedUser.setId(5L);
//        expectedUser.setFirstName("New name");
//        expectedUser.setLastName("New last name");
//        expectedUser.setPassword(SecurityHelper.hashPassword("New password"));
//        expectedUser.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        expectedUser.setRole(Role.STUDENT); // Shouldn't change
//        expectedUser.setUserName("student4"); // Shouldn't change
//        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
//        user = usersFacade.editUser(user);
//        assertEquals(expectedUser, user);
//    }
//
//    /**
//     * Test an attempt to edit an inexistent user
//     */
//    @Test
//    public void test_BL_11_2() throws InvalidVoException {
//        UserVo user = new UserVo();
//        user.setId(-1L);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setPassword("123456789");
//        user.setProgramsId(Arrays.asList(new Long[]{2537L, 2556L}));
//        user.setRole(Role.STUDENT);
//        user.setUserName("healarconr");
//        user = Config.getInstance().getFacadeFactory().getUsersFacade().editUser(user);
//        assertNull(user);
//    }
}
