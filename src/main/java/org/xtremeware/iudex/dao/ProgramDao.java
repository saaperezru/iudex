package org.xtremeware.iudex.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.ProgramEntity;

/**
 *
 * @author josebermeo
 */
public class ProgramDao extends Dao<ProgramEntity> {

    /**
     * Search a program which name contains the given parameter name
     *
     * @param em the entity manager
     * @param name
     * @return Return a list of programEntity objects
     */
    public List<ProgramEntity> getByNameLike(EntityManager em, String name) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        return em.createNamedQuery("getProgramByNameLike").setParameter("name", "%" + name + "%").getResultList();
    }
}
