package org.xtremeware.iudex.vo;

/**
 *
 * @author jdbermeol
 */
public abstract class BinaryRatingVo extends IdentifiableValueObject<Long> implements ValueObject {

    private Long evaluetedObjectId;
    private Long user;
    private int value;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BinaryRatingVo other = (BinaryRatingVo) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "SubjectRatingVo{" + "id=" + id + ", subject=" + evaluetedObjectId + ", user=" + user + ", value=" + value + '}';
    }

    public Long getEvaluetedObjectId() {
        return evaluetedObjectId;
    }

    public void setEvaluetedObjectId(Long evaluetedObjectId) {
        this.evaluetedObjectId = evaluetedObjectId;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
