package org.xtremeware.iudex.businesslogic.service.search.lucene;

import java.util.List;
import javax.persistence.EntityManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.IdentifiableValueObject;

public abstract class LuceneHelper<E,F extends IdentifiableValueObject<E>> {

    private OpenMode openMode;
    private Version version;
    private Directory directory;
    private Analyzer analyzer;

    protected LuceneHelper(OpenMode openMode, Version version, Directory directory, Analyzer analyzer) {
        this.openMode = openMode;
        this.version = version;
        this.directory = directory;
        this.analyzer = analyzer;
    }

    public void addElementsToAnIndex(List<F> elements, EntityManager entityManager) throws DataBaseException{
        IndexWriter indexWriter = startIndexWriter();
        Document document = null;
        for (F element : elements) {
            document = createDocument(element,entityManager);
            writeADocument(indexWriter, document);
        }
        closeIndexWriter(indexWriter);
        openMode = OpenMode.APPEND;
    }
    
    protected abstract Document createDocument(F element, EntityManager entityManager) throws DataBaseException;

    public void addElementToAnIndex(F vo, EntityManager entityManager) throws DataBaseException{
        IndexWriter indexWriter = startIndexWriter();
        Document document = createDocument(vo,entityManager);
        writeADocument(indexWriter, document);
        closeIndexWriter(indexWriter);
        openMode = OpenMode.APPEND;
    }

    private IndexWriter startIndexWriter() {
        try {
            return new IndexWriter(directory, new IndexWriterConfig(version, analyzer).setOpenMode(openMode));
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
    }

    private void closeIndexWriter(IndexWriter indexWriter) {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (Exception exception) {
                throw new ExternalServiceException(exception.getMessage(), exception);
            }
        }
    }

    private void writeADocument(IndexWriter indexWriter, Document document) {
        try {
            indexWriter.addDocument(document);
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
    }

    public void updateElementoInAnIndex(F vo, EntityManager entityManager) throws DataBaseException{
        IndexWriter indexWriter = startIndexWriter();
        Document document = createDocument(vo, entityManager);
        try {
            indexWriter.updateDocument(createTermForDelete(vo.getId()), document);
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
        closeIndexWriter(indexWriter);

    }
    
    protected abstract Term createTermForDelete(E id);

    public void deleteElementoInTheIndex(E id) {
        IndexWriter indexWriter = startIndexWriter();
        try {
            indexWriter.deleteDocuments(createTermForDelete(id));
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
        closeIndexWriter(indexWriter);

    }
    
    public abstract List<E> search(String query);

    protected Analyzer getAnalyzer() {
        return analyzer;
    }

    protected Directory getDirectory() {
        return directory;
    }

    protected OpenMode getOpenMode() {
        return openMode;
    }

    protected Version getVersion() {
        return version;
    }
}