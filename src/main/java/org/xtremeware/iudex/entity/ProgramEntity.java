package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.ProgramVo;

@javax.persistence.Entity(name = "Program")
@NamedQueries({
    @NamedQuery(name = "getProgramByNameLike",
    query = "SELECT result FROM Program result "
    + "WHERE result.name LIKE :name"),
    @NamedQuery(name = "getAllPrograms",
    query = "SELECT result FROM Program result")})
@Table(name = "PROGRAM",
uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NAME"})})
public class ProgramEntity implements Serializable, Entity<ProgramVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROGRAM")
    private Long id;
    @Column(name = "NAME", length = 50, nullable = false, unique = true)
    private String name;
    
    @Column(name = "CODE", nullable = false, unique = true)
    private int code;

    @Override
    public ProgramVo toVo() {
        ProgramVo vo = new ProgramVo();
        vo.setId(this.getId());
        vo.setName(SecurityHelper.sanitizeHTML(this.getName()));
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
        final ProgramEntity other = (ProgramEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.code != other.code) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + this.code;
        return hash;
    }

    @Override
    public String toString() {
        return "ProgramEntity{" + "id=" + id + ", name=" + name + ", code=" + code + '}';
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