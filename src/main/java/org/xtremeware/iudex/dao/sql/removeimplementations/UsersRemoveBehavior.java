package org.xtremeware.iudex.dao.sql.removeimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class UsersRemoveBehavior implements Remove {

    private AbstractDaoBuilder daoBuilder;
    private SimpleRemove<UserEntity> simpleRemove;

    public UsersRemoveBehavior(AbstractDaoBuilder daoBuilder,
            SimpleRemove simpleRemove) {
        this.daoBuilder = daoBuilder;
        this.simpleRemove = simpleRemove;
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
    
    @Override
    public void remove(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        removeConfirmationKey(entityManager, entity.getId());
        removeProfessorRatings(entityManager, entity.getId());
        removeCourseRatings(entityManager, entity.getId());
        removeSubjectRatings(entityManager, entity.getId());
        removeCommentRatings(entityManager, entity.getId());
        removeComments(entityManager, entity.getId());
        getSimpleRemove().remove(entityManager, entity);
    }

    private SimpleRemove<UserEntity> getSimpleRemove() {
        return simpleRemove;
    }

    private void removeConfirmationKey(EntityManager entityManager, Long entityId) throws DataBaseException {
        ConfirmationKeyEntity confirmationkey = getDaoBuilder().getConfirmationKeyDao().getById(entityManager, entityId);

        if (confirmationkey != null) {
            getDaoBuilder().getConfirmationKeyDao().remove(entityManager, confirmationkey.getId());
        }
    }

    private void removeProfessorRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<ProfessorRatingEntity> professorRatings = getDaoBuilder().
                getProfessorRatingDao().getByUserId(entityManager, entityId);
        for (ProfessorRatingEntity rating : professorRatings) {
            getDaoBuilder().getProfessorRatingDao().remove(entityManager, rating.getId());
        }
    }

    private void removeCourseRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CourseRatingEntity> courseRatings = getDaoBuilder().
                getCourseRatingDao().getByUserId(entityManager, entityId);
        for (CourseRatingEntity rating : courseRatings) {
            getDaoBuilder().getCourseRatingDao().remove(entityManager, rating.getId());
        }
    }

    private void removeSubjectRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<SubjectRatingEntity> subjectRatings = getDaoBuilder().
                getSubjectRatingDao().getByUserId(entityManager, entityId);
        for (SubjectRatingEntity rating : subjectRatings) {
            getDaoBuilder().getSubjectRatingDao().remove(entityManager, rating.getId());
        }
    }

    private void removeCommentRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CommentRatingEntity> commentRatings = getDaoBuilder().
                getCommentRatingDao().getByUserId(entityManager, entityId);
        for (CommentRatingEntity rating : commentRatings) {
            getDaoBuilder().getCommentRatingDao().remove(entityManager, rating.getId());
        }
    }

    private void removeComments(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CommentEntity> commentEntitys = getDaoBuilder().
                getCommentDao().getByUserId(entityManager, entityId);
        for (CommentEntity commentEntity : commentEntitys) {
            getDaoBuilder().getCommentDao().remove(entityManager, commentEntity.getId());
        }
    }
}
