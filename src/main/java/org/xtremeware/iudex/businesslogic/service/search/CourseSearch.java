package org.xtremeware.iudex.businesslogic.service.search;

import java.util.List;
import org.xtremeware.iudex.businesslogic.service.search.lucene.LuceneCourseHelper;

/**
 *
 * @author josebermeo
 */
public class CourseSearch implements Search<Long> {

    @Override
    public List<Long> search(String query) {
        return LuceneCourseHelper.getInstance().search(query);
    }
    
}
    

