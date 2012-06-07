package org.xtremeware.iudex.businesslogic.service.search.lucene;

import java.io.File;
import java.util.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.helper.ExternalServiceException;
import org.xtremeware.iudex.vo.*;

/**
 *
 * @author josebermeo
 */
public class LuceneFeedbackHelper extends LuceneHelper<Long,FeedbackVo> {

    private static LuceneFeedbackHelper instance;

    private LuceneFeedbackHelper(IndexWriterConfig.OpenMode openMode, Version version, Directory directory, Analyzer analyzer) {
        super(openMode, version, directory, analyzer);
    }

    @Override
    protected Document createDocument(FeedbackVo feedbackVo) {
        Document document = new Document();
        document.add(new Field("id", feedbackVo.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("Type", feedbackVo.getFeedbackTypeId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("feedback",feedbackVo.getContent(), Field.Store.YES, Field.Index.ANALYZED));
        return document;
    }

    @Override
    protected Term createTermForDelete(Long id) {
        return new Term(id.toString(), "id");
    }

    @Override
    public List<Long> search(String query) {
        ResultCollector collector = null;
        IndexReader indexReader = null;
        try {
            //FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(query, "name"));
            Query q = new QueryParser(getVersion(), "name", getAnalyzer()).parse(query);
            indexReader = IndexReader.open(getDirectory());
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            collector = new ResultCollector(new HashSet<Integer>());
            indexSearcher.search(q, collector);
            //indexSearcher.search(fuzzyQuery, collector);
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
        List<Long> resultsIds = new ArrayList<Long>();
        for(Integer integer : collector.getBag()){
            try {
                resultsIds.add(Long.parseLong(indexReader.document(integer).get("id")));
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(), exception);
            }
        }
        return resultsIds;
    }
    
    public static synchronized LuceneFeedbackHelper getInstance() {
        while (instance == null) {
            Directory directory = null;
            try {
                directory = FSDirectory.open(new File(ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.LUCENE_FEEDBACK_INDEX_PATH)));
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(), exception);
            }
            instance = new LuceneFeedbackHelper(IndexWriterConfig.OpenMode.CREATE_OR_APPEND,
                    Version.LUCENE_36,
                    directory,
                    new StandardAnalyzer(Version.LUCENE_36, ConfigLucine.getSpanishStopWords()));
        }
        
        return instance;
    }
}
