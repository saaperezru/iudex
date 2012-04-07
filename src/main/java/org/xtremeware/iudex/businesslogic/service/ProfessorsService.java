package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.dao.Dao;
import org.xtremeware.iudex.dao.ProfessorDao;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ProfessorVo;

public class ProfessorsService extends SimpleCrudService<ProfessorVo, ProfessorEntity> {

	private final int MAX_PROFESSOR_NAME_LENGTH;

	public ProfessorsService(AbstractDaoFactory daoFactory) throws ExternalServiceConnectionException {
		super(daoFactory);
		MAX_PROFESSOR_NAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_PROFESSOR_NAME_LENGTH));
	}

	public List<ProfessorVo> getByNameLike(EntityManager em, String name) {
		ArrayList<ProfessorVo> list = new ArrayList<ProfessorVo>();
		for (ProfessorEntity professor : ((ProfessorDao) getDao()).getByNameLike(em, name)) {
			list.add(professor.toVo());
		}
		return list;
	}

	public List<ProfessorVo> getBySubjectId(EntityManager em, long subjectId) {
		ArrayList<ProfessorVo> list = new ArrayList<ProfessorVo>();
		for (ProfessorEntity professor : ((ProfessorDao) getDao()).getBySubjectId(em, subjectId)) {
			list.add(professor.toVo());
		}
		return list;
	}

	@Override
	protected Dao<ProfessorEntity> getDao() {
		return getDaoFactory().getProfessorDao();
	}

	@Override
	public void validateVo(EntityManager em, ProfessorVo vo) throws InvalidVoException {
		if (em == null) {
			throw new IllegalArgumentException("EntityManager em cannot be null");
		}
		if (vo.getFirstName() == null) {
			throw new InvalidVoException("String firstName in the provided ProgramVo cannot be null");
		}
		if (vo.getLastName() == null) {
			throw new InvalidVoException("String lastName in the provided ProgramVo cannot be null");
		}
		if (vo.getDescription() == null) {
			throw new InvalidVoException("String description in the provided ProgramVo cannot be null");
		}
		if (vo.getEmail() == null) {
			throw new InvalidVoException("String email in the provided ProgramVo cannot be null");
		}
		if (vo.getImageUrl() == null) {
			throw new InvalidVoException("String imageUrl in the provided ProgramVo cannot be null");
		}
		if (vo.getWebsite() == null) {
			throw new InvalidVoException("String website in the provided ProgramVo cannot be null");
		}
		if (vo.getImageUrl().length() > 0 && !ValidityHelper.isValidUrl(vo.getImageUrl())) {
			throw new InvalidVoException("String imageUrl in the provided ProgamVo must be a valid URL");
		}
		if (vo.getEmail().length() > 0 && !ValidityHelper.isValidEmail(vo.getEmail())) {
			throw new InvalidVoException("Strng email in the provided ProgramVo must be a valid email address");
		}
		if (vo.getWebsite().length() > 0 && !ValidityHelper.isValidUrl(vo.getWebsite())) {
			throw new InvalidVoException("String website in provided ProgramVo must be a valid URL");
		}
		if (vo.getFirstName().length() > MAX_PROFESSOR_NAME_LENGTH || vo.getLastName().length() > MAX_PROFESSOR_NAME_LENGTH) {
			throw new InvalidVoException("String firstName and String lastName length must be less than " + String.valueOf(MAX_PROFESSOR_NAME_LENGTH));
		}
	}

	@Override
	public ProfessorEntity voToEntity(EntityManager em, ProfessorVo vo) throws InvalidVoException,ExternalServiceConnectionException {
		validateVo(em, vo);
		ProfessorEntity entity = new ProfessorEntity();
		entity.setId(vo.getId());
		entity.setEmail(vo.getEmail());
		try {
			entity.setDescription(SecurityHelper.sanitizeHTML(vo.getDescription()));
			entity.setFirstName(SecurityHelper.sanitizeHTML(vo.getFirstName()));
			entity.setLastName(SecurityHelper.sanitizeHTML(vo.getLastName()));
		} catch (ExternalServiceConnectionException ex) {
			throw ex;
		}
		entity.setImageUrl(vo.getImageUrl());
		entity.setWebsite(vo.getWebsite());
		return entity;
	}
}
