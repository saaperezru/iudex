/*
 */
package org.xtremeware.iudex.businesslogic.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
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

	public static ProfessorVoVwSmall fabio, mario;
	public static SubjectVoVwSmall is2, isa, afi;
	public static CourseVo marioIs2, marioIsa, marioAfi, fabioIsa;

	public CoursesFacadeIT() {
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
		List<CourseVoVwFull> search = facade.search(query);
		assertNotNull(search);
		assertNotSame(search.size(), 0);
		Set<CourseVoVwFull> expected = new HashSet<CourseVoVwFull>();
		expected.add(new CourseVoVwFull(marioIs2, is2, mario));
		expected.add(new CourseVoVwFull(marioIsa, isa, mario));
		expected.add(new CourseVoVwFull(fabioIsa, isa, fabio));
		for (CourseVoVwFull result : search) {
			if (!expected.contains(result)) {
				throw new AssertionFailedError("The following course was not expected for query " + query + ": " + result.toString());
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
				throw new AssertionFailedError("The following course was not expected for query " + query + ": " + result.toString());
			}
		}
	}



}
