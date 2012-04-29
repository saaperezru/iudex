package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.dao.jpa.JpaSubjectDao;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.CourseVo;
import org.xtremeware.iudex.vo.SubjectRatingVo;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public class SubjectsService extends SimpleCrudService<SubjectVo> {

    /**
     * SubjectsService constructor
     *
     * @param daoFactory
     */
    public SubjectsService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    /**
     * returns the JpaSubjectDao to be used.
     *
     * @return
     */
    @Override
    protected CrudDao<SubjectVo, ?> getDao() {
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
    public void validateVo(DataAccessAdapter em, SubjectVo vo) throws InvalidVoException, ExternalServiceConnectionException {
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
        vo.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
        vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
    }

    /**
     * Remove the subject and all the subjectRatings and courses associated to
     * it.
     *
     * @param em entity manager
     * @param id id of the subject
     */
    @Override
    public void remove(DataAccessAdapter em, long id) throws DataAccessException {
        List<SubjectRatingVo> subjectRatings = getDaoFactory().getSubjectRatingDao().getBySubjectId(em, id);

        for (SubjectRatingVo rating : subjectRatings) {
            getDaoFactory().getSubjectRatingDao().remove(em, rating.getId());
        }

        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CourseVo> courses = getDaoFactory().getCourseDao().getBySubjectId(em, id);

        CoursesService courseService = Config.getInstance().getServiceFactory().createCoursesService();
        for (CourseVo course : courses) {
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
    public List<SubjectVo> search(DataAccessAdapter em, String query) throws DataAccessException {
        if (query == null) {
            throw new IllegalArgumentException("Null query for a subject search");
        }
        return ((JpaSubjectDao) this.getDao()).getByName(em, query);
    }

    /**
     * Returns a list of SubjectVo according with the search name
     *
     * @param em EntityManager
     * @param name String with the name of the SubjectVo
     * @return A list if SubjectVo
     */
    public List<SubjectVo> getByNameLike(DataAccessAdapter em, String name) throws DataAccessException {
        if (name == null) {
            throw new IllegalArgumentException("Null name for a subject search");
        }

        return ((JpaSubjectDao) this.getDao()).getByName(em, name);
    }
}
