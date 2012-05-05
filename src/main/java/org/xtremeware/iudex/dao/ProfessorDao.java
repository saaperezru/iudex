package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProfessorEntity;

/**
 * DAO for the Professor entities. Implements additionally some useful finders
 * by name and subject
 *
 * @author juan
 */
public class ProfessorDao extends CrudDao<ProfessorEntity> implements ProfessorDaoInterface {

    /**
     * Professors finder according to a required name
     *
     * @param em the entity manager
     * @param name Professor's first-name or last-name
     * @return List of professors whose first-name or last-name are equal to the
     * specified
     */
    @Override
    public List<ProfessorEntity> getByNameLike(EntityManager em, String name) {
        checkEntityManager(em);
        return em.createNamedQuery("getProfessorByNameLike", ProfessorEntity.class).setParameter("name", name).getResultList();
    }

    /**
     * Professors finder according to the subjects they offer
     *
     * @param em the entity manager
     * @param subjectId The ID of the required subject
     * @return A list of professors that impart the subject
     */
    @Override
    public List<ProfessorEntity> getBySubjectId(EntityManager em, long subjectId) {
        checkEntityManager(em);
        return em.createNamedQuery("getProfessorBySubjectId", ProfessorEntity.class).setParameter("subjectId", subjectId).getResultList();
    }
}
