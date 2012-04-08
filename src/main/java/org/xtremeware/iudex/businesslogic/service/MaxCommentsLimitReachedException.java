
package org.xtremeware.iudex.businesslogic.service;

import org.xtremeware.iudex.vo.CourseVo;
import org.xtremeware.iudex.vo.UserVo;

public class MaxCommentsLimitReachedException extends Exception {

	public MaxCommentsLimitReachedException(UserVo user,CourseVo course){
		super("User " + user.toString() + "reached the maximum number of allowed comments per day, when making a comment in course " + course.toString());
	}

	MaxCommentsLimitReachedException(String message) {
		super(message);
	}

}
