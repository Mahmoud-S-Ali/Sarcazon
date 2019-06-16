package com.joyapeak.sarcazon.ui.replies;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 5/3/2018.
 */

public interface RepliesMvpPresenter<V extends RepliesMvpView> extends MvpPresenter<V> {

    long getViewerId();

    void getComment(long commentId);

    void addReply(long mainCommentId, String reply);

    void getReplies(long mainCommentId);

    void likeReply(long replyId, boolean actionLike, boolean isPositiveAction);

    void reportReply(long replyId, int adapterPosition);

    void deleteReply(long replyId, int replyPosition);

    void blockReply(long replyId, int replyPosition);

    boolean getIsEngagementUsersEnabled();
}
