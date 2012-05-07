package org.xtremeware.iudex.businesslogic.facade;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.vo.PeriodVo;
import org.xtremeware.iudex.helper.Config;

/**
 *
 * @author josebermeo
 */
public class PeriodsFacadeIT {

    private EntityManager entityManager;

    public PeriodsFacadeIT() {
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
    public void test_BL_20_1() throws InvalidVoException {
        int year = 2012;
        int semester = 3;
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(year, semester);
        assertNotNull(periodVo);
        assertEquals(semester, periodVo.getSemester());
        assertEquals(year, periodVo.getYear());
        assertNotNull(periodVo.getId());
    }

    @Test(expected = InvalidVoException.class)
    public void test_BL_20_2_1() throws InvalidVoException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(2012, 4);
    }

    @Test(expected = InvalidVoException.class)
    public void test_BL_20_2_2() throws InvalidVoException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(-1, 3);
    }

    @Test(expected = InvalidVoException.class)
    public void test_BL_20_2_3() throws InvalidVoException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(-1, 0);
    }

    @Test
    public void test_BL_20_3_1() throws InvalidVoException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(2008, 1);

        assertEquals(null, periodVo);
    }

    @Test
    public void test_BL_20_3_2() throws InvalidVoException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(2008, 2);

        assertEquals(null, periodVo);
    }

    @Test
    public void test_BL_20_3_3() throws InvalidVoException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(2009, 1);

        assertEquals(null, periodVo);
    }
    
//    @Test
//    public void test_BL_21_1() throws InvalidVoException, Exception {
//        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
//        long id = 1;
//        periodsFacade.removePeriod(id);
//        Entity result = (Entity) entityManager.createQuery("SELECT p FROM Period p WHERE p.id = :id").
//                setParameter("id", id).getSingleResult();
//        assertEquals(null, result);
//    }
//
//    @Test
//    public void test_BL_21_2_1() throws InvalidVoException, Exception {
//        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
//        long id = -1;
//        periodsFacade.removePeriod(id);
//    }
//    
//    @Test(expected=InvalidVoException.class)
//    public void test_BL_21_2_2() throws InvalidVoException, Exception {
//        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
//        long id = 20;
//        periodsFacade.removePeriod(id);
//        //TODO check elimination
//    }
//    
//    @Test(expected=InvalidVoException.class)
//    public void test_BL_21_2_3() throws InvalidVoException, Exception {
//        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
//        long id = 100;
//        periodsFacade.removePeriod(id);
//        //TODO check elimination
//    }

    @Test()
    public void test_BL_21_2() throws InvalidVoException, Exception {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().getPeriodsFacade();
        List<PeriodVo> periodVoList = periodsFacade.listPeriods();
        for (PeriodVo periodVo : periodVoList) {
            PeriodVo result = (PeriodVo)((Entity) entityManager.createQuery("SELECT p FROM Period p WHERE p.id = :id").
                    setParameter("id", periodVo.getId()).getSingleResult()).toVo();
            assertTrue(periodVo.equals(result));
        }
        int size = entityManager.createQuery("SELECT COUNT(p) FROM Period p",Long.class).getSingleResult().intValue();
        assertEquals(periodVoList.size(), size);
    }
}