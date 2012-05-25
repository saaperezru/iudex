package org.xtremeware.iudex.presentation.controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import org.xtremeware.iudex.businesslogic.facade.ProgramsFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.ProgramVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Lists {

    private List<SelectItem> programs;

    public List<SelectItem> getPrograms() {
        if (programs == null) {
            programs = new ArrayList<SelectItem>();
            ProgramsFacade programsFacade = Config.getInstance().
                    getFacadeFactory().getProgramsFacade();
            List<ProgramVo> programsList = programsFacade.listPrograms();
            if (programsList != null) {
                for (ProgramVo vo : programsList) {
                    programs.add(new SelectItem(vo.getId(), vo.getName()));
                }
            }
        }
        return programs;
    }

    public void setPrograms(List<SelectItem> programs) {
        this.programs = programs;
    }
}
