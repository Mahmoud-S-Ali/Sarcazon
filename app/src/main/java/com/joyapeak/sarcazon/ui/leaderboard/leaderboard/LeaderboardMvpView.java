package com.joyapeak.sarcazon.ui.leaderboard.leaderboard;

import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardResponse;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationMvpView;

import java.util.List;

/**
 * Created by test on 9/30/2018.
 */

public interface LeaderboardMvpView extends UploadConfirmationMvpView {

    void onLeaderboardReturned(List<LeaderboardResponse.LeaderboardUserItem> leaderboardUserItems);
}
