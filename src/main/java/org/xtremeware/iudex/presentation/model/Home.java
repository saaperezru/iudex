package org.xtremeware.iudex.presentation.model;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Home {

    private List<SelectItem> programs;

    public List<SelectItem> getPrograms() {
        return programs;
    }

    public void setPrograms(List<SelectItem> programs) {
        this.programs = programs;
    }

    public void preRenderView() {
        List<SelectItem> programsList = new ArrayList<SelectItem>();
        programsList.add(new SelectItem(null));
        // TODO: Use real data
        // ProgramsFacade programsFacade = Config.getInstance().getFacadeFactory().getProgramsFacade();
        // programsFacade.listPrograms();
        
        programsList.add(new SelectItem(1L, "Program 1"));
        programsList.add(new SelectItem(2L, "Program 2"));
        setPrograms(programsList);
    }
}
