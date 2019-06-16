package com.joyapeak.sarcazon.ui.replies;

import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 5/3/2018.
 */

public interface RepliesMvpView extends MvpView {

    void onCommentDataRetrieved(ComicComment comment);

    void removeReply(int replyPosition);

    void addReplies(List<ComicComment> replies);

    void updateAfterReplyAdded();
}
