/*
 */
package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.*;
import javax.faces.application.*;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import org.primefaces.event.RateEvent;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.MultipleMessagesException;
import org.xtremeware.iudex.presentation.vovw.*;
import org.xtremeware.iudex.presentation.vovw.builder.*;
import org.xtremeware.iudex.vo.*;

/**
 *
 * @author tuareg
 */
@ManagedBean
@ViewScoped
public class ViewCourse implements Serializable {

    public ViewCourse() {
        id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
    }
    private CourseVoVwLarge course;
    private List<CommentVoVwMedium> comments;
    private Long id;
    private CourseRatingVo courseRating;
    @ManagedProperty(value = "#{user}")
    private User user;

    public List<CommentVoVwMedium> getComments() {
        return comments;
    }

    public void setComments(List<CommentVoVwMedium> comments) {
        this.comments = comments;
    }

    public CourseVoVwLarge getCourse() {
        return course;
    }

    public void setCourse(CourseVoVwLarge course) {
        this.course = course;
    }

    public ProfessorVoVwLarge getProfessor() {
        return course.getProfessor();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int parseUserCommentVote(long commentId) {
        int userCommentVoteValue = 0;
        if (user != null && user.isLoggedIn()) {
            BinaryRatingVo commentRatingByUserId = Config.getInstance().getFacadeFactory().getCommentsFacade().getCommentRatingByUserId(commentId, user.getId());
            if (commentRatingByUserId != null) {
                userCommentVoteValue = commentRatingByUserId.getValue();
            }
        }
        return userCommentVoteValue;
    }

    public Integer getCourseRating() {
        if (user != null && user.isLoggedIn()) {
            courseRating = Config.getInstance().getFacadeFactory().getCoursesFacade().getCourseRatingByUserId(id, user.getId());
            if (courseRating == null) {
                courseRating = new CourseRatingVo();
                courseRating.setCourseId(id);
                courseRating.setUserId(user.getId());
            }
        }
        return (int) courseRating.getValue();
    }

    public void setCourseRating(Integer rating) {
        double conversion = Double.valueOf(Integer.toString(rating));
        float value = (float) conversion;
        this.courseRating.setValue(value);
        try {
            Config.getInstance().getFacadeFactory().getCoursesFacade().rateCourse(id, user.getId(), value);
        } catch (MultipleMessagesException ex) {
            Config.getInstance().getServiceFactory().getLogService().error(ex);
        } catch (Exception ex) {
            Config.getInstance().getServiceFactory().getLogService().error(ex);
        }
    }

    public void onRate(RateEvent rateEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación exitosa", "Has calificado esta materia con un : " + (rateEvent.getRating()) + ". ¡Agradecemos tu opinión!");

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void oncancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Calificación eliminada", "Hemos eliminado exitosamente tu calificación.");

        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void preRenderView() {
        if (id != null) {
            try {
                course = CourseVoVwBuilder.getInstance().getCourseVoVwFull(id);
                if (course == null) {
                    ((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
                }
                comments = CommentVoVwBuilder.getInstance().getCommentsByCourseId(id);
                Collections.sort(comments, new Comparator<CommentVoVwMedium>() {

                    @Override
                    public int compare(CommentVoVwMedium o1, CommentVoVwMedium o2) {
                        int o1Rating = o1.getRating().getPositive() - o1.getRating().getNegative();
                        int o2Rating = o2.getRating().getPositive() - o2.getRating().getNegative();
                        return (o1Rating > o2Rating ? -1 : (o1Rating == o2Rating ? 0 : 1));
                    }
                });
            } catch (Exception ex) {
                Config.getInstance().getServiceFactory().getLogService().error(ex.getMessage(), ex);
                ((ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler()).performNavigation("notfound");
            }
        }
    }
}
