package org.xtremeware.iudex.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.da.DataAccessException;
import org.xtremeware.iudex.dao.ConfirmationKeyDao;
import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.ConfirmationKeyVo;

/**
 * JpaDao for the ConfirmationKey value objects. Implements additionally some useful finders by
 * associated confirmation key.
 * 
 * @author josebermeo
 */
public class JpaConfirmationKeyDao extends JpaCrudDao<ConfirmationKeyVo,ConfirmationKeyEntity> implements ConfirmationKeyDao<EntityManager> {

    /**
     * Returns a ConfirmationKey entity using the information in the provided
     * ConfirmationKey value object.
     * 
     * @param em the data access adapter
     * @param vo the ConfirmationKey value object
     * @return the ConfirmationKey entity
     */
    @Override
    protected ConfirmationKeyEntity voToEntity(DataAccessAdapter<EntityManager> em, ConfirmationKeyVo vo) {
        
        ConfirmationKeyEntity confirmationKeyEntity = new ConfirmationKeyEntity();
        
        confirmationKeyEntity.setId(vo.getId());
        confirmationKeyEntity.setConfirmationKey(vo.getConfirmationKey());
        confirmationKeyEntity.setExpirationDate(vo.getExpirationDate());
        
        confirmationKeyEntity.setUser(em.getDataAccess().getReference(UserEntity.class, vo.getUserId()));
        
        return confirmationKeyEntity;
    }

    @Override
    protected Class getEntityClass() {
        return ConfirmationKeyEntity.class;
    }
    
    /**
     * Returns a ConfirmationKeyVo which key matched with the given string.
     * 
     * @param em the data access adapter
     * @param confirmationKey key reference for the search
     * @return the ConfirmationKeyVo with the given key
     */
    @Override
    public ConfirmationKeyVo getByConfirmationKey(DataAccessAdapter<EntityManager> em, String confirmationKey) throws DataAccessException{
        checkDataAccessAdapter(em);
        try {
            return ((ConfirmationKeyEntity) em.getDataAccess().createNamedQuery("getByConfirmationKey").setParameter("confirmationKey", confirmationKey).getSingleResult()).toVo();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
}
