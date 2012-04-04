
package org.xtremeware.iudex.dao;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.entity.Entity;

public abstract class Dao<E extends Entity>{
	public E persist(EntityManager em,E entity){
		return null;
	}
	public E merge(EntityManager em, E entity){
		return null;
	}

	public void remove(EntityManager em,long id){

	}

	public E getById(EntityManager em, long id){
		return null;
	}

	public List<E> list(EntityManager em) {
		return null;
	}

	public List<E> list(EntityManager em, int from, int max){
		return null;
	}

	public int count(EntityManager em){
		return 0;
	}


}
