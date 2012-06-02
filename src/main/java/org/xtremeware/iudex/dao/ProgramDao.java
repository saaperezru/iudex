package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public interface ProgramDao extends CrudDao<ProgramEntity> {

    /**
     * Search a program which name contains the given parameter name
     *
     * @param entityManager the entity manager
     * @param name
     * @return Return a list of programEntity objects
     */
    List<ProgramEntity> getByNameLike(EntityManager entityManager, String programName)
            throws DataBaseException;

    List<ProgramEntity> getAll(EntityManager entityManager)
            throws DataBaseException;
}
