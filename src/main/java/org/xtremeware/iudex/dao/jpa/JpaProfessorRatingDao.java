package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.ProfessorRatingDao;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.ProfessorRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 * DAO for the ProfessorRatingVo. Implements additionally some useful
 * finders by professor and user
 *
 * @author juan
 */
public class JpaProfessorRatingDao extends JpaCrudDao< ProfessorRatingVo, ProfessorRatingEntity> implements ProfessorRatingDao<EntityManager> {

    /**
     * Professors ratings finder according to a specified professor
     *
     * @param em the DataAccessAdapter
     * @param professorId Professor's ID
     * @return A list with all ratings associated to the specified professor
     */
    @Override
    public List<ProfessorRatingVo> getByProfessorId(DataAccessAdapter<EntityManager> em, long professorId) {
        checkDataAccessAdapter(em);
        List<ProfessorRatingEntity> list = em.getDataAccess().createNamedQuery("getRatingByProfessorId", ProfessorRatingEntity.class).setParameter("professor", professorId).getResultList();
        return entitiesToVos(list);
    }

    /**
     * Professor ratings finder according to a professor and a student
     *
     * @param em the DataAccessAdapter
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return The rating a student has submitted to a professor
     */
    @Override
    public ProfessorRatingVo getByProfessorIdAndUserId(DataAccessAdapter<EntityManager> em, long professorId, long userId) {
        checkDataAccessAdapter(em);
        try {
            return em.getDataAccess().createNamedQuery("getRatingByProfessorIdAndUserId", ProfessorRatingEntity.class).setParameter("professor", professorId).setParameter("user", userId).getSingleResult().toVo();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * Professors ratings finder according to a specified user
     *
     * @param em the DataAccessAdapter
     * @param userId user's ID
     * @return A list with all ratings associated to the specified user
     */
    @Override
    public List<ProfessorRatingVo> getByUserId(DataAccessAdapter<EntityManager> em, long userId) {
        checkDataAccessAdapter(em);
        List<ProfessorRatingEntity> list = em.getDataAccess().createNamedQuery("getRatingByUserId", ProfessorRatingEntity.class).setParameter("user", userId).getResultList();
        return entitiesToVos(list);
    }

    /**
     * Professor rating summary calculator
     *
     * @param em the DataAccessAdapter
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified
     * professor has obtained positive and negative ratings
     */
    @Override
    public RatingSummaryVo getSummary(DataAccessAdapter<EntityManager> em, long professorId) {
        checkDataAccessAdapter(em);

        RatingSummaryVo rsv = new RatingSummaryVo();

        try {
            rsv.setPositive(em.getDataAccess().createNamedQuery("countPositiveProfessorRating", Long.class).setParameter("professorId", professorId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        try {
            rsv.setNegative(em.getDataAccess().createNamedQuery("countNegativeProfessorRating", Long.class).setParameter("professorId", professorId).getSingleResult().intValue());
        } catch (NoResultException noResultException) {
            return null;
        }

        return rsv;
    }

    @Override
    protected ProfessorRatingEntity voToEntity(DataAccessAdapter<EntityManager> em, ProfessorRatingVo vo) {
        ProfessorRatingEntity entity = new ProfessorRatingEntity();
        entity.setId(vo.getId());
        entity.setProfessor(em.getDataAccess().getReference(ProfessorEntity.class, vo.getEvaluetedObjectId()));
        entity.setUser(em.getDataAccess().getReference(UserEntity.class, vo.getUser()));
        entity.setValue(vo.getValue());
        return entity;
    }

    private List<ProfessorRatingVo> entitiesToVos(List<ProfessorRatingEntity> list) {
        ArrayList<ProfessorRatingVo> arrayList = new ArrayList<ProfessorRatingVo>();
        for (ProfessorRatingEntity entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }

    @Override
    protected Class getEntityClass() {
        return ProfessorRatingEntity.class;
    }
}
