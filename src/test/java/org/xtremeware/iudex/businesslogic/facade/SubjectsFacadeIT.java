/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtremeware.iudex.businesslogic.facade;

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
import org.xtremeware.iudex.vo.SubjectRatingVo;

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
        TestHelper.initializeDatabase();
    }

    @Before
    public void setUp() {
        entityManager = TestHelper.createEntityManager();
    }

    
   @Test
    public void test_BL_12_1() throws DataBaseException, MultipleMessagesException, Exception {
        Long userId = 5L;

        Long[] subjects = {2016702L, 2021814L, 2016025L};
        int[] ratings = {-1, 0, 1};

        SubjectRatingVo subjectRatingVo;

        for (int i = 0; i < subjects.length; i++) {
            
            subjectsFacade.rateSubject(userId, subjects[i], ratings[i]);
            
            subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjects[i], userId);

            assertNotNull(subjectRatingVo);
            assertEquals(subjectRatingVo.getUser(), userId);
            assertEquals(subjectRatingVo.getEvaluetedObjectId(), subjects[i]);
            assertEquals(subjectRatingVo.getValue(), ratings[i]);
        }
    }

    @Test
    public void test_BL_12_2() throws Exception{
        
        Long subjectId = 2021815L;
        Long userId = 2L;
        
        SubjectRatingVo subjectRatingVo;
        try {
            subjectsFacade.rateSubject(userId, subjectId, 0);
            fail();
        } catch (MultipleMessagesException ex) {
            assertEquals(ex.getMessages().size(), 1);
            assertEquals(ex.getMessages().get(0), "subjectRating.subjectId.element.notFound");
        }
        
    }
 
    @Test
    public void test_BL_12_3() throws MultipleMessagesException, Exception {
        SubjectRatingVo subjectRatingVo;
        
        Long userId = 2L;
        Long subjectId = 2016702L;
        int rating = 0;

        subjectsFacade.rateSubject(userId, subjectId, rating);
        
        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId, userId);
        
        assertEquals(subjectRatingVo.getValue(), rating);

        rating = -1;

        subjectsFacade.rateSubject(userId, subjectId, rating);
        
        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId, userId);
        
        assertEquals(subjectRatingVo.getValue(), rating);
        
        rating = -1;

        subjectsFacade.rateSubject(userId, subjectId, rating);
        
        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId, userId);
        
        assertEquals(subjectRatingVo.getValue(), rating);
        
    }

    @Test
    public void test_BL_12_4() throws MultipleMessagesException, Exception {
        SubjectRatingVo subjectRatingVo;
        
        Long userId = 2L;
        Long subjectId = 2016025L;
        int rating = 1;

        subjectsFacade.rateSubject(userId, subjectId, rating);
        
        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId, userId);
        
        assertEquals(subjectRatingVo.getValue(), rating);

        subjectId = 2019855L;
        rating = -1;

        subjectsFacade.rateSubject(userId, subjectId, rating);
        
        subjectRatingVo = subjectsFacade.getSubjectRatingByUserId(subjectId, userId);
        
        assertEquals(subjectRatingVo.getValue(), rating);
        
    }

    @Test
    public void test_BL_16_1() throws MultipleMessagesException, Exception {
        
        Long subjectId =1234567L;
        String subjectName ="MATEMATICAS BASICAS";
        String subjectDescription = "Materia para nivelacion en la facultad de ingenieria.";
        
        subjectsFacade.addSubject(subjectId, subjectName ,subjectDescription);
        
        SubjectVoVwFull subjectVoVw = subjectsFacade.getSubject(subjectId);
        
        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);
        
        
        subjectId =2468013L;
        subjectName ="LECTO-ESCRITURA";
        subjectDescription = "Materia para nivelacion en la facultad de ingenieria.";
        
        subjectId =subjectsFacade.addSubject(subjectId, subjectName ,subjectDescription).getId();
        
        subjectVoVw = subjectsFacade.getSubject(subjectId);
        
        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);

        subjectId =5555555L;
        subjectName =TestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_NAME_LENGTH)));
        subjectDescription = "";
        
        subjectId =subjectsFacade.addSubject(subjectId, subjectName ,subjectDescription).getId();
        
        subjectVoVw = subjectsFacade.getSubject(subjectId);
        
        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);
        
        
        subjectId =7654321L;
        subjectName = "A";
        subjectDescription = TestHelper.randomString(Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_SUBJECT_DESCRIPTION_LENGTH)));
        
        subjectId =subjectsFacade.addSubject(subjectId, subjectName ,subjectDescription).getId();
        
        subjectVoVw = subjectsFacade.getSubject(subjectId);
        
        assertEquals(subjectVoVw.getId(), subjectId);
        assertEquals(subjectVoVw.getName(), subjectName);
        assertEquals(subjectVoVw.getDescription(), subjectDescription);
        
    }

    /*@Test
    public void test_BL_16_2() {
    }

    @Test
    public void test_BL_16_3() {
    }

    @Test
    public void test_BL_17_1() {
    }

    @Test
    public void test_BL_17_2() {
    }

    @Test
    public void test_BL_18_1() {
    }*/
}
