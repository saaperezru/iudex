package org.xtremeware.iudex.businesslogic.service.search.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public final class LuceneSubjectHelper extends LuceneHelper<Long, SubjectVo> {

    private static LuceneSubjectHelper instance;

    private LuceneSubjectHelper(OpenMode openMode, Version version, Directory directory, Analyzer analyzer) {
        super(openMode, version, directory, analyzer);
    }

    @Override
    protected Document createDocument(SubjectVo subjectVo, EntityManager entityManager) {
        Document document = new Document();
        document.add(new Field("id", subjectVo.getId().toString(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("name",
                subjectVo.getName(), Field.Store.YES, Field.Index.ANALYZED));
        return document;
    }

    @Override
    protected Term createTermForDelete(Long id) {
        return new Term(id.toString(), "id");
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
                resultsIds.add(Long.parseLong(indexReader.document(scoreDoc.doc).get("id")));
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(), exception);
            }
        }
        return resultsIds;
    }

    public static synchronized LuceneSubjectHelper getInstance() {
        while (instance == null) {
            Directory directory = null;
            try {
                directory = FSDirectory.open(new File(ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.LUCENE_SUBJECT_INDEX_PATH)));
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(), exception);
            }
            instance = new LuceneSubjectHelper(IndexWriterConfig.OpenMode.CREATE_OR_APPEND,
                    Version.LUCENE_36,
                    directory,
                    new StandardAnalyzer(Version.LUCENE_36, ConfigLucine.getSpanishStopWords()));
        }
        return instance;
    }
}
