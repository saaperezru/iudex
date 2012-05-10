/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author josebermeo
 */
public class FeedbacksFacadeIT {

    private EntityManager entityManager;

    public FeedbacksFacadeIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestHelper.initializeDatabase();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.xtremeware.iudex_local");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void BL_9_1() throws MultipleMessagesException {
        FeedbacksFacade ff = Config.getInstance().getFacadeFactory().
                getFeedbacksFacade();
        FeedbackVo fv = ff.addFeedback(1L, "EL programa es muy lento", Calendar.getInstance().getTime());
        assertNotNull(fv);
        assertNotNull(fv.getId());
        assertTrue(fv.getFeedbackTypeId() == 1L);
        assertEquals("EL programa es muy lento", fv.getContent());
        int size = entityManager.createQuery("SELECT COUNT(p) FROM Feedback p WHERE p.id =:id", Long.class).setParameter("id", fv.getId()).getSingleResult().intValue();
        assertEquals(1, size);
    }

    @Test
    public void BL_9_2() {
        FeedbacksFacade ff = Config.getInstance().getFacadeFactory().
                getFeedbacksFacade();
        FeedbackVo fv = null;
        String[] expectedMessages = new String[]{
            "user.firstName.null",
            "user.lastName.null",
            "user.userName.null",
            "user.password.null", "user.programsId.null",
            "user.role.null"};
        try {
            fv = ff.addFeedback(0L, null, null);
        } catch (MultipleMessagesException ex) {
            TestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        try {
            fv = ff.addFeedback(Long.MAX_VALUE, "", null);
        } catch (MultipleMessagesException ex) {
            TestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        try {
            fv = ff.addFeedback(Long.MIN_VALUE, "", null);
        } catch (MultipleMessagesException ex) {
            TestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        assertNull(fv);

    }
//    /**
//     * Test of getFeedbackTypes method, of class FeedbacksFacade.
//     */
//    @Test
//    public void testGetFeedbackTypes() throws Exception {
//        System.out.println("getFeedbackTypes");
//        FeedbacksFacade instance = null;
//        List expResult = null;
//        List result = instance.getFeedbackTypes();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addFeedback method, of class FeedbacksFacade.
//     */
//    @Test
//    public void testAddFeedback() throws Exception {
//        System.out.println("addFeedback");
//        long feedbackType = 0L;
//        String content = "";
//        Date date = null;
//        FeedbacksFacade instance = null;
//        FeedbackVo expResult = null;
//        FeedbackVo result = instance.addFeedback(feedbackType, content, date);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
