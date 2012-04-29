package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.CourseDao;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.PeriodEntity;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.vo.CourseVo;

/**
 * JpaDao for the Course value objects. Implements additionally some useful
 * finders by associated professor id, subject id and period id.
 *
 * @author saaperezru
 */
public class JpaCourseDao extends JpaCrudDao<CourseVo, CourseEntity> implements CourseDao<EntityManager> {

    /**
     * Returns a Course entity using the information in the provided Course
     * value object.
     *
     * @param em the data access adapter
     * @param vo the Course value object
     * @return the Course entity
     */
    @Override
    protected CourseEntity voToEntity(DataAccessAdapter<EntityManager> em, CourseVo vo) {
        CourseEntity courseEntity = new CourseEntity();

        courseEntity.setId(vo.getId());
        courseEntity.setRatingAverage(vo.getRatingAverage());
        courseEntity.setRatingCount(vo.getRatingCount());

        courseEntity.setPeriod(em.getDataAccess().getReference(PeriodEntity.class, vo.getPeriodId()));
        courseEntity.setProfessor(em.getDataAccess().getReference(ProfessorEntity.class, vo.getProfessorId()));
        courseEntity.setSubject(em.getDataAccess().getReference(SubjectEntity.class, vo.getSubjectId()));

        return courseEntity;
    }

    @Override
    protected Class<CourseEntity> getEntityClass() {
        return CourseEntity.class;
    }

    /**
     * Returns a list of Courses associated with the professor identified by the
     * given id
     *
     * @param em the data access adapter
     * @param professorId Professor identifier to look for in courses entities
     * @return The list of found courses
     */
    @Override
    public List<CourseVo> getByProfessorId(DataAccessAdapter<EntityManager> em, long professorId) throws DataAccessException {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCourseByProfessorId", getEntityClass()).setParameter("professorId", professorId).getResultList());
    }

    /**
     * Returns a list of Courses associated with the subject identified by the
     * given id
     *
     * @param em the data access adapter
     * @param subjectId Subject identifier to look for in courses entities
     * @return The list of found courses
     */
    @Override
    public List<CourseVo> getBySubjectId(DataAccessAdapter<EntityManager> em, long subjectId) throws DataAccessException {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCourseBySubjectId", getEntityClass()).setParameter("subjectId", subjectId).getResultList());
    }

    /**
     * Returns a list of Courses associated with the period identified by the
     * given id
     *
     * @param em the data access adapter
     * @param periodId Period identifier to look for in courses entities
     * @return The list of found courses
     */
    @Override
    public List<CourseVo> getByPeriodId(DataAccessAdapter<EntityManager> em, long periodId) throws DataAccessException {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCourseByPeriodId").setParameter("periodId", periodId).getResultList());
    }

    /**
     * Returns a list of Courses associated with the professor and subject
     * identified by the given ids
     *
     * @param em the data access adapter
     * @param professorId Professor identifier to look for in courses entities
     * @param subjectId Subject identifier to look for in courses entities
     * @return The list of found courses
     */
    @Override
    public List<CourseVo> getByProfessorIdAndSubjectId(DataAccessAdapter<EntityManager> em, long professorId, long subjectId) throws DataAccessException {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getCourseByProfessorIdAndSubjectId").setParameter("professorId", professorId).setParameter("subjectId", subjectId).getResultList());
    }

    /**
     * Returns a list of Courses associated with professors and subjects whose
     * names are like the ones provided in
     * <code>subjectName</code> and
     * <code>professorName</code>, and whose period corresponds to the one
     * identified by the id
     * <code>periodId</code>.
     *
     * @param em the data access adapter
     * @param professorName If null will search for any professor
     * @param subjectName If null will search for any subject
     * @param periodId If null will search for any period
     * @return The list of courses related to professors and subjects with names
     * like the ones provided.
     */
    @Override
    public List<CourseVo> getCoursesByProfessorNameLikeAndSubjectNameLike(DataAccessAdapter<EntityManager> em, String professorName, String subjectName, Long periodId) throws DataAccessException {
        checkDataAccessAdapter(em);

        String professor = (professorName == null) ? "%" : "%" + professorName + "%";
        String subject = (subjectName == null) ? "%" : "%" + subjectName + "%";
        if (periodId == null) {

            return entitiesToVos(em.getDataAccess().createNamedQuery("getCoursesByProfessorNameLikeAndSubjectNameLike",getEntityClass()).setParameter("professorName", professor).setParameter("subjectName", subject).getResultList());
        } else {

            return entitiesToVos(em.getDataAccess().createNamedQuery("getCoursesByProfessorNameLikeAndSubjectNameLikeAndPeriodId",getEntityClass()).setParameter("professorName", professor).setParameter("subjectName", subject).setParameter("periodId", periodId).getResultList());
        }
    }
}
