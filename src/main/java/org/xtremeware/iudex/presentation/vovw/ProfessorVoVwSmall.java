
package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.ValueObject;

public class ProfessorVoVwSmall implements ValueObject{

	private long id;
	private String fullName;
	private RatingSummaryVo ratingSummary;

	public ProfessorVoVwSmall(long id, String fullName, RatingSummaryVo ratingSummary) {
		this.id = id;
		this.fullName = fullName;
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
		final ProfessorVoVwSmall other = (ProfessorVoVwSmall) obj;
		if (this.id != other.id) {
			return false;
		}
		if ((this.fullName == null) ? (other.fullName != null) : !this.fullName.equals(other.fullName)) {
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
		hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
		hash = 71 * hash + (this.fullName != null ? this.fullName.hashCode() : 0);
		hash = 71 * hash + (this.ratingSummary != null ? this.ratingSummary.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "ProfessorVoVwSmall{" + "id=" + id + ", fullName=" + fullName + ", ratingSummary=" + ratingSummary + '}';
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RatingSummaryVo getRatingSummary() {
		return ratingSummary;
	}

	public void setRatingSummary(RatingSummaryVo ratingSummary) {
		this.ratingSummary = ratingSummary;
	}


}
