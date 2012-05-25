/*
 */
package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
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

	public static ProfessorVoVwSmall fabio, mario;
	public static SubjectVoVwSmall is2, isa, afi;
	public static CourseVo marioIs2, marioIsa, marioAfi, fabioIsa;
	private EntityManager entityManager;

	@Before
	public void setUp() {
		entityManager = FacadesTestHelper.createEntityManagerFactory().createEntityManager();
	}

	@BeforeClass
	public static void setUpClass() throws Exception {


		FacadesTestHelper.initializeDatabase();

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
		rating.setPositive(3);
		rating.setNegative(1);
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
		fabioIsa.setSubjectId(isa.getId());
		fabioIsa.setRatingAverage(3.7);
		fabioIsa.setRatingCount(4L);

		Class.forName("org.h2.Driver");


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
		List<CourseVoVwFull> search = facade.search(query);
		assertNotNull(search);
		assertNotSame(search.size(), 0);
		Set<CourseVoVwFull> expected = new HashSet<CourseVoVwFull>();
		expected.add(new CourseVoVwFull(marioIs2, is2, mario));
		expected.add(new CourseVoVwFull(marioIsa, isa, mario));
		expected.add(new CourseVoVwFull(fabioIsa, isa, fabio));
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				fail("The following course was not expected for query " + query + ": " + result.toString());
			}
		}
		query = "sOFTWArE";
		search = facade.search(query);
		assertNotNull(search);
		assertNotSame(search.size(), 0);
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				fail("The following course was not expected for query " + query + ": " + result.toString());
			}
		}
		//Now lets look by a professor's name
		query = "maRio";
		search = facade.search(query);
		assertNotNull(search);
		assertNotSame(search.size(), 0);
		expected = new HashSet<CourseVoVwFull>();
		expected.add(new CourseVoVwFull(marioIs2, is2, mario));
		expected.add(new CourseVoVwFull(marioIsa, isa, mario));
		expected.add(new CourseVoVwFull(marioAfi, afi, mario));
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				fail("The following course was not expected for query " + query + ": " + result.toString());
			}
		}
		query = "mario";
		search = facade.search(query);
		assertNotNull(search);
		assertNotSame(search.size(), 0);
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				fail("The following course was not expected for query " + query + ": " + result.toString());
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
		List<CourseVoVwFull> search = facade.search(query);
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
		List<CourseVoVwFull> search = facade.search(query);
		assertNotNull(search);
		assertEquals(search.size(), 0);
	}

	/**
	 * Test of details of course with comments and ratings
	 */
	@Test
	public void test_BL_6_1() {
		CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
		CourseVoVwFull expectedCourse = new CourseVoVwFull(marioIs2, is2, mario);
		long courseId = 1L;
		try {
			CourseVoVwFull course = facade.getCourse(courseId);
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
			CourseVoVwFull course = facade.getCourse(courseId);
			assertNull(course);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
		courseId = Long.MIN_VALUE;
		try {
			CourseVoVwFull course = facade.getCourse(courseId);
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
			CourseVoVwFull course = facade.getCourse(courseId);
			assertEquals(course.getRatingAverage(), 0.0, 0);
			assertEquals(course.getRatingCount(), 0, 0);
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
			expected.setProfessorId(fabio.getId());
			expected.setSubjectId(is2.getId());
			expected.setRatingAverage(0.0);
			expected.setRatingCount(0L);

			CourseVo result = facade.addCourse(fabio.getId(), is2.getId(), 1L);
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
			CourseVo result = facade.addCourse(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
		} catch (MultipleMessagesException ex) {
			FacadesTestHelper.checkExceptionMessages(ex, expectedMessage);
		}
		expectedMessage = new String[]{"course.periodId.notFound"};
		try {
			CourseVo result = facade.addCourse(fabio.getId(), afi.getId(), Long.MAX_VALUE);
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
			facade.addCourse(mario.getId(), is2.getId(), 1L);
			fail("No exception was thrown for a duplicity case");
		} catch (DuplicityException ex) {
			assertEquals("entity.exists", ex.getMessage());
		}
	}

	@Test
	public void test_BL_19_1() throws Exception {
		CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
		long listToRemove[] = {1L, 2L};
		for (long i : listToRemove) {
			facade.removeCourse(i);
			assertNull(facade.getCourse(i));
		}

	}

	@Test
	public void test_BL_19_2() throws Exception {
		CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
		long listToRemove[] = {Long.MAX_VALUE, Long.MIN_VALUE};
		for (long i : listToRemove) {
			try {
				facade.removeCourse(i);
			} catch (Exception e) {
				assertEquals(RuntimeException.class, e.getClass());
				assertEquals(e.getCause().getMessage(), "No entity found for id " + String.valueOf(i) + "while triying to delete the associated record");
			}
		}

	}

	@Test
	public void getByProfessorIdTest() {
		CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();

		HashSet<CourseVoVwFull> expected = new HashSet<CourseVoVwFull>();
		expected.add(new CourseVoVwFull(marioIs2, is2, mario));
		expected.add(new CourseVoVwFull(marioIsa, isa, mario));
		expected.add(new CourseVoVwFull(marioAfi, afi, mario));

		List<CourseVoVwFull> search = facade.getByProfessorId(mario.getId());
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				fail("The following course was not expected for the professor Id " + mario.getId() + ": " + result.toString());
			}
		}
	}

	@Test
	public void getBySubjectId() {
		CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();

		HashSet<CourseVoVwFull> expected = new HashSet<CourseVoVwFull>();
		expected.add(new CourseVoVwFull(marioIsa, isa, mario));
		expected.add(new CourseVoVwFull(fabioIsa, isa, fabio));

		List<CourseVoVwFull> search = facade.getBySubjectId(isa.getId());
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				fail("The following course was not expected for the professor Id " + mario.getId() + ": " + result.toString());
			}
		}
	}

	@Test
	public void rateComment() {
		CoursesFacade facade = Config.getInstance().getFacadeFactory().getCoursesFacade();
		//Rate a non-existing course and a non-existing user, with an invalid rating value
		rateCommentInvalidInputry(facade, Long.MIN_VALUE, Long.MIN_VALUE, -0.1f, new String[]{"courseRating.courseId.notFound", "courseRating.userId.notFound", "courseRating.value.invalidRange"});
		//Rate a existing course and a user, with an invalid rating value
		rateCommentInvalidInputry(facade, 1L, 1L, -0.1f, new String[]{"courseRating.value.invalidRange"});

		//Rate a valid course, with valid user and valid value
		try {
			CourseRatingVo result = facade.rateCourse(5L, 1L, 0.5f);
			CourseRatingVo expected = facade.getCourseRatingByUserId(5L, 1L);
			assertEquals(expected, result);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("An unexpected exception ocurred");
		}

		//Edit an existing rating
		try {
			CourseRatingVo result = facade.rateCourse(5L, 1L, 1.5f);
			CourseRatingVo expected = facade.getCourseRatingByUserId(5L, 1L);
			assertEquals(expected, result);
		} catch (Exception ex) {
			ex.printStackTrace();
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
			ex.printStackTrace();
			fail("An unexpected exception ocurred");
		}
	}
}
