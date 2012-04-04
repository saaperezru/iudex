package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import org.xtremeware.iudex.vo.CommentVo;

@javax.persistence.Entity
public class CommentEntity implements Serializable, Entity<CommentVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    private UserEntity user;
    private CourseEntity course;
    private boolean anonymous;
    private Float rating;

    @Override
    public CommentVo toVo() {
        CommentVo vo = new CommentVo();
        vo.setId(getId());
        vo.setContent(getContent());
        vo.setDate(getDate());
        vo.setUserId(getUser().getId());
        vo.setCourseId(getCourse().getId());
        vo.setAnonymous(isAnonymous());
        vo.setRating(getRating());
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
        final CommentEntity other = (CommentEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CommentEntity{" + "id=" + id + ", content=" + content + ", date=" + date + ", user=" + user + ", course=" + course + ", anonymous=" + anonymous + ", rating=" + rating + '}';
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

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
