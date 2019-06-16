package com.joyapeak.sarcazon.ui.comics;

import android.view.View;

import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by test on 10/16/2018.
 */

public interface ComicsMvpView extends MvpView {

    void onComicsRetrieved(List<ComicResponse.SingleComic> comics);

    void onFeaturedComicsReleased();

    void onComicBlocked(boolean isSuccessful);

    void onComicHid(boolean isSuccessful);

    void onComicDeleted(boolean isSuccessful);

    void onComicAddedToFeatured();

    void handleShareComicGif();

    void handleShareComicImage();

    void handleShareVideoUrl(String url);

    View getProgressBarView();
}
