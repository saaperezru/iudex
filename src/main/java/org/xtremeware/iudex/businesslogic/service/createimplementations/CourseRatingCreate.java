package org.xtremeware.iudex.businesslogic.service.createimplementations;

import javax.persistence.EntityManager;
import org.xtremeware.iudex.businesslogic.DuplicityException;
import org.xtremeware.iudex.businesslogic.service.crudinterfaces.Create;
import org.xtremeware.iudex.dao.AbstractDaoBuilder;
import org.xtremeware.iudex.dao.CourseDao;
import org.xtremeware.iudex.dao.CrudDao;
import org.xtremeware.iudex.entity.*;
import org.xtremeware.iudex.helper.DataBaseException;

/**
 *
 * @author josebermeo
 */
public class CourseRatingCreate implements Create<CourseRatingEntity> {

	private AbstractDaoBuilder daoBuilder;

	public CourseRatingCreate(AbstractDaoBuilder daoBuilder) {
		this.daoBuilder = daoBuilder;
	}

	@Override
	public CourseRatingEntity create(EntityManager entityManager, CourseRatingEntity entity) throws DataBaseException {

		CourseDao courseDao = daoBuilder.getCourseDao();
		CourseEntity course = entity.getCourse();
		Long totalRatings = course.getRatingCount();
		Double oldAverageSum = course.getRatingAverage() * totalRatings;
		totalRatings = totalRatings + 1;
		Double newAverage = (oldAverageSum + entity.getValue()) / totalRatings;

		course.setRatingAverage(newAverage);
		course.setRatingCount(totalRatings);
		getDao().create(entityManager, entity);
		courseDao.update(entityManager, course);
		return entity;
	}

	public CrudDao<CourseRatingEntity> getDao() {
		return daoBuilder.getCourseRatingDao();
	}
}
