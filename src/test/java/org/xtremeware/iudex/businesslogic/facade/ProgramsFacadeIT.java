package org.xtremeware.iudex.businesslogic.facade;

import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
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
        FacadesTestHelper.initializeDatabase();
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
     * Test successfully insert of a period
     */
    @Test
    public void testBL_26_1() throws MultipleMessagesException, Exception {
        String name = FacadesTestHelper.randomString(50);
        ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
        ProgramVo programVo = programsFacade.addProgram(name);
        assertNotNull(programVo);
        assertEquals(name, programVo.getName());
        assertNotNull(programVo.getId());
    }

    @Test()
    public void testBL_26_2() throws Exception {
        ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
        String[] expectedMessages = new String[]{
            "program.name.null"};
        try {
            ProgramVo programVo = programsFacade.addProgram(null);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        String name = FacadesTestHelper.randomString(51);
        expectedMessages = new String[]{
            "program.name.tooLong"};
        try {
            ProgramVo programVo = programsFacade.addProgram(name);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
        expectedMessages = new String[]{
            "program.name.tooShort"};

        try {
            ProgramVo programVo = programsFacade.addProgram("");
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }
    
    @Test
    public void BL_29_1() throws Exception {
        String name = FacadesTestHelper.randomString(10);
        Set<String> names = new TreeSet<String>();
        ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
        names.add(name);
        assertNotNull(programsFacade.addProgram(name));
        
        names.add("PRE"+name);
        assertNotNull(programsFacade.addProgram("PRE"+name));
        
        names.add(name+"POS");
        assertNotNull(programsFacade.addProgram(name+"POS"));
        
        names.add("PRE"+name+"POS");
        assertNotNull(programsFacade.addProgram("PRE"+name+"POS"));
        
        List<ProgramVo> pvs  = programsFacade.getProgramsAutocomplete(name);
        for (ProgramVo pv : pvs) {
            ProgramVo result = entityManager.createQuery(
                    "SELECT p FROM Program p WHERE p.id =:id", ProgramEntity.class).
                    setParameter("id", pv.getId()).getSingleResult().toVo();
            assertNotNull(result);
            assertEquals(result.getId(), pv.getId());
            assertEquals(result.getName(), pv.getName());
            assertTrue(names.contains(pv.getName()));
        }
        assertEquals(names.size(), pvs.size());
    }

    @Test
    public void testBL_27_1() throws MultipleMessagesException, Exception {
        ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
        programsFacade.removeProgram(2532L);
        programsFacade.removeProgram(2541L);


    }

    @Test
    public void testBL_27_2() throws MultipleMessagesException, Exception {
        ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
        Long id = 2532L;
        try {
            programsFacade.removeProgram(id);
        } catch (DataBaseException ex) {
            assertEquals("No entity found for id "
                    + String.valueOf(id)
                    + "while triying to delete the associated record", ex.getMessage());
        }

        id = 2541L;
        try {
            programsFacade.removeProgram(id);
        } catch (DataBaseException ex) {
            assertEquals("No entity found for id "
                    + String.valueOf(id)
                    + "while triying to delete the associated record", ex.getMessage());
        }
        id = Long.MAX_VALUE;
        try {
            programsFacade.removeProgram(id);
        } catch (DataBaseException ex) {
            assertEquals("No entity found for id "
                    + String.valueOf(id)
                    + "while triying to delete the associated record", ex.getMessage());
        }
        id = Long.MIN_VALUE;
        try {
            programsFacade.removeProgram(id);
        } catch (DataBaseException ex) {
            assertEquals("No entity found for id "
                    + String.valueOf(id)
                    + "while triying to delete the associated record", ex.getMessage());
        }
        id = 0L;
        try {
            programsFacade.removeProgram(id);
        } catch (DataBaseException ex) {
            assertEquals("No entity found for id "
                    + String.valueOf(id)
                    + "while triying to delete the associated record", ex.getMessage());
        }
    }

    @Test
    public void BL_28_1() throws Exception {
        ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
        List<ProgramVo> pvs  = programsFacade.listPrograms();
        for (ProgramVo pv : pvs) {
            ProgramVo result = entityManager.createQuery(
                    "SELECT p FROM Program p WHERE p.id =:id", ProgramEntity.class).
                    setParameter("id", pv.getId()).getSingleResult().toVo();
            assertNotNull(result);
            assertEquals(result.getId(), pv.getId());
            assertEquals(result.getName(), pv.getName());
        }
        int size = entityManager.createQuery("SELECT COUNT(p) FROM Program p", Long.class).getSingleResult().intValue();
        assertEquals(size, pvs.size());
    }
}
