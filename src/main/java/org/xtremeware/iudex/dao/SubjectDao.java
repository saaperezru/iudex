package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for subject entities.
 *
 * @author healarconr
 */
public interface SubjectDao extends CrudDao<SubjectEntity> {

    /**
     * Returns a list of subject entities which name contains the argument name
     *
     * @param entityManager the entity manager
     * @param name the subject's name
     * @return a list of matched subject entities
     */
    List<Long> getByName(EntityManager entityManager, String name)
            throws DataBaseException;

    /**
     * Returns a list of subject entities associated with a specific professor
     *
     * @param entityManager the entity manager
     * @param professorId the professor's id
     * @return a list of matched subject entities
     */
    List<SubjectEntity> getByProfessorId(EntityManager entityManager, long professorId)
            throws DataBaseException;
}
