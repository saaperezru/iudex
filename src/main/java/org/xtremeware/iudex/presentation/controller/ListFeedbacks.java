package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.xtremeware.iudex.businesslogic.facade.FeedbacksFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@ViewScoped
public class ListFeedbacks implements Serializable {

    private Long feedbackTypeId;
    private List<FeedbackVo> feedbacks;

    public List<FeedbackVo> getFeedbacks() {
        if (feedbacks == null) {
            loadFeedbacks();
        }
        return feedbacks;
    }

    public Long getFeedbackTypeId() {
        return feedbackTypeId;
    }

    public void setFeedbackTypeId(Long feedbackTypeId) {
        this.feedbackTypeId = feedbackTypeId;
    }

    public void loadFeedbacks() {
        FeedbacksFacade feedbacksFacade = Config.getInstance().getFacadeFactory().
                getFeedbacksFacade();
        if (feedbackTypeId != null && !feedbackTypeId.equals(0L)) {
            feedbacks = feedbacksFacade.getFeedbacksByFeedbackType(
                    feedbackTypeId);
        } else {
            feedbacks = feedbacksFacade.getAllFeedbacks();
        }
    }
}