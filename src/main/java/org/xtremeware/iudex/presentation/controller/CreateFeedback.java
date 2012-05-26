package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.helper.ViewHelper;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class CreateFeedback implements Serializable {
    
    private Long feedbackTypeId;
    private String content;
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getFeedbackTypeId() {
        return feedbackTypeId;
    }
    
    public void setFeedbackTypeId(Long feedbackTypeId) {
        this.feedbackTypeId = feedbackTypeId;
    }
    
    public void submit() {
        try {
            Config.getInstance().getFacadeFactory().getFeedbacksFacade().
                    addFeedback(feedbackTypeId, content, new Date());
            feedbackTypeId = null;
            content = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    "Comentario guardado exitosamente"));
        } catch (MultipleMessagesException ex) {
            ViewHelper.addExceptionFacesMessage(null, ex);
        }
    }
}