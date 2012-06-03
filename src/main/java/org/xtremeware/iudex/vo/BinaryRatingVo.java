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
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        if (this.evaluatedObjectId != other.evaluatedObjectId && (this.evaluatedObjectId == null || !this.evaluatedObjectId.equals(other.evaluatedObjectId))) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 29 * hash + (this.evaluatedObjectId != null ? this.evaluatedObjectId.hashCode() : 0);
        hash = 29 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 29 * hash + this.value;
        return hash;
    }

    @Override
    public String toString() {
        return "SubjectRatingVo{" + "id=" + getId() + ", subject=" + evaluatedObjectId + ", user=" + userId + ", value=" + value + '}';
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
