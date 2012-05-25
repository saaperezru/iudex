package org.xtremeware.iudex.vo;

import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class RatingSummaryVo implements ValueObject {

    private int positive;
    private int negative;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RatingSummaryVo other = (RatingSummaryVo) obj;
        if (this.positive != other.positive) {
            return false;
        }
        if (this.negative != other.negative) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.positive;
        hash = 29 * hash + this.negative;
        return hash;
    }

    @Override
    public String toString() {
        return "RatingSummaryVo{" + "positive=" + positive + ", negative=" + negative + '}';
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }
}
