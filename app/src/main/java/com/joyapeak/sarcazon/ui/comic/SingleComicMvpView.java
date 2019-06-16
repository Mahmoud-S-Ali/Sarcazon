package com.joyapeak.sarcazon.ui.comic;

import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 4/17/2018.
 */

public interface SingleComicMvpView extends MvpView {

    void updateComicLikeView(boolean hasLiked);

    void updateComicDislikeView(boolean hasDisliked);

    void onComicLikeResult(boolean likeAction, boolean isSuccessful);

    void addComments(List<CommentResponse.ComicComment> comments);

    void removeComment(int position);

    void updateCommentsCount(int count);

    void updateAfterCommentAdded();

    void pauseComic();

    void resumeComic();

    void handleShareVideoUrl(String url);

    void handleShareComicImage();

    void handleShareComicGif();

    void removeComic();

    void onComicAddedToFeatured();
}
