package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import org.xtremeware.iudex.vo.ProfessorVo;

@javax.persistence.Entity
@NamedQuery(name="getProfessorByNameLike",query="SELECT p FROM Professor p WHERE p.firstName = :name OR p.lastName = :name")
public class ProfessorEntity implements Serializable, Entity<ProfessorVo> {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String website;
    private String imageUrl;
    private String description;
    
    @Override
    public ProfessorVo toVo() {
        ProfessorVo vo = new ProfessorVo();
        vo.setId(this.getId());
        vo.setFirstName(this.getFirstName());
        vo.setLastName(this.getLastName());
        vo.setWebsite(this.getWebsite());
        vo.setImageUrl(this.getImageUrl());
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
        final ProfessorEntity other = (ProfessorEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "ProfessorEntity{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", website=" + website + ", imageUrl=" + imageUrl + ", description=" + description + '}';
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
}
