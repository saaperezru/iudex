
package org.xtremeware.iudex.businesslogic.service;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.CommentEntity;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.Config;
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
                
            List<CourseRatingEntity> courseRatings = getDaoFactory().getCourseRatingDao().getByCourseId(em, courseId);
                for (CourseRatingEntity rating : courseRatings){
                    getDaoFactory().getCourseRatingDao().remove(em,rating.getId());
                }

             /**
              * This is a bad implementation, but due to few time, it had to be implemented,
              * it will be changed for the next release.
              */
                List<CommentEntity> comments = getDaoFactory().getCommentDao().getByCourseId(em, courseId);
                
                CommentsService commentService = Config.getInstance().getServiceFactory().createCommentsService();
                for (CommentEntity comment : comments){
                        commentService.remove(em, comment.getId());    
                } 
            
            getDaoFactory().getCourseDao().remove(em, courseId);
                
	}

		
	public CourseEntity voToEntity(EntityManager em, CourseVo vo){

	}


}
