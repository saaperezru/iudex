
package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.xtremeware.iudex.vo.ProfessorRatingVo;

@javax.persistence.Entity
public class ProfessorRatingEntity implements Serializable , Entity<ProfessorRatingVo> {
	private static final long serialVersionUID = 1L;
	@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private ProfessorEntity professor;
        private UserEntity user;
        private int value;
        
        @Override
        public ProfessorRatingVo toVo() {
            ProfessorRatingVo vo = new ProfessorRatingVo();
            
            vo.setId(getId());
            vo.setProfessor(getProfessor().getId());
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
        
        
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
        
        public ProfessorEntity getProfessor(){
                return this.professor;
        }
        
        public void setProfessor(ProfessorEntity professor){
                this.professor = professor;
        }

        public UserEntity getUser(){
                return this.user;
        }
        
        public void setUser(UserEntity user){
                this.user = user;
        }
        
        public int getValue(){
                return this.value;
        }
        
        public void setValue(int value){
                this.value = value;
        }

	

	

}
