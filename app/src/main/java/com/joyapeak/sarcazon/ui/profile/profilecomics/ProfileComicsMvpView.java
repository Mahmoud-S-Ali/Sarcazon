package com.joyapeak.sarcazon.ui.profile.profilecomics;

import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 5/7/2018.
 */

public interface ProfileComicsMvpView extends MvpView {

    void addUserComics(List<ProfileResponse.ProfileComicInfo> comicsList);

    void updateTotalComicsCount(int count);
}
