package org.xtremeware.iudex.presentation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import org.xtremeware.iudex.businesslogic.facade.FeedbacksFacade;
import org.xtremeware.iudex.businesslogic.facade.ProgramsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.FeedbackTypeVo;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Lists {

	private List<SelectItem> programs;
	private List<SelectItem> feedbackTypes;

	public List<SelectItem> getPrograms() {
		if (programs == null) {
			programs = new ArrayList<SelectItem>();
			ProgramsFacade programsFacade = Config.getInstance().
					getFacadeFactory().getProgramsFacade();
			List<ProgramVo> programsList = programsFacade.listPrograms();
			if (programsList != null) {
				for (ProgramVo program : programsList) {
					programs.add(new SelectItem(program.getId(),
							program.getName()));
				}
			}
		}
		return programs;
	}

	public List<SelectItem> getFeedbackTypes() {
		if (feedbackTypes == null) {
			feedbackTypes = new ArrayList<SelectItem>();
			FeedbacksFacade feedbacksFacade = Config.getInstance().
					getFacadeFactory().getFeedbacksFacade();
			List<FeedbackTypeVo> feedbacksTypeList;
			try {
				feedbacksTypeList = feedbacksFacade.getFeedbackTypes();
				if (feedbacksTypeList != null) {
					for (FeedbackTypeVo feedbackType : feedbacksTypeList) {
						feedbackTypes.add(new SelectItem(feedbackType.getId(),
								feedbackType.getName()));
					}
				}
			} catch (Exception ex) {
				Config.getInstance().getServiceFactory().getLogService().error(ex);
			}


		}
		return feedbackTypes;
	}
}