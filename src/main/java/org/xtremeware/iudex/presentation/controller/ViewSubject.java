package org.xtremeware.iudex.presentation.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwFull;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwFull;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwFull;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class ViewSubject {

	public class courseInfo{
		private CourseVoVwFull course;
		private List<CommentVoVwFull> comments;

		public courseInfo(CourseVoVwFull course, List<CommentVoVwFull> comments) {
			this.course = course;
			this.comments = comments;
		}

		public List<CommentVoVwFull> getComments() {
			return comments;
		}

		public CourseVoVwFull getCourse() {
			return course;
		}
		
	}
    @ManagedProperty(value = "#{param['id']}")
    private long id;
	private SubjectVoVwFull subject ;
	private List<courseInfo> courses;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public SubjectVoVwFull getSubject() {
		return subject;
	}

	public List<courseInfo> getCourses() {
		return courses;
	}


    public void preRenderView() {
		try {
			subject = Config.getInstance().getFacadeFactory().getSubjectsFacade().getSubject(id);
			if (subject==null){
				((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
			}
			for (CourseVoVwFull c :Config.getInstance().getFacadeFactory().getCoursesFacade().getBySubjectId(id)){
				courses.add(new courseInfo(c, Config.getInstance().getFacadeFactory().getCommentsFacade().getCommentsByCourseId(c.getId())));
			}
		} catch (Exception ex) {
			//TODO : Deal with the exception
			Logger.getLogger(ViewSubject.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
}
