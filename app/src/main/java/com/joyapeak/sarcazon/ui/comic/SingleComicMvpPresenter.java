package com.joyapeak.sarcazon.ui.comic;

import android.content.Context;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 4/17/2018.
 */

public interface SingleComicMvpPresenter<V extends SingleComicMvpView> extends MvpPresenter<V> {

    void onViewInitialized();

    long getViewerId();

    // Could be sad or smile
    void likeComic(long comicId, boolean actionLike);

    void deleteComic(long comicId);

    void hideComic(long comicId);

    void blockComic(long comicId);

    void featureComic(long comicId);

    void shareComic(Context context, long comicId, String thumbnailUrl, int comicType);

    void updateFacebookComicVidSourceUrl(long comicId, String vidSourceUrl);

    void markComicAsReviewed(long comicId, int contentReportAction, Integer userReportAction);

    void getNewComments(long comicId);

    void onCommentsRemoved();

    void likeComment(long commentId, boolean actionLike, boolean isPositiveAction);

    void reportComment(long commentId, int adapterPosition);

    void deleteComment(long commentId, int commentPosition);

    void blockComment(long commentId, int commentPosition);

    void addComment(long comicId, String comment);
}
