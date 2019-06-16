package com.joyapeak.sarcazon.ui.comics;

import android.content.Context;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by test on 10/16/2018.
 */

public interface ComicsMvpPresenter <V extends ComicsMvpView> extends MvpPresenter <V> {

    void onViewInitialized();

    void getNewComics(int comicsSource, String category);

    // Could be sad or smile
    void likeComic(long comicId, boolean actionLike);

    void deleteComic(long comicId);

    void hideComic(long comicId);

    void blockComic(long comicId);

    void featureComic(long comicId);

    void shareComic(Context context, long comicId, String thumbnailUrl, int comicType);

    int getNewFeaturedComicsCount();

    void updateFacebookComicVidSourceUrl(long comicId, String vidSourceUrl);

    void markComicAsViewed(long comicId);

    long getLastFeaturedComicId();

    void updateLastFeaturedComicId(long id);

    void releaseFeatured(String message);

    void addReportedComic(long comicId);

    void onComicsReset();
}
