package org.xtremeware.iudex.vo;

import java.util.Date;

public class CommentVo extends IdentifiableValueObject<Long> implements ValueObject {

    private String content;
    private Date date;
    private Long userId;
    private Long courseId;
    private boolean anonymous;
    private Float rating;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommentVo other = (CommentVo) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
            return false;
        }
        if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
            return false;
        }
        if (this.courseId != other.courseId && (this.courseId == null || !this.courseId.equals(other.courseId))) {
            return false;
        }
        if (this.anonymous != other.anonymous) {
            return false;
        }
        if (this.rating != other.rating && (this.rating == null || !this.rating.equals(other.rating))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 97 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 97 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 97 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 97 * hash + (this.courseId != null ? this.courseId.hashCode() : 0);
        hash = 97 * hash + (this.anonymous ? 1 : 0);
        hash = 97 * hash + (this.rating != null ? this.rating.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CommentVo{" + "id=" + getId() + ", content=" + content 
                + ", date=" + date + ", user=" + userId + ", course=" 
                + courseId + ", anonymous=" + anonymous + ", rating=" + rating + '}';
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
