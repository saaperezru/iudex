package org.xtremeware.iudex.businesslogic.facade;

import java.util.*;
import javax.persistence.EntityManager;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.entity.FeedbackEntity;
import org.xtremeware.iudex.entity.FeedbackTypeEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.FeedbackTypeVo;
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

    @Before
    public void setUp() {
        entityManager = TestHelper.createEntityManager();
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
            "feedback.date.null",
            "feedback.content.null",
            "feedback.feedbackTypeId.element.notFound"};
        try {
            fv = ff.addFeedback(0L, null, null);
        } catch (MultipleMessagesException ex) {
            TestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        expectedMessages = new String[]{
            "feedback.date.null",
            "feedback.content.tooShort",
            "feedback.feedbackTypeId.element.notFound"};
        try {
            fv = ff.addFeedback(Long.MAX_VALUE, "", null);
        } catch (MultipleMessagesException ex) {
            TestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        expectedMessages = new String[]{
            "feedback.date.null",
            "feedback.content.tooLong",
            "feedback.feedbackTypeId.element.notFound"};
        try {
            fv = ff.addFeedback(Long.MIN_VALUE, TestHelper.randomString(2001), null);
        } catch (MultipleMessagesException ex) {
            TestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        assertNull(fv);

    }

    @Test
    public void BL_9_3() throws Exception {
        FeedbacksFacade ff = Config.getInstance().getFacadeFactory().
                getFeedbacksFacade();
        List<FeedbackVo> fvs = ff.getAllFeedbacks();
        for (FeedbackVo fv : fvs) {
            FeedbackVo result = entityManager.createQuery(
                    "SELECT p FROM Feedback p WHERE p.id =:id", FeedbackEntity.class).
                    setParameter("id", fv.getId()).getSingleResult().toVo();
            assertNotNull(result);
            assertEquals(result.getId(), fv.getId());
            assertEquals(result.getFeedbackTypeId(), fv.getFeedbackTypeId());
            assertEquals(result.getContent(), fv.getContent());
        }
        int size = entityManager.createQuery("SELECT COUNT(p) FROM Feedback p", Long.class).getSingleResult().intValue();
        assertEquals(size, fvs.size());
    }

    @Test
    public void BL_9_4() throws Exception {

        List<Long> feedbackTypeIds = entityManager.createQuery(
                "SELECT p.id FROM FeedbackType p", Long.class).getResultList();

        FeedbacksFacade ff = Config.getInstance().getFacadeFactory().
                getFeedbacksFacade();
        for (Long id : feedbackTypeIds) {
            List<FeedbackVo> fvs = ff.getFeedbacksByFeedbackType(id);
            for (FeedbackVo fv : fvs) {
                FeedbackVo result = entityManager.createQuery(
                        "SELECT p FROM Feedback p WHERE p.id =:id", FeedbackEntity.class).
                        setParameter("id", fv.getId()).getSingleResult().toVo();

                assertNotNull(result);
                assertEquals(result.getId(), fv.getId());
                assertEquals(id, fv.getFeedbackTypeId());
                assertEquals(result.getFeedbackTypeId(), fv.getFeedbackTypeId());
                assertEquals(result.getContent(), fv.getContent());
            }
            int size = entityManager.createQuery("SELECT COUNT(p) FROM Feedback p WHERE p.type.id =:id", Long.class).setParameter("id", id).getSingleResult().intValue();
            assertEquals(size, fvs.size());
        }
    }

    @Test
    public void BL_9_5() throws Exception {
        FeedbacksFacade ff = Config.getInstance().getFacadeFactory().
                getFeedbacksFacade();
        List<FeedbackTypeVo> fvs = ff.getFeedbackTypes();
        for (FeedbackTypeVo fv : fvs) {
            FeedbackTypeVo result = entityManager.createQuery(
                    "SELECT p FROM FeedbackType p WHERE p.id =:id", FeedbackTypeEntity.class).
                    setParameter("id", fv.getId()).getSingleResult().toVo();
            assertNotNull(result);
            assertEquals(result.getId(), fv.getId());
            assertEquals(result.getName(), fv.getName());
        }
        int size = entityManager.createQuery("SELECT COUNT(p) FROM FeedbackType p", Long.class).getSingleResult().intValue();
        assertEquals(size, fvs.size());
    }
}
