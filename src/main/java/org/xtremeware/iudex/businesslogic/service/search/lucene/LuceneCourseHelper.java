package org.xtremeware.iudex.businesslogic.service.search.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.entity.ProfessorEntity;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.CourseVo;

/**
 *
 * @author josebermeo
 */
public final class LuceneCourseHelper extends LuceneHelper<Long, CourseVo> {

    private static LuceneCourseHelper instance;
    private AbstractDaoBuilder abstractDaoBuilder;
    private static final String FUZZY_SEARCH = "~";

    private LuceneCourseHelper(AbstractDaoBuilder abstractDaoBuilder,
            OpenMode openMode, Version version, Directory directory,
            Analyzer analyzer) {
        super(openMode, version, directory, analyzer);
        this.abstractDaoBuilder = abstractDaoBuilder;
    }

    @Override
    protected Document createDocument(CourseVo courseVo,
            EntityManager entityManager) throws DataBaseException {
        Document document = new Document();
        document.add(new Field("id", courseVo.getId().toString(),
                Field.Store.YES, Field.Index.NOT_ANALYZED));
        ProfessorEntity professorEntity;
        String name;

        professorEntity = abstractDaoBuilder.getProfessorDao().read(
                entityManager, courseVo.getProfessorId());
        name = professorEntity.getFirstName() + " " + professorEntity.
                getLastName();
        name = name + " " + abstractDaoBuilder.getSubjectDao().read(
                entityManager, courseVo.getSubjectId()).getName();

        document.add(new Field("name",
                name, Field.Store.YES, Field.Index.ANALYZED));
        return document;

    }

    @Override
    protected Term createTermForDelete(Long id) {
        return new Term(id.toString(), "id");
    }

    public List<Long> fussySearch(String query, int totalHints) {
        return search(query + FUZZY_SEARCH, totalHints);
    }

    @Override
    public List<Long> search(String query, int totalHints) {
        TopScoreDocCollector collector = null;
        IndexReader indexReader = null;
        try {
            QueryParser q = new QueryParser(getVersion(), "name", getAnalyzer());
            indexReader = IndexReader.open(getDirectory());
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            collector = TopScoreDocCollector.create(totalHints, true);
            indexSearcher.search(q.parse(query), collector);
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
        List<Long> resultsIds = new ArrayList<Long>();
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        for (ScoreDoc scoreDoc : hits) {
            try {
                resultsIds.add(Long.parseLong(indexReader.document(scoreDoc.doc).
                        get("id")));
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(),
                        exception);
            }
        }
        return resultsIds;
    }

    public static synchronized LuceneCourseHelper getInstance() {
        while (instance == null) {
            Directory directory = null;
            try {
                directory = FSDirectory.open(new File(LuceneCourseHelper.class.
                        getResource(ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.LUCENE_COURSE_INDEX_PATH)).
                        getFile()));
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(),
                        exception);
            }
            instance = new LuceneCourseHelper(
                    Config.getInstance().getDaoFactory(),
                    IndexWriterConfig.OpenMode.CREATE_OR_APPEND,
                    Version.LUCENE_36,
                    directory,
                    new StandardAnalyzer(Version.LUCENE_36, ConfigLucene.
                    getSpanishStopWords()));
        }
        return instance;
    }
}
