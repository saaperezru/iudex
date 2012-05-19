package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.SubjectDaoInterface;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO for subject entities. Implements additionally some useful finders by name
 * or by professor id
 *
 * @author healarconr
 */
public class SubjectDao extends CrudDao<SubjectEntity> implements SubjectDaoInterface {

    /**
     * Returns a list of subject entities which name contains the argument name
     *
     * @param entityManager the entity manager
     * @param name the subject's name
     * @return a list of matched subject entities
     */
    @Override
    public List<SubjectEntity> getByName(EntityManager entityManager, String name) throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getSubjectsByNameLike", SubjectEntity.class).
                    setParameter("name", "%" + name + "%").getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Returns a list of subject entities associated with a specific professor
     *
     * @param entityManager the entity manager
     * @param professorId the professor's id
     * @return a list of matched subject entities
     */
    @Override
    public List<SubjectEntity> getByProfessorId(EntityManager entityManager, long professorId) throws
            DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getSubjectsByProfessorId", SubjectEntity.class).
                    setParameter("professorId", professorId).getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }

    }

    @Override
    protected Class getEntityClass() {
        return SubjectEntity.class;
    }
}
