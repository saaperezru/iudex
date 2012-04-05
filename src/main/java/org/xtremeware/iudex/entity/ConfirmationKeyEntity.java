package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.xtremeware.iudex.vo.ConfirmationKeyVo;

@javax.persistence.Entity(name="ConfirmationKey")
@NamedQuery(
    name="getByConfirmationKey",
    query="SELECT result FROM ConfirmationKeyEntity result "
                + "WHERE result.confirmationKey = :CK"
)
public class ConfirmationKeyEntity implements Serializable, Entity<ConfirmationKeyVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expirationDate;
    private String confirmationKey;
    private UserEntity user;

    @Override
    public ConfirmationKeyVo toVo() {
        ConfirmationKeyVo vo = new ConfirmationKeyVo();
        vo.setId(getId());
        vo.setExpirationDate(getExpirationDate());
        vo.setConfirmationKey(getConfirmationKey());
        vo.setUserId(getUser().getId());
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
        final ConfirmationKeyEntity other = (ConfirmationKeyEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ConfirmationKeyEntity{" + "id=" + id + ", expirationDate=" + expirationDate + ", confirmationKey=" + confirmationKey + ", user=" + user + '}';
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}