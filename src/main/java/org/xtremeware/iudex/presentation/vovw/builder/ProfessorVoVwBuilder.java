package org.xtremeware.iudex.presentation.vovw.builder;

import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.businesslogic.facade.ProfessorsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.ProfessorVoVwFull;
import org.xtremeware.iudex.vo.ProfessorVo;
import org.xtremeware.iudex.vo.ProfessorVoFull;

public class ProfessorVoVwBuilder {

	private static final String DEFAULTIMAGEURL = "../resources/images/professor.png";
	private static ProfessorVoVwBuilder instance;
	private FacadeFactory facadeFactory;

	private ProfessorVoVwBuilder(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}

	public static synchronized ProfessorVoVwBuilder getInstance() {
		if (instance == null) {
			instance = new ProfessorVoVwBuilder(Config.getInstance().getFacadeFactory());
		}
		return instance;
	}

	public ProfessorVoVwFull getProfessorFull(long professorId) {
		ProfessorsFacade professorsFacade = facadeFactory.getProfessorsFacade();
		ProfessorVoFull professor = professorsFacade.getProfessor(professorId);
		if (professor == null) {
			return null;
		}
		normalizeUrls(professor);
		return new ProfessorVoVwFull(professor.getVo(), professor.getRatingSummary());
	}

	public ProfessorVoVwFull getProfessorFull(ProfessorVo professor) {
		ProfessorsFacade professorsFacade = facadeFactory.getProfessorsFacade();
		normalizeUrls(professor);
		return new ProfessorVoVwFull(professor, professorsFacade.getProfessorRatingSummary(professor.getId()));
	}

	private void normalizeUrls(ProfessorVoFull professor) {
		normalizeUrls(professor.getVo());
	}

	private void normalizeUrls(ProfessorVo professor) {
		if (professor.getImageUrl() == null || professor.getImageUrl().isEmpty()) {
			professor.setImageUrl(DEFAULTIMAGEURL);
		}
		if (!professor.getWebsite().contains("http://") && !professor.getWebsite().contains("https://")) {
			professor.setWebsite("http://" + professor.getWebsite());
		}
	}
}
