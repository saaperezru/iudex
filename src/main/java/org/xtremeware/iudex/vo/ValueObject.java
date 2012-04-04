package org.xtremeware.iudex.vo;

import java.io.Serializable;

public abstract class ValueObject implements Comparable<ValueObject>, Serializable{
	
	@Override
	public abstract boolean equals(Object ob);
	
	@Override
	public abstract int hashCode();

	@Override
	public abstract String toString();

	@Override
	public abstract int compareTo(ValueObject vo);

}
