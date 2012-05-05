package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProgramEntity;

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
    public List<ProgramEntity> getByNameLike(EntityManager em, String name) {
        checkEntityManager(em);
        return em.createNamedQuery("getProgramByNameLike", ProgramEntity.class).setParameter("name", "%" + name + "%").getResultList();
    }
}
