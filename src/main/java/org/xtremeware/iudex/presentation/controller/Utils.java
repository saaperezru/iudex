/*
 */
package org.xtremeware.iudex.presentation.controller;

import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 *
 * @author tuareg
 */
@ManagedBean
@RequestScoped
public class Utils {

	private static Integer[][] listLists = {{1}, {1, 2}, {1, 2, 3}, {1, 2, 3, 4}, {1, 2, 3, 4, 5}};

	public int ceil(float number) {
		return (int) Math.ceil(number);
	}

	public int floor(float number) {
		return (int) Math.floor(number);
	}

	public List<Integer> buildArrayFloor(float size) {
		int index = (int) Math.floor(size);
		if (index < 6 && index > 0) {
			return Arrays.asList(listLists[index - 1]);
		}
		return null;
	}

	public List<Integer> buildArrayCeil(float size) {
		int index = (int) Math.ceil(size);
		if (index < 6 && index > 0) {
			return Arrays.asList(listLists[index - 1]);
		}
		return null;
	}
	
	public String parseProgramName(long programId){
		ProgramVo program = Config.getInstance().getFacadeFactory().getProgramsFacade().getProgram(programId);
		if(program == null){
			return "";
		}else{
			return program.getName();
		}
	}
}
