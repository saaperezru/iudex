package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.xtremeware.iudex.vo.FeedbackVo;

@javax.persistence.Entity(name="Feedback")
@Table(name="FEEDBACK")
public class FeedbackEntity implements Serializable, Entity<FeedbackVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_FEEDBACK")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="ID_TYPE_FEEDBACK", nullable= false)
    private FeedbackTypeEntity type;
    
    @Lob @Column(name="CONTENT", nullable=false)
    private String content;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="DATE_FEEDBACK", nullable= false)
    private Date date;

    @Override
    public FeedbackVo toVo() {
        FeedbackVo vo = new FeedbackVo();

        vo.setId(id);
        vo.setType(type.toVo());
        vo.setContent(content);
        vo.setDate(date);

        return vo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeedbackEntity other = (FeedbackEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
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
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 97 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 97 * hash + (this.date != null ? this.date.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "FeedbackEntity{" + "id=" + id + ", type=" + type + ", content=" + content + ", date=" + date + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public FeedbackTypeEntity getType() {
        return type;
    }

    public void setType(FeedbackTypeEntity type) {
        this.type = type;
    }
}
