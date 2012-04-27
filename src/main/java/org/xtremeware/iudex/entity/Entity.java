package org.xtremeware.iudex.entity;

import org.xtremeware.iudex.vo.ValueObject;

public interface Entity<E extends ValueObject> {

	public E toVo();
	
}
