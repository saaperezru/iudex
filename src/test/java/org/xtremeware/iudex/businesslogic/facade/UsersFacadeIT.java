package org.xtremeware.iudex.businesslogic.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author healarconr
 */
public class UsersFacadeIT {
    
    public UsersFacadeIT() {
    }

    /**
     * Test of activateUser method, of class UsersFacade.
     */
    @Test
    public void testActivateUser() throws Exception {
        String confirmationKey = "1d141671f909bb21d3658372a7dbb87af521bc8d8a92088fbdada64604bf1cf1";
        UsersFacade usersFacade = Config.getInstance().getFacadeFactory().getUsersFacade();
        UserVo user = usersFacade.activateUser(confirmationKey);
        assertEquals(true, user.isActive());
        user = usersFacade.activateUser(confirmationKey);
        assertNull(user);
    }

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
