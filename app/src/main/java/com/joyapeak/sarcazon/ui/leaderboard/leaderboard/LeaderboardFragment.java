package com.joyapeak.sarcazon.ui.leaderboard.leaderboard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.async.ImageCompression;
import com.joyapeak.sarcazon.async.ViewImageGeneration;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramResponse;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardResponse;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.popupmessage.MessageDialog;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.utils.FileUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by test on 9/30/2018.
 */

public class LeaderboardFragment extends BaseFragment implements LeaderboardMvpView {

    @BindView(R.id.layout_leaderboard_root)
    LinearLayout mLeaderboardRoot;

    @BindView(R.id.rv_leaderboard)
    RecyclerView mLeaderboardRV;

    @BindView(R.id.tv_leaderboard_pageDesc)
    TextView mLeaderboardPageDescTV;

    @BindView(R.id.layout_empty_items)
    View mEmptyLayout;

    @Inject
    LeaderboardMvpPresenter<LeaderboardMvpView> mPresenter;

    @Inject
    @Named("nonscrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    @Inject
    LeaderboardAdapter mLeaderboardAdapter;

    private String mLeaderboardSource;
    private final static String EXTRA_LEADERBOARD_TYPE = "EXTRA_LEADERBOARD_TYPE";

    private ViewImageGeneration mImageDownloadTask;
    private ImageCompression mImageCompressionTask;


    public static LeaderboardFragment newInstance(String leaderboardType) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_LEADERBOARD_TYPE, leaderboardType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            setUnBinder(ButterKnife.bind(this, root));

            mPresenter.onAttach(this);

            mLeaderboardSource = getArguments().getString(EXTRA_LEADERBOARD_TYPE);

            setUp(root);
            mPresenter.onViewInitialized(mLeaderboardSource);
        }

        return root;
    }


    @Override
    public void onStop() {
        if (mImageDownloadTask != null) {
            mImageDownloadTask.cancelTask();
        }

        if (mImageCompressionTask != null) {
            mImageCompressionTask.cancelImageCompressionTask();
        }

        super.onStop();
    }

    @Override
    protected void setUp(View view) {
        setupLeaderboardAdapter();
        setupLeaderboardRV();

        if (mLeaderboardSource != null && mLeaderboardSource.equals(ApiHelper.LEADERBOARD_SOURCES.ALL_TIME)) {
            mLeaderboardPageDescTV.setText(getString(R.string.leaderboard_allTime_desc));

        } else {
            mLeaderboardPageDescTV.setText(getString(R.string.leaderboard_weekly_desc));
        }
    }

    private void setupLeaderboardAdapter() {
        mLeaderboardAdapter.setCallback(new LeaderboardAdapter.Callback() {
            @Override
            public void onItemClicked(int position, long userId) {
                startActivity(ProfileActivity.getStartIntent(getActivity(), userId));
            }
        });

        mLeaderboardAdapter.setEmptyView(mEmptyLayout);
    }
    private void setupLeaderboardRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLeaderboardRV.setLayoutManager(mLayoutManager);
        mLeaderboardRV.setAdapter(mLeaderboardAdapter);
    }

    @Override
    public void onLeaderboardReturned(List<LeaderboardResponse.LeaderboardUserItem> leaderboardUserItems) {
        if (mLeaderboardAdapter.getItemCount() == 0) {
            mLeaderboardAdapter.addUsers(leaderboardUserItems);

            if (mLeaderboardSource.equals(ApiHelper.LEADERBOARD_SOURCES.PREV_WEEK)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startImageDownloadTask();
                    }
                }, 500);
            }
        }
    }


    private void startImageDownloadTask() {
        if (mImageDownloadTask == null) {

            mImageDownloadTask = new ViewImageGeneration(getContext(),
                    new ViewImageGeneration.ViewImageGenerationHandler() {
                        @Override
                        public void onImageGenerated(Bitmap bitmap) {

                            mImageDownloadTask = null;

                            if (bitmap == null) {
                                MessageDialog dialog = MessageDialog.newInstance(getString(R.string.free_some_space));
                                dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());

                            } else {
                                startImageCompressionToByteArrTask(FileUtils.getCachedImagePath(getContext()));
                            }
                        }
                    });

            mImageDownloadTask.startNewTask(mLeaderboardRoot, true);
        }
    }
    private void startImageCompressionToByteArrTask(String imagePath) {
        if (mImageCompressionTask == null) {

            mImageCompressionTask = new ImageCompression(getContext(),
                    new ImageCompression.ImageCompressionHandler() {

                        @Override
                        public void onCompressionCompleted(byte[][] filesByteArrays) {
                            mImageCompressionTask = null;
                            mPresenter.uploadFiles(filesByteArrays);
                        }

                        @Override
                        public void onCompressionCompleted(Bitmap bitmap) {
                        }
                    });

            mImageCompressionTask.startNewImageCompressionToByteArrTask(imagePath);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onComicUrlUploadResult(boolean isSuccessful, Long comicId) {
        getActivity().finish();
    }

    @Override
    public void onInstagramVideoDataRetrieved(boolean successful, InstagramResponse.InstagramVideoResponse videoData) {
    }

    @Override
    public void onPhotoFilesUploaded(String photoUrl) {
        mPresenter.uploadComicUrl(ApiHelper.COMIC_TYPES.LEADERBOARD_RESULTS,
                photoUrl,
                null,
                getString(R.string.leaderboard_top_users_header),
                null,
                "leaderboard,top,users",
                null,
                getContentAspectRatio());
    }

    private double getContentAspectRatio() {

        double width;
        double height;

        width = (double) mLeaderboardRoot.getMeasuredWidth();
        height = (double) mLeaderboardRoot.getMeasuredHeight();

        return height == 0? 0 : width / height;
    }
}
