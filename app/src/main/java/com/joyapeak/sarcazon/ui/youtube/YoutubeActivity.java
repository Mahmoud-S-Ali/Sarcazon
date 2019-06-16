package com.joyapeak.sarcazon.ui.youtube;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.joyapeak.sarcazon.BuildConfig;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.utils.UrlUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT;

/**
 * Created by Mahmoud Ali on 4/23/2018.
 */

public class YoutubeActivity extends YouTubeBaseActivity {

    private Unbinder mUnbinder;

    @BindView(R.id.youtube_player) YouTubePlayerView mYoutubePV;

    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private YouTubePlayer mYoutubePlayer;

    private String mVideoUrl;
    private final static String EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL";

    public final static String KEY_URL_VIDEO_ID = "v";


    public static Intent getStartIntent(Context context, String videoUrl) {
        Intent intent = new Intent(context, YoutubeActivity.class);
        intent.putExtra(EXTRA_VIDEO_URL, videoUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);
        mUnbinder = ButterKnife.bind(this);

        mVideoUrl = getIntent().getStringExtra(EXTRA_VIDEO_URL);

        initYoutubeListener();
        mYoutubePV.initialize(BuildConfig.GOOGLE_API_KEY, onInitializedListener);
    }

    private void initYoutubeListener() {
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                mYoutubePlayer = youTubePlayer;
                mYoutubePlayer.loadVideo(getYoutubeVideoId(mVideoUrl));
                mYoutubePlayer.setShowFullscreenButton(false);
                mYoutubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                mYoutubePlayer.setManageAudioFocus(true);
                mYoutubePlayer.play();
                mYoutubePlayer.setFullscreenControlFlags(FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Timber.d("Youtube failed to initialize: " + youTubeInitializationResult);
            }
        };
    }

    public static String getYoutubeVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.isEmpty())
            return null;

        String videoId = UrlUtils.getKeyValueFromUrl(videoUrl, YoutubeActivity.KEY_URL_VIDEO_ID);
        if (videoId != null) {
            return videoId;
        }

        List<String> pathSegments = Uri.parse(videoUrl).getPathSegments();
        return pathSegments.get(pathSegments.size() - 1);
    }
    public static String getYoutubeThumbnail(String videoUrl) {
        String videoId = getYoutubeVideoId(videoUrl);
        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
