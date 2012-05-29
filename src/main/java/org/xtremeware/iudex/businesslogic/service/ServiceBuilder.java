package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.businesslogic.service.createimplementations.*;
import org.xtremeware.iudex.businesslogic.service.readimplementations.*;
import org.xtremeware.iudex.businesslogic.service.removeimplementations.*;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.vo.MailingConfigVo;

/**
 * ServiceBuilder
 *
 * @author juan
 */
public class ServiceBuilder {

    private UsersService usersService;
    private ProfessorsService professorsService;
    private SubjectsService subjectsService;
    private FeedbacksService feedbacksService;
    private CommentsService commentsService;
    private CoursesService coursesService;
    private SubjectRatingsService subjectRatingsService;
    private ProfessorRatingsService professorRatingsService;
    private CommentRatingsService commentRatingService;
    private CourseRatingsService courseRatingsService;
    private MailingService mailingService;
    private PeriodsService periodsService;
    private ProgramsService programsService;
    private FeedbackTypesService feedbackTypesService;
    private LogService logService;
    private AbstractDaoBuilder daoFactory;

    public ServiceBuilder(AbstractDaoBuilder daoFactory, MailingConfigVo mailingConfig)
            throws ExternalServiceConnectionException {
        this.daoFactory = daoFactory;
        this.mailingService = new MailingService(mailingConfig);
    }

