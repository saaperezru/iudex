package org.xtremeware.iudex.presentation.vovw;

import java.io.Serializable;
import java.util.Date;
import org.xtremeware.iudex.vo.FeedbackVo;

/**
 *
 * @author healarconr
 */
public class FeedbackVoVwFull implements Serializable {

    private Long feedbackTypeId;
    private String feedbackTypeName;
    private String content;
    private Date date;

    public FeedbackVoVwFull(FeedbackVo feedbackVo, String feedbackTypeName) {
        feedbackTypeId = feedbackVo.getFeedbackTypeId();
        this.feedbackTypeName = feedbackTypeName;
        content = feedbackVo.getContent();
        date = feedbackVo.getDate();
    }

    public Long getFeedbackTypeId() {
        return feedbackTypeId;
    }

    public void setFeedbackTypeId(Long feedbackTypeId) {
        this.feedbackTypeId = feedbackTypeId;
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

    public String getFeedbackTypeName() {
        return feedbackTypeName;
    }

    public void setFeedbackTypeName(String feedbackTypeName) {
        this.feedbackTypeName = feedbackTypeName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeedbackVoVwFull other = (FeedbackVoVwFull) obj;
        if ((this.feedbackTypeName == null) ? (other.feedbackTypeName != null) :
                !this.feedbackTypeName.equals(other.feedbackTypeName)) {
            return false;
        }
        if ((this.content == null) ? (other.content != null) :
                !this.content.equals(other.content)) {
            return false;
        }
        if (this.date != other.date && (this.date == null ||
                !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash =
                67 * hash +
                (this.feedbackTypeName != null ?
                this.feedbackTypeName.hashCode() :
                0);
        hash = 67 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 67 * hash + (this.date != null ? this.date.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "FeedbackVoVwFull{" + "feedbackTypeName=" + feedbackTypeName +
                ", content=" + content + ", date=" + date + '}';
    }
}
