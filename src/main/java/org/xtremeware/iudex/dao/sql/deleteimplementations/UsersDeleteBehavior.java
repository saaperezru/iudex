package org.xtremeware.iudex.dao.sql.deleteimplementations;

import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class UsersDeleteBehavior implements Delete {

    private AbstractDaoBuilder daoBuilder;
    private SimpleDeleteBehavior<UserEntity> simpleDelete;

    public UsersDeleteBehavior(AbstractDaoBuilder daoBuilder,
            SimpleDeleteBehavior simpleDelete) {
        this.daoBuilder = daoBuilder;
        this.simpleDelete = simpleDelete;
    }

    private AbstractDaoBuilder getDaoBuilder() {
        return daoBuilder;
    }
    
    @Override
    public void delete(EntityManager entityManager, Entity entity) 
            throws DataBaseException {
        deleteConfirmationKey(entityManager, entity.getId());
        deleteProfessorRatings(entityManager, entity.getId());
        deleteCourseRatings(entityManager, entity.getId());
        deleteSubjectRatings(entityManager, entity.getId());
        deleteComments(entityManager, entity.getId());
        deleteCommentRatings(entityManager, entity.getId());
        
        getSimpleDelete().delete(entityManager, entity);
    }

    private SimpleDeleteBehavior<UserEntity> getSimpleDelete() {
        return simpleDelete;
    }

    private void deleteConfirmationKey(EntityManager entityManager, Long entityId) throws DataBaseException {
        ConfirmationKeyEntity confirmationkey = getDaoBuilder().getConfirmationKeyDao().read(entityManager, entityId);

        if (confirmationkey != null) {
            getDaoBuilder().getConfirmationKeyDao().delete(entityManager, confirmationkey.getId());
        }
    }

    private void deleteProfessorRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<ProfessorRatingEntity> professorRatings = getDaoBuilder().
                getProfessorRatingDao().getByUserId(entityManager, entityId);
        for (ProfessorRatingEntity rating : professorRatings) {
            getDaoBuilder().getProfessorRatingDao().delete(entityManager, rating.getId());
        }
    }

    private void deleteCourseRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CourseRatingEntity> courseRatings = getDaoBuilder().
                getCourseRatingDao().getByUserId(entityManager, entityId);
        for (CourseRatingEntity rating : courseRatings) {
            getDaoBuilder().getCourseRatingDao().delete(entityManager, rating.getId());
        }
    }

    private void deleteSubjectRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<SubjectRatingEntity> subjectRatings = getDaoBuilder().
                getSubjectRatingDao().getByUserId(entityManager, entityId);
        for (SubjectRatingEntity rating : subjectRatings) {
            getDaoBuilder().getSubjectRatingDao().delete(entityManager, rating.getId());
        }
    }

    private void deleteCommentRatings(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CommentRatingEntity> commentRatings = getDaoBuilder().
                getCommentRatingDao().getByUserId(entityManager, entityId);
        for (CommentRatingEntity rating : commentRatings) {
            getDaoBuilder().getCommentRatingDao().delete(entityManager, rating.getId());
        }
    }

    private void deleteComments(EntityManager entityManager, Long entityId) throws DataBaseException {
        List<CommentEntity> commentEntitys = getDaoBuilder().
                getCommentDao().getByUserId(entityManager, entityId);
        for (CommentEntity commentEntity : commentEntitys) {
            getDaoBuilder().getCommentDao().delete(entityManager, commentEntity.getId());
        }
    }
}
