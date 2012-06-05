package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.ProfessorVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.ValueObject;

public class ProfessorVoVwFull implements ValueObject {

    private ProfessorVo vo;
    private RatingSummaryVo ratingSummary;
    //TODO: Get this from a centralized place
    private static final String DEFAULTIMAGEURL = "../resources/professor.png";

    public ProfessorVoVwFull(ProfessorVo vo, RatingSummaryVo ratingSummary) {
        this.vo = vo;
        this.ratingSummary = ratingSummary;
    }

    @Override
    public String toString() {
        return "ProfessorVoVwFill{" + vo.toString() + "ratingSummary=" + ratingSummary + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProfessorVoVwFull other = (ProfessorVoVwFull) obj;
        if (this.vo.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.vo != null ? this.vo.hashCode() : 0);
        hash = 97 * hash + (this.ratingSummary != null ? this.ratingSummary.hashCode() : 0);
        return hash;
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
        String url = vo.getImageUrl();
        if (url == null) {
            return DEFAULTIMAGEURL;
        }
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
