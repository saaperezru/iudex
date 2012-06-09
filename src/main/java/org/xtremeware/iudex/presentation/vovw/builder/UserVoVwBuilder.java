package org.xtremeware.iudex.presentation.vovw.builder;

import java.util.*;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;
import org.xtremeware.iudex.vo.UserVo;

public final class UserVoVwBuilder {
	
	private static UserVoVwBuilder instance;
	private FacadeFactory facadeFactory;
	private Map<Long, String> programs = new HashMap<Long, String>();
	private static final String DEFAULT_ANONYMOUS_IMAGE = "/resources/images/anonymous3.png";
	private static final String DEFAULT_IMAGE = "/resources/images/user.png";
	public static final UserVoVwSmall ANONYMOUS_USER = new UserVoVwSmall(0, "Anónimo", "anónimo", "...", DEFAULT_ANONYMOUS_IMAGE);
	
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
		return new UserVoVwSmall(userId, user.getFirstName() + " " + user.getLastName(), user.getUserName(), mainProgram,DEFAULT_IMAGE);
	}
}
