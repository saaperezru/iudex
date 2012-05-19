package org.xtremeware.iudex.dao.internal;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.ProgramDaoInterface;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class ProgramDao extends CrudDao<ProgramEntity> implements ProgramDaoInterface {

    /**
     * Search a program which name contains the given parameter name
     *
     * @param em the entity manager
     * @param name
     * @return Return a list of programEntity objects
     */
    @Override
    public List<ProgramEntity> getByNameLike(EntityManager em, String name) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getProgramByNameLike", ProgramEntity.class).
                    setParameter("name", "%" + name + "%").getResultList();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<ProgramEntity> getAll(EntityManager em) throws DataBaseException {
        checkEntityManager(em);
        try {
            return em.createNamedQuery("getAllPrograms", ProgramEntity.class).
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
