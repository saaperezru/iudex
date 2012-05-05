package org.xtremeware.iudex.vo;

public class PeriodVo extends IdentifiableValueObject<Long> implements ValueObject {

    private int year;
    private int semester;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PeriodVo other = (PeriodVo) obj;
        if (this.id != other.id) {
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
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 67 * hash + this.year;
        hash = 67 * hash + this.semester;
        return hash;
    }

    @Override
    public String toString() {
        return "PeriodVo{" + "id=" + id + ", year=" + year + ", semester=" + semester + '}';
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
