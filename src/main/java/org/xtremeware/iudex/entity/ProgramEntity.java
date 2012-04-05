package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.ProgramVo;

@javax.persistence.Entity(name="Program")
@Table(name="PROGRAM")
public class ProgramEntity implements Serializable, Entity<ProgramVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_PROGRAM")
    private Long id;
    
    @Column(name="NAME",length=50,nullable=false)
    private String name;

    @Override
    public ProgramVo toVo() {
        ProgramVo vo = new ProgramVo();
        vo.setId(this.getId());
        vo.setName(this.getName());
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
        final ProgramEntity other = (ProgramEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ProgramEntity{" + "id=" + id + ", name=" + name + '}';
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
