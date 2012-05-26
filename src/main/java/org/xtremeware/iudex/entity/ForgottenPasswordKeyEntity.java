package org.xtremeware.iudex.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.xtremeware.iudex.helper.SecurityHelper;
import org.xtremeware.iudex.vo.ForgottenPasswordKeyVo;

@javax.persistence.Entity(name = "ForgottenPasswordKey")

@NamedQueries(
{@NamedQuery(name = "getForgottenPasswordKeyByKey",
    query = "SELECT fpk FROM ForgottenPasswordKey fpk WHERE fpk.key = :key AND fpk.expirationDate >= CURRENT_DATE"),
    @NamedQuery(name = "getForgottenPasswordKeyByUserName",
    query =
    "SELECT fpk FROM ForgottenPasswordKey fpk JOIN fpk.user u WHERE u.userName = :userName AND fpk.expirationDate >= CURRENT_DATE")})
@Table(name = "FORGOTTEN_PASSWORD_KEY")
public class ForgottenPasswordKeyEntity implements Serializable,
        Entity<ForgottenPasswordKeyVo> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FORGOTTEN_PASSWORD_KEY")
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "EXPIRATION_DATE", nullable = false)
    private Date expirationDate;
    @Column(name = "KEY_", length = 64, nullable = false)
    private String key;
    @OneToOne
    @JoinColumn(name = "ID_USER_", nullable = false, unique = true)
    private UserEntity user;

    @Override
    public ForgottenPasswordKeyVo toVo() {
        ForgottenPasswordKeyVo vo = new ForgottenPasswordKeyVo();
        vo.setId(getId());
        vo.setExpirationDate(getExpirationDate());
        vo.setKey(SecurityHelper.sanitizeHTML(getKey()));
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
        final ForgottenPasswordKeyEntity other =
                (ForgottenPasswordKeyEntity) obj;
        if (this.id != other.id &&
                (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.expirationDate != other.expirationDate &&
                (this.expirationDate == null ||
                !this.expirationDate.equals(other.expirationDate))) {
            return false;
        }
        if ((this.key == null) ? (other.key != null) :
                !this.key.equals(other.key)) {
            return false;
        }
        if (this.user != other.user && (this.user == null ||
                !this.user.equals(other.user))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash =
                89 * hash +
                (this.expirationDate != null ? this.expirationDate.hashCode() :
                0);
        hash = 89 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 89 * hash + (this.user != null ? this.user.hashCode() : 0);
        return hash;
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
