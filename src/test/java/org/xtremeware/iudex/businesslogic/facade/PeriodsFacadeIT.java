package org.xtremeware.iudex.businesslogic.facade;

import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.junit.Assert.*;
import org.junit.*;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.entity.Entity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.PeriodVo;

/**
 *
 * @author josebermeo
 */
public class PeriodsFacadeIT {

    private Set<String> exceptionMessage;
    private EntityManager entityManager;

    public PeriodsFacadeIT() {
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

        exceptionMessage = new TreeSet<String>();
        exceptionMessage.add(
                "Int Semester in the provided PeriodVo must be greater than 1 and less than 3");
        exceptionMessage.add(
                "Int Year in the provided PeriodVo must be possitive");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test successfully insert of a period
     */
    @Test
    public void test_BL_20_1()
            throws MultipleMessagesException, DataBaseException {
        int year = 2012;
        int semester = 3;
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().
                getPeriodsFacade();
        PeriodVo periodVo = periodsFacade.addPeriod(year, semester);
        assertNotNull(periodVo);
        assertEquals(semester, periodVo.getSemester());
        assertEquals(year, periodVo.getYear());
        assertNotNull(periodVo.getId());
        year = 2013;
        semester = 2;
        periodVo = periodsFacade.addPeriod(year, semester);
        assertNotNull(periodVo);
        assertEquals(semester, periodVo.getSemester());
        assertEquals(year, periodVo.getYear());
        assertNotNull(periodVo.getId());
    }

    @Test()
    public void test_BL_20_2()
            throws DataBaseException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().
                getPeriodsFacade();
        try {
            PeriodVo periodVo = periodsFacade.addPeriod(2012, 4);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessage.contains(message));
            }
        }
        try {
            PeriodVo periodVo = periodsFacade.addPeriod(-1, 3);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessage.contains(message));
            }
        }
        try {
            PeriodVo periodVo = periodsFacade.addPeriod(-1, 0);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessage.contains(message));
            }
        }
        try {
            PeriodVo periodVo = periodsFacade.addPeriod(Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessage.contains(message));
            }
        }
        try {
            PeriodVo periodVo = periodsFacade.addPeriod(Integer.MIN_VALUE,
                    Integer.MIN_VALUE);
        } catch (MultipleMessagesException ex) {
            for (String message : ex.getMessages()) {
                assertTrue(exceptionMessage.contains(message));
            }
        }
    }

    @Test
    public void test_BL_20_3() throws MultipleMessagesException, Exception {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().
                getPeriodsFacade();
        PeriodVo periodVo = null;
        try {
            periodVo = periodsFacade.addPeriod(2008, 1);
        } catch (Exception ex) {
            assertEquals(null, periodVo);
            assertEquals(DataBaseException.class, ex.getClass());
        }
        try {
            periodVo = periodsFacade.addPeriod(2008, 2);
        } catch (Exception ex) {
            assertEquals(null, periodVo);
            assertEquals(DataBaseException.class, ex.getClass());
        }
        try {
            periodsFacade.addPeriod(2009, 1);
        } catch (Exception ex) {
            assertEquals(null, periodVo);
            assertEquals(DataBaseException.class, ex.getClass());
        }
    }

    @Test
    public void test_BL_21_1() throws DataBaseException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().
                getPeriodsFacade();
        Long id = 1L;
        periodsFacade.removePeriod(id);
        int result = entityManager.createQuery(
                "SELECT COUNT(p) FROM Period p WHERE p.id = :id", Long.class).
                setParameter("id", id).getSingleResult().intValue();
        assertEquals(0, result);
    }

    @Test
    public void test_BL_21_2() throws DataBaseException {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().
                getPeriodsFacade();
        Long id = 0L;
        try {
            periodsFacade.removePeriod(id);
        } catch (Exception ex) {
            assertEquals(DataBaseException.class, ex.getClass());
            assertEquals("No entity found for id " + String.valueOf(id) +
                    "while triying to delete the associated record", ex.
                    getMessage());
            int size = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Course p WHERE period.id = :id",
                    Long.class).setParameter("id", id).getSingleResult().
                    intValue();
            assertEquals(0, size);
        }
        id = Long.MAX_VALUE;
        try {
            periodsFacade.removePeriod(id);
        } catch (Exception ex) {
            assertEquals(DataBaseException.class, ex.getClass());
            assertEquals("No entity found for id " + String.valueOf(id) +
                    "while triying to delete the associated record", ex.
                    getMessage());
            int size = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Course p WHERE period.id = :id",
                    Long.class).setParameter("id", id).getSingleResult().
                    intValue();
            assertEquals(0, size);
        }
        id = Long.MIN_VALUE;
        try {
            periodsFacade.removePeriod(id);
        } catch (Exception ex) {
            assertEquals(DataBaseException.class, ex.getClass());
            assertEquals("No entity found for id " + String.valueOf(id) +
                    "while triying to delete the associated record", ex.
                    getMessage());
            int size = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Course p WHERE period.id = :id",
                    Long.class).setParameter("id", id).getSingleResult().
                    intValue();
            assertEquals(0, size);
        }
        id = 1L;
        try {
            periodsFacade.removePeriod(id);
        } catch (Exception ex) {
            assertEquals(DataBaseException.class, ex.getClass());
            assertEquals("No entity found for id " + String.valueOf(id) +
                    "while triying to delete the associated record", ex.
                    getMessage());
            int size = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Course p WHERE period.id = :id",
                    Long.class).setParameter("id", id).getSingleResult().
                    intValue();
            assertEquals(0, size);
        }

    }

    @Test()
    public void test_BL_21_3() throws InvalidVoException, Exception {
        PeriodsFacade periodsFacade = Config.getInstance().getFacadeFactory().
                getPeriodsFacade();
        List<PeriodVo> periodVoList = periodsFacade.listPeriods();
        for (PeriodVo periodVo : periodVoList) {
            PeriodVo result =
                    (PeriodVo) ((Entity) entityManager.createQuery(
                    "SELECT p FROM Period p WHERE p.id = :id").
                    setParameter("id", periodVo.getId()).getSingleResult()).toVo();
            assertTrue(periodVo.equals(result));
        }
        int size = entityManager.createQuery("SELECT COUNT(p) FROM Period p",
                Long.class).getSingleResult().intValue();
        assertEquals(periodVoList.size(), size);
    }
}