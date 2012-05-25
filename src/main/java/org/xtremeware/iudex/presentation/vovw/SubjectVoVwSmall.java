
package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.ValueObject;

public class SubjectVoVwSmall implements ValueObject{

	private long id;
	private String name;
	private RatingSummaryVo ratingSummary;

	public SubjectVoVwSmall(long id, String name, RatingSummaryVo ratingSummary) {
		this.id = id;
		this.name = name;
		this.ratingSummary = ratingSummary;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SubjectVoVwSmall other = (SubjectVoVwSmall) obj;
		if (this.id != other.id) {
			return false;
		}
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		if (this.ratingSummary != other.ratingSummary && (this.ratingSummary == null || !this.ratingSummary.equals(other.ratingSummary))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
		hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 97 * hash + (this.ratingSummary != null ? this.ratingSummary.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "SubjectVoVwSmall{" + "id=" + id + ", name=" + name + ", ratingSummary=" + ratingSummary + '}';
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RatingSummaryVo getRatingSummary() {
		return ratingSummary;
	}

	public void setRatingSummary(RatingSummaryVo ratingSummary) {
		this.ratingSummary = ratingSummary;
	}


}
