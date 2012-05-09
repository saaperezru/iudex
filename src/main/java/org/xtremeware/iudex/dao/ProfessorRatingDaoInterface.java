package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProfessorRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.vo.RatingSummaryVo;

/**
 * DAO Interface for the ProfessorRating entities.
 *
 * @author juan
 */
public interface ProfessorRatingDaoInterface extends CrudDaoInterface<ProfessorRatingEntity> {

    /**
     * Professors ratings finder according to a specified professor
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A list with all ratings associated to the specified professor
     */
    public List<ProfessorRatingEntity> getByProfessorId(EntityManager em,
            long professorId) throws DataBaseException;

    /**
     * Professor ratings finder according to a professor and a student
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @param userId Student's ID
     * @return The rating a student has submitted to a professor
     */
    public ProfessorRatingEntity getByProfessorIdAndUserId(EntityManager em,
            long professorId, long userId) throws DataBaseException;

    /**
     * Professors ratings finder according to a specified user
     *
     * @param em the entity manager
     * @param userId user's ID
     * @return A list with all ratings associated to the specified user
     */
    public List<ProfessorRatingEntity> getByUserId(EntityManager em, long userId) 
            throws DataBaseException;

    /**
     * Professor rating summary calculator
     *
     * @param em the entity manager
     * @param professorId Professor's ID
     * @return A value object containing the number of times the specified
     * professor has obtained positive and negative ratings
     */
    public RatingSummaryVo getSummary(EntityManager em, long professorId) throws
            DataBaseException;
}
