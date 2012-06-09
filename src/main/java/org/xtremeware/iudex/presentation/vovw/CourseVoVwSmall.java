package org.xtremeware.iudex.presentation.vovw;

import java.io.Serializable;

/**
 *
 * @author Diego Gerena (SNIPERCAT) <dagerenaq@gmail.com>
 */
public class CourseVoVwSmall implements Serializable {
    
    private Long courseId;
    private String period;
    private double average;
    private Long ratingcount;

    public CourseVoVwSmall(Long courseId, String period, double average, Long ratingcount) {
        this.courseId = courseId;
        this.period = period;
        this.average = average;
        this.ratingcount = ratingcount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CourseVoVwSmall other = (CourseVoVwSmall) obj;
        if (this.courseId != other.courseId && (this.courseId == null || !this.courseId.equals(other.courseId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.courseId != null ? this.courseId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CourseVoVwSmall{" + "courseId=" + courseId + ", Period=" + period + ", average=" + average + ", ratingcount=" + ratingcount + '}';
    }



    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getRatingcount() {
        return ratingcount;
    }

    public void setRatingcount(Long ratingcount) {
        this.ratingcount = ratingcount;
    }

}
