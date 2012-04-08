package org.xtremeware.iudex.presentation.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class Search {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    public void preRenderView(){
        
    }
}
