package org.xtremeware.iudex.presentation.controller;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.xtremeware.iudex.presentation.vovw.CommentVoVwLarge;
import org.xtremeware.iudex.presentation.vovw.builder.CommentVoVwBuilder;

@ManagedBean
@RequestScoped
public class Home implements Serializable {

    private static final int LAST_COMMENTS_NUMBER = 10;
    private List<CommentVoVwLarge> lastComments;

    public List<CommentVoVwLarge> getLastComments() {
        loadLastComments();
        return lastComments;
    }

    private void loadLastComments() {
        if (lastComments == null) {
            lastComments = CommentVoVwBuilder.getInstance().getLastComments(
                    LAST_COMMENTS_NUMBER);
        }
    }
}
