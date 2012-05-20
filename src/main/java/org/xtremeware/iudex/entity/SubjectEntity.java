package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.SubjectVo;

@javax.persistence.Entity(name = "Subject")
@NamedQueries({
    @NamedQuery(name = "getSubjectsByNameLike", query = "SELECT s FROM Subject s WHERE UPPER(s.name) LIKE :name"),
    @NamedQuery(name = "getSubjectsByProfessorId", query = "SELECT DISTINCT c.subject FROM Course c WHERE c.professor.id = :professorId")
})
@Table(name = "SUBJECT")
public class SubjectEntity implements Serializable, Entity<SubjectVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUBJECT")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 50, unique = true)
    private String name;
    @Column(name = "CODE", unique = true)
    private int code;
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @Override
    public SubjectVo toVo() {
        SubjectVo vo = new SubjectVo();
        vo.setId(this.getId());
        vo.setName(SecurityHelper.sanitizeHTML(this.getName()));
        vo.setDescription(SecurityHelper.sanitizeHTML(this.getDescription()));
        vo.setCode(this.getCode());
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
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.code != other.code) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + this.code;
        hash = 79 * hash + (this.description != null ? this.description.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "SubjectEntity{" + "id=" + id + ", name=" + name + ", code=" + code + ", description=" + description + '}';
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}