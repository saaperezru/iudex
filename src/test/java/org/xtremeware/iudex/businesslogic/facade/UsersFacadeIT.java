package org.xtremeware.iudex.businesslogic.facade;

import static org.junit.Assert.assertNotNull;
import org.junit.*;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author healarconr
 */
public class UsersFacadeIT {
    
    public UsersFacadeIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void test() throws Exception{
        assertNotNull(Config.getInstance().getFacadeFactory().getUsersFacade().logIn("test", "123456789"));
    }

//    /**
//     * Test of activateUser method, of class UsersFacade.
//     */
//    @Test
//    public void testActivateUser() throws Exception {
//        System.out.println("activateUser");
//        String confirmationKey = "";
//        UsersFacade instance = null;
//        UserVo expResult = null;
//        UserVo result = instance.activateUser(confirmationKey);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addUser method, of class UsersFacade.
//     */
//    @Test
//    public void testAddUser() throws Exception {
//        System.out.println("addUser");
//        UserVo vo = null;
//        UsersFacade instance = null;
//        UserVo expResult = null;
//        UserVo result = instance.addUser(vo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of logIn method, of class UsersFacade.
//     */
//    @Test
//    public void testLogIn() throws Exception {
//        System.out.println("logIn");
//        String username = "";
//        String password = "";
//        UsersFacade instance = null;
//        UserVo expResult = null;
//        UserVo result = instance.logIn(username, password);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of editUser method, of class UsersFacade.
//     */
//    @Test
//    public void testEditUser() throws Exception {
//        System.out.println("editUser");
//        UserVo vo = null;
//        UsersFacade instance = null;
//        UserVo expResult = null;
//        UserVo result = instance.editUser(vo);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
