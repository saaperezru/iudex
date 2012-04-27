/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.facade;

import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.vo.PeriodVo;

/**
 *
 * @author Administrator
 */
public class PeriodsFacadeTest {
    
    public PeriodsFacadeTest() {
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
     * Test of removePeriod method, of class PeriodsFacade.
     */
    @Test
    public void testRemovePeriod() throws Exception {
        System.out.println("removePeriod");
        long id = 0L;
        PeriodsFacade instance = null;
        instance.removePeriod(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPeriod method, of class PeriodsFacade.
     */
    @Test
    public void testAddPeriod() throws Exception {
        System.out.println("addPeriod");
        int year = 0;
        int semester = 0;
        PeriodsFacade instance = null;
        PeriodVo expResult = null;
        PeriodVo result = instance.addPeriod(year, semester);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listPeriods method, of class PeriodsFacade.
     */
    @Test
    public void testListPeriods() throws Exception {
        System.out.println("listPeriods");
        PeriodsFacade instance = null;
        List expResult = null;
        List result = instance.listPeriods();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
