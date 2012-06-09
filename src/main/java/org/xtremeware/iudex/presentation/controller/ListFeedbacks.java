package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.PieChartModel;
import org.xtremeware.iudex.businesslogic.facade.FeedbacksFacade;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.FeedbackVoVwLarge;
import org.xtremeware.iudex.presentation.vovw.builder.FeedbackVoVwBuilder;
import org.xtremeware.iudex.vo.FeedbackTypeVo;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author healarconr
 */
@ManagedBean
@ViewScoped
public class ListFeedbacks implements Serializable {

    // TODO: Make the page size configurable
    private static final int PAGESIZE = 10;
    private Long feedbackTypeId;
    private List<FeedbackVoVwLarge> feedbacks;
    private List<Integer> pages;
    private Integer currentPage;
    private PieChartModel pieModel;
    
    public PieChartModel getPieModel() {
        if (pieModel == null) {
            pieModel = new PieChartModel();
            FeedbacksFacade feedbacksFacade = Config.getInstance().
                    getFacadeFactory().getFeedbacksFacade();
            List<FeedbackTypeVo> feedbackTypes = feedbacksFacade.
                    getFeedbackTypes();
            for (FeedbackTypeVo feedbackType : feedbackTypes) {
                pieModel.set(feedbackType.getName(), feedbacksFacade.
                        countFeedbacksByFeedbackType(feedbackType.getId()));
            }
        }
        return pieModel;
    }
    
    public Integer getCurrentPage() {
        return currentPage;
    }
    
    public List<Integer> getPages() {
        return pages;
    }
    
    public List<FeedbackVoVwLarge> getFeedbacks() {
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
        loadFeedbacks(1);
    }
    
    public void loadFeedbacks(int page) {
        FeedbacksFacade feedbacksFacade = Config.getInstance().getFacadeFactory().
                getFeedbacksFacade();
        List<FeedbackVo> feedbackVos;
        int firstResult = (page - 1) * PAGESIZE;
        int pagesCount;
        if (feedbackTypeId != null && !feedbackTypeId.equals(0L)) {
            feedbackVos = feedbacksFacade.getFeedbacksByFeedbackType(
                    feedbackTypeId, firstResult, PAGESIZE);
            pagesCount = (int) Math.ceil(feedbacksFacade.
                    countFeedbacksByFeedbackType(feedbackTypeId) /
                    (float) PAGESIZE);
        } else {
            feedbackVos = feedbacksFacade.getAllFeedbacks(firstResult, PAGESIZE);
            pagesCount = (int) Math.ceil(feedbacksFacade.countAllFeedbacks() /
                    (float) PAGESIZE);
        }
        pages = new ArrayList<Integer>(pagesCount);
        for (int i = 1; i <= pagesCount; i++) {
            pages.add(i);
        }
        currentPage = page;
        FeedbackVoVwBuilder builder = FeedbackVoVwBuilder.getInstance();
        feedbacks = new ArrayList<FeedbackVoVwLarge>(feedbackVos.size());
        for (FeedbackVo feedbackVo : feedbackVos) {
            feedbacks.add(builder.getFeedbackVoVwFull(feedbackVo));
        }
    }
}
