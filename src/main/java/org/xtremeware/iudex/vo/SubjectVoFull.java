package org.xtremeware.iudex.vo;

public class SubjectVoFull implements ValueObject{

    private SubjectVo vo;
    private RatingSummaryVo ratingSummary;

    public SubjectVoFull(SubjectVo vo, RatingSummaryVo ratingSummary) {
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
        final SubjectVoFull other = (SubjectVoFull) obj;
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
        hash = 61 * hash + (this.vo != null ? this.vo.hashCode() : 0);
        hash = 61 * hash + (this.ratingSummary != null ? this.ratingSummary.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "SubjectVoFull{" + "vo=" + vo + ", ratingSummary=" + ratingSummary + '}';
    }

    public RatingSummaryVo getRatingSummary() {
        return ratingSummary;
    }

    public void setRatingSummary(RatingSummaryVo ratingSummary) {
        this.ratingSummary = ratingSummary;
    }

    public SubjectVo getVo() {
        return vo;
    }

    public void setVo(SubjectVo vo) {
        this.vo = vo;
    }
    
}
