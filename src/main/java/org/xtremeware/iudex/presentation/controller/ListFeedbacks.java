package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.xtremeware.iudex.businesslogic.facade.FeedbacksFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.FeedbackVoVwFull;
import org.xtremeware.iudex.presentation.vovw.builder.FeedbackVoVwBuilder;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@ViewScoped
public class ListFeedbacks implements Serializable {

    private Long feedbackTypeId;
    private List<FeedbackVoVwFull> feedbacks;

    public List<FeedbackVoVwFull> getFeedbacks() {
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
        List<FeedbackVo> feedbackVos;
        if (feedbackTypeId != null && !feedbackTypeId.equals(0L)) {
            feedbackVos = feedbacksFacade.getFeedbacksByFeedbackType(
                    feedbackTypeId);
        } else {
            feedbackVos = feedbacksFacade.getAllFeedbacks();
        }
        FeedbackVoVwBuilder builder = FeedbackVoVwBuilder.getInstance();
        feedbacks = new ArrayList<FeedbackVoVwFull>(feedbackVos.size());
        for (FeedbackVo feedbackVo : feedbackVos) {
            feedbacks.add(builder.getFeedbackVoVwFull(feedbackVo));
        }
    }
}
