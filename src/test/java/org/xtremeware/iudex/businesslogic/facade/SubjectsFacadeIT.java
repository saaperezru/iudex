/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.facade;

import java.util.Map;
import javax.persistence.EntityManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwFull;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;
import org.xtremeware.iudex.vo.SubjectVo;
import org.xtremeware.iudex.businesslogic.helper.FacadesTestHelper;

/**
 *
 * @author juan
 */
public class SubjectsFacadeIT {

    private EntityManager entityManager;
    private SubjectsFacade subjectsFacade = Config.getInstance().getFacadeFactory().getSubjectsFacade();

    public SubjectsFacadeIT() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        FacadesTestHelper.initializeDatabase();
    }

    @Before
    public void setUp() {
        entityManager = FacadesTestHelper.createEntityManagerFactory().createEntityManager();
    }

    @Test
    public void test_BL_12_1() throws DataBaseException,
            MultipleMessagesException, Exception {
        Long userId = 5L;

        Long[] subjects = {2016702L, 2021814L, 2016025L};
        int[] ratings = {-1, 0, 1};

        SubjectRatingVo subjectRatingVo;

        for (int i = 0; i < subjects.length; i++) {

            subjectsFacade.rateSubject(userId, subjects[i], ratings[i]);

            subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjects[i],
                    userId);

            assertNotNull(subjectRatingVo);
            assertEquals(subjectRatingVo.getUser(),
                    userId);
            assertEquals(subjectRatingVo.getEvaluatedObjectId(),
                    subjects[i]);
            assertEquals(subjectRatingVo.getValue(), ratings[i]);
        }

    }

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

    @Test
    public void test_BL_12_3() throws MultipleMessagesException,
            Exception {
        SubjectRatingVo subjectRatingVo;

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

    @Test
    public void test_BL_12_4() throws MultipleMessagesException,
            Exception {
        SubjectRatingVo subjectRatingVo;

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

    @Test
    public void test_BL_16_1() throws MultipleMessagesException, Exception {

        Long subjectId = 1234567L;
        String subjectName = "MATEMATICAS BASICAS";
        String subjectDescription = "Materia para nivelacion en la facultad de ingenieria.";

        subjectsFacade.addSubject(subjectId, subjectName, subjectDescription);

        SubjectVoVwFull subjectVoVw = subjectsFacade.getSubject(subjectId);

        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);


        subjectId = 2468013L;
        subjectName = "LECTO-ESCRITURA";
        subjectDescription = "Materia para nivelacion en la facultad de ingenieria.";

        subjectId = subjectsFacade.addSubject(subjectId, subjectName, subjectDescription).getId();

        subjectVoVw = subjectsFacade.getSubject(subjectId);

        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);

        subjectId = 5555555L;
        subjectName = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH)));
        subjectDescription = "";
        String injectedJavascriptCode = "<script type=" + '"' + "text/javascript" + '"' + ">document.getElementById(" + '"' + "demo" + '"' + ").innerHTML=Date();</script>";

        subjectId = subjectsFacade.addSubject(subjectId, subjectName, subjectDescription + injectedJavascriptCode).getId();

        subjectVoVw = subjectsFacade.getSubject(subjectId);

        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);


        subjectId = 7654321L;
        subjectName = "A";
        subjectDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)));

        subjectId = subjectsFacade.addSubject(subjectId, subjectName, subjectDescription).getId();

        subjectVoVw = subjectsFacade.getSubject(subjectId);

        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);


        subjectId = 1234568L;
        subjectName = "MATEMATICAS BASICAS";
        subjectDescription = "Materia para nivelacion en la facultad de ciencias.";

        subjectsFacade.addSubject(subjectId, subjectName, subjectDescription);

        subjectVoVw = subjectsFacade.getSubject(subjectId);

        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);
    }

    @Test
    public void test_BL_16_2() throws Exception {

        try {
            subjectsFacade.addSubject(null, null, null);
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 2);
            assertTrue(ex.getMessages().contains("subject.id.null"));
            assertTrue(ex.getMessages().contains("subject.name.null"));
        }

        try {
            String tooLongName = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH)) + 1);
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 1);

            subjectsFacade.addSubject(Long.MIN_VALUE, tooLongName, tooLongDescription);

            fail();

        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 2);
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
            assertTrue(ex.getMessages().contains("subject.name.tooLong"));
        }

        try {
            String injectedCodeInDescription = "<script type=" + '"' + "text/javascript" + '"' + ">document.getElementById(" + '"' + "demo" + '"' + ").innerHTML=Date();</script>";
            subjectsFacade.addSubject(0L, "", injectedCodeInDescription + "Hola");
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 1);
            assertTrue(ex.getMessages().contains("subject.name.null"));
        }


        try {
            String injectedCodeInName = "<h1>Los profesores son unos tiranos</h1>";
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 1);
            subjectsFacade.addSubject(null, injectedCodeInName, tooLongDescription);
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 2);
            assertTrue(ex.getMessages().contains("subject.id.null"));
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
        }

    }

    @Test
    public void test_BL_16_3() throws MultipleMessagesException, Exception {
    }

    @Test
    public void test_BL_17_1() throws Exception {
        Long subjectId = 2033859L;
        String newSubjectName = "INTRODUCCION A LAS FINANZAS";
        String newSubjectDescription = "Se bajo la exigencia del curso.";
        subjectsFacade.updateSubject(subjectId, newSubjectName, newSubjectDescription);

        SubjectVoVwFull subject = subjectsFacade.getSubject(subjectId);

        assertEquals(subject.getId(), subjectId);
        assertEquals(subject.getName(), newSubjectName);
        assertEquals(subject.getDescription(), newSubjectDescription);

        subjectId = 2039461L;
        newSubjectName = "SEGURIDAD VIAL";
        newSubjectDescription = "Se tratan temas sobre auditorias de seguridad vial";
        subjectsFacade.updateSubject(subjectId, newSubjectName, newSubjectDescription);

        subject = subjectsFacade.getSubject(subjectId);

        assertEquals(subject.getId(), subjectId);
        assertEquals(subject.getName(), newSubjectName);
        assertEquals(subject.getDescription(), newSubjectDescription);

        subjectId = 2039372L;
        newSubjectName = "SOFTWARE";
        newSubjectDescription = "Los dos cursos se condensaron en uno solo.";
        subjectsFacade.updateSubject(subjectId, newSubjectName, newSubjectDescription);

        subject = subjectsFacade.getSubject(subjectId);

        assertEquals(subject.getId(), subjectId);
        assertEquals(subject.getName(), newSubjectName);
        assertEquals(subject.getDescription(), newSubjectDescription);
    }

    @Test
    public void test_BL_17_2() throws MultipleMessagesException, Exception {
        Long subjectId = 2016722L;
        String newSubjectName = "SOFTWARE";
        String newSubjectDescription = "Los dos cursos se condensaron en uno solo.";

        SubjectVo subject = subjectsFacade.updateSubject(subjectId, newSubjectName, newSubjectDescription);

        assertNull(subject);

        try {
            subjectsFacade.updateSubject(null, null, null);
        } catch (MultipleMessagesException e) {
            assertEquals(e.getMessages().size(), 2);
            assertTrue(e.getMessages().contains("subject.id.null"));
            assertTrue(e.getMessages().contains("subject.name.null"));
        }
    }

    @Test
    public void test_BL_17_3() throws Exception {
        try {
            subjectsFacade.updateSubject(null, null, null);
            fail();
        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 2);
            assertTrue(ex.getMessages().contains("subject.id.null"));
            assertTrue(ex.getMessages().contains("subject.name.null"));
        }

        try {
            String tooLongName = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH)) + 1);
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 1);

            subjectsFacade.updateSubject(Long.MIN_VALUE, tooLongName, tooLongDescription);

            fail();

        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 2);
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
            assertTrue(ex.getMessages().contains("subject.name.tooLong"));
        }

        try {
            String injectedCodeInDescription = "<script type=" + '"' + "text/javascript" + '"' + ">document.getElementById(" + '"' + "demo" + '"' + ").innerHTML=Date();</script>";
            subjectsFacade.updateSubject(0L, "", injectedCodeInDescription + "Hola");
            fail();
        } catch (MultipleMessagesException ex) {
            assertTrue(ex.getMessages().size() == 1);
            assertTrue(ex.getMessages().contains("subject.name.null"));
        }


        try {
            String injectedCodeInName = "<h1>Los profesores son unos tiranos</h1>";
            String tooLongDescription = FacadesTestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)) + 1);
            subjectsFacade.updateSubject(null, injectedCodeInName, tooLongDescription);
            fail();
        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 2);
            assertTrue(ex.getMessages().contains("subject.id.null"));
            assertTrue(ex.getMessages().contains("subject.description.tooLong"));
        }
    }

    @Test
    public void test_BL_18_1() throws Exception {

        Long[] subjects = {2023859L, 2029461L, 2029372L};

        for (Long i : subjects) {
            subjectsFacade.removeSubject(i);
            assertNull(subjectsFacade.getSubject(i).getVo());
        }

    }

    @Test
    public void test_BL_18_2() throws Exception {
        Long[] subjects = {2016722L, 9283792L, 7409261L, 7583632L};
        for (Long subjectId : subjects) {
            try {
                subjectsFacade.removeSubject(subjectId);
                fail();
            } catch (DataBaseException ex) {
            }
        }
    }

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

    @Test
    public void test_BL_29_2() throws Exception {
        assertEquals(subjectsFacade.getSubjectsAutocomplete(null).size(), 0);
    }

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

    @Test
    public void test_BL_30_2() throws Exception {

        assertNull(subjectsFacade.getSubjectsRatingSummary(3372983L));

    }
}
