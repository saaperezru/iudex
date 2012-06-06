package org.xtremeware.iudex.presentation.vovw.builder;

import java.util.HashMap;
import java.util.List;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;
import org.xtremeware.iudex.vo.UserVo;

public class UserVoVwBuilder {
	
	private static UserVoVwBuilder instance;
	private FacadeFactory facadeFactory;
	private HashMap<Long, String> programs = new HashMap<Long, String>();
	private static final String defaultAnonymousImage = "/resources/images/anonymous3.png";
	private static final String defaultImage = "/resources/images/user.png";
	public static final UserVoVwSmall anonymousUser = new UserVoVwSmall(0, "Anónimo", "anónimo", "...", defaultAnonymousImage);
	
	private UserVoVwBuilder(FacadeFactory facadeFactory) {
		this.facadeFactory = facadeFactory;
	}
	
	public static synchronized UserVoVwBuilder getInstance() {
		if (instance == null) {
			instance = new UserVoVwBuilder(Config.getInstance().getFacadeFactory());
		}
		return instance;
	}
	
	public UserVoVwSmall getUser(long userId) {		
		UserVo user = facadeFactory.getUsersFacade().getUser(userId);
		List<Long> programsId = user.getProgramsId();
		String mainProgram = "";
		if (!programsId.isEmpty()) {
			long mainProgramId = programsId.get(0);
			if (!programs.containsKey(mainProgramId)) {
				programs.put(mainProgramId, facadeFactory.getProgramsFacade().getProgram(mainProgramId).getName());
			}
			mainProgram = programs.get(mainProgramId);
		}
		return new UserVoVwSmall(userId, user.getFirstName() + " " + user.getLastName(), user.getUserName(), mainProgram,defaultImage);
	}
}
