package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.SubjectVo;

@javax.persistence.Entity(name = "Subject")
@NamedQueries({
    @NamedQuery(name = "getSubjectsByNameLike", query =
    "SELECT s FROM Subject s WHERE UPPER(s.name) LIKE :name"),
    @NamedQuery(name = "getSubjectsByProfessorId",
    query =
    "SELECT DISTINCT c.subject FROM Course c WHERE c.professor.id = :professorId")
})
@Table(name = "SUBJECT")
public class SubjectEntity implements Serializable, Entity<SubjectVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_SUBJECT")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 50, unique = true)
    private String name;
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @Override
    public SubjectVo toVo() {
        SubjectVo vo = new SubjectVo();
        vo.setId(this.getId());
        vo.setName(SecurityHelper.sanitizeHTML(this.getName()));
        if (getDescription() != null) {
            vo.setDescription(SecurityHelper.sanitizeHTML(this.getDescription()));
        } else {
            vo.setDescription("");
        }
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
        final SubjectEntity other = (SubjectEntity) obj;
        if (this.id != other.id &&
                (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "SubjectEntity{" + "id=" + id + ", name=" + name +
                ", description=" + description + '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}