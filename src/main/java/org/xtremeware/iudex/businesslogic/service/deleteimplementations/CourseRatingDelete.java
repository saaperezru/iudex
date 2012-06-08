package org.xtremeware.iudex.businesslogic.service.deleteimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Delete;
import org.xtremeware.iudex.dao.*;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

public class CourseRatingDelete implements Delete<CourseRatingEntity> {

	private AbstractDaoBuilder daoBuilder;

	public CourseRatingDelete(AbstractDaoBuilder daoBuilder) {
		this.daoBuilder = daoBuilder;
	}

	@Override
	public void delete(EntityManager entityManager, Long entityId)
			throws DataBaseException {
		CourseDao courseDao = daoBuilder.getCourseDao();
		CourseRatingEntity deletingRating = getDao().read(entityManager, entityId);
		CourseEntity course = deletingRating.getCourse();


		Long totalRatings = course.getRatingCount();
		Double oldAverageSum = course.getRatingAverage() * totalRatings;

		totalRatings = totalRatings - 1;
		Double newAverage;
		if (totalRatings == 0) {
			newAverage = 0.0;
		} else {
			newAverage = (oldAverageSum - deletingRating.getValue()) / totalRatings;
		}
		course.setRatingAverage(newAverage);
		course.setRatingCount(totalRatings);

		courseDao.update(entityManager, course);
		getDao().delete(entityManager, entityId);
	}

	public CrudDao<CourseRatingEntity> getDao() {
		return daoBuilder.getCourseRatingDao();
	}
}
