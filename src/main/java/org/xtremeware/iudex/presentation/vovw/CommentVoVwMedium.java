package org.xtremeware.iudex.presentation.vovw;

import java.util.Date;
import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.xtremeware.iudex.vo.CommentVo;
import org.xtremeware.iudex.vo.RatingSummaryVo;
import org.xtremeware.iudex.vo.ValueObject;

@ManagedBean
@ViewScoped
public class CommentVoVwMedium implements ValueObject {

    private CommentVo vo;
    private UserVoVwSmall user;
    private RatingSummaryVo rating;

    public CommentVoVwMedium() {
    }

    public CommentVoVwMedium(CommentVo vo, UserVoVwSmall user,
            RatingSummaryVo rating) {
        this.vo = vo;
        this.rating = rating;
        this.user = user;
    }

    @Override
    public String toString() {
        return "CommentVoVwMedium{" + "vo=" + vo + ", user=" + user +
                ", rating=" +
                rating + '}';
    }

    public CommentVo getVo() {
        return vo;
    }

    public void setVo(CommentVo vo) {
        this.vo = vo;
    }

    public RatingSummaryVo getRating() {
        return this.rating;
    }

    public void setRating(RatingSummaryVo rating) {
        this.rating = rating;
    }

    public UserVoVwSmall getUser() {
        return user;
    }

    public void setUser(UserVoVwSmall user) {
        this.user = user;
    }

    public boolean isAnonymous() {
        return vo.isAnonymous();
    }

    public String getContent() {
        return vo.getContent();
    }

    public Date getDate() {
        return vo.getDate();
    }

    public Long getId() {
        return vo.getId();
    }

    public Float getCourseRating() {
        return vo.getRating();
    }
}
