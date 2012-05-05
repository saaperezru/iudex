package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SubjectsRemove;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public class SubjectsService extends CrudService<SubjectVo, SubjectEntity> {

    /**
     * SubjectsService constructor
     *
     * @param daoFactory
     */
    public SubjectsService(AbstractDaoFactory daoFactory) {
        super(daoFactory,
                new SimpleCreate<SubjectEntity>(daoFactory.getSubjectDao()),
                new SimpleRead<SubjectEntity>(daoFactory.getSubjectDao()),
                new SimpleUpdate<SubjectEntity>(daoFactory.getSubjectDao()),
                new SubjectsRemove(daoFactory));
    }

    /**
     * Validate the provided SubjectVo, if the SubjectVo is not correct the
     * methods throws an exception
     *
     * @param em EntityManager
     * @param vo SubjectVo
     * @throws InvalidVoException
     */
    @Override
    public void validateVo(EntityManager em, SubjectVo vo) throws InvalidVoException, ExternalServiceConnectionException {
        if (vo == null) {
            throw new InvalidVoException("Null SubjectVo");
        }
        if (vo.getName() == null) {
            throw new InvalidVoException("Null name in the provided SubjectVo");
        }
        if (vo.getDescription() == null) {
            throw new InvalidVoException("Null description in the provided SubjectVo");
        }
        vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
        vo.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
        if (vo.getName().length() > 50) {
            throw new InvalidVoException("Invalid name length in the provided SubjectVo");
        }
        if (vo.getDescription().length() > 2000) {
            throw new InvalidVoException("Invalid description length in the provided SubjectVo");
        }
    }

    /**
     * Returns a SubjectEntity using the information in the provided SubjectVo.
     *
     * @param em EntityManager
     * @param vo SubjectVo
     * @return SubjectEntity
     * @throws InvalidVoException
     */
    @Override
    public SubjectEntity voToEntity(EntityManager em, SubjectVo vo) throws InvalidVoException, ExternalServiceConnectionException {

        validateVo(em, vo);

        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(vo.getId());
        subjectEntity.setName(vo.getName());
        subjectEntity.setDescription(vo.getDescription());

        return subjectEntity;
    }

    /**
     * Returns a list of SubjectVo according with the search query
     *
     * @param em EntityManager
     * @param query String with the search parameter
     * @return A list of SubjectVo
     */
    public List<SubjectVo> search(EntityManager em, String query) throws ExternalServiceConnectionException {
        if (query == null) {
            throw new IllegalArgumentException("Null query for a subject search");
        }
        query = SecurityHelper.sanitizeHTML(query);
        List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().getByName(em, query);
        if (subjectEntitys.isEmpty()) {
            return null;
        }
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }

    /**
     * Returns a list of SubjectVo according with the search name
     *
     * @param em EntityManager
     * @param name String with the name of the SubjectVo
     * @return A list if SubjectVo
     */
    public List<SubjectVo> getByNameLike(EntityManager em, String name) throws ExternalServiceConnectionException {
        if (name == null) {
            throw new IllegalArgumentException("Null name for a subject search");
        }
        name = SecurityHelper.sanitizeHTML(name);
        List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().getByName(em, name);
        if (subjectEntitys.isEmpty()) {
            return null;
        }
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity subjectEntity : subjectEntitys) {
            arrayList.add(subjectEntity.toVo());
        }
        return arrayList;
    }
}
