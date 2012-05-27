package org.xtremeware.iudex.businesslogic.facade;

import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import java.util.*;
import javax.persistence.EntityManager;
import static org.junit.Assert.*;
import org.junit.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.BinaryRatingVo;
import org.xtremeware.iudex.vo.CommentVo;
import org.xtremeware.iudex.vo.CommentVoFull;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class CommentsFacadeIT {

    private EntityManager entityManager;

    public CommentsFacadeIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        FacadesTestHelper.initializeDatabase();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

        entityManager = FacadesTestHelper.createEntityManagerFactory().createEntityManager();

    }

    @After
    public void tearDown() {
        entityManager.clear();
        entityManager.close();
    }

    /**
     * Test of addComment method, of class CommentsFacade.
     */
    @Test
    public void test_BL_5_1() throws
            MultipleMessagesException, MaxCommentsLimitReachedException, DuplicityException {
        CommentVo commentVo = new CommentVo();
        commentVo.setAnonymous(true);
        commentVo.setContent("bueno");
        commentVo.setCourseId(4L);
        commentVo.setDate(new Date(100L));
        commentVo.setRating(3F);
        commentVo.setUserId(2L);
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        CommentVo result = commentsFacade.addComment(commentVo);
        assertNotNull(result);
        assertNotNull(result.getId());
        commentVo.setId(result.getId());
        assertTrue(result.equals(commentVo));
    }

    @Test
    public void test_BL_5_2() throws
            MaxCommentsLimitReachedException, DuplicityException {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        CommentVo result = null;
        try {
            result = commentsFacade.addComment(null);
        } catch (MultipleMessagesException ex) {
            assertEquals("comment.null", ex.getMessages().get(0));
        }
        CommentVo commentVo = new CommentVo();
        commentVo.setAnonymous(true);
        commentVo.setContent(null);
        commentVo.setCourseId(null);
        commentVo.setDate(null);
        commentVo.setRating(null);
        commentVo.setUserId(null);
        String[] expectedMessages = new String[]{
            "comment.content.null",
            "comment.courseId.null",
            "comment.rating.null",
            "comment.userId.null",
            "comment.date.null"};
        try {
            result = commentsFacade.addComment(commentVo);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        commentVo.setCourseId(10L);
        commentVo.setDate(new Date());
        commentVo.setRating(Float.MAX_VALUE);
        commentVo.setUserId(100L);
        commentVo.setContent(FacadesTestHelper.randomString(2001));
        expectedMessages = new String[]{
            "comment.rating.invalidRating",
            "comment.courseId.element.notFound",
            "comment.userId.element.notFound",
            "comment.content.invalidSize"};
        try {
            result = commentsFacade.addComment(commentVo);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        commentVo.setCourseId(Long.MAX_VALUE);
        commentVo.setUserId(Long.MAX_VALUE);
        commentVo.setRating(Float.POSITIVE_INFINITY);
        commentVo.setContent("");
        expectedMessages = new String[]{
            "comment.rating.invalidRating",
            "comment.courseId.element.notFound",
            "comment.userId.element.notFound",
            "comment.content.invalidSize"};
        try {
            result = commentsFacade.addComment(commentVo);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        commentVo.setCourseId(Long.MIN_VALUE);
        commentVo.setUserId(Long.MIN_VALUE);
        commentVo.setRating(Float.NEGATIVE_INFINITY);
        commentVo.setContent("");
        expectedMessages = new String[]{
            "comment.rating.invalidRating",
            "comment.courseId.element.notFound",
            "comment.userId.element.notFound",
            "comment.content.invalidSize"};
        try {
            result = commentsFacade.addComment(commentVo);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    @Test
    public void test_BL_5_3() throws MultipleMessagesException,
            MaxCommentsLimitReachedException,
            DuplicityException {
        CommentVo commentVo = new CommentVo();
        commentVo.setAnonymous(true);
        commentVo.setContent("bueno");
        commentVo.setCourseId(4L);
        Calendar calendar = Calendar.getInstance();
        commentVo.setDate(calendar.getTime());
        commentVo.setRating(3F);
        commentVo.setUserId(1L);
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        CommentVo result = commentsFacade.addComment(commentVo);


        commentVo.setAnonymous(false);
        commentVo.setContent("bueno");
        commentVo.setCourseId(3L);
        commentVo.setDate(calendar.getTime());
        commentVo.setRating(2F);
        commentVo.setUserId(1L);
        try {
            result = commentsFacade.addComment(commentVo);
        } catch (MaxCommentsLimitReachedException e) {
            assertEquals("Maximum comments per day reached", e.getMessage());
        }

    }

    @Test
    public void test_BL_7_1() throws MultipleMessagesException, Exception {
        Long commmendId = 2L;
        Long userId = 2L;
        int value = 1;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        BinaryRatingVo commentRatingVo = commentsFacade.rateComment(commmendId,
                userId, value);
        assertNotNull(commentRatingVo);
        assertNotNull(commentRatingVo.getId());
        assertEquals(value, commentRatingVo.getValue());
        assertEquals(commmendId, commentRatingVo.getEvaluatedObjectId());
        assertEquals(userId, commentRatingVo.getUserId());
        int size = entityManager.createQuery(
                "SELECT COUNT(c) FROM CommentRating c WHERE c.id = :id",
                Long.class).
                setParameter("id", commentRatingVo.getId()).getSingleResult().
                intValue();
        assertEquals(1, size);
    }

    @Test
    public void test_BL_7_2() throws MultipleMessagesException, Exception {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();

        Long commmendId = Long.MAX_VALUE;
        Long userId = Long.MAX_VALUE;
        int value = Integer.MAX_VALUE;
        BinaryRatingVo commentRatingVo = null;

        String[] expectedMessages = new String[]{
            "commentRating.commentId.element.notFound",
            "commentRating.value.invalidValue",
            "commentRating.userId.element.notFound"
        };
        try {
            commentRatingVo = commentsFacade.rateComment(
                    commmendId, userId, value);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        commmendId = Long.MAX_VALUE;
        userId = Long.MAX_VALUE;
        value = Integer.MAX_VALUE;

        try {
            commentRatingVo = commentsFacade.rateComment(
                    commmendId, userId, value);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        commmendId = 0L;
        userId = 0L;
        value = 0;
        expectedMessages = new String[]{
            "commentRating.commentId.element.notFound",
            "commentRating.userId.element.notFound"
        };
        try {
            commentRatingVo = commentsFacade.rateComment(
                    commmendId, userId, value);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    @Test
    public void test_BL_7_3() throws MultipleMessagesException, Exception {
        Long commmendId = 2L;
        Long userId = 1L;
        int value = 1;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        BinaryRatingVo commentRatingVo = commentsFacade.rateComment(commmendId,
                userId, value);
        assertNotNull(commentRatingVo);
        assertEquals(value, commentRatingVo.getValue());
        assertEquals(commmendId, commentRatingVo.getEvaluatedObjectId());
        assertEquals(userId, commentRatingVo.getUserId());
        value = -1;
        BinaryRatingVo result = commentsFacade.rateComment(commmendId, userId,
                value);
        assertEquals(commentRatingVo.getId(), result.getId());
        assertEquals(commentRatingVo.getUserId(), result.getUserId());
        assertEquals(commentRatingVo.getEvaluatedObjectId(), result.getEvaluatedObjectId());
        assertEquals(value, result.getValue());
    }

    @Test
    public void test_BL_7_4() throws MultipleMessagesException, Exception {
        Long commmendId = 2L;
        Long userId = 3L;
        int value = 1;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        BinaryRatingVo commentRatingVo = commentsFacade.rateComment(commmendId,
                userId, value);
        assertNotNull(commentRatingVo);
        assertEquals(value, commentRatingVo.getValue());
        assertEquals(commmendId, commentRatingVo.getEvaluatedObjectId());
        assertEquals(userId, commentRatingVo.getUserId());
        value = 1;
        BinaryRatingVo result = commentsFacade.rateComment(commmendId, userId,
                value);
        assertEquals(commentRatingVo.getId(), result.getId());
        assertEquals(commentRatingVo.getUserId(), result.getUserId());
        assertEquals(commentRatingVo.getEvaluatedObjectId(), result.getEvaluatedObjectId());
        assertEquals(commentRatingVo.getValue(), result.getValue());
    }

    @Test
    public void test_BL_22_1() {
        Long commmendId = 1L;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        RatingSummaryVo commentRatingSummary = commentsFacade.getCommentRatingSummary(commmendId);
        assertEquals(2, commentRatingSummary.getNegative());
        assertEquals(2, commentRatingSummary.getPositive());

        commmendId = 10L;
        commentRatingSummary =
                commentsFacade.getCommentRatingSummary(commmendId);
        assertEquals(0, commentRatingSummary.getNegative());
        assertEquals(0, commentRatingSummary.getPositive());
    }

    @Test
    public void test_BL_22_2() {
        Long commmendId = 10L;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        RatingSummaryVo commentRatingSummary = commentsFacade.getCommentRatingSummary(commmendId);
        assertEquals(0, commentRatingSummary.getNegative());
        assertEquals(0, commentRatingSummary.getPositive());
    }

    @Test
    public void test_BL_23_1() throws DataBaseException, Exception {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        Long id = 1L;
        List<CommentVoFull> commentsByCourseId = commentsFacade.getCommentsByCourseId(id);
        int size = entityManager.createQuery(
                "SELECT COUNT(c) FROM Comment c WHERE c.course.id = :id",
                Long.class).
                setParameter("id", id).getSingleResult().intValue();
        assertEquals(3, size);
        for (CommentVoFull cvvf : commentsByCourseId) {
            CommentVo result = entityManager.createQuery(
                    "SELECT c FROM Comment c WHERE c.id = :id",
                    CommentEntity.class).
                    setParameter("id", cvvf.getId()).getSingleResult().toVo();
            if (cvvf.isAnonymous()) {
                assertNull(cvvf.getUser());
            } else {
                assertNotNull(cvvf.getUser());
                assertEquals(result.getUserId(), cvvf.getUser().getId());
            }
            assertEquals(result.getContent(), cvvf.getContent());
            assertEquals(result.getId(), cvvf.getId());
            assertEquals(result.getRating(), cvvf.getCourseRating());
        }

    }

    @Test
    public void test_BL_23_2() throws DataBaseException, Exception {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        Long id = 0L;
        List<CommentVoFull> commentsByCourseId = commentsFacade.
                getCommentsByCourseId(id);
        assertEquals(0, commentsByCourseId.size());

        id = Long.MAX_VALUE;
        commentsByCourseId = commentsFacade.getCommentsByCourseId(id);
        assertEquals(0, commentsByCourseId.size());

        id = Long.MIN_VALUE;
        commentsByCourseId = commentsFacade.getCommentsByCourseId(id);
        assertEquals(0, commentsByCourseId.size());
    }

    @Test
    public void test_BL_24_1() throws DataBaseException {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        Long userId = 1L;
        Long commentId = 1L;
        BinaryRatingVo commentRatingByUserId = commentsFacade.getCommentRatingByUserId(commentId, userId);
        assertNotNull(commentRatingByUserId);
        assertEquals(commentId, commentRatingByUserId.getEvaluatedObjectId());
        assertEquals(userId, commentRatingByUserId.getUserId());
        int value =
                entityManager.createQuery(
                "SELECT c.value FROM CommentRating c WHERE c.comment.id = :commentId AND c.user.id = :userId",
                Integer.class).
                setParameter("commentId", commentId).setParameter("userId",
                userId).getSingleResult();
        assertEquals(value, commentRatingByUserId.getValue());

    }

    @Test
    public void test_BL_24_2() throws DataBaseException {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        Long userId = 1L;
        Long commentId = 10L;
        BinaryRatingVo commentRatingByUserId = commentsFacade.getCommentRatingByUserId(commentId, userId);
        assertNull(commentRatingByUserId);

        userId = 0L;
        commentId = 1L;
        commentRatingByUserId = commentsFacade.getCommentRatingByUserId(
                commentId, userId);
        assertNull(commentRatingByUserId);

        userId = Long.MAX_VALUE;
        commentId = Long.MAX_VALUE;
        commentRatingByUserId = commentsFacade.getCommentRatingByUserId(
                commentId, userId);
        assertNull(commentRatingByUserId);

        userId = Long.MIN_VALUE;
        commentId = Long.MIN_VALUE;
        commentRatingByUserId = commentsFacade.getCommentRatingByUserId(
                commentId, userId);
        assertNull(commentRatingByUserId);

    }

    @Test
    public void test_BL_25_1() throws Exception {
        Long commmendId = 1L;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        commentsFacade.removeComment(commmendId);
        Long singleResult = entityManager.createQuery(
                "SELECT COUNT(c) FROM Comment c WHERE c.id = :commmendId",
                Long.class).setParameter("commmendId", commmendId).
                getSingleResult();
        assertEquals(0, singleResult.intValue());
        singleResult =
                entityManager.createQuery(
                "SELECT COUNT(c) FROM CommentRating c WHERE c.comment.id = :commmendId",
                Long.class).setParameter("commmendId", commmendId).
                getSingleResult();
        assertEquals(0, singleResult.intValue());
    }

    @Test
    public void test_BL_25_2() throws Exception {
        Long commmendId = 1L;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        try {
            commentsFacade.removeComment(commmendId);
        } catch (Exception ex) {
            assertEquals(RuntimeException.class, ex.getClass());
            assertEquals("entity.notFound", ex.getCause().getMessage());
            Long singleResult = entityManager.createQuery(
                    "SELECT COUNT(c) FROM Comment c WHERE c.id = :commmendId",
                    Long.class).setParameter("commmendId", commmendId).
                    getSingleResult();
            assertEquals(0, singleResult.intValue());
            singleResult =
                    entityManager.createQuery(
                    "SELECT COUNT(c) FROM CommentRating c WHERE c.comment.id = :commmendId",
                    Long.class).setParameter("commmendId", commmendId).
                    getSingleResult();
            assertEquals(0, singleResult.intValue());
        }
        commmendId = Long.MIN_VALUE;
        try {
            commentsFacade.removeComment(commmendId);
        } catch (Exception ex) {
            assertEquals(RuntimeException.class, ex.getClass());
            assertEquals("entity.notFound", ex.getCause().getMessage());
            Long singleResult = entityManager.createQuery(
                    "SELECT COUNT(c) FROM Comment c WHERE c.id = :commmendId",
                    Long.class).setParameter("commmendId", commmendId).
                    getSingleResult();
            assertEquals(0, singleResult.intValue());
            singleResult =
                    entityManager.createQuery(
                    "SELECT COUNT(c) FROM CommentRating c WHERE c.comment.id = :commmendId",
                    Long.class).setParameter("commmendId", commmendId).
                    getSingleResult();
            assertEquals(0, singleResult.intValue());
        }

        commmendId = Long.MAX_VALUE;
        try {
            commentsFacade.removeComment(commmendId);
        } catch (Exception ex) {
            assertEquals(RuntimeException.class, ex.getClass());
            assertEquals("entity.notFound", ex.getCause().getMessage());
            Long singleResult = entityManager.createQuery(
                    "SELECT COUNT(c) FROM Comment c WHERE c.id = :commmendId",
                    Long.class).setParameter("commmendId", commmendId).
                    getSingleResult();
            assertEquals(0, singleResult.intValue());
            singleResult =
                    entityManager.createQuery(
                    "SELECT COUNT(c) FROM CommentRating c WHERE c.comment.id = :commmendId",
                    Long.class).setParameter("commmendId", commmendId).
                    getSingleResult();
            assertEquals(0, singleResult.intValue());
        }
    }
}
