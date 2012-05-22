package org.xtremeware.iudex.businesslogic.service.removeimplementations;

import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Remove;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.CommentsService;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class UsersRemove implements Remove {

    private AbstractDaoFactory daoFactory;

    public UsersRemove(AbstractDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Remove the user and all the objects associated to him These elements are
     * - CONFIRMATION_KEY - PROFESSOR_RATING - COURSE_RATING - SUBJECT_RATING -
     * COMMENT_RATING - COMMENT - COMMENT_RATINGS associated to the comments
     * made by the uses
     *
     * @param em EntityManager
     * @param id User ID
     */
    @Override
    public void remove(EntityManager em, Long id) throws DataBaseException {

        ConfirmationKeyEntity confirmationkey = getDaoFactory().getConfirmationKeyDao().getById(em, id);
        if (confirmationkey != null) {
            getDaoFactory().getConfirmationKeyDao().remove(em, confirmationkey.getId());
        }

        List<ProfessorRatingEntity> professorRatings = getDaoFactory().getProfessorRatingDao().getByUserId(em, id);
        for (ProfessorRatingEntity rating : professorRatings) {
            getDaoFactory().getProfessorRatingDao().remove(em, rating.getId());
        }

        List<CourseRatingEntity> courseRatings = getDaoFactory().getCourseRatingDao().getByUserId(em, id);
        for (CourseRatingEntity rating : courseRatings) {
            getDaoFactory().getCourseRatingDao().remove(em, rating.getId());
        }

        List<SubjectRatingEntity> subjectRatings = getDaoFactory().getSubjectRatingDao().getByUserId(em, id);
        for (SubjectRatingEntity rating : subjectRatings) {
            getDaoFactory().getSubjectRatingDao().remove(em, rating.getId());
        }

        List<CommentRatingEntity> commentRatings = getDaoFactory().getCommentRatingDao().getByUserId(em, id);
        for (CommentRatingEntity rating : commentRatings) {
            getDaoFactory().getCommentRatingDao().remove(em, rating.getId());
        }

        /**
         * This is a bad implementation, but due to few time, it had to be
         * implemented, it will be changed for the next release.
         */
        List<CommentEntity> comments = getDaoFactory().getCommentDao().getByUserId(em, id);

        CommentsService commentService = Config.getInstance().getServiceFactory().createCommentsService();
        for (CommentEntity comment : comments) {
            commentService.remove(em, comment.getId());
        }

        getDaoFactory().getUserDao().remove(em, id);
    }

    private AbstractDaoFactory getDaoFactory() {
        return daoFactory;
    }
}
