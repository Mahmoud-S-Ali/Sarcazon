package com.joyapeak.sarcazon.ui.comments;

import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by test on 11/1/2018.
 */

public interface CommentsMvpView extends MvpView {

    void addComments(List<CommentResponse.ComicComment> comments);

    void removeComment(int position);

    void updateCommentsCount(int count);

    void updateAfterCommentAdded();
}
