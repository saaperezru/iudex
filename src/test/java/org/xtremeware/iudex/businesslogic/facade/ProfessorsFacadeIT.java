package org.xtremeware.iudex.businesslogic.facade;

import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.*;
import static org.junit.Assert.*;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.ProfessorVo;
import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import org.xtremeware.iudex.vo.BinaryRatingVo;
import org.xtremeware.iudex.vo.ProfessorVoFull;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 *
 * @author josebermeo
 */
public class ProfessorsFacadeIT {

    private EntityManager entityManager;

    public ProfessorsFacadeIT() {
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

    @Test
    public void testBL_8_1() throws MultipleMessagesException, Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long professorId = 1L;
        Long userId = 5L;
        int value = 1;
        BinaryRatingVo rateProfessor = pf.rateProfessor(professorId, userId, value);
    }

    @Test
    public void testBL_8_2() throws Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long professorId = Long.MAX_VALUE;
        Long userId = Long.MAX_VALUE;
        int value = Integer.MAX_VALUE;
        String[] expectedMessages = new String[]{
            "professorRating.value.invalidValue",
            "professorRating.professorId.element.notFound",
            "professorRating.userId.element.notFound"};
        try {
            BinaryRatingVo rateProfessor = pf.rateProfessor(professorId, userId, value);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        professorId = Long.MIN_VALUE;
        userId = Long.MIN_VALUE;
        value = Integer.MIN_VALUE;
        try {
            BinaryRatingVo rateProfessor = pf.rateProfessor(professorId, userId, value);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        professorId = 0L;
        userId = 0L;
        value = 0;

        expectedMessages = new String[]{
            "professorRating.professorId.element.notFound",
            "professorRating.userId.element.notFound"};
        try {
            BinaryRatingVo rateProfessor = pf.rateProfessor(professorId, userId, value);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    @Test
    public void testBL_8_3() throws MultipleMessagesException, Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long professorId = 2L;
        Long userId = 5L;
        int value = 1;
        BinaryRatingVo rateProfessor = pf.rateProfessor(professorId, userId, value);
        Long id = rateProfessor.getId();
        value = -1;
        rateProfessor = pf.rateProfessor(professorId, userId, value);
        assertEquals(id, rateProfessor.getId());
        assertEquals(value, rateProfessor.getValue());
        value = 0;
        rateProfessor = pf.rateProfessor(professorId, userId, value);
        assertEquals(id, rateProfessor.getId());
        assertEquals(value, rateProfessor.getValue());
    }

    @Test
    public void testBL_8_4() throws MultipleMessagesException, Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long professorId = 2L;
        Long userId = 5L;
        int value = 1;
        BinaryRatingVo rateProfessor = pf.rateProfessor(professorId, userId, value);
        Long id = rateProfessor.getId();

        rateProfessor = pf.rateProfessor(professorId, userId, value);
        assertEquals(id, rateProfessor.getId());
        assertEquals(value, rateProfessor.getValue());

        rateProfessor = pf.rateProfessor(professorId, userId, value);
        assertEquals(id, rateProfessor.getId());
        assertEquals(value, rateProfessor.getValue());
    }

    @Test
    public void testBL_13_1() throws MultipleMessagesException, Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        ProfessorVo pv = new ProfessorVo();
        pv.setDescription(FacadesTestHelper.randomString(50));
        pv.setFirstName(FacadesTestHelper.randomString(50));
        pv.setLastName(FacadesTestHelper.randomString(50));
        pv.setEmail("jdbermeol@gmail.com");
        pv.setImageUrl("www.ing.unal.edu.co/progsfac/civil_agricola/images/stories/Civil__Agricola/Profesores/villarreal.meglan.adela.png");
        pv.setWebsite("www.docentes.unal.edu.co/avillarrealme");
        pv = pf.addProfessor(pv);

        ProfessorVo result = entityManager.createQuery("SELECT p FROM Professor p WHERE p.id = :id", ProfessorEntity.class).setParameter("id", pv.getId()).getSingleResult().toVo();
        assertTrue(result.equals(pv));
    }

    @Test
    public void testBL_13_2() throws Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        ProfessorVo pv;

