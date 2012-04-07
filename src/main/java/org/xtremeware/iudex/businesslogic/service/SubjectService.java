package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.SubjectDao;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public class SubjectService extends SimpleCrudService<SubjectVo, SubjectEntity> {

    public SubjectService(AbstractDaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    protected Dao<SubjectEntity> getDao() {
        return getDaoFactory().getSubjectDao();
    }

    @Override
    public void validateVo(EntityManager em, SubjectVo vo) throws InvalidVoException {
        if (vo == null) {
            throw new InvalidVoException("Null SubjectVo");
        }
        if (vo.getName()== null) {
            throw new InvalidVoException("Null name in the provided SubjectVo");
        }
        if (vo.getDescription() == null) {
            throw new InvalidVoException("Null description in the provided SubjectVo");
        }
        if(vo.getName().length()>50){
            throw new InvalidVoException("Invalid name length in the provided SubjectVo");
        }
        if(vo.getDescription().length()>2000){
            throw new InvalidVoException("Invalid description length in the provided SubjectVo");
        }
    }

    @Override
    public SubjectEntity voToEntity(EntityManager em, SubjectVo vo) throws InvalidVoException{
        
        validateVo(em, vo);
        
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(vo.getId());
        subjectEntity.setName(vo.getName());
        subjectEntity.setDescription(vo.getDescription());
        
        return subjectEntity;
    }

    public List<SubjectVo> search(EntityManager em, String query) {
        List<SubjectEntity> subjectEntitys = ((SubjectDao)this.getDao()).getByName(em, query);
        if(subjectEntitys.isEmpty())
            return null;
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for(SubjectEntity subjectEntity: subjectEntitys){
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }

    public List<SubjectVo> getByNameLike(EntityManager em, String name) {
        List<SubjectEntity> subjectEntitys = ((SubjectDao)this.getDao()).getByName(em, name);
        if(subjectEntitys.isEmpty())
            return null;
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for(SubjectEntity subjectEntity: subjectEntitys){
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }
}
