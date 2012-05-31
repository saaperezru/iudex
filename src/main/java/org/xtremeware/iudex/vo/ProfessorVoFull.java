package org.xtremeware.iudex.vo;

public class ProfessorVoFull implements ValueObject {

    private ProfessorVo vo;
    private RatingSummaryVo ratingSummary;

    public ProfessorVoFull(ProfessorVo vo, RatingSummaryVo ratingSummary) {
        this.vo = vo;
        this.ratingSummary = ratingSummary;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProfessorVoFull other = (ProfessorVoFull) obj;
        if (this.vo != other.vo && (this.vo == null || !this.vo.equals(other.vo))) {
            return false;
        }
        if (this.ratingSummary != other.ratingSummary && (this.ratingSummary == null || !this.ratingSummary.equals(other.ratingSummary))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.vo != null ? this.vo.hashCode() : 0);
        hash = 11 * hash + (this.ratingSummary != null ? this.ratingSummary.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ProfessorVoFull{" + "vo=" + vo + ", ratingSummary=" + ratingSummary + '}';
    }

    public RatingSummaryVo getRatingSummary() {
        return ratingSummary;
    }

    public void setRatingSummary(RatingSummaryVo ratingSummary) {
        this.ratingSummary = ratingSummary;
    }

    public ProfessorVo getVo() {
        return vo;
    }

    public void setVo(ProfessorVo vo) {
        this.vo = vo;
    }
}
