package org.xtremeware.iudex.businesslogic.service.search;

import java.util.List;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneCourseHelper;

/**
 *
 * @author josebermeo
 */
public class FuzzyCourseSearch implements Search<Long> {

    @Override
    public List<Long> search(String query, int totalHints) {
        return LuceneCourseHelper.getInstance().fussySearch(query,totalHints);
    }
    
}
