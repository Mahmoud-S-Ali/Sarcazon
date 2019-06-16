package com.joyapeak.sarcazon.ui.pendingcommentsforreview;

import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by test on 8/27/2018.
 */

public interface PendingCommentsForReviewMvpView extends MvpView {

    void updateTotalCommentsCount(int count);
    void addComments(List<CommentResponse.ComicComment> comments);

    void updateAfterMarkReviewed(int adapterPosition);
}
