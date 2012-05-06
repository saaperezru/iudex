/*
 */
package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import junit.framework.AssertionFailedError;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwSmall;
import org.xtremeware.iudex.vo.CourseRatingVo;
import org.xtremeware.iudex.vo.CourseVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author tuareg
 */
public class CoursesFacadeIT {

	private CoursesFacade facade;
	public static ProfessorVoVwSmall fabio, mario;
	public static SubjectVoVwSmall is2, isa, afi;
	public static CourseVo marioIs2, marioIsa, marioAfi, fabioIsa;

	public CoursesFacadeIT() {
		facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		RatingSummaryVo rating = new RatingSummaryVo();
		rating.setPositive(3);
		rating.setNegative(0);
		fabio = new ProfessorVoVwSmall(2, "FABIO AUGUSTO GONZALEZ OSORIO", rating);
		rating = new RatingSummaryVo();
		rating.setPositive(3);
		rating.setNegative(1);
		mario = new ProfessorVoVwSmall(1, "MARIO LINARES VASQUEZ", rating);
		rating = new RatingSummaryVo();
		rating.setPositive(4);
		rating.setNegative(0);
		is2 = new SubjectVoVwSmall(2016702, "INGENIERIA DE SOFTWARE II", rating);
		rating = new RatingSummaryVo();
		rating.setPositive(2);
		rating.setNegative(2);
		isa = new SubjectVoVwSmall(2019772, "INGENIERIA DE SOFTWARE AVANZADA", rating);
		rating = new RatingSummaryVo();
		rating.setPositive(4);
		rating.setNegative(0);
		afi = new SubjectVoVwSmall(2016025, "AUDITORIA FINANCIERA I", rating);

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
		fabioIsa.setSubjectId(afi.getId());
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
		System.out.println(Config.getInstance().getPersistenceUnit());
		EntityManager em = Persistence.createEntityManagerFactory(Config.getInstance().getPersistenceUnit()).createEntityManager();
		List resultList = em.createQuery("SELECT p FROM Professor p WHERE p.id=1").getResultList();
		System.out.println(resultList.size());
		//TODO: As long as we are still not giving a precise order to the results, i'm not testing the order, just the presence, of the results.
		//First lets look by a subject's name
		String query = "SOFTWARE";
		List<CourseVoVwFull> search = facade.search(query);
		assertNotNull(search);
		assertNotSame(search.size(), 0);
		Set<CourseVoVwFull> expected = new HashSet<CourseVoVwFull>();
		expected.add(new CourseVoVwFull(marioIs2, is2, mario));
		expected.add(new CourseVoVwFull(marioIsa, isa, mario));
		expected.add(new CourseVoVwFull(fabioIsa, isa, fabio));
		for (CourseVoVwFull result : search) {
			System.out.println(result.toString());
			if (!expected.contains(result)) {
				throw new AssertionFailedError("The following course was not expected : " + result.toString());
			}
		}
		//Now lets look by a professor's name
		query = "mario";
		search = facade.search(query);
		assertNotNull(search);
		assertNotSame(search.size(), 0);
		expected = new HashSet<CourseVoVwFull>();
		expected.add(new CourseVoVwFull(marioIs2, is2, mario));
		expected.add(new CourseVoVwFull(marioIsa, isa, mario));
		expected.add(new CourseVoVwFull(marioAfi, afi, mario));
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				throw new AssertionFailedError("The following course was not expected : " + result.toString());
			}
		}
	}

	public void testSearch() {
		System.out.println("search");
		String query = "";
		CoursesFacade instance = null;
		List expResult = null;
		List result = instance.search(query);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addCourse method, of class CoursesFacade.
	 */
	@Test
	public void testAddCourse() throws Exception {
		System.out.println("addCourse");
		long professorId = 0L;
		long subjectId = 0L;
		long periodId = 0L;
		CoursesFacade instance = null;
		CourseVo expResult = null;
		CourseVo result = instance.addCourse(professorId, subjectId, periodId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeCourse method, of class CoursesFacade.
	 */
	@Test
	public void testRemoveCourse() throws Exception {
		System.out.println("removeCourse");
		long courseId = 0L;
		CoursesFacade instance = null;
		instance.removeCourse(courseId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCourse method, of class CoursesFacade.
	 */
	@Test
	public void testGetCourse() throws Exception {
		System.out.println("getCourse");
		long courseId = 0L;
		CoursesFacade instance = null;
		CourseVoVwFull expResult = null;
		CourseVoVwFull result = instance.getCourse(courseId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of rateCourse method, of class CoursesFacade.
	 */
	@Test
	public void testRateCourse() throws Exception {
		System.out.println("rateCourse");
		long courseId = 0L;
		long userId = 0L;
		float value = 0.0F;
		CoursesFacade instance = null;
		CourseRatingVo expResult = null;
		CourseRatingVo result = instance.rateCourse(courseId, userId, value);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCourseRatingByUserId method, of class CoursesFacade.
	 */
	@Test
	public void testGetCourseRatingByUserId() throws Exception {
		System.out.println("getCourseRatingByUserId");
		long courseId = 0L;
		long userId = 0L;
		CoursesFacade instance = null;
		CourseRatingVo expResult = null;
		CourseRatingVo result = instance.getCourseRatingByUserId(courseId, userId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSimilarCourses method, of class CoursesFacade.
	 */
	@Test
	public void testGetSimilarCourses() {
		System.out.println("getSimilarCourses");
		String professorName = "";
		String subjectName = "";
		Long periodId = null;
		CoursesFacade instance = null;
		List expResult = null;
		List result = instance.getSimilarCourses(professorName, subjectName, periodId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getBySubjectId method, of class CoursesFacade.
	 */
	@Test
	public void testGetBySubjectId() {
		System.out.println("getBySubjectId");
		long subjectId = 0L;
		CoursesFacade instance = null;
		List expResult = null;
		List result = instance.getBySubjectId(subjectId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
