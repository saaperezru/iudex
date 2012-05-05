package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProfessorEntity;

/**
 * DAO Interface for the Professor entities. 
 * 
 * @author juan
 */
public interface ProfessorDaoInterface extends CrudDaoInterface<ProfessorEntity> {

    /**
     * Professors finder according to a required name
     *
     * @param em the entity manager
     * @param name Professor's first-name or last-name
     * @return List of professors whose first-name or last-name are equal to the
     * specified
     */
    public List<ProfessorEntity> getByNameLike(EntityManager em, String name) ;

    /**
     * Professors finder according to the subjects they offer
     *
     * @param em the entity manager
     * @param subjectId The ID of the required subject
     * @return A list of professors that impart the subject
     */
    public List<ProfessorEntity> getBySubjectId(EntityManager em, long subjectId) ;
}
