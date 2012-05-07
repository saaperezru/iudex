package org.xtremeware.iudex.businesslogic.facade;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 *
 * @author josebermeo
 */
public class ProgramsFacadeIT {

	private EntityManager entityManager;

	public ProgramsFacadeIT() {
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
		entityManager = entityManagerFactory.createEntityManager();
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test successfully insert of a period
	 */
	@Test
	public void test1() throws InvalidVoException {
		String name = "SISTEMAS";
		ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
		ProgramVo programVo = programsFacade.addProgram(name);
		assertNotNull(programVo);
		assertEquals(name, programVo.getName());
		assertNotNull(programVo.getId());
	}

	@Test(expected = InvalidVoException.class)
	public void test2() throws InvalidVoException {
		String name = "123456789012345678901234567890123456789012345678901234567890";
		ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
		ProgramVo programVo = programsFacade.addProgram(name);
	}

	@Test
	public void test3_1() throws InvalidVoException {
		String name = "FARMACIA";
		ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
		ProgramVo programVo = programsFacade.addProgram(name);

		assertEquals(null, programVo);
	}

//    @Test
//    public void test3_2() throws InvalidVoException {
//        String name = "ECONOMÍA";
//        ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
//        ProgramVo programVo = programsFacade.addProgram(name);
//
//        assertEquals(null, programVo);
//    }
	@Test
	public void test4() throws InvalidVoException, Exception {
		ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
		long id = 2539;
		programsFacade.removeProgram(id);
		ProgramEntity result = (ProgramEntity) entityManager.createQuery("SELECT p FROM Program p WHERE p.id = :id").
			setParameter("id", id).getSingleResult();
		assertEquals(null, result);
	}

	@Test()
	public void test5() throws InvalidVoException, Exception {
		ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
		List<ProgramVo> programVos = programsFacade.listPrograms();
		for (ProgramVo programVo : programVos) {
			ProgramVo result = (ProgramVo) ((Entity) entityManager.createQuery("SELECT p FROM Program p WHERE p.id = :id").
				setParameter("id", programVo.getId()).getSingleResult()).toVo();
			assertTrue(programVo.equals(result));
		}
		int size = entityManager.createQuery("SELECT COUNT(p) FROM Program p", Long.class).getSingleResult().intValue();
		assertEquals(size, programVos.size());
	}
}