package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import static org.junit.Assert.*;
import org.junit.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.*;

/**
 *
 * @author tuareg
 */
public class CoursesFacadeIT {

    public static ProfessorVoFull fabio, mario;
    public static SubjectVoFull is2, isa, afi;
    public static CourseVo marioIs2, marioIsa, marioAfi, fabioIsa;
    private static EntityManager entityManager;

    @Before
    public void setUp() {
        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        FacadesTestHelper.initializeDatabase();
        Class.forName("org.h2.Driver");
        entityManager = FacadesTestHelper.createEntityManagerFactory().createEntityManager();

        RatingSummaryVo rating = new RatingSummaryVo();
        rating.setPositive(3);
        rating.setNegative(0);

        ProfessorVo professorVo = entityManager.createQuery(
                "SELECT p FROM Professor p WHERE p.id =:id", ProfessorEntity.class).
                setParameter("id", 2L).getSingleResult().toVo();
        fabio = new ProfessorVoFull(professorVo, rating);
        rating = new RatingSummaryVo();
        rating.setPositive(3);
        rating.setNegative(1);
        professorVo = entityManager.createQuery(
                "SELECT p FROM Professor p WHERE p.id =:id", ProfessorEntity.class).
                setParameter("id", 1L).getSingleResult().toVo();
        mario = new ProfessorVoFull(professorVo, rating);
        rating = new RatingSummaryVo();
        rating.setPositive(4);
        rating.setNegative(0);
        SubjectVo subjectVo = entityManager.createQuery(
                "SELECT p FROM Subject p WHERE p.id =:id", SubjectEntity.class).
                setParameter("id", 2016702L).getSingleResult().toVo();
        is2 = new SubjectVoFull(subjectVo, rating);
        rating = new RatingSummaryVo();
        rating.setPositive(2);
        rating.setNegative(2);
        subjectVo = entityManager.createQuery(
                "SELECT p FROM Subject p WHERE p.id =:id", SubjectEntity.class).
                setParameter("id", 2019772L).getSingleResult().toVo();
        isa = new SubjectVoFull(subjectVo, rating);
        rating = new RatingSummaryVo();
        rating.setPositive(3);
        rating.setNegative(1);
        subjectVo = entityManager.createQuery(
                "SELECT p FROM Subject p WHERE p.id =:id", SubjectEntity.class).
                setParameter("id", 2016025L).getSingleResult().toVo();
        afi = new SubjectVoFull(subjectVo, rating);

        marioIs2 = new CourseVo();
        marioIs2.setId(1L);
        marioIs2.setPeriodId(1L);
        marioIs2.setProfessorId(mario.getVo().getId());
        marioIs2.setSubjectId(is2.getVo().getId());
        marioIs2.setRatingAverage(4.3);
        marioIs2.setRatingCount(4L);

        marioIsa = new CourseVo();
        marioIsa.setId(2L);
        marioIsa.setPeriodId(1L);
        marioIsa.setProfessorId(mario.getVo().getId());
        marioIsa.setSubjectId(isa.getVo().getId());
        marioIsa.setRatingAverage(4.2);
        marioIsa.setRatingCount(4L);

        marioAfi = new CourseVo();
        marioAfi.setId(3L);
        marioAfi.setPeriodId(1L);
        marioAfi.setProfessorId(mario.getVo().getId());
        marioAfi.setSubjectId(afi.getVo().getId());
        marioAfi.setRatingAverage(3.1);
        marioAfi.setRatingCount(4L);

        fabioIsa = new CourseVo();
        fabioIsa.setId(4L);
        fabioIsa.setPeriodId(1L);
        fabioIsa.setProfessorId(fabio.getVo().getId());
        fabioIsa.setSubjectId(isa.getVo().getId());
        fabioIsa.setRatingAverage(3.7);
        fabioIsa.setRatingCount(4L);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of search method, of class CoursesFacade.
     */
    @Test
    public void test_BL_1_1() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        //TODO: As long as we are still not giving a precise order to the results, i'm not testing the order, just the presence, of the results.
        //First lets look by a subject's name
        String query = "SOFTWARE";
        List<Long> search = facade.search(query,10);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        Set<Long> expected = new HashSet<Long>();
        expected.add(marioIs2.getId());
        expected.add(marioIsa.getId());
        expected.add(fabioIsa.getId());
        for (Long result : search) {
            if (!expected.contains(result)) {
                fail("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
        query = "sOFTWArE";
        search = facade.search(query,10);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        for (Long result : search) {
            if (!expected.contains(result)) {
                fail("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
        //Now lets look by a professor's name
        query = "maRio";
        search = facade.search(query,10);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        expected = new HashSet<Long>();
        expected.add(marioIs2.getId());
        expected.add(marioIsa.getId());
        expected.add(marioAfi.getId());
        for (Long result : search) {
            if (!expected.contains(result)) {
                fail("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
        query = "linares";
        search = facade.search(query,10);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        for (Long result : search) {
            if (!expected.contains(result)) {
                fail("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
    }

    /**
     * Test of search method when the query has no results
     */
	//icial releases are usually created when the developers feel there are sufficient changes, improvements an
    @Test
    public void test_BL_1_2() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();

        String query = "' OR 1=1--'";
        List<Long> search = facade.search(query,10);
        assertNotNull(search);
        assertEquals(search.size(), 0);
        query = "# DROP DATABASE TEST";
        search = facade.search(query,10);
        assertNotNull(search);
        assertEquals(search.size(), 0);
        query = "Marios";
        search = facade.search(query,10);
        assertNotNull(search);
        assertEquals(search.size(), 3);
        search = facade.search(null,10);
        assertNotNull(search);
        assertEquals(search.size(), 0);
    }

    /**
     * Test of search method when the query is empty
     */
    @Test
    public void test_BL_1_3() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        String query = "";
        List<Long> search = facade.search(query,10);
        assertNotNull(search);
        assertEquals(search.size(), 0);
    }

    /**
     * Test of details of course with comments and ratings
     */
    @Test
    public void test_BL_6_1() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        CourseVoFull expectedCourse = new CourseVoFull(marioIs2, is2.getVo(), mario.getVo());
        long courseId = 1L;
        try {
            CourseVoFull course = facade.getCourse(courseId);
            assertEquals(expectedCourse, course);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

    /**
     * Test of details of course that doesn't exists
     */
    @Test
    public void test_BL_6_2() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        long courseId = Long.MAX_VALUE;
        try {
            CourseVoFull course = facade.getCourse(courseId);
            assertNull(course);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        courseId = Long.MIN_VALUE;
        try {
            CourseVoFull course = facade.getCourse(courseId);
            assertNull(course);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

    /**
     * Test of details of course that doesn't have comments and ratings
     */
    @Test
    public void test_BL_6_3() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        long courseId = 5L;
        try {
            CourseVoFull course = facade.getCourse(courseId);
            assertEquals(course.getVo().getRatingAverage(), 0.0, 0);
            assertEquals(course.getVo().getRatingCount(), 0, 0);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

    /**
     * Test of courses creation success.
     */
    @Test
    public void test_BL_4_1() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        try {
            CourseVo expected = new CourseVo();
            expected.setPeriodId(1L);
            expected.setProfessorId(fabio.getVo().getId());
            expected.setSubjectId(is2.getVo().getId());
            expected.setRatingAverage(0.0);
            expected.setRatingCount(0L);

            CourseVo result = facade.createCourse(fabio.getVo().getId(), is2.getVo().getId(), 1L);
            expected.setId(result.getId());
            assertEquals(expected, result);
            assertTrue("Invalid id for created course", result.getId() > 0);

            CourseVo actual = ((CourseEntity) entityManager.createQuery("SELECT c FROM Course c WHERE c.id= :id").setParameter("id", result.getId()).getSingleResult()).toVo();
            assertEquals(expected, actual);

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of courses creation failure because of invalid data.
     */
    @Test
    public void test_BL_4_2() throws Exception {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        String[] expectedMessage = new String[]{"course.professorId.notFound", "course.subjectId.notFound", "course.periodId.notFound"};
        try {
            CourseVo result = facade.createCourse(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessage);
        }
        expectedMessage = new String[]{"course.periodId.notFound"};
        try {
            CourseVo result = facade.createCourse(fabio.getVo().getId(), afi.getVo().getId(), Long.MAX_VALUE);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessage);
        }

    }

    /**
     * Test of courses creation failure because of a repeated record.
     */
    @Test
    public void test_BL_4_3() throws Exception {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        try {
            facade.createCourse(mario.getVo().getId(), is2.getVo().getId(), 1L);
            fail("No exception was thrown for a duplicity case");
        } catch (DuplicityException ex) {
            assertEquals("entity.exists", ex.getMessage());
        }
    }

    @Test
    public void test_BL_19_1() throws Exception {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        long listTodelete[] = {3L, 4L};
        for (long i : listTodelete) {
            facade.deleteCourse(i);
            assertNull(facade.getCourse(i));
        }

    }

    @Test
    public void test_BL_19_2() throws Exception {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        long listTodelete[] = {Long.MAX_VALUE, Long.MIN_VALUE};
        for (long i : listTodelete) {
            try {
                facade.deleteCourse(i);
            } catch (Exception e) {
                assertEquals(DataBaseException.class, e.getClass());
                assertEquals("entity.notFound", e.getMessage());
            }
        }

    }

    @Test
    public void getByProfessorIdTest() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();

        HashSet<CourseVoFull> expected = new HashSet<CourseVoFull>();
        expected.add(new CourseVoFull(marioIs2, is2.getVo(), mario.getVo()));
        expected.add(new CourseVoFull(marioIsa, isa.getVo(), mario.getVo()));
        expected.add(new CourseVoFull(marioAfi, afi.getVo(), mario.getVo()));

        List<CourseVoFull> search = facade.getByProfessorId(mario.getVo().getId());
        for (CourseVoFull result : search) {
            if (!expected.contains(result)) {
                fail("The following course was not expected for the professor Id " + mario.getVo().getId() + ": " + result.toString());
            }
        }
    }

    @Test
    public void getBySubjectId() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();

        HashSet<CourseVoFull> expected = new HashSet<CourseVoFull>();
        expected.add(new CourseVoFull(marioIsa, isa.getVo(), mario.getVo()));
        expected.add(new CourseVoFull(fabioIsa, isa.getVo(), fabio.getVo()));

        List<CourseVoFull> search = facade.getBySubjectId(isa.getVo().getId());
        for (CourseVoFull result : search) {
            if (!expected.contains(result)) {
                fail("The following course was not expected for the professor Id " + mario.getVo().getId() + ": " + result.toString());
            }
        }
    }

    @Test
    public void rateComment() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        //Rate a non-existing course and a non-existing user, with an invalid rating value
        rateCommentInvalidInputry(facade, Long.MIN_VALUE, Long.MIN_VALUE, -0.1f, new String[]{"courseRating.courseId.notFound", "courseRating.userId.notFound", "courseRating.value.invalidRange"});
        //Rate a existing course and a user, with an invalid rating value
        rateCommentInvalidInputry(facade, 5L, 1L, -0.1f, new String[]{"courseRating.value.invalidRange"});

        //Rate a valid course, with valid user and valid value
        try {
            CourseRatingVo result = facade.rateCourse(5L, 1L, 0.5f);
            CourseRatingVo expected = facade.getCourseRatingByUserId(5L, 1L);
            assertEquals(expected, result);
        } catch (Exception ex) {
            fail("An unexpected exception ocurred");
        }

        //Edit an existing rating
        try {
            CourseRatingVo result = facade.rateCourse(5L, 1L, 1.5f);
            CourseRatingVo expected = facade.getCourseRatingByUserId(5L, 1L);
            assertEquals(expected, result);
        } catch (Exception ex) {
            fail("An unexpected exception ocurred");
        }


    }

    private void rateCommentInvalidInputry(CoursesFacade facade, Long courseId, Long userId, float value, String[] expectedMessages) {
        try {
            facade.rateCourse(courseId, userId, value);
            fail("A non-existing course was rated");
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        } catch (Exception ex) {
            fail("An unexpected exception ocurred");
        }
    }
}
