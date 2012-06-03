package org.xtremeware.iudex.vo;

public class CommentVoFull implements ValueObject {

    private CommentVo vo;
    private UserVo user;
    private RatingSummaryVo rating;

    public CommentVoFull(CommentVo vo, UserVo user, RatingSummaryVo rating) {
        this.vo = vo;
        this.rating = rating;
        if (vo.isAnonymous()) {
            this.user = null;
        } else {
            this.user = user;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommentVoFull other = (CommentVoFull) obj;
        if (this.vo != other.vo && (this.vo == null || !this.vo.equals(other.vo))) {
            return false;
        }
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.rating != other.rating && (this.rating == null || !this.rating.equals(other.rating))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.vo != null ? this.vo.hashCode() : 0);
        hash = 23 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 23 * hash + (this.rating != null ? this.rating.hashCode() : 0);
        return hash;
    }

    public RatingSummaryVo getRating() {
        return rating;
    }

    public void setRating(RatingSummaryVo rating) {
        this.rating = rating;
    }

    public UserVo getUser() {
        return user;
    }

    public void setUser(UserVo user) {
        this.user = user;
    }

    public CommentVo getVo() {
        return vo;
    }

    public void setVo(CommentVo vo) {
        this.vo = vo;
    }
}
