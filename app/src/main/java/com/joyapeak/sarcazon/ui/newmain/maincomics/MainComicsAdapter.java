package com.joyapeak.sarcazon.ui.newmain.maincomics;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.joyapeak.sarcazon.GlideApp;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse.SingleComic;
import com.joyapeak.sarcazon.helper.FBVideoSourceGrabber;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.ui.custom.CustomHeightImageView;
import com.joyapeak.sarcazon.ui.custom.DoubleClickListener;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import timber.log.Timber;

/**
 * Created by test on 10/14/2018.
 */

public class MainComicsAdapter extends InfiniteReloadableAdapter {

    private List<SingleComic> mItemsList;
    private View mEmptyView;

    private final int ITEM_VIEW_TYPE_IMAGE = 0;
    private final int ITEM_VIEW_TYPE_VIDEO = 1;

    private Integer mUserControlFlag;
    private Long mViewerId;

    private Callback mCallback;

    private Container mContainer;

    public interface Callback {
        void onProfileClicked(long userId);
        void onCommentsClicked(long comicId);
        void onLikeClicked(long comicId, BaseComicViewHolder holder, boolean isFromDoubleClick);
        void onDislikeClicked(long comicId, BaseComicViewHolder holder);
        void onComicOptionsClicked(long comicId, boolean isAdmin, boolean isOwner, BaseComicViewHolder holder);
        void onFBVideoUrlUpdated(long comicId, String videoUrl);
        void onYoutubeVideoClicked(String videoUrl);
        void onShareClicked(ComicResponse.ComicInfo comicInfo, View viewToShare);
        void onItemClicked(int position);
        void onFeatureClicked(long comicId, BaseComicViewHolder holder);
    }


    public MainComicsAdapter(List<SingleComic> items) {
        mItemsList = items;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContainer == null) {
            mContainer = (Container) parent;
        }

