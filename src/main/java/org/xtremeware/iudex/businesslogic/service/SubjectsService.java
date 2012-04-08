package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.SubjectDao;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.entity.SubjectRatingEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public class SubjectsService extends SimpleCrudService<SubjectVo, SubjectEntity> {

    /**
     * SubjectsService constructor
     *
     * @param daoFactory
     */
    public SubjectsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the SubjectDao to be used.
     *
     * @return
     */
    @Override
    protected Dao<SubjectEntity> getDao() {
        return getDaoFactory().getSubjectDao();
    }
    

    /**
     * Validate the provided SubjectVo, if the SubjectVo is not correct the
     * methods throws an exception
     *
     * @param em EntityManager
     * @param vo SubjectVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVo(EntityManager em, SubjectVo vo) throws InvalidVoException {
        if (vo == null) {
            throw new InvalidVoException("Null SubjectVo");
        }
        if (vo.getName() == null) {
            throw new InvalidVoException("Null name in the provided SubjectVo");
        }
        if (vo.getDescription() == null) {
            throw new InvalidVoException("Null description in the provided SubjectVo");
        }
        if (vo.getName().length() > 50) {
            throw new InvalidVoException("Invalid name length in the provided SubjectVo");
        }
        if (vo.getDescription().length() > 2000) {
            throw new InvalidVoException("Invalid description length in the provided SubjectVo");
        }
    }

    /**
     * Returns a SubjectEntity using the information in the provided SubjectVo.
     *
     * @param em EntityManager
     * @param vo SubjectVo
     * @return SubjectEntity
     * @throws InvalidVoException
     */
    @Override
    public SubjectEntity voToEntity(EntityManager em, SubjectVo vo) throws InvalidVoException, ExternalServiceConnectionException {

        validateVo(em, vo);

        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(vo.getId());
        subjectEntity.setName(SecurityHelper.sanitizeHTML(vo.getName()));
        subjectEntity.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));

        return subjectEntity;
    }

   /**
     * Remove the subject and all the subjectRatings and courses associated  to it.
     * 
     * @param em entity manager
     * @param id id of the subject
     */    
    @Override
    public void remove(EntityManager em, long id) {
            List<SubjectRatingEntity> subjectRatings = getDaoFactory().getSubjectRatingDao().getBySubjectId(em, id);
                for (SubjectRatingEntity rating : subjectRatings){
                    getDaoFactory().getSubjectRatingDao().remove(em,rating.getId());
                }
            
            /**
            * This is a bad implementation, but due to few time, it had to be implemented,
            * it will be changed for the next release.
            */
            List<CourseEntity> courses = getDaoFactory().getCourseDao().getBySubjectId(em, id);

            CoursesService courseService = Config.getInstance().getServiceFactory().createCoursesService();
            for (CourseEntity course : courses){
                    courseService.remove(em, course.getId());    
            } 
            
            getDao().remove(em, id);
    }
    
    /**
     * Returns a list of SubjectVo according with the search query
     *
     * @param em EntityManager
     * @param query String with the search parameter
     * @return A list of SubjectVo
     */
    public List<SubjectVo> search(EntityManager em, String query) {
        if (query == null) {
            throw new IllegalArgumentException("Null query for a subject search");
        }
        List<SubjectEntity> subjectEntitys = ((SubjectDao) this.getDao()).getByName(em, query);
        if (subjectEntitys.isEmpty()) {
            return null;
        }
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }

    /**
     * Returns a list of SubjectVo according with the search name
     *
     * @param em EntityManager
     * @param name String with the name of the SubjectVo
     * @return A list if SubjectVo
     */
    public List<SubjectVo> getByNameLike(EntityManager em, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name for a subject search");
        }
        List<SubjectEntity> subjectEntitys = ((SubjectDao) this.getDao()).getByName(em, name);
        if (subjectEntitys.isEmpty()) {
            return null;
        }
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }
}
