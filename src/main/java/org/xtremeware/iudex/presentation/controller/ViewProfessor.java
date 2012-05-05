package org.xtremeware.iudex.presentation.controller;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class ViewProfessor {

    @ManagedProperty(value = "#{param['id']}")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void preRenderView() {
    }
}
