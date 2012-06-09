package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.presentation.vovw.CourseListVoVwSmall;
import org.xtremeware.iudex.presentation.vovw.builder.CourseVoVwBuilder;

/**
 *
 * @author healarconr
 */
@ManagedBean
@ViewScoped
public class Search implements Serializable {

    private String query;
    private List<CourseListVoVwSmall> courses = new ArrayList<CourseListVoVwSmall>();
    ;
    private List<Integer> pages = new ArrayList<Integer>();
    private Integer currentPage;
    private static final int itemsPerPage = 10;
    private static final int maxItemsPerPage = 100;

    public List<CourseListVoVwSmall> getCourses() {
        if (courses.isEmpty()) {
            loadCourses();
        }
        return courses;
    }

    public void loadCourses() {

        for (int count = 1; count <= Math.ceil(CourseVoVwBuilder.getInstance().getSearchCount(query, maxItemsPerPage) / itemsPerPage); count++) {
            this.pages.add(count);
        }
        loadCourses(1);
    }

    public void loadCourses(int page) {
        this.courses.clear();
        this.courses.addAll(CourseVoVwBuilder.getInstance().getSearchResults(query, itemsPerPage, itemsPerPage * page));
        currentPage = page;
    }

    public void setCourses(List<CourseListVoVwSmall> courses) {
        this.courses = courses;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isCoursesEmpty() {
        return (this.getCourses() == null || this.getCourses().isEmpty());
    }

    public String getQuery() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        this.query = params.get("query");
        return query;
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
//        FacesContext fc = FacesContext.getCurrentInstance();
//        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
//        this.query = params.get("query");
//        try {
//            this.courses = CourseVoVwBuilder.getInstance().getSearchResults(query,itemsPerPage, itemsPerPage);
//        } catch (Exception ex) {
//            FacesContext.getCurrentInstance().addMessage("searchForm", new FacesMessage(ex.getMessage()));
//        }
    }
}
