package org.xtremeware.iudex.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.ProgramDao;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 * JPADAO for ProgramVo.
 * 
 * @author josebermeo
 */
public class JpaProgramDao extends JpaCrudDao< ProgramVo, ProgramEntity> implements ProgramDao<EntityManager> {

    /**
     * Search a program which name contains the given parameter name
     *
     * @param em the DataAccessAdapter
     * @param name
     * @return Return a list of programEntity objects
     */
    @Override
    public List<ProgramVo> getByNameLike(DataAccessAdapter<EntityManager> em, String name) throws DataAccessException {
        checkDataAccessAdapter(em);
        return entitiesToVos(em.getDataAccess().createNamedQuery("getProgramByNameLike", getEntityClass()).setParameter("name", "%" + name + "%").getResultList());
    }

    @Override
    protected ProgramEntity voToEntity(DataAccessAdapter<EntityManager> em, ProgramVo vo) {
        ProgramEntity entity = new ProgramEntity();
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        return entity;
    }
    
    @Override
    protected Class<ProgramEntity> getEntityClass() {
        return ProgramEntity.class;
    }
}
