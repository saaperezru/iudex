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
import org.xtremeware.iudex.entity.CourseEntity;

/**
 *
 * @author josebermeo
 */
public final class ConfigLucine {

    private ConfigLucine() {
    }

    private static Set<String> stopWords;

    public static void indexDataBase(EntityManager entityManager) {
        createCourseIndex(entityManager);
    }

    private static void createCourseIndex(EntityManager entityManager) {
        try {
            File file = new File(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.LUCENE_COURSE_INDEX_PATH));
            Directory directory = FSDirectory.open(file);
            IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(
                    Version.LUCENE_36,
                    new StandardAnalyzer(Version.LUCENE_36, getSpanishStopWords())).setOpenMode(OpenMode.CREATE));

            List<Long> courseIds = entityManager.createQuery("SELECT p.id FROM Course p", Long.class).getResultList();
            for (Long id : courseIds) {
                CourseEntity courseEntity = entityManager.createQuery("SELECT p FROM Course p WHERE p.id = :courseId", CourseEntity.class).setParameter("courseId", id).getSingleResult();
                Document document = new Document();
                document.add(new Field("id", courseEntity.getId().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                 
                String name = courseEntity.getProfessor().getFirstName() + " " + courseEntity.getProfessor().getLastName();
                name = name + " " + courseEntity.getSubject().getName();

                document.add(new Field("name",
                        name, Field.Store.YES, Field.Index.ANALYZED));
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
                String file = ConfigurationVariablesHelper.getVariable(
                        ConfigurationVariablesHelper.SPANISH_STOP_WORDS_PATH);
                FileInputStream fstream = new FileInputStream(file);

                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                stopWords = new HashSet<String>();
                while ((strLine = br.readLine()) != null) {
                    stopWords.addAll(Arrays.asList(strLine.split(" ")));
                }
                in.close();
            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage(), exception.getCause());
            }
        }
        return stopWords;
    }
}