    private AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }

    public CommentRatingsService getCommentRatingService() {
        if (commentRatingService == null) {
            commentRatingService = new CommentRatingsService(getDaoFactory(),
                    new SimpleRead<CommentRatingEntity>(daoFactory.getCommentRatingDao()),
                    new SimpleRemove<CommentRatingEntity>(daoFactory.getCommentRatingDao()));
        }
        return commentRatingService;
    }

    public CommentsService createCommentsService() {
        if (commentsService == null) {
            commentsService = new CommentsService(getDaoFactory(),
                    new CommentsCreate(getDaoFactory()),
                    new SimpleRead<CommentEntity>(getDaoFactory().getCommentDao()),
                    new SimpleUpdate<CommentEntity>(getDaoFactory().getCommentDao()),
                    new SimpleRemove<CommentEntity>(getDaoFactory().getCommentDao()));
        }
        
        return commentsService;
    }

    public CourseRatingsService getCourseRatingsService() {
        if (courseRatingsService == null) {
            courseRatingsService = new CourseRatingsService(getDaoFactory(),
                    new SimpleCreate<CourseRatingEntity>(getDaoFactory().getCourseRatingDao()),
                    new SimpleRead<CourseRatingEntity>(getDaoFactory().getCourseRatingDao()),
                    new SimpleUpdate<CourseRatingEntity>(getDaoFactory().getCourseRatingDao()),
                    new SimpleRemove<CourseRatingEntity>(getDaoFactory().getCourseRatingDao()));
        }
        return courseRatingsService;
    }

    public CoursesService getCoursesService() {
        if (coursesService == null) {
            coursesService = new CoursesService(getDaoFactory(),
                    new SimpleCreate<CourseEntity>(getDaoFactory().getCourseDao()),
                    new SimpleRead<CourseEntity>(getDaoFactory().getCourseDao()),
                    new SimpleUpdate<CourseEntity>(getDaoFactory().getCourseDao()),
                    new SimpleRemove<CourseEntity>(getDaoFactory().getCourseDao()));
        }
        return coursesService;
    }

    public FeedbackTypesService getFeedbackTypesService() {
        if (feedbackTypesService == null) {
            feedbackTypesService = new FeedbackTypesService(getDaoFactory(),
                    new SimpleCreate<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()),
                    new SimpleRead<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()),
                    new SimpleUpdate<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()),
                    new SimpleRemove<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()));
        }
        return feedbackTypesService;
    }

    public FeedbacksService getFeedbacksService() {
        if (feedbacksService == null) {
            feedbacksService = new FeedbacksService(getDaoFactory(),
                    new SimpleCreate<FeedbackEntity>(getDaoFactory().getFeedbackDao()),
                    new SimpleRead<FeedbackEntity>(getDaoFactory().getFeedbackDao()),
                    new SimpleUpdate<FeedbackEntity>(getDaoFactory().getFeedbackDao()),
                    new SimpleRemove<FeedbackEntity>(getDaoFactory().getFeedbackDao()));
        }
        return feedbacksService;
    }

    public MailingService getMailingService() {
        return mailingService;
    }

    public PeriodsService getPeriodsService() {
        if (periodsService == null) {
            periodsService = new PeriodsService(getDaoFactory(),
                    new SimpleCreate<PeriodEntity>(getDaoFactory().getPeriodDao()),
                    new SimpleRead<PeriodEntity>(getDaoFactory().getPeriodDao()),
                    new SimpleUpdate<PeriodEntity>(getDaoFactory().getPeriodDao()),
                    new SimpleRemove<PeriodEntity>(getDaoFactory().getPeriodDao()));
        }
        return periodsService;
    }

    public ProfessorRatingsService getProfessorRatingsService() {
        if (professorRatingsService == null) {
            professorRatingsService = new ProfessorRatingsService(getDaoFactory(),
                    new SimpleRead<ProfessorRatingEntity>(getDaoFactory().getProfessorRatingDao()),
                    new SimpleRemove<ProfessorRatingEntity>(getDaoFactory().getProfessorRatingDao()));
        }
        return professorRatingsService;
    }

    public ProfessorsService getProfessorsService() {
        if (professorsService == null) {
            professorsService = new ProfessorsService(getDaoFactory(),
                    new SimpleCreate<ProfessorEntity>(getDaoFactory().getProfessorDao()),
                    new SimpleRead<ProfessorEntity>(getDaoFactory().getProfessorDao()),
                    new SimpleUpdate<ProfessorEntity>(getDaoFactory().getProfessorDao()),
                    new SimpleRemove<ProfessorEntity>(getDaoFactory().getProfessorDao()));
        }
        return professorsService;
    }

    public ProgramsService getProgramsService() {
        if (programsService == null) {
            programsService = new ProgramsService(getDaoFactory(),
                    new SimpleCreate<ProgramEntity>(getDaoFactory().getProgramDao()),
                    new SimpleRead<ProgramEntity>(getDaoFactory().getProgramDao()),
                    new SimpleUpdate<ProgramEntity>(getDaoFactory().getProgramDao()),
                    new SimpleRemove<ProgramEntity>(getDaoFactory().getProgramDao()));
        }
        return programsService;
    }

    public SubjectRatingsService getSubjectRatingsService() {
        if (subjectRatingsService == null) {
            subjectRatingsService = new SubjectRatingsService(getDaoFactory(),
                    new SimpleRead<SubjectRatingEntity>(getDaoFactory().getSubjectRatingDao()),
                    new SimpleRemove<SubjectRatingEntity>(getDaoFactory().getSubjectRatingDao()));
        }
        return subjectRatingsService;
    }

    public SubjectsService getSubjectsService() {
        if (subjectsService == null) {
            subjectsService = new SubjectsService(getDaoFactory(),
                    new SimpleCreate<SubjectEntity>(getDaoFactory().getSubjectDao()),
                    new SimpleRead<SubjectEntity>(getDaoFactory().getSubjectDao()),
                    new SimpleUpdate<SubjectEntity>(getDaoFactory().getSubjectDao()),
                    new SimpleRemove<SubjectEntity>(getDaoFactory().getSubjectDao()));
        }
        return subjectsService;
    }

    public UsersService getUsersService() {
        if (usersService == null) {
            usersService = new UsersService(getDaoFactory(),
                    new UsersCreate(getDaoFactory()),
                    new SimpleRead<UserEntity>(getDaoFactory().getUserDao()),
                    new UsersUpdate(getDaoFactory().getUserDao()),
                    new SimpleRemove<UserEntity>(getDaoFactory().getUserDao()));
        }
        return usersService;
    }

    public LogService getLogService() {
        if (logService == null) {
            logService = new LogService();
        }
        return logService;
    }
}