        switch (viewType) {
            case ITEM_VIEW_TYPE_VIDEO:
                return new VideoViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_comic_video, parent, false));

            default:
                return new ImageViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_comic_image, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mItemsList == null? 0 : mItemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemsList != null) {
            int comicType = mItemsList.get(position).getComicInfo().getComicType();

            if (comicType == ApiHelper.COMIC_TYPES.IMAGE ||
                    comicType == ApiHelper.COMIC_TYPES.GIF ||
                    comicType == ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE) {
                return ITEM_VIEW_TYPE_IMAGE;

            } else {
                return ITEM_VIEW_TYPE_VIDEO;
            }
        }

        return super.getItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((BaseComicViewHolder) holder).hideAnimationView();
    }

    @Override
    public long getItemId(int position) {
        if (mItemsList != null) {
            return mItemsList.get(position).getComicInfo().getComicId();
        }

        return position;
    }

    public void setTotalComicsCount(int count) {
        mTotalItemsCount = count;
    }

    public void setViewerId(long viewerId) {
        mViewerId = viewerId;
    }
    public void setUserControlFlag(int userControlFlag) {
        mUserControlFlag = userControlFlag;
    }


    private boolean getIsOwner(long posterId) {
        return mViewerId != null && mViewerId == posterId;
    }
    private boolean getIsAdmin() {
        return mUserControlFlag != null && mUserControlFlag == ApiHelper.CONTROL_FLAG_TYPES.ADMIN;
    }

    private void updateEmptyViewVisibility() {
        if (mEmptyView != null) {
            if (getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);

            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }
    public void removeItem(int position) {
        mItemsList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearAll() {
        int itemsCount = getItemCount();
        mItemsList = new ArrayList<>();
        notifyItemRangeRemoved(0, itemsCount);
    }


    public void updateComicLikeStatus(MainComicsAdapter.BaseComicViewHolder holder) {
        ComicResponse.ComicInfo comicInfo = mItemsList.get(holder.getAdapterPosition()).getComicInfo();
        Integer updatedLikesCount = comicInfo.getLikesCount();

        if (!comicInfo.getIsLiked()) {
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
        holder.updateLikesView(comicInfo.getIsLiked(), comicInfo.getLikesCount());
        holder.updateDislikesView(comicInfo.getIsDisliked(), comicInfo.getDislikesCount());
    }
    public void updateComicDislikeStatus(MainComicsAdapter.BaseComicViewHolder holder) {
        ComicResponse.ComicInfo comicInfo = mItemsList.get(holder.getAdapterPosition()).getComicInfo();
        Integer updatedDislikesCount = comicInfo.getDislikesCount();

        if (!comicInfo.getIsDisliked()) {
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
        holder.updateDislikesView(comicInfo.getIsDisliked(), updatedDislikesCount);
        holder.updateLikesView(comicInfo.getIsLiked(), comicInfo.getLikesCount());
    }

    public void updateComicFeatureStatus(MainComicsAdapter.BaseComicViewHolder holder) {
        ComicResponse.ComicInfo comicInfo = mItemsList.get(holder.getAdapterPosition()).getComicInfo();

        if (comicInfo.getFeatureStatus() == ApiHelper.FEATURE_STATUS_TYPES.NORMAL) {
            holder.mFeatureComicIB.setImageResource(R.drawable.ic_star_active);
            comicInfo.setFeatureStatus(ApiHelper.FEATURE_STATUS_TYPES.PENDING);

        } else {
            holder.mFeatureComicIB.setImageResource(R.drawable.ic_star_inactive);
            comicInfo.setFeatureStatus(ApiHelper.FEATURE_STATUS_TYPES.NORMAL);
        }
    }

    public void addComics(List<SingleComic> comics) {
        int startPosition = getItemCount();
        mItemsList.addAll(comics);
        notifyItemRangeInserted(startPosition, mItemsList.size());

        updateEmptyViewVisibility();
    }


    public class ImageViewHolder extends BaseComicViewHolder {

        @BindView(R.id.iv_comic_contentImage)
        CustomHeightImageView mContentImageIV;

        @BindView(R.id.tv_comic_caption)
        TextView mCaptionTV;

        @BindView(R.id.ib_comic_youtube)
        ImageButton mYoutubeIB;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            super.clear();
            mContentImageIV.setImageDrawable(null);
            mCaptionTV.setText("");

            mYoutubeIB.setVisibility(View.GONE);
            mCaptionTV.setVisibility(View.GONE);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            SingleComic singleComic = mItemsList.get(getAdapterPosition());
            setupComicViews(singleComic);
        }

        @Override
        protected void setupComicViews(final SingleComic singleComic) {
            super.setupComicViews(singleComic);

            final ComicResponse.ComicInfo comicInfo = singleComic.getComicInfo();
            switch (comicInfo.getComicType()) {
                case ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE:
                    mYoutubeIB.setVisibility(View.VISIBLE);
                    ImageUtils.loadImageUrlIntoView(mContext,
                            mContentImageIV,
                            comicInfo.getThumbnailUrl(),
                            false,
                            -1);
                    break;

                default: // Image or Gif
                    mYoutubeIB.setVisibility(View.GONE);
                    GlideApp.with(mContext)
                            .load(comicInfo.getComicUrl())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(mContentImageIV);
            }

            mContentImageIV.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                }

                @Override
                public void onDoubleClick(View v) {
                    mCallback.onLikeClicked(mComicId, ImageViewHolder.this, true);
                }
            });
        }

        @Override
        protected void updateComicSize(final Double aspectRatio) {
            mContentImageIV.setAspectRatio(aspectRatio);
        }


        @Override
        protected void updateComicPhoto(ComicResponse.ComicInfo comicInfo) {
            /*int comicType = comicInfo.getComicType();
            String photoUrl = "";

            if (comicType == ApiHelper.COMIC_TYPES.IMAGE || comicType == ApiHelper.COMIC_TYPES.GIF) {
                photoUrl = comicInfo.getComicUrl();

            } else {
                photoUrl = comicInfo.getThumbnailUrl();
            }

            ImageUtils.loadImageUrlIntoView(mContext, mContentImageIV, photoUrl);*/
        }

        @OnClick(R.id.layout_comic_share)
        public void onShareClicked() {
            mCallback.onShareClicked(mItemsList.get(getAdapterPosition()).getComicInfo(), mContentImageIV);
        }

        @OnClick(R.id.ib_comic_youtube)
        public void onYoutubePlayClicked() {
            mCallback.onYoutubeVideoClicked(mItemsList.get(getAdapterPosition()).getComicInfo().getComicUrl());
        }

        @OnClick(R.id.layout_comic_comments)
        public void onCommentsClicked() {
            mCallback.onCommentsClicked(mComicId);
        }
    }

    public class VideoViewHolder extends BaseComicViewHolder implements
            FBVideoSourceGrabber.FBVideoSourceHandler,
            ToroPlayer {

        @BindView(R.id.layout_comic_videoRoot)
        LinearLayout mComicRootLayout;

        @BindView(R.id.layout_comic_videoParent)
        RelativeLayout mVideoRootLayout;

        @BindView(R.id.arflayout_player_aspectRatio)
        AspectRatioFrameLayout mPlayerViewAspectRatioLayout;

        @BindView(R.id.player_comic_contentVideo)
        PlayerView mPlayerView;

        @BindView(R.id.iv_comic_videoThumbnail)
        CustomHeightImageView mVideoThumbnailIV;

        @BindView(R.id.progressbar_comic_loading)
        ProgressBar mLoadingProgressBar;

        @BindView(R.id.ib_comic_videoPlayIcon)
        ImageButton mPlayIconIB;

        @BindView(R.id.webview_comic_fbHack)
        WebView mFBWebView;

        @Nullable
        ExoPlayerViewHelper mPlayerViewHelper;

        @Nullable
        private Uri mMediaUri;

        FBVideoSourceGrabber mFBVideoSourceGrabber;


        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            super.clear();

            mVideoThumbnailIV.setImageDrawable(null);
            mPlayerViewHelper = null;
            mMediaUri = null;
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            mVideoThumbnailIV.setVisibility(View.VISIBLE);
            mPlayIconIB.setVisibility(View.VISIBLE);

            SingleComic singleComic = mItemsList.get(getAdapterPosition());
            setupComicViews(singleComic);
        }

        @Override
        protected void setupComicViews(final SingleComic singleComic) {
            super.setupComicViews(singleComic);

            final ComicResponse.ComicInfo comicInfo = singleComic.getComicInfo();

            switch (comicInfo.getComicType()) {
                case ApiHelper.COMIC_TYPES.VIDEO_FACEBOOK:
                    if (mFBVideoSourceGrabber == null) {
                        mFBVideoSourceGrabber = new FBVideoSourceGrabber();
                    }

                    if (!FBVideoSourceGrabber.isVideoSource(comicInfo.getComicUrl())) {
                        updateComicPhoto(comicInfo);
                        mFBVideoSourceGrabber.setHandler(this);
                        mFBVideoSourceGrabber.setupFBVideoGrabberWebView(mContext, mFBWebView,
                                comicInfo.getComicUrl());
                        break;
                    }

                case ApiHelper.COMIC_TYPES.VIDEO_INSTAGRAM:
                    updateComicPhoto(comicInfo);
                    initPlayerView(comicInfo.getComicUrl());
                    break;
            }


            mVideoRootLayout.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                }

                @Override
                public void onDoubleClick(View v) {
                    mCallback.onLikeClicked(mComicId, VideoViewHolder.this, true);
                }
            });
        }

        @Override
        protected void updateComicSize(Double aspectRatio) {
            if (aspectRatio == null || aspectRatio == 0f) {
                mPlayerViewAspectRatioLayout.setAspectRatio(AppConstants.DEFAULT_VIDEO_ASPECT_RATIO);

            } else {
                aspectRatio = aspectRatio > AppConstants.MIN_VIDEO_ASPECT_RATIO ?
                        aspectRatio : AppConstants.MIN_VIDEO_ASPECT_RATIO;

                if (aspectRatio < AppConstants.MIN_VIDEO_ASPECT_RATIO) {
                    mPlayerViewAspectRatioLayout.setAspectRatio(aspectRatio.floatValue());

                } else {
                    mPlayerViewAspectRatioLayout.setAspectRatio(aspectRatio.floatValue());
                }
            }
        }

        @Override
        protected void updateComicPhoto(ComicResponse.ComicInfo comicInfo) {
            String photoUrl = comicInfo.getThumbnailUrl();
            GlideApp.with(mContext)
                    .load(photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mVideoThumbnailIV);
        }

        private void initPlayerView(String comicUrl) {
            mMediaUri = Uri.parse(comicUrl);
            if (mPlayerViewHelper == null) {
                initialize(mContainer, getCurrentPlaybackInfo());
            }
        }


        @Override
        public void onFBVideoSourceUrlReady(final String sourceUrl) {
            final ComicResponse.ComicInfo comicInfo = mItemsList.get(getAdapterPosition()).getComicInfo();
            comicInfo.setComicUrl(sourceUrl);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    initPlayerView(sourceUrl);
                    mCallback.onFBVideoUrlUpdated(comicInfo.getComicId(), sourceUrl);
                }
            });
        }

        // Toro Player
        @NonNull
        @Override
        public View getPlayerView() {
            return mPlayerView;
        }

        @NonNull
        @Override
        public PlaybackInfo getCurrentPlaybackInfo() {
            return mPlayerViewHelper != null ? mPlayerViewHelper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        @Override
        public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
            if (mMediaUri == null)
                return;

            if (mPlayerViewHelper == null) {
                mPlayerViewHelper = new ExoPlayerViewHelper(this, mMediaUri);

                mPlayerViewHelper.addPlayerEventListener(new EventListener() {
                    @Override
                    public void onFirstFrameRendered() {
                    }

                    @Override
                    public void onBuffering() {
                        mLoadingProgressBar.setVisibility(View.VISIBLE);
                        mPlayIconIB.setVisibility(View.GONE);
                        mVideoThumbnailIV.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onPlaying() {
                        mVideoThumbnailIV.setVisibility(View.INVISIBLE);
                        mLoadingProgressBar.setVisibility(View.GONE);
                        mPlayIconIB.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPaused() {
                        mPlayIconIB.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleted() {
                        mVideoThumbnailIV.setVisibility(View.VISIBLE);
                        mPlayIconIB.setVisibility(View.VISIBLE);
                    }
                });

                mPlayerViewHelper.initialize(container, playbackInfo);
            }
        }

        @Override
        public void play() {
            if (mPlayerViewHelper != null) {
                mPlayerViewHelper.play();
            }
        }

        @Override
        public void pause() {
            if (mPlayerViewHelper != null) {
                mPlayerViewHelper.pause();
            }
        }

        @Override
        public boolean isPlaying() {
            return mPlayerViewHelper != null && mPlayerViewHelper.isPlaying();
        }

        @Override
        public void release() {
            if (mPlayerViewHelper != null) {
                mPlayerViewHelper.release();
                mPlayerViewHelper = null;
            }

            mVideoThumbnailIV.setVisibility(View.VISIBLE);
            mPlayIconIB.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean wantsToPlay() {
            return (mPlayerViewHelper != null &&
                    (ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85));
        }

        @Override
        public int getPlayerOrder() {
            return getAdapterPosition();
        }


        @OnClick(R.id.ib_comic_videoPlayIcon)
        public void onPlayClicked() {
            if (mPlayerView != null && mPlayerView.getPlayer() != null) {
                mPlayerView.getPlayer().seekTo(0);
                play();
            }
        }

        @OnClick(R.id.layout_comic_share)
        public void onShareClicked() {
            mCallback.onShareClicked(mItemsList.get(getAdapterPosition()).getComicInfo(), null);
        }

        @OnClick({R.id.layout_comic_comments, R.id.tv_comic_commentsCount})
        public void onCommentsClicked() {
            pause();
            mCallback.onCommentsClicked(mComicId);
        }
    }

    abstract class BaseComicViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_comic_posterPhoto)
        ImageView mPosterPhotoIV;

        @BindView(R.id.tv_comic_posterName)
        TextView mPosterNameTV;

        @BindView(R.id.tv_comic_date)
        TextView mComicDateTV;

        @BindView(R.id.tv_comic_caption)
        TextView mCaptionTV;

        @BindView(R.id.ib_comic_featureComic)
        ImageButton mFeatureComicIB;

        @BindView(R.id.ib_comic_like)
        ImageButton mLikeIB;

        @BindView(R.id.tv_comic_likesCount)
        TextView mLikesCountTV;

        @BindView(R.id.layout_comic_dislike)
        View mDislikeLayout;

        @BindView(R.id.ib_comic_dislike)
        ImageView mDislikeIB;

        @BindView(R.id.tv_comic_dislikesCount)
        TextView mDislikesCountTV;

        @BindView(R.id.tv_comic_commentsCount)
        TextView mCommentsCountTV;

        @BindView(R.id.iv_comic_likeImage)
        ImageView mAnimationLikeImageIV;

        @BindView(R.id.layout_comic_likeImage)
        View mAnimationLikeImageLayout;


        protected Context mContext;

        protected long mPosterId;
        protected long mComicId;


        public BaseComicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        @Override
        protected void clear() {
            mPosterPhotoIV.setImageDrawable(null);
            mPosterNameTV.setText("");
            mComicDateTV.setText("");
            mCaptionTV.setText("");
            mLikesCountTV.setText("");
            mCommentsCountTV.setText("");

            mLikeIB.setImageResource(R.drawable.ic_like);
            mDislikeIB.setImageResource(R.drawable.ic_dislike);

            mAnimationLikeImageLayout.setVisibility(View.INVISIBLE);
            mFeatureComicIB.setVisibility(View.GONE);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            SingleComic singleComic = mItemsList.get(getAdapterPosition());
            mPosterId = singleComic.getPosterInfo().getUserId();
            mComicId = singleComic.getComicInfo().getComicId();
        }

        protected void setupComicViews(final SingleComic singleComic) {
            final ComicResponse.ComicInfo comicInfo = singleComic.getComicInfo();
            final ComicResponse.ComicPosterInfo posterInfo = singleComic.getPosterInfo();

            // Setting comic size
            Timber.d("Position = " + getAdapterPosition() + " ---- Aspect Ratio = " + comicInfo.getAspectRatio());
            updateComicSize(comicInfo.getAspectRatio());

            mPosterNameTV.setText(posterInfo.getName());
            mComicDateTV.setText(CommonUtils.getFriendlyShortDateString(1544194196));

            ImageUtils.loadImageUrlIntoView(mContext,
                    mPosterPhotoIV,
                    posterInfo.getThumbnailUrl(),
                    true,
                    R.drawable.im_default);

            // Showing feature button if user is admin
            // Showing dislike btn if post is not featured
            mFeatureComicIB.setVisibility(View.GONE);
            mDislikeLayout.setVisibility(View.VISIBLE);

            Integer featureStatus = comicInfo.getFeatureStatus();
            if (featureStatus != null) {
                if (featureStatus == ApiHelper.FEATURE_STATUS_TYPES.FEATURED) {
                    mDislikeLayout.setVisibility(View.GONE);

                } else if (getIsAdmin()) {
                    mFeatureComicIB.setVisibility(View.VISIBLE);
                    if (featureStatus == ApiHelper.FEATURE_STATUS_TYPES.PENDING) {
                        mFeatureComicIB.setImageResource(R.drawable.ic_star_active);

                    } else {
                        mFeatureComicIB.setImageResource(R.drawable.ic_star_inactive);
                    }
                }
            }

            // Hide likes count for new comics if user hasn't posted any action
            if (!getIsOwner(posterInfo.getUserId()) && !getIsAdmin() &&
                    comicInfo.getFeatureStatus() == ApiHelper.FEATURE_STATUS_TYPES.NORMAL &&
                    !comicInfo.getIsLiked() && !comicInfo.getIsDisliked()) {

                mLikesCountTV.setText(mContext.getString(R.string.empty_str));
                mDislikesCountTV.setText(mContext.getString(R.string.empty_str));

            } else {
                updateLikesView(comicInfo.getIsLiked(), comicInfo.getLikesCount());
                updateDislikesView(comicInfo.getIsDisliked(), comicInfo.getDislikesCount());
            }

            updateCommentsCountView(comicInfo.getCommentsCount().intValue());

            // Show caption view only if comic is a video
            // TODO: put if condition to remove repetition in old versions
            if (comicInfo.getCaption() != null && !comicInfo.getCaption().isEmpty()) {
                mCaptionTV.setText(comicInfo.getCaption());
                mCaptionTV.setVisibility(View.VISIBLE);

            } else {
                mCaptionTV.setVisibility(View.GONE);
            }
        }


        abstract protected void updateComicSize(Double aspectRatio);
        abstract protected void updateComicPhoto(ComicResponse.ComicInfo comicInfo);

        public void updateLikesView(Boolean isLiked, int likesCount) {
            mLikesCountTV.setText(CommonUtils.getShortNumberRepresentation(likesCount));

            if (isLiked != null && isLiked) {
                //setViewColor(mLikesCountTV, R.color.colorAccent);
                mLikeIB.setImageResource(R.drawable.ic_like_active);

            } else {
                //setViewColor(mLikesCountTV, R.color.white_op87);
                mLikeIB.setImageResource(R.drawable.ic_like);
            }
        }
        public void updateDislikesView(Boolean isDisliked, int dislikesCount) {
            mDislikesCountTV.setText(CommonUtils.getShortNumberRepresentation(dislikesCount));

            if (isDisliked != null && isDisliked) {
                // setViewColor(mLikesCountTV, R.color.red);
                mDislikeIB.setImageResource(R.drawable.ic_dislike_active);

            } else {
                // setViewColor(mDislikesCountTV, R.color.white_op87);
                mDislikeIB.setImageResource(R.drawable.ic_dislike);
            }
        }

        private void updateCommentsCountView(Integer count) {
            mCommentsCountTV.setText(CommonUtils.getShortNumberRepresentation(count));
        }

        public void makeLikeAnimation(BaseActivity baseActivity, boolean isLike) {
            if (isLike) {
                mAnimationLikeImageIV.setImageResource(R.drawable.ic_like_active);
            } else {
                mAnimationLikeImageIV.setImageResource(R.drawable.ic_dislike_active);
            }

            mAnimationLikeImageLayout.setVisibility(View.VISIBLE);
            int animRes = isLike? R.anim.like_image_anim : R.anim.dislike_image_anim;

            Animation likeImageAnim = baseActivity.addAnimationToView(mAnimationLikeImageLayout, animRes,
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

        public boolean getIsLiked() {
            return mItemsList.get(getAdapterPosition()).getComicInfo().getIsLiked();
        }
        public boolean getIsDisliked() {
            return mItemsList.get(getAdapterPosition()).getComicInfo().getIsDisliked();
        }

        public void hideAnimationView() {
            mAnimationLikeImageLayout.setVisibility(View.INVISIBLE);
        }

        @OnClick(R.id.ib_comic_options)
        public void onComicOptionsClicked() {
            mCallback.onComicOptionsClicked(mComicId, getIsAdmin(), getIsOwner(mPosterId), this);
        }

        @OnClick({R.id.iv_comic_posterPhoto, R.id.tv_comic_posterName})
        public void onProfileClicked() {
            mCallback.onProfileClicked(mPosterId);
        }

        @OnClick(R.id.ib_comic_like)
        public void onComicLikeClicked() {
            mCallback.onLikeClicked(mComicId, this, false);
        }

        @OnClick(R.id.ib_comic_dislike)
        public void onComicDislikeClicked() {
            mCallback.onDislikeClicked(mComicId, this);
        }

        @OnClick(R.id.ib_comic_featureComic)
        public void onFeatureComicClicked(){
            mCallback.onFeatureClicked(mComicId, this);
        }
    }
}
