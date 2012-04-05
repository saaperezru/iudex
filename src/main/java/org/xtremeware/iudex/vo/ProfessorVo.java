package org.xtremeware.iudex.vo;

public class ProfessorVo extends ValueObject {

    private Long id;
    private String firstName;
    private String lastName;
    private String website;
    private String imageUrl;
    private String description;
    private String email;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProfessorVo other = (ProfessorVo) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.firstName == null) ? (other.firstName != null) : !this.firstName.equals(other.firstName)) {
            return false;
        }
        if ((this.lastName == null) ? (other.lastName != null) : !this.lastName.equals(other.lastName)) {
            return false;
        }
        if ((this.website == null) ? (other.website != null) : !this.website.equals(other.website)) {
            return false;
        }
        if ((this.imageUrl == null) ? (other.imageUrl != null) : !this.imageUrl.equals(other.imageUrl)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 19 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
        hash = 19 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
        hash = 19 * hash + (this.website != null ? this.website.hashCode() : 0);
        hash = 19 * hash + (this.imageUrl != null ? this.imageUrl.hashCode() : 0);
        hash = 19 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 19 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ProfessorVo{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", website=" + website + ", imageUrl=" + imageUrl + ", description=" + description + ", email=" + email + '}';
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getEmail() {
        return website;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
