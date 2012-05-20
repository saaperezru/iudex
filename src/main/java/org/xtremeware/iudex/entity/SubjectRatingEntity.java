package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.SubjectRatingVo;

@javax.persistence.Entity(name = "SubjectRating")
@NamedQueries({
    @NamedQuery(name = "getSubjectRatingBySubjectId",
    query = "SELECT result FROM SubjectRating result "
    + "WHERE result.subject.id = :evaluatedObjectId"),
    @NamedQuery(name = "getSubjectRatingBySubjectIdAndUserId",
    query = "SELECT result FROM SubjectRating result "
    + "WHERE result.subject.id = :evaluatedObjectId AND result.user.id = :userId"),
    @NamedQuery(name = "getSubjectRatingByIdUser",
    query = "SELECT result FROM SubjectRating result "
    + "WHERE result.user.id = :user"),
    @NamedQuery(name = "countPositiveSubjectRating",
    query = "SELECT COUNT (result) FROM SubjectRating result "
    + "WHERE result.subject.id = :evaluatedObjectId AND result.value = 1"),
    @NamedQuery(name = "countNegativeSubjectRating",
    query = "SELECT COUNT (result) FROM SubjectRating result "
    + "WHERE result.subject.id = :evaluatedObjectId AND result.value = -1")
})
@Table( name = "SUBJECT_RATING",
            uniqueConstraints = { @UniqueConstraint( columnNames = { "ID_USER_", "ID_SUBJECT" } ) } )
public class SubjectRatingEntity implements Serializable, Entity<SubjectRatingVo>, RatingEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUBJECT_RATING")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_SUBJECT", nullable = false)
    private SubjectEntity subject;
    @ManyToOne
    @JoinColumn(name = "ID_USER_", nullable = false)
    private UserEntity user;
    @Column(name = "RATING", nullable = false)
    private int value;

    @Override
    public SubjectRatingVo toVo() {
        SubjectRatingVo vo = new SubjectRatingVo();

        vo.setId(this.getId());
        vo.setEvaluatedObjectId(this.getSubject().getId());
        vo.setUserId(this.getUser().getId());
        vo.setValue(this.getValue());

        return vo;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubjectRatingEntity)) {
            return false;

        }
        SubjectRatingEntity other = (SubjectRatingEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public SubjectEntity getSubject() {
        return this.subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }
}
