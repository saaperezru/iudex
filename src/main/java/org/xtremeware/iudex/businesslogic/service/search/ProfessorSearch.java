package org.xtremeware.iudex.businesslogic.service.search;

import java.util.List;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneProfessorHelper;

/**
 *
 * @author josebermeo
 */
public class ProfessorSearch implements Search<Long> {

    @Override
    public List<Long> search(String query) {
        return LuceneProfessorHelper.getInstance().search(query);
    }
    
}
