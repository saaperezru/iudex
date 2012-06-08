package org.xtremeware.iudex.businesslogic.facade;

import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.vo.BinaryRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectVo;
import org.xtremeware.iudex.vo.SubjectVoFull;

/**
 *
 * @author juan
 */
public class SubjectsFacadeIT {

    private SubjectsFacade subjectsFacade = Config.getInstance().getFacadeFactory().getSubjectsFacade();

    public SubjectsFacadeIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        FacadesTestHelper.initializeDatabase();
    }

    @Before
    public void setUp() {
    }

    //@Ignore
    @Test
    public void test_BL_12_1() throws DataBaseException,
            MultipleMessagesException, Exception {
        Long userId = 5L;

        Long[] subjects = {2016702L, 2021814L, 2016025L};
        int[] ratings = {-1, 0, 1};

        BinaryRatingVo subjectRatingVo;

        for (int i = 0; i < subjects.length; i++) {

            subjectsFacade.rateSubject(userId, subjects[i], ratings[i]);

            subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjects[i],
                    userId);

            assertNotNull(subjectRatingVo);
            assertEquals(subjectRatingVo.getUserId(),
                    userId);
            assertEquals(subjectRatingVo.getEvaluatedObjectId(),
                    subjects[i]);
            assertEquals(subjectRatingVo.getValue(), ratings[i]);
        }

    }

    //@Ignore
    @Test
    public void test_BL_12_2() throws Exception {

        Long subjectId = 2021815L;
        Long userId = 2L;

        try {
            subjectsFacade.rateSubject(userId, subjectId, 0);
            fail();
        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 1);
            assertEquals(ex.getMessages().get(0),
                    "subjectRating.subjectId.element.notFound");
        }
    }

    //@Ignore
    @Test
    public void test_BL_12_3() throws MultipleMessagesException,
            Exception {
        BinaryRatingVo subjectRatingVo;

        Long userId = 2L;
        Long subjectId = 2016702L;
        int rating = 0;

        subjectsFacade.rateSubject(userId, subjectId, rating);

        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId,
                userId);

        assertEquals(subjectRatingVo.getValue(), rating);

        rating = -1;

        subjectsFacade.rateSubject(userId, subjectId, rating);

        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId,
                userId);

        assertEquals(subjectRatingVo.getValue(), rating);

        rating = -1;

        subjectsFacade.rateSubject(userId, subjectId, rating);

        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId,
                userId);

        assertEquals(subjectRatingVo.getValue(), rating);

    }

    //@Ignore
    @Test
    public void test_BL_12_4() throws MultipleMessagesException,
            Exception {
        BinaryRatingVo subjectRatingVo;

        Long userId = 2L;
        Long subjectId = 2016025L;
        int rating = 1;

        subjectsFacade.rateSubject(userId, subjectId, rating);

        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId,
                userId);

        assertEquals(subjectRatingVo.getValue(), rating);

        subjectId = 2019855L;
        rating = -1;

        subjectsFacade.rateSubject(userId, subjectId, rating);

        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId,
                userId);

        assertEquals(subjectRatingVo.getValue(), rating);

    }

    //@Ignore
    @Test
    public void test_BL_16_1() throws MultipleMessagesException, Exception {

        SubjectVo subjectVo = new SubjectVo();
        Long createdSubjectId;

        int subjectCode = 1234567;
        String subjectName = "MATEMATICAS BASICAS";
        String subjectDescription = "Materia para nivelacion en la facultad de ingenieria.";

        
        subjectVo.setName(subjectName);
        subjectVo.setCode(subjectCode);
        subjectVo.setDescription(subjectDescription);

        createdSubjectId = subjectsFacade.createSubject(subjectVo).getId();

        SubjectVoFull subjectVoFull = subjectsFacade.getSubject(createdSubjectId);

        assertEquals(subjectVoFull.getVo().getId(), createdSubjectId);
        assertEquals(subjectVoFull.getVo().getName(), subjectName);
        assertEquals(subjectVoFull.getVo().getDescription(), subjectDescription);
        assertEquals(subjectVoFull.getVo().getCode(), subjectCode);


        subjectCode = 2468013;
        subjectName = "LECTO-ESCRITURA";
        subjectDescription = "Materia para nivelacion en la facultad de ingenieria.";
        subjectVo.setId(null);
        subjectVo.setName(subjectName);
        subjectVo.setCode(subjectCode);
        subjectVo.setDescription(subjectDescription);

        createdSubjectId = subjectsFacade.createSubject(subjectVo).getId();

        subjectVoFull = subjectsFacade.getSubject(createdSubjectId);

        assertEquals(subjectVoFull.getVo().getId(), createdSubjectId);
        assertEquals(subjectVoFull.getVo().getName(), subjectName);
        assertEquals(subjectVoFull.getVo().getDescription(), subjectDescription);

        subjectCode = 5555555;
        subjectName = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH)));
        subjectDescription = "";
        String injectedJavascriptCode = "<script type=" + '"' + "text/javascript" + '"' + ">document.getElementById(" + '"' + "demo" + '"' + ").innerHTML=Date();</script>";

        subjectVo.setId(null);
        subjectVo.setName(subjectName);
        subjectVo.setCode(subjectCode);
        subjectVo.setDescription(subjectDescription + injectedJavascriptCode);

        createdSubjectId = subjectsFacade.createSubject(subjectVo).getId();

        subjectVoFull = subjectsFacade.getSubject(createdSubjectId);

        assertEquals(subjectVoFull.getVo().getId(), createdSubjectId);
        assertEquals(subjectVoFull.getVo().getName(), subjectName);
        assertEquals(subjectVoFull.getVo().getDescription(), subjectDescription);


        subjectCode = 7654321;
        subjectName = "A";
        subjectDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)));

        subjectVo.setId(null);
        subjectVo.setName(subjectName);
        subjectVo.setCode(subjectCode);
        subjectVo.setDescription(subjectDescription);
        createdSubjectId = subjectsFacade.createSubject(subjectVo).getId();

        subjectVoFull = subjectsFacade.getSubject(createdSubjectId);

        assertEquals(subjectVoFull.getVo().getId(), createdSubjectId);
        assertEquals(subjectVoFull.getVo().getName(), subjectName);
        assertEquals(subjectVoFull.getVo().getDescription(), subjectDescription);


        subjectCode = 1234568;
        subjectName = "MATEMATICAS BASICAS";
        subjectDescription = "Materia para nivelacion en la facultad de ciencias.";

        subjectVo.setId(null);
        subjectVo.setName(subjectName);
        subjectVo.setCode(subjectCode);
        subjectVo.setDescription(subjectDescription);

        createdSubjectId = subjectsFacade.createSubject(subjectVo).getId();

        subjectVoFull = subjectsFacade.getSubject(createdSubjectId);

        assertEquals(subjectVoFull.getVo().getId(), createdSubjectId);
        assertEquals(subjectVoFull.getVo().getName(), subjectName);
        assertEquals(subjectVoFull.getVo().getDescription(), subjectDescription);
    }

    //@Ignore
    @Test
    public void test_BL_16_2() throws Exception {

        SubjectVo subjectVo = new SubjectVo();

        try {
            subjectVo.setId(null);
            subjectVo.setName(null);
            subjectVo.setCode(5343451);
            subjectVo.setDescription(null);
            subjectsFacade.createSubject(subjectVo);
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 1);
            assertTrue(ex.getMessages().contains("subject.name.null"));
        }

        try {
            subjectVo.setId(79L);
            subjectVo.setName("Materia convenio con los Andes");
            subjectVo.setCode(5343251);
            subjectVo.setDescription(null);
            subjectsFacade.createSubject(subjectVo);
            fail();
        } catch (Exception ex) {
            assertEquals(ex.getCause().getClass(), DataBaseException.class);
        }

        try {
            String tooLongName = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH)) + 1);
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 1);

            subjectVo.setId(Long.MIN_VALUE);
            subjectVo.setName(tooLongName);
            subjectVo.setCode(0);
            subjectVo.setDescription(tooLongDescription);
            subjectsFacade.createSubject(subjectVo);

            fail();

        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 2);
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
            assertTrue(ex.getMessages().contains("subject.name.tooLong"));
        }

        try {
            String injectedCodeInDescription = "<script type=" + '"' + "text/javascript" + '"' + ">document.getElementById(" + '"' + "demo" + '"' + ").innerHTML=Date();</script>";

            subjectVo.setId(null);
            subjectVo.setName(injectedCodeInDescription);
            subjectVo.setCode(0);
            subjectVo.setDescription(injectedCodeInDescription + "Hola");
            subjectsFacade.createSubject(subjectVo);
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 1);
            assertTrue(ex.getMessages().contains("subject.name.null"));
        }


        try {
            String injectedCodeInName = "<h1>Los profesores son unos tiranos</h1>";
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 1);

            subjectVo.setId(null);
            subjectVo.setName(injectedCodeInName);
            subjectVo.setCode(-1);
            subjectVo.setDescription(tooLongDescription);
            subjectsFacade.createSubject(subjectVo);
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 2);
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
            assertTrue(ex.getMessages().contains("subject.code.negativeValue"));
        }

    }

    //@Ignore
    @Test
    public void test_BL_16_3() throws MultipleMessagesException, Exception {
        try {
            SubjectVo subjectVo = new SubjectVo();
            subjectVo.setId(null);
            subjectVo.setName("Algoritmos");
            subjectVo.setCode(2019855);
            subjectVo.setDescription(null);
            subjectsFacade.createSubject(subjectVo);
            fail();
        } catch (DuplicityException ex) {
        }
    }

    //@Ignore
    @Test
    public void test_BL_17_1() throws Exception {
        SubjectVo subjectVo = new SubjectVo();

        Long subjectId = 2033859L;
        String newSubjectName = "INTRODUCCION A LAS FINANZAS";
        String newSubjectDescription = "Se bajo la exigencia del curso.";
        int newCode = 4555460;

        subjectVo.setId(subjectId);
        subjectVo.setName(newSubjectName);
        subjectVo.setCode(newCode);
        subjectVo.setDescription(newSubjectDescription);
        subjectsFacade.updateSubject(subjectVo);

        SubjectVoFull subject = subjectsFacade.getSubject(subjectId);

        assertEquals(subject.getVo().getId(), subjectId);
        assertEquals(subject.getVo().getName(), newSubjectName);
        assertEquals(subject.getVo().getDescription(), newSubjectDescription);
        assertEquals(subject.getVo().getCode(), newCode);

        subjectId = 2039461L;
        newSubjectName = "SEGURIDAD VIAL";
        newSubjectDescription = "Se tratan temas sobre auditorias de seguridad vial";
        newCode = 9009843;
        subjectVo.setId(subjectId);
        subjectVo.setName(newSubjectName);
        subjectVo.setCode(newCode);
        subjectVo.setDescription(newSubjectDescription);
        subjectsFacade.updateSubject(subjectVo);

        subject = subjectsFacade.getSubject(subjectId);

        assertEquals(subject.getVo().getId(), subjectId);
        assertEquals(subject.getVo().getName(), newSubjectName);
        assertEquals(subject.getVo().getDescription(), newSubjectDescription);
        assertEquals(subject.getVo().getCode(), newCode);

        subjectId = 2039372L;
        newSubjectName = "SOFTWARE";
        newSubjectDescription = "Los dos cursos se condensaron en uno solo.";
        newCode = subjectId.intValue();
        subjectVo.setId(subjectId);
        subjectVo.setName(newSubjectName);
        subjectVo.setCode(newCode);
        subjectVo.setDescription(newSubjectDescription);
        subjectsFacade.updateSubject(subjectVo);

        subject = subjectsFacade.getSubject(subjectId);

        assertEquals(subject.getVo().getId(), subjectId);
        assertEquals(subject.getVo().getName(), newSubjectName);
        assertEquals(subject.getVo().getDescription(), newSubjectDescription);
        assertEquals(subject.getVo().getCode(), newCode);
    }

    //@Ignore
    @Test
    public void test_BL_17_2() throws MultipleMessagesException, Exception {
        SubjectVo subjectVo = new SubjectVo();

        Long subjectId = 2016722L;
        String newSubjectName = "SOFTWARE";
        String newSubjectDescription = "Los dos cursos se condensaron en uno solo.";

        subjectVo.setId(subjectId);
        subjectVo.setName(newSubjectName);
        subjectVo.setCode(subjectId.intValue());
        subjectVo.setDescription(newSubjectDescription);

        SubjectVo subject = subjectsFacade.updateSubject(subjectVo);

        assertNull(subject);

        try {
            subjectVo.setId(null);
            subjectVo.setName("Nombre");
            subjectVo.setCode(0);
            subjectVo.setDescription(null);
            subjectsFacade.updateSubject(subjectVo);
        } catch (MultipleMessagesException e) {
            assertEquals(e.getMessages().size(), 1);
            assertEquals(e.getMessages().get(0), "subject.id.null");
        }
    }

    //@Ignore
    @Test
    public void test_BL_17_3() throws Exception {
        SubjectVo subjectVo = new SubjectVo();
        try {
            subjectVo.setId(2033859L);
            subjectVo.setName(null);
            subjectVo.setCode(-5789);
            subjectVo.setDescription(null);
            subjectsFacade.updateSubject(subjectVo);
            fail();
        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 2);
            assertTrue(ex.getMessages().contains("subject.name.null"));
            assertTrue(ex.getMessages().contains("subject.code.negativeValue"));
        }

        try {
            String tooLongName = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH)) + 10);
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 10);

            subjectVo.setId(2039461L);
            subjectVo.setName(tooLongName);
            subjectVo.setCode(479387);
            subjectVo.setDescription(tooLongDescription);
            subjectsFacade.updateSubject(subjectVo);

            fail();

        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 2);
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
            assertTrue(ex.getMessages().contains("subject.name.tooLong"));
        }

        try {
            String injectedCodeInDescription = "<script type=" + '"' + "text/javascript" + '"' + ">document.getElementById(" + '"' + "demo" + '"' + ").innerHTML=Date();</script>";
            subjectVo.setId(0L);
            subjectVo.setName("");
            subjectVo.setCode(0);
            subjectVo.setDescription(injectedCodeInDescription + "Hola");
            subjectsFacade.updateSubject(subjectVo);
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 1);
            assertTrue(ex.getMessages().contains("subject.name.null"));
        }


        try {
            String injectedCodeInName = "<h1>Los profesores son unos tiranos</h1>";
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 1);
            subjectVo.setId(2039461L);
            subjectVo.setName(injectedCodeInName);
            subjectVo.setCode(0);
            subjectVo.setDescription(tooLongDescription);
            subjectsFacade.updateSubject(subjectVo);
            fail();
        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 1);
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
        }

        try {
            subjectVo.setId(2039372L);
            subjectVo.setName("Materia con nuevo nombre");
            subjectVo.setCode(2019772);
            subjectVo.setDescription("No se tuvo mucha imaginación con el nombre de esta materia.");
            subjectsFacade.updateSubject(subjectVo);
            fail();
        } catch (DuplicityException ex) {
        }

    }

    //@Ignore
    @Test
    public void test_BL_18_1() throws Exception {

        Long[] subjects = {2023859L, 2029461L, 2029372L};

        for (Long i : subjects) {
            subjectsFacade.deleteSubject(i);
            assertNull(subjectsFacade.getSubject(i).getVo());
        }

    }

    //@Ignore
    @Test
    public void test_BL_18_2() throws Exception {
        Long[] subjects = {2016722L, 9283792L, 7409261L, 7583632L};
        for (Long subjectId : subjects) {
            try {
                subjectsFacade.deleteSubject(subjectId);
                fail();
            } catch (DataBaseException ex) {
                assertEquals("entity.notFound", ex.getMessage());
            }
        }
    }

    //@Ignore
    @Test
    public void test_BL_29_1() throws Exception {
        String string;
        Map<Long, String> map;

        string = "GeNi";
        map = subjectsFacade.getSubjectsAutocomplete(string);

        assertEquals(map.size(), 2);
        assertEquals("INGENIERIA DE SOFTWARE II", map.get(2016702L));
        assertEquals("INGENIERIA DE SOFTWARE AVANZADA", map.get(2019772L));


        string = "De";
        map = subjectsFacade.getSubjectsAutocomplete(string);

        assertEquals(map.size(), 3);
        assertEquals("INGENIERIA DE SOFTWARE II", map.get(2016702L));
        assertEquals("INGENIERIA DE SOFTWARE AVANZADA", map.get(2019772L));
        assertEquals("AUDITORIAS DE SEGURIDAD VIAL", map.get(2019855L));

        string = "rIa";
        map = subjectsFacade.getSubjectsAutocomplete(string);

        assertEquals(map.size(), 5);
        assertEquals("INGENIERIA DE SOFTWARE II", map.get(2016702L));
        assertEquals("INGENIERIA DE SOFTWARE AVANZADA", map.get(2019772L));
        assertEquals("AUDITORIAS DE SEGURIDAD VIAL", map.get(2019855L));
        assertEquals("AUDITORIA FINANCIERA I", map.get(2016025L));
        assertEquals("AUDITORIA TRIBUTARIA", map.get(2021814L));

    }

    //@Ignore
    @Test
    public void test_BL_29_2() throws Exception {
        assertEquals(subjectsFacade.getSubjectsAutocomplete(null).size(), 0);
    }

    //@Ignore
    @Test
    public void test_BL_30_1() throws Exception {
        RatingSummaryVo ratingSummary;

        ratingSummary = subjectsFacade.getSubjectsRatingSummary(2044356L);
        assertEquals(ratingSummary.getPositive(), 2);
        assertEquals(ratingSummary.getNegative(), 1);

        ratingSummary = subjectsFacade.getSubjectsRatingSummary(2042911L);
        assertEquals(ratingSummary.getPositive(), 1);
        assertEquals(ratingSummary.getNegative(), 2);
    }

    //@Ignore
    @Test
    public void test_BL_30_2() throws Exception {

        assertNull(subjectsFacade.getSubjectsRatingSummary(3372983L));

    }
}
