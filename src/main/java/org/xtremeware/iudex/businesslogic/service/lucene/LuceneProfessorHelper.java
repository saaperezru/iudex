package org.xtremeware.iudex.businesslogic.service.lucene;

import java.io.File;
import org.xtremeware.iudex.helper.ConfigurationVariablesHelper;
import org.xtremeware.iudex.vo.ProfessorVo;

/**
 *
 * @author josebermeo
 */
public final class LuceneProfessorHelper extends LuceneHelper<ProfessorVo> {
    
    private static LuceneProfessorHelper instance;

    public LuceneProfessorHelper() {
        super(new File(
                ConfigurationVariablesHelper.getVariable(
                ConfigurationVariablesHelper.LUCENE_PROFESSOR_INDEX_PATH)));
    }
    
    @Override
    protected String getId(ProfessorVo professorVo) {
        return professorVo.getId().toString();
    }

    @Override
    protected String getContent(ProfessorVo professorVo) {
        return professorVo.getFirstName()+" "+professorVo.getLastName();
    }
    
    public static synchronized LuceneProfessorHelper getInstance() {
        while (instance == null) {
            instance = new LuceneProfessorHelper();
        }
        return instance;
    }
}
