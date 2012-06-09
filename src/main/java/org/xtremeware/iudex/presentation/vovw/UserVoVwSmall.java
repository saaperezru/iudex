package org.xtremeware.iudex.presentation.vovw;

import org.xtremeware.iudex.vo.ValueObject;

public class UserVoVwSmall implements ValueObject {

    private Long id;
    private String name;
    private String username;
    private String mainProgram;
    private String imageUrl;

    public UserVoVwSmall(long userId, String name, String userName, String mainProgram, String image) {
        this.id = userId;
        this.name = name;
        this.username = userName;
        this.mainProgram = mainProgram;
        this.imageUrl = image;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserVoVwSmall other = (UserVoVwSmall) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 67 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "UserVoVwSmall{" + "id=" + id + ", name=" + name + ", username=" + username + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMainProgram() {
        return mainProgram;
    }

    public void setMainProgram(String mainProgram) {
        this.mainProgram = mainProgram;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}