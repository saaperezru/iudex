package org.xtremeware.iudex.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.xtremeware.iudex.vo.PeriodVo;

@javax.persistence.Entity(name = "Period")
@NamedQueries({
	@NamedQuery(name = "getByYear", query = "SELECT p FROM Period p WHERE p.year = :year"),
	@NamedQuery(name = "getByYearAndSemester", query = "SELECT p FROM Period p WHERE p.year = :year AND p.semester = :semester")
})
@Table(name = "PERIOD_")
public class PeriodEntity implements Serializable, Entity<PeriodVo> {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PERIOD")
	private Long id;
	@Column(name = "YEAR_")
	private int year;
	@Column(name = "SEMESTER", nullable = false)
	private int semester;

	@Override
	public PeriodVo toVo() {
		PeriodVo vo = new PeriodVo();

		vo.setId(id);
		vo.setSemester(semester);
		vo.setYear(year);

		return vo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PeriodEntity other = (PeriodEntity) obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		if (this.year != other.year) {
			return false;
		}
		if (this.semester != other.semester) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 97 * hash + this.year;
		hash = 97 * hash + this.semester;
		return hash;
	}

	@Override
	public String toString() {
		return "PeriodEntity{" + "id=" + id + ", year=" + year + ", semester=" + semester + '}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
