package com.joyapeak.sarcazon.ui.pendingcommentsforreview;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by test on 8/27/2018.
 */

public interface PendingCommentsForReviewMvpPresenter<V extends PendingCommentsForReviewMvpView>
        extends MvpPresenter <V> {

    void getPendingCommentsForReviews();

    void postMarkReviewed(long commentId, int reviewAction, Integer userAction, int adapterPosition);
}
