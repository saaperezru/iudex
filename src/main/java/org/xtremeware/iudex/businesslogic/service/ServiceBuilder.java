package org.xtremeware.iudex.businesslogic.service;

import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.xtremeware.iudex.businesslogic.service.createimplementations.*;
import org.xtremeware.iudex.businesslogic.service.deleteimplementations.SimpleDelete;
import org.xtremeware.iudex.businesslogic.service.readimplementations.SimpleRead;
import org.xtremeware.iudex.businesslogic.service.updateimplementations.*;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.vo.MailingConfigVo;
import org.apache.lucene.document.*;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;

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

	public ServiceBuilder(AbstractDaoBuilder daoFactory, MailingConfigVo mailingConfig, EntityManager entityManager) throws CorruptIndexException, IOException, ParseException {
		this.daoFactory = daoFactory;
		this.mailingService = new MailingService(mailingConfig);

		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		IndexWriter writer = new IndexWriter(LuceneHelper.getInstance().getProfessoreAndSubjectIndex(), config);
		List<ProfessorEntity> professorEntitys = entityManager.createQuery("SELECT p FROM Professor p", ProfessorEntity.class).getResultList();
		for (ProfessorEntity professorEntity : professorEntitys) {
			Document doc = new Document();
			doc.add(new Field("id", professorEntity.getId().toString(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("name", professorEntity.getFirstName() + " " + professorEntity.getLastName(), Field.Store.YES, Field.Index.ANALYZED));

			writer.addDocument(doc);
		}

		List<SubjectEntity> subjectEntitys = entityManager.createQuery("SELECT p FROM Subject p", SubjectEntity.class).getResultList();
		for (SubjectEntity subjectEntity : subjectEntitys) {
			Document doc = new Document();
			doc.add(new Field("id", subjectEntity.getId().toString(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("name", subjectEntity.getName(), Field.Store.YES, Field.Index.ANALYZED));

			writer.addDocument(doc);
		}
		Document doc = new Document();
		//doc.add(new Field("id", "200000000", Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field("name", "LUCENE", Field.Store.YES, Field.Index.ANALYZED));
		writer.addDocument(doc);
		writer.close();
		Query q = new QueryParser(Version.LUCENE_35, "name", analyzer).parse("MaRiO");

		int hitsPerPage = 10;
		IndexReader reader = IndexReader.open(LuceneHelper.getInstance().getProfessoreAndSubjectIndex());
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("name"));
		}

		// searcher can only be closed when there
		// is no need to access the documents any more. 
		searcher.close();

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
					new SimpleCreate<ProfessorEntity>(getDaoFactory().getProfessorDao()),
					new SimpleRead<ProfessorEntity>(getDaoFactory().getProfessorDao()),
					new SimpleUpdate<ProfessorEntity>(getDaoFactory().getProfessorDao()),
					new SimpleDelete<ProfessorEntity>(getDaoFactory().getProfessorDao()));
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
					new SimpleCreate<SubjectEntity>(getDaoFactory().getSubjectDao()),
					new SimpleRead<SubjectEntity>(getDaoFactory().getSubjectDao()),
					new SimpleUpdate<SubjectEntity>(getDaoFactory().getSubjectDao()),
					new SimpleDelete<SubjectEntity>(getDaoFactory().getSubjectDao()));
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
