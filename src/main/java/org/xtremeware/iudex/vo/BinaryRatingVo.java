package org.xtremeware.iudex.vo;

/**
 *
 * @author jdbermeol
 */
public class BinaryRatingVo extends IdentifiableValueObject<Long> implements ValueObject {

    private Long evaluatedObjectId;
    private Long userId;
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
        return "SubjectRatingVo{" + "id=" + id + ", subject=" + evaluatedObjectId + ", user=" + userId + ", value=" + value + '}';
    }

    public Long getEvaluatedObjectId() {
        return evaluatedObjectId;
    }

    public void setEvaluatedObjectId(Long evaluetedObjectId) {
        this.evaluatedObjectId = evaluetedObjectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
