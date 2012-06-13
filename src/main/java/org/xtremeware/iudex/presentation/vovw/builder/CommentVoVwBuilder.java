package org.xtremeware.iudex.presentation.vovw.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.xtremeware.iudex.businesslogic.facade.CommentsFacade;
import org.xtremeware.iudex.businesslogic.facade.FacadeFactory;
import org.xtremeware.iudex.helper.Config;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwLarge;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwMedium;
import org.xtremeware.iudex.presentation.vovw.UserVoVwSmall;
import org.xtremeware.iudex.vo.CommentVo;

public final class CommentVoVwBuilder {

    private static CommentVoVwBuilder instance;
    private FacadeFactory facadeFactory;

    private CommentVoVwBuilder(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public static synchronized CommentVoVwBuilder getInstance() {
        if (instance == null) {
            instance = new CommentVoVwBuilder(Config.getInstance().
                    getFacadeFactory());
        }
        return instance;
    }

    public List<CommentVoVwMedium> getCommentsByCourseId(long courseId) {
        List<CommentVo> listOfCommentVo = facadeFactory.getCommentsFacade().
                getCommentsByCourseId(courseId);
        List<CommentVoVwMedium> listOfCommentVoVwMedium =
                initListOfCommentVoMedium(listOfCommentVo.size());
        return getListOfCommentVoVwMedium(listOfCommentVo,
                listOfCommentVoVwMedium);
    }

    private List<CommentVoVwMedium> initListOfCommentVoMedium(int size) {
        List<CommentVoVwMedium> listOfCommentVoVwMedium =
                new ArrayList<CommentVoVwMedium>(size);
        for (int i = 1; i <= size; i++) {
            listOfCommentVoVwMedium.add(new CommentVoVwMedium());
        }
        return listOfCommentVoVwMedium;
    }

    private <T extends CommentVoVwMedium> List<T> getListOfCommentVoVwMedium(
            List<CommentVo> source, List<T> destination) {
        HashMap<Long, UserVoVwSmall> users = new HashMap<Long, UserVoVwSmall>();
        UserVoVwSmall anonymousUser = UserVoVwBuilder.ANONYMOUS_USER;
        CommentsFacade commentsFacade = facadeFactory.getCommentsFacade();

        UserVoVwSmall commentUser;
        CommentVo commentVo;
        CommentVoVwMedium commentVoVwMedium;
        int listSize = source.size();
        for (int i = 0; i < listSize; i++) {
            commentVo = source.get(i);
            if (commentVo.isAnonymous()) {
				anonymousUser.setId(commentVo.getUserId());
                commentUser = anonymousUser;
            } else {
                long userId = commentVo.getUserId();
                if (!users.containsKey(userId)) {
                    users.put(userId, UserVoVwBuilder.getInstance().getUser(
                            userId));
                }
                commentUser = users.get(userId);
            }
            commentVoVwMedium = destination.get(i);
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT - 5"));
			try {
				commentVo.setDate(dateFormatGmt.parse(dateFormatGmt.format(commentVo.getDate())));
			} catch (ParseException ex) {
				Config.getInstance().getServiceFactory().getLogService().error("Error while parsing date in CommentVoVwMedium building process.",ex);
			}
            commentVoVwMedium.setVo(commentVo);
            commentVoVwMedium.setUser(commentUser);
            commentVoVwMedium.setRating(commentsFacade.getCommentRatingSummary(commentVo.
                    getId()));
        }
        return destination;
    }

    public List<CommentVoVwLarge> getLastComments(int maxResults) {
        List<CommentVo> listOfCommentVo = facadeFactory.getCommentsFacade().
                getLastComments(maxResults);
        List<CommentVoVwLarge> listOfCommentVoVwLarge =
                initListOfCommentVoVwLarge(listOfCommentVo.size());
        listOfCommentVoVwLarge = getListOfCommentVoVwMedium(listOfCommentVo,
                listOfCommentVoVwLarge);
        return completeListOfCommentVoVwLarge(listOfCommentVoVwLarge);
    }

    private List<CommentVoVwLarge> initListOfCommentVoVwLarge(int size) {
        List<CommentVoVwLarge> listOfCommentVoVwLarge =
                new ArrayList<CommentVoVwLarge>(size);
        for (int i = 1; i <= size; i++) {
            listOfCommentVoVwLarge.add(new CommentVoVwLarge());
        }
        return listOfCommentVoVwLarge;
    }

    private List<CommentVoVwLarge> completeListOfCommentVoVwLarge(
            List<CommentVoVwLarge> listOfVwLarge) {
        CourseVoVwBuilder courseVoVwBuilder = CourseVoVwBuilder.getInstance();
        for (CommentVoVwLarge commentVoVwLarge : listOfVwLarge) {
            commentVoVwLarge.setCourse(courseVoVwBuilder.getCourseVoVwFull(
                    commentVoVwLarge.getVo().getCourseId()));
        }
        return listOfVwLarge;
    }
}
