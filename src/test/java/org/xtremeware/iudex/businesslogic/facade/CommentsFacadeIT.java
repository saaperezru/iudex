package org.xtremeware.iudex.businesslogic.facade;

import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.junit.Assert.*;
import org.junit.*;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.MaxCommentsLimitReachedException;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.vo.CommentRatingVo;
import org.xtremeware.iudex.vo.CommentVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class CommentsFacadeIT {

    private Set<String> exceptionMessageComment;
    private Set<String> exceptionMessageCommentR;
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

        EntityManagerFactory entityManagerFactory = Persistence.
                createEntityManagerFactory("org.xtremeware.iudex_local");
        entityManager = entityManagerFactory.createEntityManager();

        exceptionMessageComment = new TreeSet<String>();
        exceptionMessageComment.add("Null CommentVo");
        exceptionMessageComment.add(
                "String Content in the provided CommentVo cannot be null");
        exceptionMessageComment.add(
                "Long CourseId in the provided CommentVo cannot be null");
        exceptionMessageComment.add(
                "Long CourseID in the provided CommentVo does not have matches with existent courses");
        exceptionMessageComment.add(
                "Date Date in the provided CommentVo cannot be null");
        exceptionMessageComment.add(
                "Long UserId in the provided CommentVo cannot be null");
        exceptionMessageComment.add(
                "Long UserId in the provided CommentVo does not have matches with existent users");
        exceptionMessageComment.add(
                "Float Rating in the provided CommentVo cannot be null");
        exceptionMessageComment.add(
                "Float Rating in the provided CommentVo must be greater or equal than 0.0 and less or equal than 5.0");
        exceptionMessageComment.add(
                "String Content length in the provided CommentVo must be grater or equal than 1 and less or equal than 20");
        exceptionMessageComment.add("Maximum comments per day reached");

        exceptionMessageCommentR = new TreeSet<String>();
        exceptionMessageCommentR.add("Null CommentRatingVo");
        exceptionMessageCommentR.add(
                "Null commentId in the provided CourseRatingVo");
        exceptionMessageCommentR.add(
                "No such comment associeted with CommentRatingVo.commentId");
        exceptionMessageCommentR.add(
                "Null userId in the provided CommentRatingVo");
        exceptionMessageCommentR.add(
                "No such user associated with CommentRatingVo.userId");
        exceptionMessageCommentR.add(
                "int Value in the provided CommentRatingVo " +
                
                "must be less than or equal to 1 and greater than or equal to -1");

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addComment method, of class CommentsFacade.
     */
    @Test
    public void test_BL_5_1() throws InvalidVoException,
            MultipleMessagesException, MaxCommentsLimitReachedException {
        CommentVo commentVo = new CommentVo();
        commentVo.setAnonymous(true);
        commentVo.setContent("bueno");
        commentVo.setCourseId(4L);
        commentVo.setDate(new Date(100L));
        commentVo.setRating(3F);
        commentVo.setUserId(1L);
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        CommentVo result = commentsFacade.addComment(commentVo);
        assertNotNull(result);
        assertNotNull(result.getId());
        commentVo.setId(result.getId());
        assertTrue(result.equals(commentVo));
    }

    @Test
    public void test_BL_5_2_1() throws MultipleMessagesException,
            MaxCommentsLimitReachedException {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        try {
            CommentVo result = commentsFacade.addComment(null);
        } catch (MultipleMessagesException ex) {
            assertEquals("Null CommentVo", ex.getMessages().get(0));
        }
        CommentVo commentVo = new CommentVo();
        commentVo.setAnonymous(true);
        commentVo.setContent(null);
        commentVo.setCourseId(null);
        commentVo.setDate(null);
        commentVo.setRating(null);
        commentVo.setUserId(null);
        try {
            CommentVo result = commentsFacade.addComment(commentVo);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessageComment.contains(message));
            }
        }
        commentVo.setContent(
                "Entrada muy larga, muy muy largaEntrada muy larga, muy muy larga");
        commentVo.setCourseId(10L);
        commentVo.setDate(new Date());
        commentVo.setRating(Float.MAX_VALUE);
        commentVo.setUserId(6L);
        try {
            CommentVo result = commentsFacade.addComment(commentVo);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessageComment.contains(message));
            }
        }
        commentVo.setCourseId(2L);
        commentVo.setUserId(1L);
        commentVo.setRating(Float.MIN_VALUE);
        commentVo.setContent("");
        try {
            CommentVo result = commentsFacade.addComment(commentVo);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessageComment.contains(message));
            }
        }
    }

    @Test
    public void test_BL_5_3() throws MultipleMessagesException,
            MaxCommentsLimitReachedException {
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
    public void test_BL_7_1() throws MultipleMessagesException {
        Long commmendId = 2L;
        Long userId = 2L;
        int value = 1;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        CommentRatingVo commentRatingVo = commentsFacade.rateComment(commmendId,
                userId, value);
        assertNotNull(commentRatingVo);
        assertNotNull(commentRatingVo.getId());
        assertEquals(value, commentRatingVo.getValue());
        assertEquals(commmendId, commentRatingVo.getEvaluetedObjectId());
        assertEquals(userId, commentRatingVo.getUser());
        int size = entityManager.createQuery(
                "SELECT COUNT(c) FROM CommentRating c WHERE c.id = :id",
                Long.class).
                setParameter("id", commentRatingVo.getId()).getSingleResult().
                intValue();
        assertEquals(1, size);
    }

    @Test
    public void test_BL_7_2() throws MultipleMessagesException {
        Long commmendId = 5L;
        Long userId = 1L;
        int value = 1;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        try {
            CommentRatingVo commentRatingVo = commentsFacade.rateComment(
                    commmendId, userId, value);
        } catch (MultipleMessagesException ex) {
            for (String e : ex.getMessages()) {
                assertTrue(exceptionMessageCommentR.contains(e));
            }
        }
    }

    @Test
    public void test_BL_7_3() throws MultipleMessagesException {
        Long commmendId = 2L;
        Long userId = 1L;
        int value = 1;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        CommentRatingVo commentRatingVo = commentsFacade.rateComment(commmendId,
                userId, value);
        assertNotNull(commentRatingVo);
        assertEquals(value, commentRatingVo.getValue());
        assertEquals(commmendId, commentRatingVo.getEvaluetedObjectId());
        assertEquals(userId, commentRatingVo.getUser());
        value = -1;
        CommentRatingVo result = commentsFacade.rateComment(commmendId, userId,
                value);
        assertEquals(commentRatingVo.getId(), result.getId());
        assertEquals(commentRatingVo.getUser(), result.getUser());
        assertEquals(commentRatingVo.getEvaluetedObjectId(), result.
                getEvaluetedObjectId());
        assertEquals(value, result.getValue());
    }

    @Test
    public void test_BL_7_4() throws MultipleMessagesException {
        Long commmendId = 2L;
        Long userId = 3L;
        int value = 1;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        CommentRatingVo commentRatingVo = commentsFacade.rateComment(commmendId,
                userId, value);
        assertNotNull(commentRatingVo);
        assertEquals(value, commentRatingVo.getValue());
        assertEquals(commmendId, commentRatingVo.getEvaluetedObjectId());
        assertEquals(userId, commentRatingVo.getUser());
        value = 1;
        CommentRatingVo result = commentsFacade.rateComment(commmendId, userId,
                value);
        assertEquals(commentRatingVo.getId(), result.getId());
        assertEquals(commentRatingVo.getUser(), result.getUser());
        assertEquals(commentRatingVo.getEvaluetedObjectId(), result.
                getEvaluetedObjectId());
        assertEquals(commentRatingVo.getValue(), result.getValue());
    }

    @Test
    public void test_BL_22_1() {
        Long commmendId = 1L;
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        RatingSummaryVo commentRatingSummary = commentsFacade.
                getCommentRatingSummary(commmendId);
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
        RatingSummaryVo commentRatingSummary = commentsFacade.
                getCommentRatingSummary(commmendId);
        assertEquals(0, commentRatingSummary.getNegative());
        assertEquals(0, commentRatingSummary.getPositive());
    }

    @Test
    public void test_BL_23_1() throws DataBaseException {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        Long id = 1L;
        List<CommentVoVwFull> commentsByCourseId = commentsFacade.
                getCommentsByCourseId(id);
        int size = entityManager.createQuery(
                "SELECT COUNT(c) FROM Comment c WHERE c.course.id = :id",
                Long.class).
                setParameter("id", id).getSingleResult().intValue();
        assertEquals(2, size);
        for (CommentVoVwFull cvvf : commentsByCourseId) {
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
            assertEquals(result.getContent(),cvvf.getContent());
            assertEquals(result.getId(),cvvf.getId());
            assertEquals(result.getRating(),cvvf.getCourseRating());
        }

    }

    @Test
    public void test_BL_23_2() throws DataBaseException {
        CommentsFacade commentsFacade = Config.getInstance().getFacadeFactory().
                getCommentsFacade();
        Long id = 0L;
        List<CommentVoVwFull> commentsByCourseId = commentsFacade.
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
        CommentRatingVo commentRatingByUserId = commentsFacade.
                getCommentRatingByUserId(commentId, userId);
        assertNotNull(commentRatingByUserId);
        assertEquals(commentId, commentRatingByUserId.getEvaluetedObjectId());
        assertEquals(userId, commentRatingByUserId.getUser());
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
        CommentRatingVo commentRatingByUserId = commentsFacade.
                getCommentRatingByUserId(commentId, userId);
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
            assertEquals(DataBaseException.class, ex.getClass());
            assertEquals("No entity found for id " + String.valueOf(commmendId) +
                    "while triying to delete the associated record", ex.
                    getMessage());
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
            assertEquals(DataBaseException.class, ex.getClass());
            assertEquals("No entity found for id " + String.valueOf(commmendId) +
                    "while triying to delete the associated record", ex.
                    getMessage());
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
            assertEquals(DataBaseException.class, ex.getClass());
            assertEquals("No entity found for id " + String.valueOf(commmendId) +
                    "while triying to delete the associated record", ex.
                    getMessage());
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
