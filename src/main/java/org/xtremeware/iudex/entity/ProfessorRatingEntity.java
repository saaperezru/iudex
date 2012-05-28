package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.BinaryRatingVo;

@javax.persistence.Entity(name = "ProfessorRating")
@NamedQueries({
    @NamedQuery(name = "getProfessorRatingByProfessorId",
    query = "SELECT r FROM ProfessorRating r WHERE r.professor.id = :evaluatedObjectId"),
    @NamedQuery(name = "getProfessorRatingByProfessorIdAndUserId",
    query = "SELECT r FROM ProfessorRating r WHERE r.professor.id = :evaluatedObjectId AND r.user.id = :userId"),
    @NamedQuery(name = "getProfessorRatingByUserId",
    query = "SELECT r FROM ProfessorRating r WHERE r.user.id = :userId"),
    @NamedQuery(name = "countPositiveProfessorRating",
    query = "SELECT COUNT (result) FROM ProfessorRating result "
    + "WHERE result.professor.id = :evaluatedObjectId AND result.value = 1"),
    @NamedQuery(name = "countNegativeProfessorRating",
    query = "SELECT COUNT (result) FROM ProfessorRating result "
    + "WHERE result.professor.id = :evaluatedObjectId AND result.value = -1")})
@Table(name = "PROFESSOR_RATING")
public class ProfessorRatingEntity implements Serializable, Entity<BinaryRatingVo>, BinaryRatingEntity {

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
    public BinaryRatingVo toVo() {
        BinaryRatingVo vo = new BinaryRatingVo();

        vo.setId(getId());
        vo.setEvaluatedObjectId(getProfessor().getId());
        vo.setUserId(getUser().getId());
        vo.setValue(getValue());

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
        final ProfessorRatingEntity other = (ProfessorRatingEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.professor != other.professor && (this.professor == null || !this.professor.equals(other.professor))) {
            return false;
        }
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.value != other.value) {
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

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }
}
