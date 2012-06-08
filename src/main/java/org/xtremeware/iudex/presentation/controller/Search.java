package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.presentation.vovw.CourseListVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.builder.CourseVoVwBuilder;

/**
 *
 * @author healarconr
 */

@ManagedBean 
@ViewScoped
public class Search implements Serializable {

    private String query;
    private List<CourseListVoVwSmall> courses;
    
    public List<CourseListVoVwSmall> getCourses() {
        return courses;
    }
    
    public String getQuery() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        this.query = params.get("query");
        return query;
    }

    public void setCourses(List<CourseListVoVwSmall> courses) {
        this.courses = courses;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    public boolean isCoursesEmpty() {
        return (this.getCourses() == null || this.getCourses().isEmpty());
    }

    public boolean isQueryEmpty() {
        return (this.getQuery() == null || this.getQuery().isEmpty());
    }

    public String submit() {
//        FacesContext fc = FacesContext.getCurrentInstance();
//        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
//        this.query = params.get("searchForm:query");
        return "success";
    }
    

    public void preRenderView() {
        try {
            this.courses = CourseVoVwBuilder.getInstance().getSearchResults(query);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("searchForm", new FacesMessage(ex.getMessage()));
        }
    }
}
