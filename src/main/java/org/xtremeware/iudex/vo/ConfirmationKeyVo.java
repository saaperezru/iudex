package org.xtremeware.iudex.vo;

import java.util.Date;

public class ConfirmationKeyVo implements ValueObject {

    private Long id;
    private Date expirationDate;
    private String confirmationKey;
    private Long userId;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfirmationKeyVo other = (ConfirmationKeyVo) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.expirationDate != other.expirationDate && (this.expirationDate == null || !this.expirationDate.equals(other.expirationDate))) {
            return false;
        }
        if ((this.confirmationKey == null) ? (other.confirmationKey != null) : !this.confirmationKey.equals(other.confirmationKey)) {
            return false;
        }
        if (this.userId != other.userId && (this.userId == null || !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.expirationDate != null ? this.expirationDate.hashCode() : 0);
        hash = 97 * hash + (this.confirmationKey != null ? this.confirmationKey.hashCode() : 0);
        hash = 97 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ConfirmationKeyVo{" + "id=" + id + ", expirationDate="
                + expirationDate + ", confirmationKey="
                + confirmationKey + ", userId=" + userId + '}';
    }

    public String getConfirmationKey() {
        return confirmationKey;
    }

    public void setConfirmationKey(String confirmationKey) {
        this.confirmationKey = confirmationKey;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
