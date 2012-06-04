package org.xtremeware.iudex.businesslogic.service;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;

public class LuceneHelper {

	private Directory professoreAndSubjectIndex;
	private static LuceneHelper instance;

	private LuceneHelper() throws IOException {
		File indexFile = new File(ConfigurationVariablesHelper.getVariable(ConfigurationVariablesHelper.LUCENE_PATH));
		deleteDir(indexFile);
		this.professoreAndSubjectIndex = FSDirectory.open(indexFile,
					new NativeFSLockFactory());
	}

	public static synchronized LuceneHelper getInstance() throws IOException {
		while (instance == null) {
			instance = new LuceneHelper();
		}
		return instance;
	}
	
	private void deleteDir(File dir) {
    if (dir.isDirectory()) {
        String[] children = dir.list();
        for (int i=0; i<children.length; i++) {
            deleteDir(new File(dir, children[i]));
            
        }
    }
}

	public Directory getProfessoreAndSubjectIndex() {
		return professoreAndSubjectIndex;
	}
	
}
