
package org.xtremeware.iudex.vo;

public class RatingSummaryVo extends ValueObject{

	private int positive;
	private int negative;

	@Override
	public boolean equals(Object ob) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int compareTo(ValueObject vo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public int getNegative() {
		return negative;
	}

	public void setNegative(int negative) {
		this.negative = negative;
	}

	public int getPositive() {
		return positive;
	}

	public void setPositive(int positive) {
		this.positive = positive;
	}

}
