package com.joyapeak.sarcazon.ui.comic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.async.GifDownload;
import com.joyapeak.sarcazon.async.ViewImageGeneration;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.helper.FBVideoSourceGrabber;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.bottomsheetlist.BottomSheetListDialog;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.comments.GeneralCommentsAdapter;
import com.joyapeak.sarcazon.ui.custom.CustomHeightImageView;
import com.joyapeak.sarcazon.ui.custom.DoubleClickListener;
import com.joyapeak.sarcazon.ui.main.MainActivity;
import com.joyapeak.sarcazon.ui.popupmessage.MessageDialog;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.ui.replies.RepliesActivity;
import com.joyapeak.sarcazon.ui.report.ReportTypesBSDialog;
import com.joyapeak.sarcazon.ui.share.link.ShareLinkDialog;
import com.joyapeak.sarcazon.ui.share.photo.SharePhotoDialog;
import com.joyapeak.sarcazon.ui.youtube.YoutubeActivity;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;
import com.joyapeak.sarcazon.utils.MarshmallowPermissions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 4/17/2018.
 */

public class SingleComicFragment extends BaseFragment implements
        SingleComicMvpView,
        BottomSheetListDialog.BottomSheetListHandler,
        ReportTypesBSDialog.ReportTypesDialogHandler,
        MessageDialog.MessageDialogHandler,
        FBVideoSourceGrabber.FBVideoSourceHandler {

    public static final String TAG = "MainContentFragment";

    @BindView(R.id.main_top_panel_posterSummary)
    SlidingUpPanelLayout mPosterSummaryPanelLayout;

    @BindView(R.id.main_bottom_panel_comments)
    SlidingUpPanelLayout mCommentsPanelLayout;

    @BindView(R.id.layout_comic_posterInfo)
    LinearLayout mPosterInfoLayout;

    @BindView(R.id.iv_comic_posterPhoto)
    ImageView mPosterPhotoIV;

    @BindView(R.id.tv_comic_posterName)
    TextView mPosterNameTV;

    @BindView(R.id.tv_main_removedMsg)
    TextView mComicRemovedTV;

    @BindView(R.id.layout_comic_content)
    View mComicRootLayout;

    @BindView(R.id.iv_comic_likeImage)
    ImageView mAnimationLikeImageIV;

    @BindView(R.id.layout_comic_likeImage)
    View mAnimationLikeImageLayout;

    @BindView(R.id.layout_main_comicContent)
    View mContentLayout;

    @BindView(R.id.iv_comic_contentImage)
    CustomHeightImageView mContentImageIV;

    @BindView(R.id.tv_comic_caption)
    TextView mCaptionTV;

    @BindView(R.id.ib_comic_youtube)
    ImageButton mYoutubeIB;

    @BindView(R.id.layout_comic_contentImage)
    View mImageContentLayout;

    @BindView(R.id.layout_comic_video)
    View mVideoViewLayout;

    @BindView(R.id.vv_comic_contentVideo)
    VideoView mVideoView;

    @BindView(R.id.iv_comic_videoThumbnail)
    CustomHeightImageView mVideoThumbnailIV;

    @BindView(R.id.webview_comic_fbHack)
    WebView mFBWebView;

    @BindView(R.id.progressbar_comic_loading)
    ProgressBar mContentLoadingProgressbar;

    @BindView(R.id.layout_comic_bottomLayout)
    View mBottomLayout;

    @BindView(R.id.ib_comic_like)
    ImageView mLikeIB;

    @BindView(R.id.tv_comic_likesCount)
    TextView mLikesCountTV;

    @BindView(R.id.ib_comic_dislike)
    ImageButton mDislikeIB;

    @BindView(R.id.tv_comic_dislikesCount)
    TextView mDislikesCountTV;

    @BindView(R.id.tv_comic_commentsCount)
    TextView mCommentsCountTV;

    @BindView(R.id.rv_comments)
    RecyclerView mCommentsRV;

    @BindView(R.id.layout_empty_items)
    View mEmptyCommentsView;

    @BindView(R.id.et_comments_newComment)
    EditText mNewCommentET;

    @BindView(R.id.ib_comic_featureComic)
    ImageButton mFeatureComicIB;

    @BindView(R.id.layout_main_reviewActions)
    View mReportActionsLayout;

    @BindArray(R.array.user_review_actions)
    String[] mUserReviewActions;

    @Inject
    SingleComicMvpPresenter<SingleComicMvpView> mPresenter;

    @Inject
    GeneralCommentsAdapter mCommentsAdapter;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    @Inject
    FBVideoSourceGrabber mFBVideoSourceGrabber;

    // Holds the current fragment position
    private Integer mFragmentPos;
    private final static String EXTRA_FRAGMENT_POS = "EXTRA_FRAGMENT_POS";

    private ComicResponse.SingleComic mComicData;
    private final static String EXTRA_COMIC_DATA = "EXTRA COMIC DATA";

    private MediaPlayer mMediaPlayer;

    // Is comments page is open or closed?
    private boolean mCommentsPanelWasCollapsed = true;
    private boolean mSummaryPanelWasCollapsed = true;

    // This task is used to save any comic as an image locally
    private ViewImageGeneration mImageDownloadTask;
    private GifDownload mGifDownloadTask;

    private final static int COMIC_SHARE_TYPE_SAVE_POS = 0;
    private final static int COMIC_SHARE_TYPE_SHARE_POS = 1;


    public static SingleComicFragment newInstance(ComicResponse.SingleComic comic, int fragmentPos) {
        SingleComicFragment mainContentFragment = new SingleComicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_FRAGMENT_POS, fragmentPos);
        bundle.putParcelable(EXTRA_COMIC_DATA, comic);
        mainContentFragment.setArguments(bundle);
        return mainContentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            ButterKnife.bind(this, root);

            Bundle bundle = getArguments();
            if (bundle != null) {
                mFragmentPos = bundle.getInt(EXTRA_FRAGMENT_POS, 0);
                mComicData = bundle.getParcelable(EXTRA_COMIC_DATA);
            }

            mPresenter.onAttach(this);

            setUp(root);

            mPresenter.onViewInitialized();
        }

        return root;
    }

    @Override
    public void onStop() {
        if (mImageDownloadTask != null) {
            mImageDownloadTask.cancelTask();
        }

        if (mGifDownloadTask != null) {
            mGifDownloadTask.cancelGifDownloadTask();
        }

        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (hasFocus()) {
            pauseComic();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeComic();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MarshmallowPermissions.STORAGE_PERMISSION_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.shareComic(getActivity(),
                            mComicData.getComicInfo().getComicId(),
                            mComicData.getComicInfo().getThumbnailUrl(),
                            mComicData.getComicInfo().getComicType());

                } else {
                    showMessage("Permission must be granted");
                }
                break;
        }
    }

    @Override
    protected void setUp(View root) {

        // This is a bottom tint used so the bottom actions can appear in light backgrounds
        mBottomLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tint_background));

        if (mComicData != null) {
            updateComicViews(mComicData.getComicInfo());
            updatePosterViews(mComicData.getPosterInfo());
        }

        if (getActivity() instanceof MainActivity &&
                ((MainActivity) getActivity()).getSelectedComicsSource() == ApiHelper.COMIC_SOURCES.PENDING_REVIEW) {
            mBottomLayout.setVisibility(View.GONE);
            mReportActionsLayout.setVisibility(View.VISIBLE);
            mCommentsPanelLayout.setEnabled(false);
            mPosterSummaryPanelLayout.setEnabled(false);

        } else {
            setupCommentsPanelLayout();
            setupPosterSummaryPanelLayout();

            setupCommentsAdapter();
            setupCommentsRV();

            setComicRootDoubleClickListener();

            if (mComicData != null) {
                ComicResponse.ComicInfo comicInfo = mComicData.getComicInfo();

                // Hide dislike button for featured comics
                if (comicInfo.getFeatureStatus() == ApiHelper.FEATURE_STATUS_TYPES.FEATURED) {
                    mDislikeIB.setVisibility(View.INVISIBLE);

                } else {
                    mDislikeIB.setVisibility(View.VISIBLE);
                }
            }
        }

        // OverScrollDecoratorHelper.setUpOverScroll(mMainSV);
        /*new PhotoViewAttacher(mContentImageIV);*/
    }

    // Making like to comic by double clicking it
    private void setComicRootDoubleClickListener() {
        mComicRootLayout.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
            }

            @Override
            public void onDoubleClick(View v) {

                if (mPresenter.getIsEngagementUsersEnabled()) {
                    onLikeClicked();
                    return;
                }

                if (!mComicData.getComicInfo().getIsLiked()) {
                    onLikeClicked();

                } else {
                    makeLikeAnimation(true);
                }
            }
        });
    }
    private void makeLikeAnimation(boolean isLike) {
        if (isLike) {
            mAnimationLikeImageIV.setImageResource(R.drawable.ic_like_active);

        } else {
            mAnimationLikeImageIV.setImageResource(R.drawable.ic_dislike_active);
        }

        mAnimationLikeImageLayout.setVisibility(View.VISIBLE);
        int animRes = isLike? R.anim.like_image_anim : R.anim.dislike_image_anim;

        Animation likeImageAnim = addAnimationToView(mAnimationLikeImageLayout, animRes,
                null, null);

        likeImageAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimationLikeImageLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    // Returns if the current fragment is the currently selected fragment on the screen or not
    private boolean hasFocus() {
        if (getActivity() instanceof MainActivity) {
            return mFragmentPos == ((MainActivity) getActivity()).getFocusedFragmentPos();
        }

        return true;
    }

    // Updating comic views with the actual comic info
    public void updateComicViews(final ComicResponse.ComicInfo comicInfo) {

        Integer controlFlag = mPresenter.getUserControlFlag();
        controlFlag = controlFlag == null? ApiHelper.CONTROL_FLAG_TYPES.NORMAL : controlFlag;
        Integer featureStatus = comicInfo.getFeatureStatus();

        if (controlFlag == ApiHelper.CONTROL_FLAG_TYPES.ADMIN &&
                featureStatus != null && featureStatus != ApiHelper.FEATURE_STATUS_TYPES.FEATURED) {

            mFeatureComicIB.setVisibility(View.VISIBLE);
            if (featureStatus == ApiHelper.FEATURE_STATUS_TYPES.PENDING) {
                mFeatureComicIB.setImageResource(R.drawable.ic_star_active);
            }

        } else {
            mFeatureComicIB.setVisibility(View.GONE);
        }

        // Hide likes count for new comics if user hasn't posted any action
        if (!getIsOwner() && !mPresenter.getIsAdmin() &&
                comicInfo.getFeatureStatus() == ApiHelper.FEATURE_STATUS_TYPES.NORMAL &&
                !comicInfo.getIsLiked() && !comicInfo.getIsDisliked()) {
            mLikesCountTV.setText(getString(R.string.empty_str));

        } else {
            updateLikesView(comicInfo.getIsLiked(), comicInfo.getLikesCount() - comicInfo.getDislikesCount());
            updateDislikesView(comicInfo.getIsDisliked(), comicInfo.getDislikesCount());
        }

        /*updateLikesView(comicInfo.getIsLiked(), comicInfo.getLikesCount() - comicInfo.getDislikesCount());
        updateDislikesView(comicInfo.getIsDisliked(), comicInfo.getDislikesCount());*/
        mLikesCountTV.setTextColor(ContextCompat.getColor(getContext(), R.color.white_op87));
        mCommentsCountTV.setTextColor(ContextCompat.getColor(getContext(), R.color.white_op87));
        updateCommentsCountView(comicInfo.getCommentsCount().intValue());

        Boolean comicReportStatus = mComicData.getComicInfo().getIsRemoved();
        if (comicReportStatus != null && comicReportStatus) {
            hideComic();
            return;
        }

        if (comicInfo.getComicType() != ApiHelper.COMIC_TYPES.IMAGE &&
                comicInfo.getCaption() != null &&
                !comicInfo.getCaption().isEmpty()) {

            mCaptionTV.setText(comicInfo.getCaption());
            mCaptionTV.setVisibility(View.VISIBLE);

        } else {
            mCaptionTV.setVisibility(View.GONE);
        }

        switch (comicInfo.getComicType()) {
            case ApiHelper.COMIC_TYPES.VIDEO_FACEBOOK:
                mVideoThumbnailIV.setAspectRatio(comicInfo.getAspectRatio());
                if (!mFBVideoSourceGrabber.isVideoSource(comicInfo.getComicUrl())) {
                    mFBVideoSourceGrabber.setHandler(this);
                    mFBVideoSourceGrabber.setupFBVideoGrabberWebView(getContext(), mFBWebView,
                            comicInfo.getComicUrl());
                    break;
                }

            case ApiHelper.COMIC_TYPES.VIDEO_INSTAGRAM:
                mVideoThumbnailIV.setAspectRatio(comicInfo.getAspectRatio());
                mVideoViewLayout.setVisibility(View.VISIBLE);
                initVideoView(comicInfo.getComicUrl());
                break;

            case ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE:
                mContentImageIV.setAspectRatio(comicInfo.getAspectRatio());
                mYoutubeIB.setVisibility(View.VISIBLE);
                mImageContentLayout.setVisibility(View.VISIBLE);
                updateComicPhoto();
                break;

            default: // Image or Gif
                mContentImageIV.setAspectRatio(comicInfo.getAspectRatio());
                mImageContentLayout.setVisibility(View.VISIBLE);
                mImageContentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if(mImageContentLayout.getVisibility() == View.VISIBLE)
                                {
                                    ImageUtils.loadImageUrlIntoView(getActivity(),
                                            mContentImageIV,
                                            comicInfo.getComicUrl(),
                                            false,
                                            -1);
                                }
                            }
                        });
                /**/
                break;
        }
    }

    private boolean getIsOwner() {
        return mPresenter.getIsOwner(mComicData.getPosterInfo().getUserId());
    }

    public void updatePosterViews(ComicResponse.ComicPosterInfo posterInfo) {
        mPosterNameTV.setText(posterInfo.getName());
        ImageUtils.loadImageUrlIntoView(getActivity(),
                mPosterPhotoIV,
                posterInfo.getThumbnailUrl(),
                true,
                R.drawable.im_default);
    }

    private void setupCommentsPanelLayout() {
        mCommentsPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // Timber.d("Comments slide offset = " + String.valueOf(slideOffset));
            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                // Timber.d("Comments state changed from : " + previousState + " ...to : " + newState);

                if (mCommentsPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mCommentsPanelWasCollapsed = false;

                    mPresenter.getNewComments(mComicData.getComicInfo().getComicId());

                    pauseComic();

                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).updateAppbarToCommentsPage(
                                mComicData.getComicInfo().getCommentsCount());
                    }

                } else if (!mCommentsPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mCommentsPanelWasCollapsed = true;
                    mCommentsAdapter.clearAllComments();
                    mPresenter.onCommentsRemoved();

                    resumeComic();
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).updateAppbarToNormal();

                    } else if (getActivity() instanceof ComicViewActivity) {
                        ((ComicViewActivity) getActivity()).updateAppbarToNormal();
                    }
                }
            }
        });
        mCommentsPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommentsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }
    private void setupPosterSummaryPanelLayout() {
        mPosterSummaryPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Timber.d("Summary slide offset = " + String.valueOf(slideOffset));
            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                Timber.d("Summary state changed from : " + previousState + " ...to : " + newState);

                if (mSummaryPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mSummaryPanelWasCollapsed = false;
                    pauseComic();
                    ((MainActivity) getActivity()).updateAppbarToPosterSummaryPage();

                } else if (!mSummaryPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mSummaryPanelWasCollapsed = true;
                    resumeComic();
                    ((MainActivity) getActivity()).updateAppbarToNormal();
                }
            }
        });
        mPosterSummaryPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosterSummaryPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    private void setupCommentsAdapter() {

        if (mCommentsAdapter != null) {
            mCommentsAdapter.setCallback(new GeneralCommentsAdapter.Callback() {
                @Override
                public void onProfileClicked(long userId) {
                    openProfileActivity(userId);
                }

                @Override
                public void onLikeClicked(long commentId, boolean isPositiveAction) {
                    mPresenter.likeComment(commentId, true, isPositiveAction);
                }

                @Override
                public void onDislikedClicked(long commentId, boolean isPositiveAction) {
                    mPresenter.likeComment(commentId, false, isPositiveAction);
                }

                @Override
                public void onCommentOptionsClicked(final int commentPosition,
                                                    final long commentId, boolean isOwner) {

                    final String[] commentOptions;

                    if (mPresenter.getIsAdmin()) {
                        commentOptions = getResources().getStringArray(R.array.options_admin);

                    } else if (isOwner) {
                        commentOptions = getResources().getStringArray(R.array.comment_options_owner);

                    } else {
                        commentOptions = getResources().getStringArray(R.array.comment_options_general);
                    }

                    GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(commentOptions);
                    dialog.setHandler(
                            new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                                @Override
                                public void onItemSelected(int position) {
                                    switch (commentOptions[position]) {
                                        case AppConstants.OPTION_REPORT:
                                            mPresenter.reportComment(commentId, commentPosition);
                                            break;

                                        case AppConstants.OPTION_DELETE:
                                            mPresenter.deleteComment(commentId, commentPosition);
                                            break;

                                        case AppConstants.OPTION_BLOCK:
                                            mPresenter.blockComment(commentId, commentPosition);
                                            break;
                                    }
                                }
                            });

                    dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
                }

                @Override
                public void onReplyClicked(ComicComment commentItem) {
                    openRepliesActivity(commentItem);
                }
            });

            mCommentsAdapter.setEmptyView(mEmptyCommentsView);
            mCommentsAdapter.setViewerId(mPresenter.getViewerId());
        }
    }
    private void setupCommentsRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mCommentsRV.setLayoutManager(mLayoutManager);
        mCommentsRV.setAdapter(mCommentsAdapter);

        mCommentsAdapter.addNewItemsOnScrollListener(mCommentsRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {

            @Override
            public void onScrollReachedEnd() {
                mPresenter.getNewComments(mComicData.getComicInfo().getComicId());
            }
        });
    }

    private void openProfileActivity(long userId) {
        startActivity(ProfileActivity.getStartIntent(getActivity(), userId));
    }
    private void openRepliesActivity(ComicComment comment) {
        startActivity(RepliesActivity.getStartIntent(getActivity(), comment));
    }

    // Handling fragment focus change
    public void onFragmentFocusChanged() {
        if (hasFocus()) {
            resumeComic();

        } else {
            pauseComic();
            collapseBottomSlidingPanel();
            collapseTopSlidingPanel();
        }
    }

    public int getFragmentPosition() {
        return mFragmentPos;
    }
    public Long getComicId() {
        return mComicData == null? null : mComicData.getComicInfo().getComicId();
    }

    public boolean collapseAnyExpandedPanels() {
        if (isTopSlidingPanelExpanded()) {
            collapseTopSlidingPanel();
            return true;

        } else if (isBottomSlidingPanelExpanded()) {
            collapseBottomSlidingPanel();
            return true;
        }

        return false;
    }
    private boolean isTopSlidingPanelExpanded() {
        return mPosterSummaryPanelLayout == null ||
                mPosterSummaryPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }
    private boolean isBottomSlidingPanelExpanded() {
        return mCommentsPanelLayout == null ||
                mCommentsPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }
    private void collapseTopSlidingPanel() {
        if (mPosterSummaryPanelLayout != null) {
            mPosterSummaryPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
    private void collapseBottomSlidingPanel() {
        if (mCommentsPanelLayout != null) {
            mCommentsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
    public void expandCommentsPanel() {
        mCommentsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @OnClick({R.id.iv_comic_posterPhoto, R.id.tv_comic_posterName})
    public void onPosterInfoClicked() {
        openProfileActivity(mComicData.getPosterInfo().getUserId());
    }

    @OnClick({R.id.ib_comic_like, R.id.tv_comic_likesCount})
    public void onLikeClicked() {
        // if (mPresenter.getIsLoggedIn()) {
        if (!mComicData.getComicInfo().getIsLiked()) {
            makeLikeAnimation(true);
        }
        // }

        mPresenter.likeComic(
                mComicData.getComicInfo().getComicId(),
                true);
    }

    @OnClick({R.id.ib_comic_dislike, R.id.tv_comic_dislikesCount})
    public void onDislikeClicked() {
        if (!mComicData.getComicInfo().getIsDisliked()) {
            makeLikeAnimation(false);
        }

        mPresenter.likeComic(
                mComicData.getComicInfo().getComicId(),
                false);
    }

    @OnClick(R.id.layout_comic_comments)
    public void onCommentsClicked() {
        mCommentsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @OnClick(R.id.layout_comic_share)
    public void onShareClicked() {

        if (MarshmallowPermissions.checkPermissionForStoringData(getActivity())) {
            mPresenter.shareComic(getActivity(),
                    mComicData.getComicInfo().getComicId(),
                    mComicData.getComicInfo().getThumbnailUrl(),
                    mComicData.getComicInfo().getComicType());

        } else {
            MarshmallowPermissions.requestFragmentPermissionForStoringData(this,
                    MarshmallowPermissions.STORAGE_PERMISSION_CODE);
        }
    }

    @OnClick(R.id.ib_comic_youtube)
    public void onYoutubeClicked() {
        startActivity(YoutubeActivity.getStartIntent(getActivity(), mComicData.getComicInfo().getComicUrl()));
    }

    @OnClick(R.id.ib_comic_options)
    public void onOptionsClicked() {

        final String[] comicOptionChoices;

        if (mPresenter.getIsAdmin()) {
            comicOptionChoices = getResources().getStringArray(R.array.options_admin);

        } else if (getIsOwner()) {
            comicOptionChoices = getResources().getStringArray(R.array.comic_options_owner);

        } else {
            comicOptionChoices = getResources().getStringArray(R.array.comic_options_general);
        }

        final GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(comicOptionChoices);
        dialog.setHandler(
                new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                    @Override
                    public void onItemSelected(int position) {

                        switch (comicOptionChoices[position]) {
                            case AppConstants.OPTION_REPORT:
                                showReportTypesBottomSheet();
                                break;

                            case AppConstants.OPTION_DELETE:
                                mPresenter.deleteComic(mComicData.getComicInfo().getComicId());
                                break;

                            case AppConstants.OPTION_BLOCK:
                                mPresenter.blockComic(mComicData.getComicInfo().getComicId());
                                break;

                            case AppConstants.OPTION_HIDE:
                                mPresenter.hideComic(mComicData.getComicInfo().getComicId());
                                break;
                        }
                    }
                });

        dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
    }

    private void showReportTypesBottomSheet() {

        long comicId = mComicData.getComicInfo().getComicId();

        final ReportTypesBSDialog reportTypesDialog = ReportTypesBSDialog.newInstance(comicId);
        reportTypesDialog.setTargetFragment(this, 0);

        reportTypesDialog.show(getActivity().getSupportFragmentManager(), reportTypesDialog.getTag());
    }

    @Override
    public void onReportSuccessful() {
        mComicData.getComicInfo().setIsRemoved(true);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).addNewReportedComic(mComicData.getComicInfo().getComicId());
        }
        hideComic();
    }

    @OnClick(R.id.ib_comments_commentAdd)
    public void onAddCommentClicked() {
        String comment = mNewCommentET.getText().toString();
        if (comment.isEmpty()) {
            mNewCommentET.setError(getString(R.string.comment_error_empty));
            return;
        }

        mPresenter.addComment(mComicData.getComicInfo().getComicId(), comment);
        hideKeyboard();
    }

    @OnClick(R.id.ib_comic_featureComic)
    public void onFeatureComicClicked() {
        mPresenter.featureComic(mComicData.getComicInfo().getComicId());
    }

    @OnClick(R.id.btn_pendingReview_allow)
    public void onPendingReviewAllowClicked() {
        mPresenter.markComicAsReviewed(mComicData.getComicInfo().getComicId(),
                ApiHelper.CONTENT_REVIEW_ACTIONS.ALLOW,
                null);
    }

    @OnClick(R.id.btn_pendingReview_block)
    public void onPendingReviewBlockClicked() {
        GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(mUserReviewActions);
        dialog.setHandler(
                new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                    @Override
                    public void onItemSelected(int position) {
                        String selectedAction = mUserReviewActions[position];
                        int userReviewAction;

                        if (selectedAction.equals(getString(R.string.nothing))) {
                            userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.NOTHING;

                        } else if (selectedAction.equals(getString(R.string.warn))) {
                            userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.WARNING;

                        } else if (selectedAction.equals(getString(R.string.block_week))) {
                            userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.BLOCK_WEEK;

                        } else if (selectedAction.equals(getString(R.string.block_month))) {
                            userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.BLOCK_MONTH;

                        } else {
                            userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.BLOCK;
                        }

                        mPresenter.markComicAsReviewed(mComicData.getComicInfo().getComicId(),
                                ApiHelper.CONTENT_REVIEW_ACTIONS.BLOCK,
                                userReviewAction);
                    }
                });

        dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
    }


    private void updateCommentsCountView(Integer count) {
        mCommentsCountTV.setText(CommonUtils.getShortNumberRepresentation(count));
    }
    private void hideComic() {
        pauseComic();
        mContentLayout.setVisibility(View.GONE);
        mComicRemovedTV.setVisibility(View.VISIBLE);

        mComicRemovedTV.setText(getString(R.string.comic_removed_msg));
    }


    @Override
    public void updateComicLikeView(boolean hasLiked) {
        ComicResponse.ComicInfo comicInfo = mComicData.getComicInfo();
        Integer updatedLikesCount = mComicData.getComicInfo().getLikesCount();

        if (hasLiked) {
            comicInfo.setIsLiked(true);
            updatedLikesCount += 1;

            if (comicInfo.getIsDisliked()) {
                comicInfo.setIsDisliked(false);
                comicInfo.setDislikesCount(comicInfo.getDislikesCount() - 1);
            }

        } else {
            comicInfo.setIsLiked(false);
            updatedLikesCount -= 1;
        }

        comicInfo.setLikesCount(updatedLikesCount);
        updateLikesView(hasLiked, comicInfo.getLikesCount() - comicInfo.getDislikesCount());
        updateDislikesView(comicInfo.getIsDisliked(), comicInfo.getDislikesCount());
    }

    @Override
    public void updateComicDislikeView(boolean hasDisliked) {
        ComicResponse.ComicInfo comicInfo = mComicData.getComicInfo();
        Integer updatedDislikesCount = mComicData.getComicInfo().getDislikesCount();

        if (hasDisliked) {
            comicInfo.setIsDisliked(true);
            updatedDislikesCount += 1;

            if (comicInfo.getIsLiked()) {
                comicInfo.setIsLiked(false);
                comicInfo.setLikesCount(comicInfo.getLikesCount() - 1);
            }

        } else {
            comicInfo.setIsDisliked(false);
            updatedDislikesCount -= 1;
        }

        comicInfo.setDislikesCount(updatedDislikesCount);
        updateDislikesView(hasDisliked, updatedDislikesCount);
        updateLikesView(comicInfo.getIsLiked(), comicInfo.getLikesCount() - comicInfo.getDislikesCount());
    }

    @Override
    public void onComicLikeResult(boolean likeAction, boolean isSuccessful) {
        if (likeAction) {
            updateComicLikeView(!mComicData.getComicInfo().getIsLiked());

        } else {
            updateComicDislikeView(!mComicData.getComicInfo().getIsDisliked());
        }

        if (!isSuccessful) {
            if (likeAction) {
                showMessage(getString(R.string.comic_like_failed_msg));

            } else {
                showMessage(getString(R.string.comic_dislike_failed_msg));
            }
        }
    }

    private void updateLikesView(Boolean isLiked, int likesCount) {
        mLikesCountTV.setText(CommonUtils.getShortNumberRepresentation(likesCount));

        if (isLiked != null && isLiked) {
            //setViewColor(mLikesCountTV, R.color.colorAccent);
            mLikeIB.setImageResource(R.drawable.ic_like_active);

        } else {
            //setViewColor(mLikesCountTV, R.color.white_op87);
            mLikeIB.setImageResource(R.drawable.ic_like);
        }
    }
    private void updateDislikesView(Boolean isDisliked, int dislikesCount) {
        mDislikesCountTV.setText(String.valueOf(dislikesCount));

        if (isDisliked != null && isDisliked) {
            // setViewColor(mLikesCountTV, R.color.red);
            mDislikeIB.setImageResource(R.drawable.ic_dislike_active);

        } else {
            // setViewColor(mDislikesCountTV, R.color.white_op87);
            mDislikeIB.setImageResource(R.drawable.ic_dislike);
        }
    }


    @Override
    public void removeComic() {
        mComicData.getComicInfo().setIsRemoved(true);
        hideComic();
    }

    @Override
    public void onComicAddedToFeatured() {
        if (mComicData.getComicInfo().getFeatureStatus() == ApiHelper.FEATURE_STATUS_TYPES.NORMAL) {
            mFeatureComicIB.setImageResource(R.drawable.ic_star_active);
            mComicData.getComicInfo().setFeatureStatus(ApiHelper.FEATURE_STATUS_TYPES.PENDING);

        } else {
            mFeatureComicIB.setImageResource(R.drawable.ic_star_inactive);
            mComicData.getComicInfo().setFeatureStatus(ApiHelper.FEATURE_STATUS_TYPES.NORMAL);
        }
    }

    @Override
    public void addComments(List<ComicComment> comments) {
        if (mCommentsPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mCommentsAdapter.resetIsLoadingNewItems();
            mCommentsAdapter.addComments(comments);
        }
    }

    @Override
    public void updateCommentsCount(int count) {
        mComicData.getComicInfo().setCommentsCount(count);
        updateCommentsCountView(count);
        mCommentsAdapter.setTotalCommentsCount(count);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateAppbarToCommentsPage(count);

        } else if (getActivity() instanceof ComicViewActivity) {
            ((ComicViewActivity) getActivity()).updateAppbarToCommentsPage(count);
        }
    }

    @Override
    public void updateAfterCommentAdded() {
        mCommentsAdapter.clearAllComments();
        mPresenter.getNewComments(mComicData.getComicInfo().getComicId());

        mNewCommentET.setText("");
        mNewCommentET.clearFocus();
    }


    @Override
    public void pauseComic() {
        if (mVideoView != null && mVideoView.getVisibility() == View.VISIBLE) {
            try {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
            } catch (IllegalStateException ex) {
                Timber.e(ex.getMessage());
            }

        }
    }

    @Override
    public void resumeComic() {
        if (hasFocus()) {
            if ((mCommentsPanelLayout == null || mCommentsPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) &&
                    (mPosterSummaryPanelLayout == null || mPosterSummaryPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)) {

                if (mVideoView != null && mVideoView.getVisibility() == View.VISIBLE) {
                    try {
                        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                            mMediaPlayer.start();
                        }

                    } catch (IllegalStateException ex) {
                        // initVideoView(mComicData.getComicInfo().getComicUrl());
                    }
                } else if (mImageContentLayout != null && mImageContentLayout.getVisibility() == View.VISIBLE &&
                        mContentImageIV.getDrawable() != null &&
                        ImageUtils.areDrawablesIdentical(mContentImageIV.getDrawable(),
                                ContextCompat.getDrawable(getActivity(), R.drawable.im_default))) {
                    // The case when image failed to load
                    updateComicPhoto();
                }
            }
        }
    }

    private void updateComicPhoto() {
        int comicType = mComicData.getComicInfo().getComicType();
        String photoUrl = "";

        if (comicType == ApiHelper.COMIC_TYPES.IMAGE || comicType == ApiHelper.COMIC_TYPES.GIF) {
            photoUrl = mComicData.getComicInfo().getComicUrl();

        } else {
            photoUrl = mComicData.getComicInfo().getThumbnailUrl();
        }

        ImageUtils.loadImageUrlIntoView(getActivity(),
                mContentImageIV,
                photoUrl,
                false,
                -1);
    }


    @Override
    public void handleShareVideoUrl(String url) {
        ShareLinkDialog dialog = ShareLinkDialog.newInstance(url);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
    }

    @Override
    public void handleShareComicImage() {
        showComicShareTypesBottomSheet();
    }

    @Override
    public void handleShareComicGif() {
        showComicShareTypesBottomSheet();
    }

    @Override
    public void removeComment(int adapterPosition) {
        mCommentsAdapter.removeComment(adapterPosition);
    }

    private void showComicShareTypesBottomSheet() {

        final BottomSheetListDialog bottomSheetListFragment = BottomSheetListDialog.newInstance(
                getResources().getStringArray(R.array.comic_share_type));

        bottomSheetListFragment.setTargetFragment(this, 0);

        bottomSheetListFragment.show(getActivity().getSupportFragmentManager(),
                bottomSheetListFragment.getTag());
    }

    private void createImageDownloadTask() {
        showLoadingDialog();
        mImageDownloadTask = new ViewImageGeneration(getActivity(),
                new ViewImageGeneration.ViewImageGenerationHandler() {
                    @Override
                    public void onImageGenerated(Bitmap bitmap) {
                        mImageDownloadTask = null;
                        hideLoadingDialog();

                        if (bitmap == null) {
                            MessageDialog dialog = MessageDialog.newInstance(getString(R.string.free_some_space));
                            dialog.setTargetFragment(SingleComicFragment.this, 0);
                            dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());

                        } else {
                            showMessage("Image saved to your gallery");
                        }
                    }
                });

        mImageDownloadTask.startNewTask(mContentImageIV, false);
    }
    private void createImageShareTask() {
        showLoadingDialog();
        mImageDownloadTask = new ViewImageGeneration(getActivity(),
                new ViewImageGeneration.ViewImageGenerationHandler() {
                    @Override
                    public void onImageGenerated(Bitmap bitmap) {
                        mImageDownloadTask = null;
                        hideLoadingDialog();

                        if (bitmap == null) {
                            MessageDialog dialog = MessageDialog.newInstance(getString(R.string.free_some_space));
                            dialog.setTargetFragment(SingleComicFragment.this, 0);
                            dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());

                        } else {
                            SharePhotoDialog dialog = SharePhotoDialog.newInstance(
                                    ImageUtils.getCachedImageUri(getActivity()), false);
                            dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
                        }
                    }
                });

        mImageDownloadTask.startNewTask(mContentImageIV, true);
    }

    private void createGifDownloadTask() {
        showLoadingDialog();
        mGifDownloadTask = new GifDownload(getActivity(), new GifDownload.GifDownloadHandler() {
            @Override
            public void onGifDownloaded(File gifFile) {
                mGifDownloadTask = null;
                hideLoadingDialog();
            }
        });

        mGifDownloadTask.startGifDownloadAsyncTask(mComicData.getComicInfo().getComicUrl(), false);
    }
    private void createGifShareTask() {
        showLoadingDialog();
        mGifDownloadTask = new GifDownload(getActivity(), new GifDownload.GifDownloadHandler() {
            @Override
            public void onGifDownloaded(File gifFile) {
                mGifDownloadTask = null;
                hideLoadingDialog();

                SharePhotoDialog.newInstance(ImageUtils.getCachedGifUri(getActivity()),
                        true)
                        .show(getActivity().getSupportFragmentManager(), getTag());
            }
        });

        mGifDownloadTask.startGifDownloadAsyncTask(mComicData.getComicInfo().getComicUrl(), true);
    }

    private void initVideoView(String videoUrl) {
        mVideoView.setVideoPath(videoUrl);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Timber.d("Video prepared");
                mMediaPlayer = mp;
                mMediaPlayer.setLooping(true);
                resumeComic();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showMessage("Video failed to load");
                return true;
            }
        });
    }

    private void hideContentLoading() {
        if (mContentLoadingProgressbar.getVisibility() == View.VISIBLE) {
            mContentLoadingProgressbar.setVisibility(View.GONE);
        }
    }
    private void showContentLoading() {
        if (mContentLoadingProgressbar.getVisibility() != View.VISIBLE) {
            mContentLoadingProgressbar.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBottomDialogItemSelected(int position) {
        Integer comicType = mComicData.getComicInfo().getComicType();

        switch (position) {
            case COMIC_SHARE_TYPE_SAVE_POS:
                if (comicType == ApiHelper.COMIC_TYPES.IMAGE) {
                    createImageDownloadTask();

                } else if (comicType == ApiHelper.COMIC_TYPES.GIF) {
                    createGifDownloadTask();
                }

                break;

            case COMIC_SHARE_TYPE_SHARE_POS:
                if (comicType == ApiHelper.COMIC_TYPES.IMAGE) {
                    createImageShareTask();

                } else if (comicType == ApiHelper.COMIC_TYPES.GIF) {
                    createGifShareTask();
                }

                break;
        }
    }

    @Override
    public void onErrorMsgOkClicked() {
    }

    @Override
    public void onFBVideoSourceUrlReady(final String sourceUrl) {
        mComicData.getComicInfo().setComicUrl(sourceUrl);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVideoViewLayout.setVisibility(View.VISIBLE);
                initVideoView(sourceUrl);
            }
        });

        mPresenter.updateFacebookComicVidSourceUrl(mComicData.getComicInfo().getComicId(), sourceUrl);
    }

    @Override
    public void onDestroyView() {
        /*BitmapDrawable bmpDrawable = (BitmapDrawable) mContentImageIV.getDrawable();
        if (bmpDrawable != null && bmpDrawable.getBitmap() != null && !bmpDrawable.getBitmap().isRecycled()) {
            Bitmap bitmap = bmpDrawable.getBitmap();
            bitmap.recycle();
            bitmap = null;
            mContentImageIV.setImageBitmap(null);
        }*/

        ImageUtils.clearImageFromView(getActivity().getApplicationContext(), mContentImageIV);
        ImageUtils.clearImageFromView(getActivity().getApplicationContext(), mPosterPhotoIV);

        mPresenter.onDetach();
        super.onDestroyView();
    }
}
