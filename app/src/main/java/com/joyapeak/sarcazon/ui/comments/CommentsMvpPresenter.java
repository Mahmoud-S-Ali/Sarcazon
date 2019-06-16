package com.joyapeak.sarcazon.ui.comments;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by test on 11/1/2018.
 */

public interface CommentsMvpPresenter <V extends CommentsMvpView> extends MvpPresenter<V> {

    void getNewComments(long comicId);

    void onCommentsRemoved();

    void likeComment(long commentId, boolean actionLike, boolean isPositiveAction);

    void reportComment(long commentId, int adapterPosition);

    void deleteComment(long commentId, int commentPosition);

    void blockComment(long commentId, int commentPosition);

    void addComment(long comicId, String comment);
}
