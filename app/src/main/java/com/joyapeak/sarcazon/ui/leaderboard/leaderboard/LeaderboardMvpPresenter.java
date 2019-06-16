package com.joyapeak.sarcazon.ui.leaderboard.leaderboard;

import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationMvpPresenter;

/**
 * Created by test on 9/30/2018.
 */

public interface LeaderboardMvpPresenter <V extends LeaderboardMvpView> extends UploadConfirmationMvpPresenter<V> {

    void onViewInitialized(String leaderboardType);

    void getLeaderboard(String leaderboardSource);
}
