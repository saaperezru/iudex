package org.xtremeware.iudex.businesslogic.service.updateimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Update;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.CourseDao;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.CourseEntity;
import org.xtremeware.iudex.entity.CourseRatingEntity;
import org.xtremeware.iudex.helper.DataBaseException;

public class CourseRatingUpdate implements Update<CourseRatingEntity> {

	private AbstractDaoBuilder daoBuilder;

	public CourseRatingUpdate(AbstractDaoBuilder daoBuilder) {
		this.daoBuilder = daoBuilder;
	}

	@Override
	public CourseRatingEntity update(EntityManager entityManager, CourseRatingEntity entity) throws DataBaseException {
		CourseRatingEntity existingRating = getDao().read(entityManager, entity.getId());
		if (existingRating != null) {
			CourseDao courseDao = daoBuilder.getCourseDao();
			CourseEntity course = entity.getCourse();
			Long totalRatings = course.getRatingCount();
			Double oldAverageSum = course.getRatingAverage() * totalRatings;
			Double newAverage = ( oldAverageSum-existingRating.getValue()+entity.getValue() )/totalRatings;

			course.setRatingAverage(newAverage);
			courseDao.update(entityManager, course);
			return getDao().update(entityManager, entity);
		} else {
			return null;
		}
	}

	public CrudDao<CourseRatingEntity> getDao() {
		return daoBuilder.getCourseRatingDao();
	}
}
