
package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.vo.CourseVo;
import org.xtremeware.iudex.vo.ValueObject;

public class CoursesService extends CrudService<CourseVo> {

	public CoursesService(AbstractDaoFactory daoFactory){
		super(daoFactory);
	}

	public void validateVo(EntityManager em, CourseVo course){
	}

	public CourseVo create(EntityManager em, CourseVo course){
	}	

	public CourseVo getById(EntityManager em, long id){

	}

	public List<CourseVo>  getByProfessorId(EntityManager em, long professorId){

	}

	public List<CourseVo> getBySubjectId(EntityManager em, long subjectId){

	}

	public List<CourseVo> getSimilarCourses(EntityManager em, String professorName, String subjectName, long preiodId){

	}

	public void update(EntityManager em, CourseVo course){

	}

	public void remove(EntityManager em, long courseId){

	}

		
	public CourseEntity voToEntity(EntityManager em, CourseVo vo){

	}


}
