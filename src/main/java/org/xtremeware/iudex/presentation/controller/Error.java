package org.xtremeware.iudex.presentation.controller;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import javax.faces.application.ProjectStage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Error implements Serializable {

    private String stackTrace;

    public String getStackTrace() {
        if (stackTrace == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc.getApplication().getProjectStage().equals(
                    ProjectStage.Development)) {
                Throwable t = (Throwable) fc.getExternalContext().getRequestMap().
                        get("javax.servlet.error.exception");
                if (t != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    t.printStackTrace(pw);
                    stackTrace = sw.toString();
                }
            }
        }
        return stackTrace;
    }
}
