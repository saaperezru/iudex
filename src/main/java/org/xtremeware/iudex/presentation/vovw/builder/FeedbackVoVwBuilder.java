package org.xtremeware.iudex.presentation.vovw.builder;

import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.FeedbackVoVwFull;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author healarconr
 */
public class FeedbackVoVwBuilder {

    private static FeedbackVoVwBuilder instance;
    private FacadeFactory facadeFactory;

    private FeedbackVoVwBuilder(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public static synchronized FeedbackVoVwBuilder getInstance() {
        if (instance == null) {
            instance = new FeedbackVoVwBuilder(Config.getInstance().
                    getFacadeFactory());
        }
        return instance;
    }

    public FeedbackVoVwFull getFeedbackVoVwFull(FeedbackVo feedbackVo) {
        return new FeedbackVoVwFull(feedbackVo,
                facadeFactory.getFeedbacksFacade().getFeedbackType(feedbackVo.
                getFeedbackTypeId()).getName());
    }
}
