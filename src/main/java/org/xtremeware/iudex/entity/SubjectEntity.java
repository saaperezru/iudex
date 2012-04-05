package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.xtremeware.iudex.vo.SubjectVo;

@javax.persistence.Entity(name="Subject")
@Table(name="SUBJECT")
public class SubjectEntity implements Serializable, Entity<SubjectVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ID_SUBJECT")
    private Long id;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="DESCRIPTION")
    private String description;

    @Override
    public SubjectVo toVo() {
        SubjectVo vo = new SubjectVo();
        vo.setId(this.getId());
        vo.setName(this.getName());
        vo.setDescription(this.getDescription());
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
        return "SubjectEntity{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

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
