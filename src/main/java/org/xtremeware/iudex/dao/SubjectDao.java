package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public interface SubjectDao<E> extends CrudDao<SubjectVo, E> {

    /**
     * Returns a list of subject entities which name contains the argument name
     *
     * @param em the DataAccessAdapter
     * @param name the subject's name
     * @return a list of matched subjectVo
     */
    public List<SubjectVo> getByName(DataAccessAdapter<E> em, String name)throws DataAccessException;

    /**
     * Returns a list of subjectVo associated with a specific professor
     *
     * @param em the DataAccessAdapter
     * @param professorId the professor's id
     * @return a list of matched subject value objects
     */
    public List<SubjectVo> getByProfessorId(DataAccessAdapter<E> em, long professorId)throws DataAccessException;
}
