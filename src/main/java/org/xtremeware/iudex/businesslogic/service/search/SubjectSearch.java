package org.xtremeware.iudex.businesslogic.service.search;

import java.util.List;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneSubjectHelper;

/**
 *
 * @author josebermeo
 */
public class SubjectSearch implements Search<Long> {

    @Override
    public List<Long> search(String query) {
        return LuceneSubjectHelper.getInstance().search(query);
    }
    
}
