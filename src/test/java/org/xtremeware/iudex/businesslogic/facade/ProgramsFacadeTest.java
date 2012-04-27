/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.facade;

import java.util.List;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 *
 * @author Administrator
 */
public class ProgramsFacadeTest {
    
    public ProgramsFacadeTest() {
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

    /**
     * Test of removeProgram method, of class ProgramsFacade.
     */
    @Test
    public void testRemoveProgram() throws Exception {
        System.out.println("removeProgram");
        long id = 0L;
        ProgramsFacade instance = null;
        instance.removeProgram(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addProgram method, of class ProgramsFacade.
     */
    @Test
    public void testAddProgram() throws Exception {
        System.out.println("addProgram");
        String name = "";
        ProgramsFacade instance = null;
        ProgramVo expResult = null;
        ProgramVo result = instance.addProgram(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProgramsAutocomplete method, of class ProgramsFacade.
     */
    @Test
    public void testGetProgramsAutocomplete() throws Exception {
        System.out.println("getProgramsAutocomplete");
        String name = "";
        ProgramsFacade instance = null;
        Map expResult = null;
        Map result = instance.getProgramsAutocomplete(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listPrograms method, of class ProgramsFacade.
     */
    @Test
    public void testListPrograms() {
        System.out.println("listPrograms");
        ProgramsFacade instance = null;
        List expResult = null;
        List result = instance.listPrograms();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
