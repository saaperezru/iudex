package org.xtremeware.iudex.dao.sql;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class SqlProgramDao extends SqlCrudDao<ProgramEntity> implements ProgramDao {

    public SqlProgramDao(Delete delete) {
        super(delete);
    }

    /**
     * Search a program which name contains the given parameter name
     *
     * @param entityManager the entity manager
     * @param name
     * @return Return a list of programEntity objects
     */
    @Override
    public List<ProgramEntity> getByNameLike(EntityManager entityManager, String programName) 
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getProgramByNameLike", ProgramEntity.class).
                    setParameter("name", "%" + programName + "%").getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
   public List<ProgramEntity> getAll(EntityManager entityManager) 
            throws DataBaseException {
        checkEntityManager(entityManager);
        try {
            return entityManager.createNamedQuery("getAllPrograms", ProgramEntity.class).
                    getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected Class getEntityClass() {
        return ProgramEntity.class;
    }
}
