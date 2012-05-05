package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.CourseVo;

@javax.persistence.Entity(name = "Course")
@NamedQueries({
    @NamedQuery(name = "getCourseByProfessorId",
    query = "SELECT c FROM Course c WHERE c.professor.id = :professorId"),
    @NamedQuery(name = "getCourseBySubjectId", query = "SELECT c FROM Course c WHERE c.subject.id = :subjectId"),
    @NamedQuery(name = "getCourseByPeriodId", query = "SELECT c FROM Course c WHERE c.period.id = :periodId"),
    @NamedQuery(name = "getCourseByProfessorIdAndSubjectId",
    query = "SELECT c FROM Course c WHERE c.subject.id = :subjectId AND c.professor.id = :professorId"),
    @NamedQuery(name = "getProfessorBySubjectId",
    query = "SELECT c.professor FROM Course c WHERE c.subject.id = :subjectId"),
    @NamedQuery(name = "getCoursesByProfessorNameLikeAndSubjectNameLike",
    query = "SELECT c FROM Course c WHERE c.subject.name LIKE :subjectName AND "
    + "(c.professor.firstName LIKE :professorName OR c.professor.lastName LIKE :professorName) AND c.period.id = :periodId"),
    @NamedQuery(name = "getCoursesByProfessorNameLikeAndSubjectNameLikeAndPeriodId",
    query = "SELECT c FROM Course c WHERE c.subject.name LIKE :subjectName AND "
    + "(c.professor.firstName LIKE :professorName OR c.professor.lastName LIKE :professorName) AND c.period.id = :periodId")
})
@Table(name = "COURSE")
public class CourseEntity implements Serializable, Entity<CourseVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COURSE")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ID_PROFESSOR", nullable = false)
    private ProfessorEntity professor;
    @ManyToOne
    @JoinColumn(name = "ID_SUBJECT", nullable = false)
    private SubjectEntity subject;
    @ManyToOne
    @JoinColumn(name = "ID_PERIOD", nullable = false)
    private PeriodEntity period;
    @Column(name = "AVERAGE", nullable = false)
    private Double ratingAverage;
    @Column(name = "RATINGCOUNT", nullable = false)
    private Long ratingCount;

    @Override
    public CourseVo toVo() {
        CourseVo vo = new CourseVo();
        vo.setId(this.getId());
        vo.setPeriodId(this.getPeriod().getId());
        vo.setProfessorId(this.getProfessor().getId());
        vo.setRatingAverage(this.getRatingAverage());
        vo.setRatingCount(this.getRatingCount());
        vo.setSubjectId(this.getSubject().getId());

        return vo;

    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourseEntity)) {
            return false;
        }
        CourseEntity other = (CourseEntity) object;
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
        return "CourseEntity{" + "id=" + id + ", professor=" + professor
                + ", subject=" + subject + ", period=" + period
                + ", ratingAverage=" + ratingAverage + ", ratingCount=" + ratingCount + '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public PeriodEntity getPeriod() {
        return period;
    }

    public void setPeriod(PeriodEntity period) {
        this.period = period;
    }

    public ProfessorEntity getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorEntity professor) {
        this.professor = professor;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }
}
