package org.xtremeware.iudex.presentation.controller;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwMedium;
import org.xtremeware.iudex.presentation.vovw.CourseVoVwLarge;
import org.xtremeware.iudex.presentation.vovw.SubjectVoVwLarge;

/**
 *
 * @author healarconr
 */
@ManagedBean
@RequestScoped
public class ViewSubject {

    public class CourseInfo {

        private CourseVoVwLarge course;
        private List<CommentVoVwMedium> comments;

        public CourseInfo(CourseVoVwLarge course, List<CommentVoVwMedium> comments) {
            this.course = course;
            this.comments = comments;
        }

        public List<CommentVoVwMedium> getComments() {
            return comments;
        }

        public CourseVoVwLarge getCourse() {
            return course;
        }
    }
    @ManagedProperty(value = "#{param['id']}")
    private long id;
    private SubjectVoVwLarge subject;
    private List<CourseInfo> courses;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SubjectVoVwLarge getSubject() {
        return subject;
    }

    public List<CourseInfo> getCourses() {
        return courses;
    }

//    public void preRenderView() {
//        try {
//            subject = Config.getInstance().getFacadeFactory().getSubjectsFacade().getSubject(id);
//            if (subject == null) {
//                ((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
//            }
//            for (CourseVoVwFull c : Config.getInstance().getFacadeFactory().getCoursesFacade().getBySubjectId(id)) {
//                courses.add(new CourseInfo(c, Config.getInstance().getFacadeFactory().getCommentsFacade().getCommentsByCourseId(c.getId())));
//            }
//        } catch (Exception ex) {
//            //TODO : Deal with the exception
//            Logger.getLogger(ViewSubject.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
