package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.ProfessorRatingVo;

@javax.persistence.Entity(name = "ProfessorRating")
@NamedQueries({
    @NamedQuery(name = "getRatingByProfessorId",
    query = "SELECT r FROM ProfessorRating r WHERE r.professor.id = :professor"),
    @NamedQuery(name = "getRatingByProfessorIdAndUserId",
    query = "SELECT r FROM ProfessorRating r WHERE r.professor.id = :professor AND r.user.id = :user"),
    @NamedQuery(name = "getRatingByUserId",
    query = "SELECT r FROM ProfessorRating r WHERE r.user.id = :user"),
    @NamedQuery(name = "countPositiveProfessorRating",
    query = "SELECT COUNT (result) FROM ProfessorRating result "
    + "WHERE result.professor.id = :professorId AND result.value = 1"),
    @NamedQuery(name = "countNegativeProfessorRating",
    query = "SSELECT COUNT (result) FROM ProfessorRating result "
    + "WHERE result.professor.id = :professorId AND result.value = -1")})
@Table(name = "PROFESSOR_RATING")
public class ProfessorRatingEntity implements Serializable, Entity<ProfessorRatingVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROFESSOR_RATING")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_PROFESSOR", nullable = false)
    private ProfessorEntity professor;
    @ManyToOne
    @JoinColumn(name = "ID_USER_", nullable = false)
    private UserEntity user;
    @Column(name = "RATING", nullable = false)
    private int value;

    @Override
    public ProfessorRatingVo toVo() {
        ProfessorRatingVo vo = new ProfessorRatingVo();

        vo.setId(getId());
        vo.setEvaluetedObjectId(getProfessor().getId());
        vo.setUser(getUser().getId());
        vo.setValue(getValue());

        return vo;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfessorRatingEntity)) {
            return false;
        }
        ProfessorRatingEntity other = (ProfessorRatingEntity) object;
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
    public String toString() {
        return "ProfessorRatingEntity{" + "id=" + id + '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ProfessorEntity getProfessor() {
        return this.professor;
    }

    public void setProfessor(ProfessorEntity professor) {
        this.professor = professor;
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
