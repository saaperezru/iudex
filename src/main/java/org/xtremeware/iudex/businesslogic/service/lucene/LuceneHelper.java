package org.xtremeware.iudex.businesslogic.service.lucene;

import java.io.*;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.xtremeware.iudex.helper.*;
import org.xtremeware.iudex.vo.ValueObject;

public abstract class LuceneHelper<E extends ValueObject> {

    private OpenMode openMode;
    private Directory directory;

    protected LuceneHelper(File indexFile) {
        try {
            this.openMode = OpenMode.CREATE;
            deleteDir(indexFile);
            this.directory = FSDirectory.open(indexFile);
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
    }

    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));

            }
        }
        if(!dir.delete()){
            throw new RuntimeException("Failed to clean the folder");
        }
    }

    public void addElementsToAnIndex(List<E> elements) {
        IndexWriter indexWriter = startIndexWriter();
        Document document = null;
        for (E element : elements) {
            document = addFieldsToADocument(getId(element), getContent(element));
            writeADocument(indexWriter, document);
        }
        closeIndexWriter(indexWriter);
        openMode = OpenMode.APPEND;
    }

    public void addElementToAnIndex(E vo) {
        IndexWriter indexWriter = startIndexWriter();
        Document document = addFieldsToADocument(getId(vo), getContent(vo));
        writeADocument(indexWriter, document);
        closeIndexWriter(indexWriter);
        openMode = OpenMode.APPEND;
    }
    
    protected abstract String getId(E vo);
    
    protected abstract String getContent(E vo);

    private Document addFieldsToADocument(String id, String content) {
        Document document = new Document();
        document.add(new Field("id", id, Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("name", content, Field.Store.YES, Field.Index.ANALYZED));
        return document;
    }

    private IndexWriter startIndexWriter() {
        try {
            return new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36)).setOpenMode(openMode));
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

    public void updateElementoInAnIndex(E vo) {
        IndexWriter indexWriter = startIndexWriter();
        Document document = addFieldsToADocument(getId(vo), getContent(vo));
        try {
            indexWriter.updateDocument(new Term(getId(vo), "id"), document);
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
        closeIndexWriter(indexWriter);

    }

    public void deleteElementoInTheIndex(Long id) {
        IndexWriter indexWriter = startIndexWriter();
        try {
            indexWriter.deleteDocuments(new Term(id.toString(), "id"));
        } catch (Exception exception) {
            throw new ExternalServiceException(exception.getMessage(), exception);
        }
        closeIndexWriter(indexWriter);

    }
}
