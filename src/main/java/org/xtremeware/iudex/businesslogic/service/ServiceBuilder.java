package org.xtremeware.iudex.businesslogic.service;

import java.util.*;
import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.createimplementations.*;
import org.xtremeware.iudex.businesslogic.service.deleteimplementations.*;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneProfessorHelper;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneSubjectHelper;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.search.ProfessorSearch;
import org.xtremeware.iudex.businesslogic.service.search.SubjectSearch;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.vo.*;



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

    public ServiceBuilder(AbstractDaoBuilder daoFactory, MailingConfigVo mailingConfig, EntityManager entityManager) {
        this.daoFactory = daoFactory;
        this.mailingService = new MailingService(mailingConfig);
        
//        System.out.println("[DEBUG] STARTED PROFESSOR INDEING");
//        List<Long> professorids = entityManager.createQuery("SELECT p.id FROM Professor p", Long.class).getResultList();
//        System.out.println("[DEBUG] FOUNDS IDS");
//        ProfessorEntity professorEntity = null;
//        
//        for (Long id : professorids) {
//            professorEntity = entityManager.createQuery("SELECT p FROM Professor p WHERE p.id = :professorId", ProfessorEntity.class).setParameter("professorId", id).getSingleResult();
//            LuceneProfessorHelper.getInstance().addElementToAnIndex(professorEntity.toVo());
//            System.out.println("[DEBUG] ADDED NEW PROFESSOR");
//        }
//        
//        System.out.println("[DEBUG] FINISHED PROFESSOR INDEING");
//        System.out.println("[DEBUG] STARTED SUBJECT INDEING");
//        
//        List<SubjectEntity> subjectEntitys = entityManager.createQuery("SELECT p FROM Subject p", SubjectEntity.class).setMaxResults(10).getResultList();
//        List<SubjectVo> subjectsVo = new ArrayList<SubjectVo>();
//        for (SubjectEntity subjectEntity : subjectEntitys) {
//            subjectsVo.add(subjectEntity.toVo());
//        }
//        
//        LuceneSubjectHelper.getInstance().addElementsToAnIndex(subjectsVo);
//        System.out.println("[DEBUG] FINISHED SUBJECT INDEING");
    }

    private AbstractDaoBuilder getDaoFactory() {
        return daoFactory;
    }

    public CommentRatingsService getCommentRatingService() {
        if (commentRatingService == null) {
            commentRatingService = new CommentRatingsService(getDaoFactory(),
                    new SimpleRead<CommentRatingEntity>(daoFactory.getCommentRatingDao()),
                    new SimpleDelete<CommentRatingEntity>(daoFactory.getCommentRatingDao()));
        }
        return commentRatingService;
    }

    public CommentsService getCommentsService() {
        if (commentsService == null) {
            commentsService = new CommentsService(getDaoFactory(),
                    new CommentsCreate(getDaoFactory()),
                    new SimpleRead<CommentEntity>(getDaoFactory().getCommentDao()),
                    new SimpleUpdate<CommentEntity>(getDaoFactory().getCommentDao()),
                    new SimpleDelete<CommentEntity>(getDaoFactory().getCommentDao()));
        }

        return commentsService;
    }

    public CourseRatingsService getCourseRatingsService() {
        if (courseRatingsService == null) {
            courseRatingsService = new CourseRatingsService(getDaoFactory(),
                    new CourseRate(getDaoFactory()),
                    new SimpleRead<CourseRatingEntity>(getDaoFactory().getCourseRatingDao()),
                    new SimpleUpdate<CourseRatingEntity>(getDaoFactory().getCourseRatingDao()),
                    new SimpleDelete<CourseRatingEntity>(getDaoFactory().getCourseRatingDao()));
        }
        return courseRatingsService;
    }

    public CoursesService getCoursesService() {
        if (coursesService == null) {
            coursesService = new CoursesService(getDaoFactory(),
                    new SimpleCreate<CourseEntity>(getDaoFactory().getCourseDao()),
                    new SimpleRead<CourseEntity>(getDaoFactory().getCourseDao()),
                    new SimpleUpdate<CourseEntity>(getDaoFactory().getCourseDao()),
                    new SimpleDelete<CourseEntity>(getDaoFactory().getCourseDao()));
        }
        return coursesService;
    }

    public FeedbackTypesService getFeedbackTypesService() {
        if (feedbackTypesService == null) {
            feedbackTypesService = new FeedbackTypesService(getDaoFactory(),
                    new SimpleCreate<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()),
                    new SimpleRead<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()),
                    new SimpleUpdate<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()),
                    new SimpleDelete<FeedbackTypeEntity>(getDaoFactory().getFeedbackTypeDao()));
        }
        return feedbackTypesService;
    }

    public FeedbacksService getFeedbacksService() {
        if (feedbacksService == null) {
            feedbacksService = new FeedbacksService(getDaoFactory(),
                    new SimpleCreate<FeedbackEntity>(getDaoFactory().getFeedbackDao()),
                    new SimpleRead<FeedbackEntity>(getDaoFactory().getFeedbackDao()),
                    new SimpleUpdate<FeedbackEntity>(getDaoFactory().getFeedbackDao()),
                    new SimpleDelete<FeedbackEntity>(getDaoFactory().getFeedbackDao()));
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
                    new SimpleDelete<PeriodEntity>(getDaoFactory().getPeriodDao()));
        }
        return periodsService;
    }

    public ProfessorRatingsService getProfessorRatingsService() {
        if (professorRatingsService == null) {
            professorRatingsService = new ProfessorRatingsService(getDaoFactory(),
                    new SimpleRead<ProfessorRatingEntity>(getDaoFactory().getProfessorRatingDao()),
                    new SimpleDelete<ProfessorRatingEntity>(getDaoFactory().getProfessorRatingDao()));
        }
        return professorRatingsService;
    }

    public ProfessorsService getProfessorsService() {
        if (professorsService == null) {
            professorsService = new ProfessorsService(getDaoFactory(),
                    new ProfessorCreation(getDaoFactory().getProfessorDao()),
                    new SimpleRead<ProfessorEntity>(getDaoFactory().getProfessorDao()),
                    new ProfessorUpdate(getDaoFactory().getProfessorDao()),
                    new ProfessorDelete(getDaoFactory().getProfessorDao()),
                    new ProfessorSearch());
        }
        return professorsService;
    }

    public ProgramsService getProgramsService() {
        if (programsService == null) {
            programsService = new ProgramsService(getDaoFactory(),
                    new SimpleCreate<ProgramEntity>(getDaoFactory().getProgramDao()),
                    new SimpleRead<ProgramEntity>(getDaoFactory().getProgramDao()),
                    new SimpleUpdate<ProgramEntity>(getDaoFactory().getProgramDao()),
                    new SimpleDelete<ProgramEntity>(getDaoFactory().getProgramDao()));
        }
        return programsService;
    }

    public SubjectRatingsService getSubjectRatingsService() {
        if (subjectRatingsService == null) {
            subjectRatingsService = new SubjectRatingsService(getDaoFactory(),
                    new SimpleRead<SubjectRatingEntity>(getDaoFactory().getSubjectRatingDao()),
                    new SimpleDelete<SubjectRatingEntity>(getDaoFactory().getSubjectRatingDao()));
        }
        return subjectRatingsService;
    }

    public SubjectsService getSubjectsService() {
        if (subjectsService == null) {
            subjectsService = new SubjectsService(getDaoFactory(),
                    new SubjectCreation(getDaoFactory().getSubjectDao()),
                    new SimpleRead<SubjectEntity>(getDaoFactory().getSubjectDao()),
                    new SubjectUpdate(getDaoFactory().getSubjectDao()),
                    new SubjectDelete(getDaoFactory().getSubjectDao()),
                    new SubjectSearch());
        }
        return subjectsService;
    }

    public UsersService getUsersService() {
        if (usersService == null) {
            usersService = new UsersService(getDaoFactory(),
                    new UsersCreate(getDaoFactory()),
                    new SimpleRead<UserEntity>(getDaoFactory().getUserDao()),
                    new UsersUpdate(getDaoFactory().getUserDao()),
                    new SimpleDelete<UserEntity>(getDaoFactory().getUserDao()));
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
