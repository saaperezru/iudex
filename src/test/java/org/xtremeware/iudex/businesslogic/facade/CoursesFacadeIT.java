/*
 */
package org.xtremeware.iudex.businesslogic.facade;

import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.AssertionFailedError;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.*;

/**
 *
 * @author tuareg
 */
public class CoursesFacadeIT {

    public static ProfessorVoSmall fabio, mario;
    public static SubjectVoSmall is2, isa, afi;
    public static CourseVo marioIs2, marioIsa, marioAfi, fabioIsa;

    public CoursesFacadeIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        FacadesTestHelper.initializeDatabase();

        RatingSummaryVo rating = new RatingSummaryVo();
        rating.setPositive(3);
        rating.setNegative(0);
        fabio = new ProfessorVoSmall(2, "FABIO AUGUSTO GONZALEZ OSORIO", rating);
        rating = new RatingSummaryVo();
        rating.setPositive(3);
        rating.setNegative(1);
        mario = new ProfessorVoSmall(1, "MARIO LINARES VASQUEZ", rating);
        rating = new RatingSummaryVo();
        rating.setPositive(4);
        rating.setNegative(0);
        is2 = new SubjectVoSmall(2016702, "INGENIERIA DE SOFTWARE II",2016702, rating);
        rating = new RatingSummaryVo();
        rating.setPositive(2);
        rating.setNegative(2);
        isa = new SubjectVoSmall(2019772, "INGENIERIA DE SOFTWARE AVANZADA",2019772, rating);
        rating = new RatingSummaryVo();
        rating.setPositive(3);
        rating.setNegative(1);
        afi = new SubjectVoSmall(2016025, "AUDITORIA FINANCIERA I",2016025, rating);

        marioIs2 = new CourseVo();
        marioIs2.setId(1L);
        marioIs2.setPeriodId(1L);
        marioIs2.setProfessorId(mario.getId());
        marioIs2.setSubjectId(is2.getId());
        marioIs2.setRatingAverage(4.3);
        marioIs2.setRatingCount(4L);

        marioIsa = new CourseVo();
        marioIsa.setId(2L);
        marioIsa.setPeriodId(1L);
        marioIsa.setProfessorId(mario.getId());
        marioIsa.setSubjectId(isa.getId());
        marioIsa.setRatingAverage(4.2);
        marioIsa.setRatingCount(4L);

        marioAfi = new CourseVo();
        marioAfi.setId(3L);
        marioAfi.setPeriodId(1L);
        marioAfi.setProfessorId(mario.getId());
        marioAfi.setSubjectId(afi.getId());
        marioAfi.setRatingAverage(3.1);
        marioAfi.setRatingCount(4L);

        fabioIsa = new CourseVo();
        fabioIsa.setId(4L);
        fabioIsa.setPeriodId(1L);
        fabioIsa.setProfessorId(fabio.getId());
        fabioIsa.setSubjectId(isa.getId());
        fabioIsa.setRatingAverage(3.7);
        fabioIsa.setRatingCount(4L);

        Class.forName("org.h2.Driver");


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
     * Test of search method, of class CoursesFacade.
     */
    @Test
    public void test_BL_1_1() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        //TODO: As long as we are still not giving a precise order to the results, i'm not testing the order, just the presence, of the results.
        //First lets look by a subject's name
        String query = "SOFTWARE";
        List<CourseVoFull> search = facade.search(query);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        Set<CourseVoFull> expected = new HashSet<CourseVoFull>();
        expected.add(new CourseVoFull(marioIs2, is2, mario));
        expected.add(new CourseVoFull(marioIsa, isa, mario));
        expected.add(new CourseVoFull(fabioIsa, isa, fabio));
        for (CourseVoFull result : search) {
            if (!expected.contains(result)) {
                throw new AssertionFailedError("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
        query = "sOFTWArE";
        search = facade.search(query);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        for (CourseVoFull result : search) {
            if (!expected.contains(result)) {
                throw new AssertionFailedError("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
        //Now lets look by a professor's name
        query = "maRio";
        search = facade.search(query);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        expected = new HashSet<CourseVoFull>();
        expected.add(new CourseVoFull(marioIs2, is2, mario));
        expected.add(new CourseVoFull(marioIsa, isa, mario));
        expected.add(new CourseVoFull(marioAfi, afi, mario));
        for (CourseVoFull result : search) {
            if (!expected.contains(result)) {
                throw new AssertionFailedError("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
        query = "mario";
        search = facade.search(query);
        assertNotNull(search);
        assertNotSame(search.size(), 0);
        for (CourseVoFull result : search) {
            if (!expected.contains(result)) {
                throw new AssertionFailedError("The following course was not expected for query " + query + ": " + result.toString());
            }
        }
    }

    /**
     * Test of search method when the query has no results
     */
    @Test
    public void test_BL_1_2() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();

        String query = "' OR 1=1--'";
        List<CourseVoFull> search = facade.search(query);
        assertNotNull(search);
        assertEquals(search.size(), 0);
        query = "# DROP DATABASE TEST";
        search = facade.search(query);
        assertNotNull(search);
        assertEquals(search.size(), 0);
        query = "Marios";
        search = facade.search(query);
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
        List<CourseVoFull> search = facade.search(query);
        assertNotNull(search);
        assertEquals(search.size(), 0);
    }

    /**
     * Test of details of course with comments and ratings
     */
    @Test
    public void test_BL_6_1() {
        CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        CourseVoFull expectedCourse = new CourseVoFull(marioIs2, is2, mario);
        long courseId = 1L;
        try {
            CourseVoFull course = facade.getCourse(courseId);
            assertEquals(expectedCourse, course);
        } catch (Exception ex) {
            throw new AssertionFailedError(ex.getMessage());
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
            throw new AssertionFailedError(ex.getMessage());
        }
        courseId = Long.MIN_VALUE;
        try {
            CourseVoFull course = facade.getCourse(courseId);
            assertNull(course);
        } catch (Exception ex) {
            throw new AssertionFailedError(ex.getMessage());
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
            assertEquals(course.getRatingAverage(), 0.0, 0);
            assertEquals(course.getRatingCount(), 0, 0);
        } catch (Exception ex) {
            throw new AssertionFailedError(ex.getMessage());
        }

    }
    /**
     * Test of courses creation success.
     */
//	@Test
//	public void test_BL_4_1() {
//		CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
//		try {
//			CourseVo expected = new CourseVo();
//			expected.setPeriodId(1L);
//			expected.setProfessorId(2L);
//			expected.setSubjectId(1L);
//			expected.setRatingAverage(0.0);
//			expected.setRatingCount(0L);
//
//			facade.addCourse(fabio.getId(), is2.getId(), 1L);
//
//		} catch (InvalidVoException ex) {
//			throw new AssertionFailedError(ex.getMessage());
//		}
//		throw new AssertionFailedError("Unfinished test");
//	}
}
