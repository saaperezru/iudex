package org.xtremeware.iudex.helper;

import java.io.*;
import java.util.*;
import javax.persistence.EntityManager;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;


/**
 *
 * @author josebermeo
 */
public final class ConfigLucine {

    private static Set<String> stopWords;

    public static void indexDataBase(EntityManager entityManager) {
        createProfessorIndex(entityManager);
        createSubjectIndex(entityManager);
    }

    private static void createProfessorIndex(EntityManager entityManager) {
        try {
            File file = new File(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.LUCENE_PROFESSOR_INDEX_PATH));
            Directory directory = FSDirectory.open(file);
            IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(
                    Version.LUCENE_36,
                    new StandardAnalyzer(Version.LUCENE_36, getSpanishStopWords())).setOpenMode(OpenMode.CREATE));

            List<Long> professorids = entityManager.createQuery("SELECT p.id FROM Professor p", Long.class).getResultList();
            for (Long id : professorids) {
                Object[] singleResult = entityManager.createQuery("SELECT p.firstName, p.lastName FROM Professor p WHERE p.id = :professorId", Object[].class).setParameter("professorId", id).getSingleResult();
                Document document = new Document();
                document.add(new Field("id", id.toString(), Field.Store.YES, Field.Index.ANALYZED));
                document.add(new Field("name",
                        singleResult[0].toString() + " " + singleResult[1].toString(), Field.Store.YES, Field.Index.ANALYZED));
                indexWriter.addDocument(document);
            }
            indexWriter.close();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception.getCause());
        }
    }

    private static void createSubjectIndex(EntityManager entityManager) {
        try {
            File file = new File(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.LUCENE_SUBJECT_INDEX_PATH));
            Directory directory = FSDirectory.open(file);
            IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(
                    Version.LUCENE_36,
                    new StandardAnalyzer(Version.LUCENE_36, getSpanishStopWords())).setOpenMode(OpenMode.CREATE));

            List<Long> subjectsids = entityManager.createQuery("SELECT p.id FROM Subject p", Long.class).getResultList();
            for (Long id : subjectsids) {
                String singleResult = entityManager.createQuery("SELECT p.name FROM Subject p WHERE p.id = :subjectId", String.class).setParameter("subjectId", id).getSingleResult();
                Document document = new Document();
                document.add(new Field("id", id.toString(), Field.Store.YES, Field.Index.ANALYZED));
                document.add(new Field("name",
                        singleResult, Field.Store.YES, Field.Index.ANALYZED));
                indexWriter.addDocument(document);
            }
            indexWriter.close();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage(), exception.getCause());
        }
    }

    public static Set<String> getSpanishStopWords() {
        if (stopWords == null) {
            try {
                
                FileInputStream fstream = new FileInputStream(
                        ConfigLucine.class.getResource("/org/xtremeware/iudex/iudex.properties").getFile());
                
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                
                while ((strLine = br.readLine()) != null) {
                    stopWords.addAll(Arrays.asList(strLine.split(" ")));
                }
                in.close();
                System.out.println(stopWords.size());
            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage(), exception.getCause());
            }
        }
        return stopWords;
    }
}