        String[] expectedMessages = new String[]{
            "professor.null"};
        try {
            pv = pf.addProfessor(null);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        pv = new ProfessorVo();
        pv.setDescription(null);
        pv.setFirstName(null);
        pv.setLastName(null);
        pv.setEmail(null);
        pv.setImageUrl(null);
        pv.setWebsite(null);

        expectedMessages = new String[]{
            "professor.firstName.null",
            "professor.lastName.null",
            "professor.description.null",
            "professor.email.null",
            "professor.imageUrl.null",
            "professor.website.null"};
        try {
            pv = pf.addProfessor(pv);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        pv.setDescription(FacadesTestHelper.randomString(2001));
        pv.setFirstName(FacadesTestHelper.randomString(51));
        pv.setLastName(FacadesTestHelper.randomString(51));
        pv.setEmail("");
        pv.setImageUrl("");
        pv.setWebsite("");

        expectedMessages = new String[]{
            "professor.firstName.tooLong",
            "professor.lastName.tooLong",
            "professor.description.tooLong",
            "professor.email.empty",
            "professor.website.empty"};
        try {
            pv = pf.addProfessor(pv);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        pv.setDescription("");
        pv.setFirstName("");
        pv.setLastName("");
        pv.setEmail(FacadesTestHelper.randomString(51));
        pv.setImageUrl(FacadesTestHelper.randomString(51));
        pv.setWebsite(FacadesTestHelper.randomString(51));

        expectedMessages = new String[]{
            "professor.firstName.empty",
            "professor.lastName.empty",
            "professor.description.empty",
            "professor.email.invalidEmail",
            "professor.imageUrl.invalidImage",
            "professor.website.invalidWebsite"};
        try {
            pv = pf.addProfessor(pv);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    @Test
    public void testBL_13_3() throws MultipleMessagesException, Exception {

        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        ProfessorVo pv = new ProfessorVo();
        pv.setDescription(FacadesTestHelper.randomString(50));
        pv.setFirstName(FacadesTestHelper.randomString(50));
        pv.setLastName(FacadesTestHelper.randomString(50));
        pv.setEmail("jdbermeol@gmail.com");
        pv.setImageUrl("www.ing.unal.edu.co/progsfac/civil_agricola/images/stories/Civil__Agricola/Profesores/villarreal.meglan.adela.png");
        pv.setWebsite("www.docentes.unal.edu.co/avillarrealme");

        try {
            pv = pf.addProfessor(pv);
        } catch (Exception ex) {
            assertEquals(DuplicityException.class, ex.getClass());
        }
    }

    @Test
    public void testBL_14_1() throws MultipleMessagesException, Exception {

        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        ProfessorVo pv = new ProfessorVo();
        pv.setDescription(FacadesTestHelper.randomString(50));
        pv.setFirstName(FacadesTestHelper.randomString(50));
        pv.setLastName(FacadesTestHelper.randomString(50));
        pv.setEmail("jdbermeo@gmail.com");
        pv.setImageUrl("www.ing.unal.edu.co/progsfac/sistemas/images/stories/Civil__Agricola/Profesores/villarreal.meglan.adela.png");
        pv.setWebsite("www.docentes.unal.edu.co/avillarrealmer");
        pv = pf.addProfessor(pv);

        String description = FacadesTestHelper.randomString(50);
        pv.setDescription(description);
        String firstName = FacadesTestHelper.randomString(50);
        pv.setFirstName(firstName);
        String lasttName = FacadesTestHelper.randomString(50);
        pv.setLastName(lasttName);
        String email = "saperrez@gmail.com";
        pv.setEmail(email);
        String image = "www.ing.unal.edu.co/progsfac/sistemas_industrial/images/stories/Civil__Agricola/Profesores/villarreal.meglan.adela.png";
        pv.setImageUrl(image);
        String webSite = "www.docentes.unal.edu.co/avill";
        pv.setWebsite(webSite);
        pv = pf.editProfessor(pv);

        ProfessorVo result = entityManager.createQuery("SELECT p FROM Professor p WHERE p.id = :id", ProfessorEntity.class).setParameter("id", pv.getId()).getSingleResult().toVo();
        assertEquals(description, result.getDescription());
        assertEquals(firstName, result.getFirstName());
        assertEquals(lasttName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals(image, result.getImageUrl());
        assertEquals(webSite, result.getWebsite());
        assertTrue(result.equals(pv));
    }

    @Test
    public void testBL_14_2() throws MultipleMessagesException, Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        ProfessorVo pv;

        String[] expectedMessages = new String[]{
            "professor.null"};
        try {
            pv = pf.editProfessor(null);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        pv = new ProfessorVo();
        pv.setId(1L);
        pv.setDescription(null);
        pv.setFirstName(null);
        pv.setLastName(null);
        pv.setEmail(null);
        pv.setImageUrl(null);
        pv.setWebsite(null);

        expectedMessages = new String[]{
            "professor.firstName.null",
            "professor.lastName.null",
            "professor.description.null",
            "professor.email.null",
            "professor.imageUrl.null",
            "professor.website.null"};
        try {
            pv = pf.editProfessor(pv);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        pv.setDescription(FacadesTestHelper.randomString(2001));
        pv.setFirstName(FacadesTestHelper.randomString(51));
        pv.setLastName(FacadesTestHelper.randomString(51));
        pv.setEmail("");
        pv.setImageUrl("");
        pv.setWebsite("");

        expectedMessages = new String[]{
            "professor.firstName.tooLong",
            "professor.lastName.tooLong",
            "professor.description.tooLong",
            "professor.email.empty",
            "professor.website.empty"};
        try {
            pv = pf.editProfessor(pv);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }

        pv.setDescription("");
        pv.setFirstName("");
        pv.setLastName("");
        pv.setEmail(FacadesTestHelper.randomString(51));
        pv.setImageUrl(FacadesTestHelper.randomString(51));
        pv.setWebsite(FacadesTestHelper.randomString(51));

        expectedMessages = new String[]{
            "professor.firstName.empty",
            "professor.lastName.empty",
            "professor.description.empty",
            "professor.email.invalidEmail",
            "professor.imageUrl.invalidImage",
            "professor.website.invalidWebsite"};
        try {
            pv = pf.editProfessor(pv);
        } catch (MultipleMessagesException ex) {
            FacadesTestHelper.checkExceptionMessages(ex, expectedMessages);
        }
    }

    @Test
    public void testBL_14_3() throws MultipleMessagesException, Exception {
        
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        ProfessorVo pv = new ProfessorVo();
        pv.setId(Long.MIN_VALUE);
        pv.setDescription(FacadesTestHelper.randomString(50));
        pv.setFirstName(FacadesTestHelper.randomString(50));
        pv.setLastName(FacadesTestHelper.randomString(50));
        pv.setEmail("jdbermeol@gmail.com");
        pv.setImageUrl("www.ing.unal.edu.co/progsfac/civil_agricola/images/stories/Civil__Agricola/Profesores/villarreal.meglan.adela.png");
        pv.setWebsite("www.docentes.unal.edu.co/avillarrealme");
        ProfessorVo result = pf.editProfessor(pv);
        assertNull(result);
        
        pv.setId(Long.MAX_VALUE);
        result = pf.editProfessor(pv);
        assertNull(result);
        
        pv.setId(0L);
        result = pf.editProfessor(pv);
        assertNull(result);
    }

    @Test
    public void testBL_31_1() throws MultipleMessagesException, Exception {

        ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long id = 3L;
        RatingSummaryVo professorRatingSummary = professorsFacade.getProfessorRatingSummary(id);

        int count = entityManager.createQuery("SELECT COUNT (result) FROM ProfessorRating result "
                + "WHERE result.professor.id = :id AND result.value = -1", Long.class).setParameter("id", id).getSingleResult().intValue();
        assertTrue(count == professorRatingSummary.getPositive());

        count = entityManager.createQuery("SELECT COUNT (result) FROM ProfessorRating result "
                + "WHERE result.professor.id = :id AND result.value = -1", Long.class).setParameter("id", id).getSingleResult().intValue();
        assertTrue(count == professorRatingSummary.getNegative());
    }

    @Test
    public void testBL_31_2() throws MultipleMessagesException, Exception {

        ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long id = Long.MAX_VALUE;
        RatingSummaryVo professorRatingSummary = null;

        professorRatingSummary = professorsFacade.getProfessorRatingSummary(id);
        assertTrue(0 == professorRatingSummary.getPositive());
        assertTrue(0 == professorRatingSummary.getNegative());

        id = Long.MIN_VALUE;
        professorRatingSummary = professorsFacade.getProfessorRatingSummary(id);
        assertTrue(0 == professorRatingSummary.getPositive());
        assertTrue(0 == professorRatingSummary.getNegative());

        id = 0L;
        professorRatingSummary = professorsFacade.getProfessorRatingSummary(id);
        assertTrue(0 == professorRatingSummary.getPositive());
        assertTrue(0 == professorRatingSummary.getNegative());

    }

    @Test
    public void testBL_32_1() throws MultipleMessagesException, Exception {

        ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long id = 3L;
        ProfessorVoFull pvvf = professorsFacade.getProfessor(id);

        ProfessorVo pv = entityManager.createQuery("SELECT result FROM Professor result "
                + "WHERE result.id = :id", ProfessorEntity.class).setParameter("id", id).getSingleResult().toVo();
        assertEquals(pv.getId(), pvvf.getId());
        assertEquals(pv.getDescription(), pvvf.getDescription());
        assertEquals(pv.getEmail(), pvvf.getEmail());
        assertEquals(pv.getFirstName(), pvvf.getFirstName());
        assertEquals(pv.getImageUrl(), pvvf.getImageUrl());
        assertEquals(pv.getLastName(), pvvf.getLastName());
        assertEquals(pv.getWebsite(), pvvf.getWebsite());

        int count = entityManager.createQuery("SELECT COUNT (result) FROM ProfessorRating result "
                + "WHERE result.professor.id = :id AND result.value = 1", Long.class).setParameter("id", id).getSingleResult().intValue();
        assertTrue(count == pvvf.getRatingSummary().getPositive());

        count = entityManager.createQuery("SELECT COUNT (result) FROM ProfessorRating result "
                + "WHERE result.professor.id = :id AND result.value = -1", Long.class).setParameter("id", id).getSingleResult().intValue();
        assertTrue(count == pvvf.getRatingSummary().getNegative());
    }

    @Test
    public void testBL_32_2() throws MultipleMessagesException, Exception {

        ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        ProfessorVoFull pvvf = null;

        Long id = 0L;
        pvvf = professorsFacade.getProfessor(id);
        assertNull(pvvf);

        id = Long.MIN_VALUE;
        pvvf = professorsFacade.getProfessor(id);
        assertNull(pvvf);

        id = Long.MAX_VALUE;
        pvvf = professorsFacade.getProfessor(id);
        assertNull(pvvf);
    }

    @Test
    public void testBL_33_1() throws MultipleMessagesException, Exception {

        ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        BinaryRatingVo prv = null;

        Long professorId = 1L;
        Long userId = 1L;
        prv = professorsFacade.getProfessorRatingByUserId(professorId, userId);
        int value = entityManager.createQuery("SELECT result.value FROM ProfessorRating result "
                + "WHERE result.professor.id = :professorId AND result.user.id = :userId", Integer.class).
                setParameter("professorId", professorId).
                setParameter("userId", userId).
                getSingleResult().intValue();
        assertTrue(value == prv.getValue());


    }

    @Test
    public void testBL_33_2() throws MultipleMessagesException, Exception {

         ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        BinaryRatingVo prv = null;

        Long professorId = 0L;
        Long userId = 0L;
        prv = professorsFacade.getProfessorRatingByUserId(professorId, userId);
        assertNull(prv);
        
        professorId = Long.MAX_VALUE;
        userId = Long.MAX_VALUE;
        prv = professorsFacade.getProfessorRatingByUserId(professorId, userId);
        assertNull(prv);
        
        professorId = Long.MIN_VALUE;
        userId = Long.MAX_VALUE;
        prv = professorsFacade.getProfessorRatingByUserId(professorId, userId);
        assertNull(prv);
        
        professorId = Long.MAX_VALUE;
        userId = Long.MIN_VALUE;
        prv = professorsFacade.getProfessorRatingByUserId(professorId, userId);
        assertNull(prv);
        
        professorId = Long.MIN_VALUE;
        userId = Long.MIN_VALUE;
        prv = professorsFacade.getProfessorRatingByUserId(professorId, userId);
        assertNull(prv);
    }
    
    @Test
    public void testBL_34_1() throws MultipleMessagesException, Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        String firstName = FacadesTestHelper.randomString(10);
        String lastName = FacadesTestHelper.randomString(10);
        ProfessorVo pv = new ProfessorVo();
        pv.setDescription(FacadesTestHelper.randomString(50));
        pv.setFirstName(firstName);
        pv.setLastName(lastName);
        pv.setEmail("jdbermeol1@gmail.com");
        pv.setImageUrl("www.ing.unal.edu.co/progsfac/civil_agricola1/images/stories/Civil__Agricola/Profesores/villarreal.meglan.adela.png");
        pv.setWebsite("www.docentes.unal.edu.co/avillarrealme1");
        pv = pf.addProfessor(pv);
        Long id1 = pv.getId();
        
        pv.setId(null);
        pv.setDescription(FacadesTestHelper.randomString(50));
        pv.setFirstName("Pre"+firstName);
        pv.setLastName(lastName+"Pos");
        pv.setEmail("jdbermeol2@gmail.com");
        pv.setImageUrl("www.ing.unal.edu.co/progsfac/civil_agricola2/images/stories/Civil__Agricola/Profesores/villarreal.meglan.adela.png");
        pv.setWebsite("www.docentes.unal.edu.co/avillarrealme2");
        pv = pf.addProfessor(pv);
        Long id2 = pv.getId();
        
        Map<Long, String> professorsAutocomplete = pf.getProfessorsAutocomplete(lastName);
        assertEquals(2, professorsAutocomplete.size());
        assertTrue(professorsAutocomplete.containsKey(id1));
        assertEquals(firstName+" "+lastName, professorsAutocomplete.get(id1));
        
        assertTrue(professorsAutocomplete.containsKey(id2));
        assertEquals("Pre"+firstName+" "+lastName+"Pos", professorsAutocomplete.get(id2));
        
        professorsAutocomplete = pf.getProfessorsAutocomplete(firstName);
        assertEquals(2, professorsAutocomplete.size());
        assertTrue(professorsAutocomplete.containsKey(id1));
        assertEquals(firstName+" "+lastName, professorsAutocomplete.get(id1));
        
        assertTrue(professorsAutocomplete.containsKey(id2));
        assertEquals("Pre"+firstName+" "+lastName+"Pos", professorsAutocomplete.get(id2));
        
        professorsAutocomplete = pf.getProfessorsAutocomplete(null);
        assertTrue(professorsAutocomplete.isEmpty());
        
    }

    @Test
    public void testBL_34_2() throws MultipleMessagesException, Exception {
        ProfessorsFacade pf = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Map<Long, String> professorsAutocomplete = pf.getProfessorsAutocomplete("aaa"+FacadesTestHelper.randomString(5));
        assertTrue(professorsAutocomplete.isEmpty());
        
        professorsAutocomplete = pf.getProfessorsAutocomplete(FacadesTestHelper.randomString(5)+"aasfa");
        assertTrue(professorsAutocomplete.isEmpty());
        
        professorsAutocomplete = pf.getProfessorsAutocomplete(null);
        assertTrue(professorsAutocomplete.isEmpty());
    }

    @Test
    public void testBL_15_1() throws MultipleMessagesException, Exception {

        ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long id = 3L;
        professorsFacade.removeProfessor(id);

        int count = entityManager.createQuery("SELECT COUNT(p) FROM Professor p WHERE p.id =:id", Long.class).setParameter("id", id).getSingleResult().intValue();
        assertTrue(0 == count);

        count = entityManager.createQuery("SELECT COUNT(r) FROM ProfessorRating r WHERE r.professor.id = :id", Long.class).setParameter("id", id).getSingleResult().intValue();
        assertTrue(0 == count);

        count = entityManager.createQuery("SELECT COUNT(c) FROM Course c WHERE c.professor.id = :id", Long.class).setParameter("id", id).getSingleResult().intValue();
        assertTrue(0 == count);
    }

    @Test
    public void testBL_15_2() throws MultipleMessagesException, Exception {

        ProfessorsFacade professorsFacade = Config.getInstance().getFacadeFactory().getProfessorsFacade();
        Long id = Long.MAX_VALUE;

        try {
            professorsFacade.removeProfessor(id);
        } catch (Exception ex) {
            assertEquals(RuntimeException.class, ex.getClass());
            assertEquals("entity.notFound", ex.getCause().getMessage());
        }

        id = Long.MIN_VALUE;
        try {
            professorsFacade.removeProfessor(id);
        } catch (Exception ex) {
            assertEquals(RuntimeException.class, ex.getClass());
            assertEquals("entity.notFound", ex.getCause().getMessage());
        }

        id = 0L;
        try {
            professorsFacade.removeProfessor(id);
        } catch (Exception ex) {
            assertEquals(RuntimeException.class, ex.getClass());
            assertEquals("entity.notFound", ex.getCause().getMessage());
        }

    }
}
