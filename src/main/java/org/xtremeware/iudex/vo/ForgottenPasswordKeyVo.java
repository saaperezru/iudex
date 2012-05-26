package org.xtremeware.iudex.vo;

import java.util.Date;

public class ForgottenPasswordKeyVo extends IdentifiableValueObject<Long>
        implements ValueObject {

    private Date expirationDate;
    private String key;
    private Long userId;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ForgottenPasswordKeyVo other = (ForgottenPasswordKeyVo) obj;
        if (this.id != other.id &&
                (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.expirationDate != other.expirationDate && (this.expirationDate ==
                null || !this.expirationDate.equals(other.expirationDate))) {
            return false;
        }
        if ((this.key == null) ? (other.key != null) : !this.key.equals(
                other.key)) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.
                equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.expirationDate != null ? this.expirationDate.
                hashCode() : 0);
        hash = 97 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 97 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ForgottenPasswordKeyVo{" + "expirationDate=" + expirationDate +
                ", key=" + key + ", userId=" + userId + '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
