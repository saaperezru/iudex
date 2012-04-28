package org.xtremeware.iudex.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.da.DataAccessAdapter;
import org.xtremeware.iudex.dao.SubjectDao;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 * DAO for SubjectVo. Implements additionally some useful finders by name
 * or by professor id
 *
 * @author healarconr
 */
public class JpaSubjectDao extends JpaCrudDao<SubjectVo, SubjectEntity> implements SubjectDao<EntityManager> {

    /**
     * Returns a list of subject entities which name contains the argument name
     *
     * @param em the DataAccessAdapter
     * @param name the subject's name
     * @return a list of matched subjectVo
     */
    @Override
    public List<SubjectVo> getByName(DataAccessAdapter<EntityManager> em, String name) {
        checkDataAccessAdapter(em);
        List<SubjectEntity> list = em.getDataAccess().createNamedQuery("getSubjectsByName", SubjectEntity.class).setParameter("name", "%" + name + "%").getResultList();
        return entitiesToVos(list);
    }

    /**
     * Returns a list of subjectVo associated with a specific professor
     *
     * @param em the DataAccessAdapter
     * @param professorId the professor's id
     * @return a list of matched subject value objects
     */
    @Override
    public List<SubjectVo> getByProfessorId(DataAccessAdapter<EntityManager> em, long professorId) {
        checkDataAccessAdapter(em);
        List<SubjectEntity> list = em.getDataAccess().createNamedQuery("getSubjectsByProfessorId", SubjectEntity.class).setParameter("professorId", professorId).getResultList();
        return entitiesToVos(list);
    }

    @Override
    protected SubjectEntity voToEntity(DataAccessAdapter<EntityManager> em, SubjectVo vo) {
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(vo.getId());
        subjectEntity.setName(vo.getName());
        subjectEntity.setDescription(vo.getDescription());

        return subjectEntity;
    }

    private List<SubjectVo> entitiesToVos(List<SubjectEntity> list){
        ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
        for (SubjectEntity entity : list) {
            arrayList.add(entity.toVo());
        }
        return arrayList;
    }
    
    @Override
    protected Class getEntityClass() {
        return SubjectEntity.class;
    }
}
