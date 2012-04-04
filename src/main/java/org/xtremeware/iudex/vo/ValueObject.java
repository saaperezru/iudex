package org.xtremeware.iudex.vo;

import java.io.Serializable;

public abstract class ValueObject implements Serializable{
	
	@Override
	public abstract boolean equals(Object ob);
	
	@Override
	public abstract int hashCode();

	@Override
	public abstract String toString();

}
