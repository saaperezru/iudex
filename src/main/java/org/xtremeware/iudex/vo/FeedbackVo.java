package org.xtremeware.iudex.vo;

import java.util.Date;

public class FeedbackVo extends ValueObject {

    private Long id;
    private FeedbackTypeVo type;
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
        int hash = 7;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 47 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 47 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 47 * hash + (this.date != null ? this.date.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "FeedbackVo{" + "id=" + id + ", type=" + type + ", content=" + content + ", date=" + date + '}';
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeedbackTypeVo getType() {
        return type;
    }

    public void setType(FeedbackTypeVo type) {
        this.type = type;
    }
}
