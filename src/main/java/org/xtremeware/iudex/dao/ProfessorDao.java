package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.ProfessorVo;

/**
 * DAO for the ProfessorVo. Implements additionally some useful finders by name
 * and subject
 *
 * @author josebermeo
 */
public interface ProfessorDao<E> extends CrudDao<ProfessorVo, E> {

    /**
     * Professors finder according to a required name
     *
     * @param em the DataAccessAdapter
     * @param name Professor's firstname or lastname
     * @return List of professors whose firstname or lastname are equal to the
     * specified
     */
    public List<ProfessorVo> getByName(DataAccessAdapter<E> em, String name);

    /**
     * Professors finder according to the subjects they offer
     *
     * @param em the DataAccessAdapter
     * @param subjectId The ID of the required subject
     * @return A list of professors that impart the subject
     */
    public List<ProfessorVo> getBySubjectId(DataAccessAdapter<E> em, long subjectId);
}
