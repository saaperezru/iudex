package org.xtremeware.iudex.vo;

import java.util.Date;

public class FeedbackVo extends IdentifiableValueObject<Long> implements ValueObject {

    private Long feedbackTypeId;
    private String content;
    private Date date;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeedbackVo other = (FeedbackVo) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.feedbackTypeId != other.feedbackTypeId && (this.feedbackTypeId == null || !this.feedbackTypeId.equals(other.feedbackTypeId))) {
            return false;
        }
        if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
            return false;
        }
        if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 41 * hash + (this.feedbackTypeId != null ? this.feedbackTypeId.hashCode() : 0);
        hash = 41 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 41 * hash + (this.date != null ? this.date.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "FeedbackVo{" + "id=" + id + ", feedbackTypeid=" + feedbackTypeId + ", content=" + content + ", date=" + date + '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getFeedbackTypeId() {
        return feedbackTypeId;
    }

    public void setFeedbackTypeId(Long feedbackTypeId) {
        this.feedbackTypeId = feedbackTypeId;
    }
}
