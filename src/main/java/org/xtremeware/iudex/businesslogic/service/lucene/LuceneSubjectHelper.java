package org.xtremeware.iudex.businesslogic.service.lucene;

import java.io.File;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.vo.SubjectVo;

/**
 *
 * @author josebermeo
 */
public final class LuceneSubjectHelper extends LuceneHelper<SubjectVo> {

    private static LuceneSubjectHelper instance;
    
    private LuceneSubjectHelper() {
        super(new File(
                ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.LUCENE_SUBJECT_INDEX_PATH)));
    }
    
    @Override
    protected String getId(SubjectVo subjectVo) {
        return subjectVo.getId().toString();
    }

    @Override
    protected String getContent(SubjectVo subjectVo) {
        return subjectVo.getName();
    }
    
    public static synchronized LuceneSubjectHelper getInstance() {
        while (instance == null) {
            instance = new LuceneSubjectHelper();
        }
        return instance;
    }
}
