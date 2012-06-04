package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 * DAO Interface for the Professor entities.
 *
 * @author juan
 */
public interface ProfessorDao extends CrudDao<ProfessorEntity> {

    /**
     * Professors finder according to a required name
     *
     * @param entityManager the entity manager
     * @param name Professor's first-name or last-name
     * @return List of professors whose first-name or last-name are equal to the
     * specified
     */
    List<Long> getByNameLike(EntityManager entityManager, String professorName)
            throws DataBaseException;

    /**
     * Professors finder according to the subjects they offer
     *
     * @param entityManager the entity manager
     * @param subjectId The ID of the required subject
     * @return A list of professors that impart the subject
     */
    List<ProfessorEntity> getBySubjectId(EntityManager entityManager, long subjectId)
            throws DataBaseException;
}
