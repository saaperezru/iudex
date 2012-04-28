package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.ProfessorDao;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.vo.ProfessorVo;

/**
 * JPADAO for the ProfessorVo. Implements additionally some useful finders
 * by name and subject
 *
 * @author juan
 */
public class JpaProfessorDao extends JpaCrudDao<ProfessorVo, ProfessorEntity> implements ProfessorDao<EntityManager>{

    /**
     * Professors finder according to a required name
     *
     * @param em the DataAccessAdapter
     * @param name Professor's firstname or lastname
     * @return List of professors whose firstname or lastname are equal to the
     * specified
     */
    @Override
    public List<ProfessorVo> getByName(DataAccessAdapter<EntityManager> em, String name) {
        checkDataAccessAdapter(em);
        List<ProfessorEntity> list = em.getDataAccess().createNamedQuery("getProfessorByNameLike").setParameter("name", "%" + name + "%").getResultList();
        return entitiesToVos(list);
    }

    /**
     * Professors finder according to the subjects they offer
     *
     * @param em the DataAccessAdapter
     * @param subjectId The ID of the required subject
     * @return A list of professors that impart the subject
     */
    @Override
    public List<ProfessorVo> getBySubjectId(DataAccessAdapter<EntityManager> em, long subjectId) {
        checkDataAccessAdapter(em);
        List<ProfessorEntity> list = em.getDataAccess().createNamedQuery("getProfessorBySubjectId").setParameter("subjectId", subjectId).getResultList();
        return entitiesToVos(list);
    }

    @Override
    protected ProfessorEntity voToEntity(DataAccessAdapter<EntityManager> em, ProfessorVo vo) {
        ProfessorEntity entity = new ProfessorEntity();
        entity.setId(vo.getId());
        entity.setEmail(vo.getEmail());
	entity.setDescription(vo.getDescription());
	entity.setFirstName(vo.getFirstName());
	entity.setLastName(vo.getLastName());
	entity.setImageUrl(vo.getImageUrl());
	entity.setWebsite(vo.getWebsite());
	return entity;
    }

    @Override
    protected Class getEntityClass() {
        return ProfessorEntity.class;
    }

    private List<ProfessorVo> entitiesToVos(List<ProfessorEntity> list) {
        ArrayList<ProfessorVo> arrayList = new ArrayList<ProfessorVo>();
        for (ProfessorEntity entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }
}
