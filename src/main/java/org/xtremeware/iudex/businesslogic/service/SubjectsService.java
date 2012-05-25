package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.businesslogic.service.createimplementations.SimpleCreate;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.SubjectsRemove;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.SimpleUpdate;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.SubjectEntity;
import org.xtremeware.iudex.helper.DataBaseException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.MultipleMessagesException;
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
	public void validateVo(EntityManager em, SubjectVo vo)
			throws ExternalServiceConnectionException, MultipleMessagesException {
		if (em == null) {
			throw new IllegalArgumentException("EntityManager em cannot be null");
		}
		MultipleMessagesException multipleMessageException = new MultipleMessagesException();
		if (vo == null) {
			multipleMessageException.addMessage("Null SubjectVo");
			throw multipleMessageException;
		}
		if (vo.getName() == null) {
			multipleMessageException.addMessage(
					"Null name in the provided SubjectVo");
		} else {
			vo.setName(SecurityHelper.sanitizeHTML(vo.getName()));
			if (vo.getName().length() > 50) {
				multipleMessageException.addMessage(
						"Invalid name length in the provided SubjectVo");
			}
		}
		if (vo.getDescription() == null) {
			multipleMessageException.addMessage(
					"Null description in the provided SubjectVo");
		} else {
			vo.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
			if (vo.getDescription().length() > 2000) {
				multipleMessageException.addMessage(
						"Invalid description length in the provided SubjectVo");
			}
		}
		if (!multipleMessageException.getMessages().isEmpty()) {
			throw multipleMessageException;
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
	public SubjectEntity voToEntity(EntityManager em, SubjectVo vo)
			throws ExternalServiceConnectionException, MultipleMessagesException {

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
	public List<SubjectVo> search(EntityManager em, String query)
			throws ExternalServiceConnectionException, DataBaseException {
		query = SecurityHelper.sanitizeHTML(query);
		List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().
				getByName(em, query.toUpperCase());
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
	public List<SubjectVo> getByNameLike(EntityManager em, String name)
			throws ExternalServiceConnectionException, DataBaseException {
		name = SecurityHelper.sanitizeHTML(name);
		List<SubjectEntity> subjectEntitys;
		ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
		if (!name.isEmpty()) {
			subjectEntitys = getDaoFactory().getSubjectDao().
					getByName(em, name.toUpperCase());
			for (SubjectEntity subjectEntity : subjectEntitys) {
				arrayList.add(subjectEntity.toVo());
			}
		}
		return arrayList;
	}

	/**
	 * Returns a list of SubjectVos that had been taught by the specified
	 * professor.
	 *
	 * @param em EntityManager
	 * @param professorId Professor's id to look for subjects.
	 * @return A list of SubjectVo
	 */
	public List<SubjectVo> getByProfessorId(EntityManager em, long professorId)
			throws DataBaseException {
		List<SubjectEntity> subjectEntitys = getDaoFactory().getSubjectDao().
				getByProfessorId(em, professorId);
		ArrayList<SubjectVo> arrayList = new ArrayList<SubjectVo>();
		for (SubjectEntity subjectEntity : subjectEntitys) {
			arrayList.add(subjectEntity.toVo());
		}
		return arrayList;
	}
}
