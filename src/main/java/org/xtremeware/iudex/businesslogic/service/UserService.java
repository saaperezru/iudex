package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author josebermeo
 */
public class UserService extends CrudService<UserVo> {

    public UserService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }
    public void validateVo(EntityManager em, UserVo vo)throws InvalidVoException{
        if(vo == null)
            throw new InvalidVoException("Null UserVo"); 
        if(vo.getFirstName() == null)
            throw new InvalidVoException("Null firstName in the provided UserVo"); 
        if(vo.getLastName() == null)
            throw new InvalidVoException("Null lastName in the provided UserVo"); 
        if(vo.getUserName() == null)
            throw new InvalidVoException("Null userName in the provided UserVo");
        if(vo.getUserName().length()>20 || vo.getUserName().length()<5)
            throw new InvalidVoException("Invalid userName length in the provided UserVo");
        if(vo.getPassword() == null)
            throw new InvalidVoException("Null password in the provided UserVo");
        if(vo.getPassword().length()>18 || vo.getUserName().length()<8)
            throw new InvalidVoException("Invalid password length in the provided UserVo");
        if(vo.getProgramsId() == null)
            throw new InvalidVoException("Null programsId in the provided UserVo"); 
        if(vo.getProgramsId().isEmpty())
            throw new InvalidVoException("programsId cannot be empity");
        for(Long programId: vo.getProgramsId()){
            if(programId == null){
               throw new InvalidVoException("Any element in programsId cannot be null"); 
            }
            if(this.getDaoFactory().getProgramDao().getById(em, programId)==null)
                throw new InvalidVoException("An element in programsId cannot be found");
        }
        if(vo.getRol() == null)
            throw new InvalidVoException("Rol cannot be null");
    }
    public UserEntity voToEntity(EntityManager em, UserVo vo) throws InvalidVoException{
        
        validateVo(em, vo);
        
        UserEntity userEntity = new UserEntity();
        userEntity.setId(vo.getId());
        userEntity.setFirstName(vo.getFirstName());
        userEntity.setLastName(vo.getLastName());
        userEntity.setUserName(vo.getUserName());
        userEntity.setPassword(vo.getPassword());
        userEntity.setRol(vo.getRol());
        userEntity.setActive(vo.isActive());
        
        ArrayList<ProgramEntity> arrayList = new ArrayList<ProgramEntity>();
        for(Long programId: vo.getProgramsId()){
            arrayList.add(this.getDaoFactory().getProgramDao().getById(em, programId));
        }
        
        userEntity.setPrograms(arrayList);
        return userEntity;
        
    }
    public UserVo create(EntityManager em, UserVo user){
        return null;
    } 
    public UserVo authenticate(EntityManager em, String userName, String password){
        return null;
    }
    public void activateAccount(EntityManager em, String confirmationKey){
        
        
    }  
    public UserVo getById(EntityManager em, Long id){
        return this.getDaoFactory().getUserDao().getById(em, id).toVo();
    } 
    public void update(EntityManager em, UserVo object) throws InvalidVoException{
        this.getDaoFactory().getUserDao().merge(em, this.voToEntity(em, object));
    }
    public void remove(EntityManager em, UserVo user){
        
    } 
    
}
