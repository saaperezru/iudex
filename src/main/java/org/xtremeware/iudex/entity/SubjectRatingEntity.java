package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.SubjectRatingVo;

@javax.persistence.Entity
@NamedQueries({
    @NamedQuery(name = "getBySubjectId",
    query = "SELECT result FROM SubjectRating result "
    + "WHERE result.subject.id = :subjectId"),
    @NamedQuery(name = "getBySubjectIdAndUserId",
    query = "SELECT result FROM SubjectRating result "
    + "WHERE result.subject.id = :subjectId AND result.user.id = :userId")
})
public class SubjectRatingEntity implements Serializable, Entity<SubjectRatingVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private SubjectEntity subject;
    private UserEntity user;
    private int value;

    @Override
    public SubjectRatingVo toVo() {
        SubjectRatingVo vo = new SubjectRatingVo();

        vo.setId(getId());
        vo.setSubject(getSubject().getId());
        vo.setUser(getUser().getId());
        vo.setValue(getValue());

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

    public Long getId() {
        return id;
    }

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

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
