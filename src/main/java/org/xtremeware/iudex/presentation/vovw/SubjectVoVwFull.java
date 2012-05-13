package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.SubjectVo;

public class SubjectVoVwFull {

    private SubjectVo vo;
    private RatingSummaryVo ratingSummary;

    public SubjectVoVwFull(SubjectVo vo, RatingSummaryVo ratingSummary) {
        this.vo = vo;
        this.ratingSummary = ratingSummary;
    }

    @Override
    public String toString() {
        return "SubjectVoVwFull{" + vo.toString() + "ratingSummary=" + ratingSummary + '}';
    }

    public RatingSummaryVo getRatingSummary() {
        return ratingSummary;
    }

    public String getDescription() {
        return vo.getDescription();
    }

    public SubjectVo getVo() {
        return vo;
    }

    public Long getId() {
        return vo.getId();
    }

    public String getName() {
        return vo.getName();
    }
}
