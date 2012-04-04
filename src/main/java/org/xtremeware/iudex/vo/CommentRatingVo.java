package org.xtremeware.iudex.vo;

public class CommentRatingVo extends ValueObject {

    private Long id;
    private Long commentId;
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
        final CommentRatingVo other = (CommentRatingVo) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.commentId != other.commentId && (this.commentId == null || !this.commentId.equals(other.commentId))) {
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
        hash = 61 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 61 * hash + (this.commentId != null ? this.commentId.hashCode() : 0);
        hash = 61 * hash + (this.userId != null ? this.userId.hashCode() : 0);
        hash = 61 * hash + this.value;
        return hash;
    }

    @Override
    public String toString() {
        return "CommentRatingVo{" + "id=" + id + ", comment=" + commentId + ", userId=" + userId + ", value=" + value + '}';
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
