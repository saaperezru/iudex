package org.xtremeware.iudex.businesslogic.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.dao.AbstractDaoFactory;
import org.xtremeware.iudex.entity.*;

import org.xtremeware.iudex.entity.ConfirmationKeyEntity;
import org.xtremeware.iudex.entity.ProgramEntity;
import org.xtremeware.iudex.entity.UserEntity;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.ConfirmationKeyVo;
import org.xtremeware.iudex.vo.UserVo;

/**
 *
 * @author josebermeo
 */
public class UsersService extends CrudService<UserVo> {

    private final int MIN_USERNAME_LENGTH;
    private final int MAX_USERNAME_LENGTH;
    private final int MAX_USER_PASSWORD_LENGTH;
    private final int MIN_USER_PASSWORD_LENGTH;

    public UsersService(AbstractDaoFactory daoFactory) throws ExternalServiceConnectionException {
        super(daoFactory);
        MIN_USERNAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MIN_USERNAME_LENGTH));
        MAX_USERNAME_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_USERNAME_LENGTH));
        MAX_USER_PASSWORD_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MAX_USER_PASSWORD_LENGTH));
        MIN_USER_PASSWORD_LENGTH = Integer.parseInt(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.MIN_USER_PASSWORD_LENGTH));

    }

    public void validateVo(EntityManager em, UserVo vo) throws InvalidVoException {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager em cannot be null");
        }
        if (vo == null) {
            throw new InvalidVoException("Null UserVo");
        }
        if (vo.getFirstName() == null || vo.getFirstName().isEmpty()) {
            throw new InvalidVoException("Null firstName in the provided UserVo");
        }
        if (vo.getLastName() == null || vo.getLastName().isEmpty()) {
            throw new InvalidVoException("Null lastName in the provided UserVo");
        }
        if (vo.getUserName() == null || vo.getUserName().isEmpty()) {
            throw new InvalidVoException("Null userName in the provided UserVo");
        }
        if (vo.getUserName().length() > MAX_USERNAME_LENGTH || vo.getUserName().length() < MIN_USERNAME_LENGTH) {
            throw new InvalidVoException("Invalid userName length in the provided UserVo");
        }
        if (vo.getPassword() == null || vo.getPassword().isEmpty()) {
            throw new InvalidVoException("Null password in the provided UserVo");
        }
        if (vo.getPassword().length() > MAX_USER_PASSWORD_LENGTH || vo.getPassword().length() < MIN_USER_PASSWORD_LENGTH) {
            throw new InvalidVoException("Invalid password length in the provided UserVo");
        }
        if (vo.getProgramsId() == null) {
            throw new InvalidVoException("Null programsId in the provided UserVo");
        }
        if (vo.getProgramsId().isEmpty()) {
            throw new InvalidVoException("programsId cannot be empity");
        }
        for (Long programId : vo.getProgramsId()) {
            if (programId == null) {
                throw new InvalidVoException("Any element in programsId cannot be null");
            }
            if (this.getDaoFactory().getProgramDao().getById(em, programId) == null) {
                throw new InvalidVoException("An element in programsId cannot be found");
            }
        }
        if (vo.getRol() == null) {
            throw new InvalidVoException("Rol cannot be null");
        }
    }

    public UserEntity voToEntity(EntityManager em, UserVo vo) throws InvalidVoException, ExternalServiceConnectionException {

        validateVo(em, vo);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(vo.getId());
        userEntity.setFirstName(SecurityHelper.sanitizeHTML(vo.getFirstName()));
        userEntity.setLastName(SecurityHelper.sanitizeHTML(vo.getLastName()));
        userEntity.setUserName(SecurityHelper.sanitizeHTML(vo.getUserName()));
        userEntity.setPassword(vo.getPassword());
        userEntity.setRol(vo.getRol());
        userEntity.setActive(vo.isActive());

        ArrayList<ProgramEntity> arrayList = new ArrayList<ProgramEntity>();
        for (Long programId : vo.getProgramsId()) {
            arrayList.add(this.getDaoFactory().getProgramDao().getById(em, programId));
        }

        userEntity.setPrograms(arrayList);
        return userEntity;

    }

    public UserVo create(EntityManager em, UserVo user) throws InvalidVoException, ExternalServiceConnectionException {
        validateVo(em, user);
        UserEntity userEntity = voToEntity(em, user);
        //It is not possible to create users that are already active
        userEntity.setActive(false);
        //Create confirmation key
        ConfirmationKeyEntity confirmationKeyEntity = new ConfirmationKeyEntity();
        confirmationKeyEntity.setConfirmationKey(SecurityHelper.generateConfirmationKey());
        //Set expiration date for one day after creation
        Calendar expiration = new GregorianCalendar();
        expiration.add(Calendar.DAY_OF_MONTH, 1);
        confirmationKeyEntity.setExpirationDate(expiration.getTime());

		//Associate confirmation key with user
		userEntity.setConfirmationKey(confirmationKeyEntity);
                confirmationKeyEntity.setUser(userEntity);

                userEntity = getDaoFactory().getUserDao().persist(em, userEntity);
                confirmationKeyEntity.setId(userEntity.getId());
                
		//persist confirmation key
		confirmationKeyEntity = getDaoFactory().getConfirmationKeyDao().persist(em, confirmationKeyEntity);
                
		return userEntity.toVo();
	}

    public UserVo authenticate(EntityManager em, String userName, String password) throws InactiveUserException {
        UserEntity user = getDaoFactory().getUserDao().getByUsernameAndPassword(em, userName, password);
        if (user == null) {
            return null;
        } else {
            if (!user.isActive()) {
                throw new InactiveUserException("The user" + user.getUserName() + " is still inactive.");
            } else {
                return user.toVo();
            }
        }
    }

    public void activateAccount(EntityManager em, long userId, String confirmationKey) {
        UserEntity user = getDaoFactory().getUserDao().getById(em, userId);
        if (user.isActive() || user.getConfirmationKey() == null) {
            //There is nothing to do, user is already active
        } else {
            if (user.getConfirmationKey().getConfirmationKey().equals(confirmationKey)) {
                user.setActive(true);
            }
        }

    }

    public UserVo getById(EntityManager em, Long id) {
        return this.getDaoFactory().getUserDao().getById(em, id).toVo();
    }

    public void update(EntityManager em, UserVo user) throws InvalidVoException, ExternalServiceConnectionException {
        validateVo(em, user);
        this.getDaoFactory().getUserDao().merge(em, this.voToEntity(em, user));
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
    public void remove(EntityManager em, Long id) {

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
        
        public ConfirmationKeyVo getConfirmationKeyByUserId(EntityManager em, long id) {
            return getDaoFactory().getUserDao().getById(em, id).getConfirmationKey().toVo();
        }
}
