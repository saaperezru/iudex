package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.jpa.JpaCrudDao;
import org.xtremeware.iudex.dao.jpa.JpaProfessorDao;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.CourseVo;
import org.xtremeware.iudex.vo.ProfessorRatingVo;
import org.xtremeware.iudex.vo.ProfessorVo;

public class ProfessorsService extends SimpleCrudService<ProfessorVo> {

    private final int MAX_PROFESSOR_NAME_LENGTH;

    public ProfessorsService(AbstractDaoFactory daoFactory) throws ExternalServiceConnectionException {
        super(daoFactory);
        MAX_PROFESSOR_NAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_PROFESSOR_NAME_LENGTH));
    }

    public List<ProfessorVo> getByNameLike(DataAccessAdapter em, String name) {
        return ((JpaProfessorDao) getDao()).getByName(em, name);
    }

    public List<ProfessorVo> getBySubjectId(DataAccessAdapter em, long subjectId) {
        return ((JpaProfessorDao) getDao()).getBySubjectId(em, subjectId);
    }

    @Override
    protected CrudDao<ProfessorVo, ?> getDao() {
        return getDaoFactory().getProfessorDao();
    }

    @Override
    public void validateVo(DataAccessAdapter em, ProfessorVo vo) throws InvalidVoException, ExternalServiceConnectionException {
        if (vo == null) {
            throw new InvalidVoException("Null ProfessorVo");
        }
        if (vo.getFirstName() == null) {
            throw new InvalidVoException("String firstName in the provided ProgramVo cannot be null");
        }
        if (vo.getLastName() == null) {
            throw new InvalidVoException("String lastName in the provided ProgramVo cannot be null");
        }
        if (vo.getDescription() == null) {
            throw new InvalidVoException("String description in the provided ProgramVo cannot be null");
        }
        if (vo.getEmail() == null) {
            throw new InvalidVoException("String email in the provided ProgramVo cannot be null");
        }
        if (vo.getImageUrl() == null) {
            throw new InvalidVoException("String imageUrl in the provided ProgramVo cannot be null");
        }
        if (vo.getWebsite() == null) {
            throw new InvalidVoException("String website in the provided ProgramVo cannot be null");
        }
        if (vo.getImageUrl().length() > 0 && !ValidityHelper.isValidUrl(vo.getImageUrl())) {
            throw new InvalidVoException("String imageUrl in the provided ProgamVo must be a valid URL");
        }
        if (vo.getEmail().length() > 0 && !ValidityHelper.isValidEmail(vo.getEmail())) {
            throw new InvalidVoException("Strng email in the provided ProgramVo must be a valid email address");
        }
        if (vo.getWebsite().length() > 0 && !ValidityHelper.isValidUrl(vo.getWebsite())) {
            throw new InvalidVoException("String website in provided ProgramVo must be a valid URL");
        }
        if (vo.getFirstName().length() > MAX_PROFESSOR_NAME_LENGTH || vo.getLastName().length() > MAX_PROFESSOR_NAME_LENGTH) {
            throw new InvalidVoException("String firstName and String lastName length must be less than " + String.valueOf(MAX_PROFESSOR_NAME_LENGTH));
        }
        vo.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
        vo.setEmail(SecurityHelper.sanitizeHTML(vo.getEmail()));
        vo.setFirstName(SecurityHelper.sanitizeHTML(vo.getFirstName()));
        vo.setImageUrl(SecurityHelper.sanitizeHTML(vo.getImageUrl()));
        vo.setLastName(SecurityHelper.sanitizeHTML(vo.getLastName()));
        vo.setWebsite(SecurityHelper.sanitizeHTML(vo.getWebsite()));
    }

    /**
     * Remove the professor and all the professorRatings and courses associated
     * to him.
     *
     * @param em entity manager
     * @param id id of the professor
     */
    @Override
    public void remove(DataAccessAdapter em, long id) {
        List<ProfessorRatingVo> professorRatings = getDaoFactory().getProfessorRatingDao().getByProfessorId(em, id);

        for (ProfessorRatingVo rating : professorRatings) {
            getDaoFactory().getProfessorRatingDao().remove(em, rating.getId());
        }


        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CourseVo> courses = getDaoFactory().getCourseDao().getByProfessorId(em, id);

        CoursesService courseService = Config.getInstance().getServiceFactory().createCoursesService();
        for (CourseVo course : courses) {
            courseService.remove(em, course.getId());
        }

        getDao().remove(em, id);
    }
}
