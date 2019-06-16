package com.joyapeak.sarcazon.ui.main;

import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.ui.notifications.NotificationsMvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public interface MainMvpView extends NotificationsMvpView {

    void updateNavViewUserInfo(String name, String photoUrl);

    int getSelectedComicsSource();

    void onFeaturedComicsReleased();
    // void onLogTimeSent();

    void addNewComics(List<ComicResponse.SingleComic> comics);

    void openProfilePage();

    void openComicUploadPage();

    void informUserWithUpdates();
}
