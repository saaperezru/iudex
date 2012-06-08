package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.businesslogic.facade.CoursesFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.builder.CourseVoVwBuilder;

/**
 *
 * @author healarconr
 */

@ManagedBean
@ViewScoped
public class Search implements Serializable {

    private String query;
    private List<CourseVoVwFull> courses;
    private List<Long> listCourses;

    public List<Long> getListCourses() {
        return listCourses;
    }

    public void setListCourses(List<Long> listCourses) {
        this.listCourses = listCourses;
    }
    
    public List<CourseVoVwFull> getCourses() {
        return courses;
    }
    
    public String getQuery() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        this.query = params.get("query");
        return query;
    }

    public void setCourses(List<CourseVoVwFull> courses) {
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
    
    public String test(int id){
        return "ID= "+id;
    }

    public void preRenderView() {
        CoursesFacade coursesFacade = Config.getInstance().getFacadeFactory().getCoursesFacade();
        try {
            this.listCourses = coursesFacade.search(query);
            this.courses = CourseVoVwBuilder.getInstance().getSearchResults(query);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("searchForm", new FacesMessage(ex.getMessage()));
        }
    }
}
