package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
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
    public void validateVo(SubjectVo vo) throws InvalidVoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SubjectEntity voToEntity(SubjectVo vo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SubjectVo> search(EntityManager em, String query) {
        return null;
    }

    public List<SubjectVo> getByNameLike(EntityManager em, String naem) {
        return null;
    }
}
