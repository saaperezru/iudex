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
public interface SubjectDaoInterface extends CrudDaoInterface<SubjectEntity> {

    /**
     * Returns a list of subject entities which name contains the argument name
     *
     * @param em the entity manager
     * @param name the subject's name
     * @return a list of matched subject entities
     */
    public List<SubjectEntity> getByName(EntityManager em, String name)throws
            DataBaseException;

    /**
     * Returns a list of subject entities associated with a specific professor
     *
     * @param em the entity manager
     * @param professorId the professor's id
     * @return a list of matched subject entities
     */
    public List<SubjectEntity> getByProfessorId(EntityManager em, long professorId)throws
            DataBaseException;
}
