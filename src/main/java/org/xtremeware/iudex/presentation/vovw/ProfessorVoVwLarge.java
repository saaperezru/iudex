package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.ProfessorVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.ValueObject;

public class ProfessorVoVwLarge implements ValueObject {

    private ProfessorVo vo;
    private RatingSummaryVo ratingSummary;
    //TODO: Get this from a centralized place

    public ProfessorVoVwLarge(ProfessorVo vo, RatingSummaryVo ratingSummary) {
        this.vo = vo;
        this.ratingSummary = ratingSummary;
    }

    @Override
    public String toString() {
        return "ProfessorVoVwLarge{" + "vo=" + vo + ", ratingSummary=" +
                ratingSummary + '}';
    }

    public RatingSummaryVo getRatingSummary() {
        return ratingSummary;
    }

    public String getDescription() {
        return vo.getDescription();
    }

    public String getEmail() {
        return vo.getEmail();
    }

    public String getFirstName() {
        return vo.getFirstName();
    }

    public Long getId() {
        return vo.getId();
    }

    public String getImageUrl() {
        return vo.getImageUrl();
    }

    public String getLastName() {
        return vo.getLastName();
    }

    public String getWebsite() {
        return vo.getWebsite();
    }

    public String getFullName() {
        return vo.getFirstName() + " " + vo.getLastName();
    }
}
