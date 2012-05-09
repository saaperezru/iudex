package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.xtremeware.iudex.vo.CommentVo;

@javax.persistence.Entity(name = "Comment")
@NamedQueries({
    @NamedQuery(name = "getCommentsByProfessorId", query = "SELECT c FROM Comment c WHERE c.course.professor.id = :professorId"),
    @NamedQuery(name = "getCommentsBySubjectId", query = "SELECT c FROM Comment c WHERE c.course.subject.id = :subjectId"),
    @NamedQuery(name = "getCommentsByUserId", query = "SELECT c FROM Comment c WHERE c.user.id = :userId"),
    @NamedQuery(name = "getCommentsByCourseId", query = "SELECT c FROM Comment c WHERE c.course.id = :courseId"),
    @NamedQuery(name = "getUserCommentsCounter", query = "SELECT COUNT(c) FROM Comment c WHERE c.date >= CURRENT_DATE AND c.user.id = :userId")
})
@Table(name = "COMMENT_")
public class CommentEntity implements Serializable, Entity<CommentVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMMENT_")
    private Long id;
    @Column(name = "CONTENT", length = (2000), nullable = false)
    private String content;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "DATE_COMMENT", nullable = false)
    private Date date;
    @ManyToOne
    @JoinColumn(name = "ID_USER_", nullable = false)
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "ID_COURSE", nullable = false)
    private CourseEntity course;
    @Column(name = "ANONYMOUS", nullable = false)
    private boolean anonymous;
    @Column(name = "RATING", nullable = false)
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
        return "CommentEntity{" + "id=" + id + ", content=" + content
                + ", date=" + date + ", user=" + user + ", course=" + course
                + ", anonymous=" + anonymous + ", rating=" + rating + '}';
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
