package org.xtremeware.iudex.presentation.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.businesslogic.facade.CoursesFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Search {

    private String query;
    private List<CourseVoVwFull> courses;

    public List<CourseVoVwFull> getCourses() {
        return courses;
    }

    public String getQuery() {
        return query;
    }

    public void setCourses(List<CourseVoVwFull> courses) {
        this.courses = courses;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public boolean isCoursesEmpty(){
        return ( this.courses == null || this.courses.isEmpty());
    }
    
    public boolean isQueryEmpty(){
        return (this.query == null || this.query.isEmpty());
    }

    
    @PostConstruct
    public String getResults(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        if(params.get("searchForm:query") != null)
            this.query = params.get("searchForm:query");
        CoursesFacade coursesFacade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        try{
            courses = coursesFacade.search(this.query);
            return "success";
        }catch  (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("searchForm", new FacesMessage(ex.getMessage()));
        }
        return "failure";
    }
    
    public void preRenderView(){
        getResults();
    }
}
