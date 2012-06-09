package org.xtremeware.iudex.businesslogic.service.search;

/**
 *
 * @author josebermeo
 */
public final class SearchBehaviorFactory {

    private SearchBehaviorFactory() {
    }

    public static Search getCourseSearch() {
        return new CourseSearch();
    }

    public static Search getFuzzyCourseSearch() {
        return new FuzzyCourseSearch();
    }
}
