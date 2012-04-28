package org.xtremeware.iudex.dao;

import java.util.List;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.vo.ProfessorRatingVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 * DAO for the ProfessorRatingVo. Implements additionally some useful
 * finders by professor and user
 *
 * @author josebermeo
 */
public interface ProfessorRatingDao<E> extends CrudDao<ProfessorRatingVo, E> {

    /**
     * Professors ratings finder according to a specified professor
     *
     * @param em the DataAccessAdapter
     * @param professorId Professor's ID
     * @return A list with all ratings associated to the specified professor
     */
    public List<ProfessorRatingVo> getByProfessorId(DataAccessAdapter<E> em, long professorId);

    /**
     * Professor ratings finder according to a professor and a student
     *
     * @param em the DataAccessAdapter
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return The rating a student has submitted to a professor
     */
    public ProfessorRatingVo getByProfessorIdAndUserId(DataAccessAdapter<E> em, long professorId, long userId);

    /**
     * Professors ratings finder according to a specified user
     *
     * @param em the DataAccessAdapter
     * @param userId user's ID
     * @return A list with all ratings associated to the specified user
     */
    public List<ProfessorRatingVo> getByUserId(DataAccessAdapter<E> em, long userId);

    /**
     * Professor rating summary calculator
     *
     * @param em the DataAccessAdapter
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified
     * professor has obtained positive and negative ratings
     */
    public RatingSummaryVo getSummary(DataAccessAdapter<E> em, long professorId);
}
