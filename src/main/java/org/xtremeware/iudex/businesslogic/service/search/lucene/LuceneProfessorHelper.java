package org.xtremeware.iudex.businesslogic.service.search.lucene;

import java.io.File;
import java.util.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ProfessorVo;

/**
 *
 * @author josebermeo
 */
public final class LuceneProfessorHelper extends LuceneHelper<Long,ProfessorVo> {

    private static LuceneProfessorHelper instance;

    private LuceneProfessorHelper(OpenMode openMode, Version version, Directory directory, Analyzer analyzer) {
        super(openMode, version, directory, analyzer);
    }

    @Override
    protected Document createDocument(ProfessorVo professorVo) {
        Document document = new Document();
        document.add(new Field("id", professorVo.getId().toString(), Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("name",
                professorVo.getFirstName() + " " + professorVo.getLastName(), Field.Store.YES, Field.Index.ANALYZED));
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
            Query q = new QueryParser(getVersion(), "name", getAnalyzer()).parse(query);
            indexReader = IndexReader.open(getDirectory());
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            collector = new ResultCollector(new HashSet<Integer>());
            indexSearcher.search(q, collector);
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

    public static synchronized LuceneProfessorHelper getInstance() {
        while (instance == null) {
            FSDirectory directory = null;
            try {
                File file = new File(ConfigurationVariablesHelper.getVariable(
                                    ConfigurationVariablesHelper.LUCENE_PROFESSOR_INDEX_PATH));
                deleteDir(file);
                directory = FSDirectory.open(file);
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(), exception);
            }
            instance = new LuceneProfessorHelper(OpenMode.CREATE,
                    Version.LUCENE_36,
                    directory,
                    new StandardAnalyzer(Version.LUCENE_36));
        }
        return instance;
    }
}
