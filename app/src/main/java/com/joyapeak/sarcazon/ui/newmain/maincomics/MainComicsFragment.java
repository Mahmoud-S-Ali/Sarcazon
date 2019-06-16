package com.joyapeak.sarcazon.ui.newmain.maincomics;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.async.GifDownload;
import com.joyapeak.sarcazon.async.ViewImageGeneration;
import com.joyapeak.sarcazon.data.network.ApiEndPoint;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.bottomsheetlist.BottomSheetListDialog;
import com.joyapeak.sarcazon.ui.comics.ComicsMvpPresenter;
import com.joyapeak.sarcazon.ui.comics.ComicsMvpView;
import com.joyapeak.sarcazon.ui.comments.CommentsActivity;
import com.joyapeak.sarcazon.ui.custom.SlowerPlayerViewContainer;
import com.joyapeak.sarcazon.ui.newmain.NewMainActivity;
import com.joyapeak.sarcazon.ui.popupmessage.MessageDialog;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.ui.report.ReportTypesBSDialog;
import com.joyapeak.sarcazon.ui.share.link.ShareLinkDialog;
import com.joyapeak.sarcazon.ui.share.photo.SharePhotoDialog;
import com.joyapeak.sarcazon.ui.youtube.YoutubeActivity;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.ImageUtils;
import com.joyapeak.sarcazon.utils.MarshmallowPermissions;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by test on 10/14/2018.
 */

public class MainComicsFragment extends BaseFragment implements
        ComicsMvpView,
        BottomSheetListDialog.BottomSheetListHandler,
        ReportTypesBSDialog.ReportTypesDialogHandler,
        ApiHelper.NetworkCallStatusCallback {

    @BindView(R.id.rv_main_comics)
    SlowerPlayerViewContainer mComicsRV;

    @BindView(R.id.pb_comics_loading)
    ProgressBar mComicsProgressBar;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    @Inject
    MainComicsAdapter mComicsAdapter;

    @Inject
    ComicsMvpPresenter<ComicsMvpView> mComicsPresenter;

    private Integer mComicsSource;

    private final static String EXTRA_COMICS_SOURCE = "EXTRA_COMICS_SOURCE";

    private MainComicsAdapter.BaseComicViewHolder mComicHolder;

    private ComicResponse.ComicInfo mComicToShare;
    private View mViewToShare;

    private ViewImageGeneration mImageDownloadTask;
    private GifDownload mGifDownloadTask;

    private final static int COMIC_SHARE_TYPE_SAVE_POS = 0;
    private final static int COMIC_SHARE_TYPE_SHARE_POS = 1;


    public static MainComicsFragment newInstance(int comicsSource) {
        MainComicsFragment mainComicsFragment = new MainComicsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_COMICS_SOURCE, comicsSource);
        mainComicsFragment.setArguments(bundle);
        return mainComicsFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_new_main, container, false);

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            setUnBinder(ButterKnife.bind(this, root));

            mComicsPresenter.onAttach(this);

            mComicsSource = getArguments().getInt(EXTRA_COMICS_SOURCE);

            setUp(root);

            mComicsPresenter.setNetworkCallStatusCallback(this);
            mComicsPresenter.onViewInitialized();
            mComicsPresenter.getNewComics(mComicsSource, mComicsPresenter.getSelectedCategory());
        }

        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MarshmallowPermissions.STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mComicsPresenter.shareComic(getActivity(),
                            mComicToShare.getComicId(),
                            mComicToShare.getThumbnailUrl(),
                            mComicToShare.getComicType());

                } else {
                    showMessage("Permission must be granted");
                    mComicToShare = null;
                    mViewToShare = null;
                }

                break;
        }
    }

    @Override
    protected void setUp(View view) {
        setupComicsAdapter();
        setupComicsRV();
    }

    private void setupComicsAdapter() {
        mComicsAdapter.setCallback(new MainComicsAdapter.Callback() {
            @Override
            public void onProfileClicked(long userId) {
                openProfileActivity(userId);
            }

            @Override
            public void onCommentsClicked(long comicId) {
                openCommentsActivity(comicId);
            }

            @Override
            public void onLikeClicked(long comicId, MainComicsAdapter.BaseComicViewHolder holder, boolean isFromDoubleClick) {
                if (isFromDoubleClick && holder.getIsLiked() && !mComicsPresenter.getIsEngagementUsersEnabled()) {
                    holder.makeLikeAnimation((BaseActivity) getActivity(), true);
                    return;
                }

                if (!isNetworkConnected()) {
                    showMessage(getString(R.string.comic_like_failed_msg));
                    return;
                }

                if (!holder.getIsLiked()) {
                    holder.makeLikeAnimation((BaseActivity) getActivity(), true);
                }

                mComicsAdapter.updateComicLikeStatus(holder);
                mComicsPresenter.likeComic(comicId, true);
            }

            @Override
            public void onDislikeClicked(long comicId, MainComicsAdapter.BaseComicViewHolder holder) {
                if (!isNetworkConnected()) {
                    showMessage(getString(R.string.comic_dislike_failed_msg));
                    return;
                }

                if (!holder.getIsDisliked()) {
                    holder.makeLikeAnimation((BaseActivity) getActivity(), false);
                }

                mComicsAdapter.updateComicDislikeStatus(holder);
                mComicsPresenter.likeComic(comicId, false);
            }

            @Override
            public void onComicOptionsClicked(final long comicId, boolean isAdmin, boolean isOwner,
                                              MainComicsAdapter.BaseComicViewHolder holder) {
                mComicHolder = holder;
                final String[] comicOptionChoices;

                if (isAdmin) {
                    comicOptionChoices = getResources().getStringArray(R.array.options_admin);

                } else if (isOwner) {
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
                                        showReportTypesBottomSheet(comicId);
                                        break;

                                    case AppConstants.OPTION_DELETE:
                                        mComicsPresenter.deleteComic(comicId);
                                        break;

                                    case AppConstants.OPTION_BLOCK:
                                        mComicsPresenter.blockComic(comicId);
                                        break;

                                    case AppConstants.OPTION_HIDE:
                                        mComicsPresenter.hideComic(comicId);
                                        break;
                                }
                            }
                        });

                dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
            }

            @Override
            public void onFBVideoUrlUpdated(long comicId, String videoUrl) {
                mComicsPresenter.updateFacebookComicVidSourceUrl(comicId, videoUrl);
            }

            @Override
            public void onYoutubeVideoClicked(String videoUrl) {
                openYoutubeActivity(videoUrl);
            }

            @Override
            public void onShareClicked(ComicResponse.ComicInfo comicInfo, View viewToShare) {
                mComicToShare = comicInfo;
                mViewToShare = viewToShare;

                if (MarshmallowPermissions.checkPermissionForStoringData(getActivity())) {
                    mComicsPresenter.shareComic(getActivity(),
                            mComicToShare.getComicId(),
                            mComicToShare.getThumbnailUrl(),
                            mComicToShare.getComicType());

                } else {
                    MarshmallowPermissions.requestFragmentPermissionForStoringData(
                            MainComicsFragment.this,
                            MarshmallowPermissions.STORAGE_PERMISSION_CODE);
                }
            }


            @Override
            public void onItemClicked(int position) {
                showMessage(String.valueOf(position));
            }

            @Override
            public void onFeatureClicked(long comicId, MainComicsAdapter.BaseComicViewHolder holder) {
                mComicHolder = holder;
                mComicsPresenter.featureComic(comicId);
            }
        });

        mComicsAdapter.setViewerId(mComicsPresenter.getCurrentUserId());
        mComicsAdapter.setUserControlFlag(mComicsPresenter.getUserControlFlag());
        mComicsAdapter.setTotalComicsCount(Integer.MAX_VALUE);
    }

    private void setupComicsRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mComicsRV.setLayoutManager(mLayoutManager);
        mComicsRV.setAdapter(mComicsAdapter);
        mComicsRV.setHasFixedSize(true);

        // mComicsRV.setCacheManager(CacheManager.DEFAULT);

        mComicsAdapter.addNewItemsOnScrollListener(mComicsRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {

                    @Override
                    public void onScrollReachedEnd() {
                        mComicsPresenter.getNewComics(mComicsSource, mComicsPresenter.getSelectedCategory());
                    }
                });
    }

    public void scrollToTop() {
        if (mLayoutManager != null) {
            mLayoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    @Override
    public void onComicsRetrieved(List<ComicResponse.SingleComic> comics) {
        if (getActivity()instanceof NewMainActivity) {
            ((NewMainActivity) getActivity()).stopRefreshing();
        }

        if (mComicsSource == ApiHelper.COMIC_SOURCES.FEATURED &&
                mComicsAdapter.getItemCount() == 0 &&
                comics.size() > 0) {
            mComicsPresenter.updateLastFeaturedComicId(comics.get(0).getComicInfo().getComicId());
        }

        mComicsAdapter.addComics(comics);
        mComicsAdapter.resetIsLoadingNewItems();
    }

    @Override
    public void onFeaturedComicsReleased() {
    }

    @Override
    public void onComicBlocked(boolean isSuccessful) {
        if (isSuccessful) {
            removeComicFromAdapter();
        }
    }

    @Override
    public void onComicHid(boolean isSuccessful) {
        if (isSuccessful) {
            removeComicFromAdapter();
        }
    }

    @Override
    public void onComicDeleted(boolean isSuccessful) {
        if (isSuccessful) {
            removeComicFromAdapter();
        }
    }

    @Override
    public void onComicAddedToFeatured() {
        mComicsAdapter.updateComicFeatureStatus(mComicHolder);
        mComicHolder = null;
    }

    @Override
    public void onReportSuccessful() {
        removeComicFromAdapter();
    }

    private void removeComicFromAdapter() {
        if (mComicHolder != null) {
            mComicsAdapter.removeItem(mComicHolder.getAdapterPosition());
            mComicHolder = null;
        }
    }

    @Override
    public void handleShareVideoUrl(String url) {
        ShareLinkDialog dialog = ShareLinkDialog.newInstance(url);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
    }

    @Override
    public View getProgressBarView() {
        return mComicsProgressBar;
    }

    @Override
    public void handleShareComicImage() {
        showComicShareTypesBottomSheet();
    }

    @Override
    public void handleShareComicGif() {
        showComicShareTypesBottomSheet();
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
                            dialog.setTargetFragment(MainComicsFragment.this, 0);
                            dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());

                        } else {
                            showMessage("Image saved to your gallery");
                        }
                    }
                });

        mImageDownloadTask.startNewTask(mViewToShare, false);
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
                            dialog.setTargetFragment(MainComicsFragment.this, 0);
                            dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());

                        } else {
                            SharePhotoDialog dialog = SharePhotoDialog.newInstance(
                                    ImageUtils.getCachedImageUri(getActivity()), false);
                            dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());
                        }
                    }
                });

        mImageDownloadTask.startNewTask(mViewToShare, true);
    }

    private void createGifDownloadTask(String comicUrl) {
        showLoadingDialog();
        mGifDownloadTask = new GifDownload(getActivity(), new GifDownload.GifDownloadHandler() {
            @Override
            public void onGifDownloaded(File gifFile) {
                mGifDownloadTask = null;
                hideLoadingDialog();
            }
        });

        mGifDownloadTask.startGifDownloadAsyncTask(comicUrl, false);
    }
    private void createGifShareTask(String comicUrl) {
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

        mGifDownloadTask.startGifDownloadAsyncTask(comicUrl, true);
    }

    private void openProfileActivity(long userId) {
        startActivity(ProfileActivity.getStartIntent(getActivity(), userId));
    }
    private void openCommentsActivity(long comicId) {
        startActivity(CommentsActivity.getStartIntent(getContext(), comicId));
        getActivity().overridePendingTransition(R.anim.slide_from_bottom, 0);
    }
    private void openYoutubeActivity(String videoUrl) {
        startActivity(YoutubeActivity.getStartIntent(getActivity(), videoUrl));
    }

    private void showReportTypesBottomSheet(long comicId) {

        final ReportTypesBSDialog reportTypesDialog = ReportTypesBSDialog.newInstance(comicId);
        reportTypesDialog.setTargetFragment(this, 0);

        reportTypesDialog.show(getActivity().getSupportFragmentManager(), reportTypesDialog.getTag());
    }

    public void resetComics() {
        if (mComicsAdapter != null) {
            mComicsAdapter.clearAll();
            mComicsPresenter.onComicsReset();
        }
        // mComicsPresenter.getNewComics(mComicsSource);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mComicsAdapter != null && mComicsRV != null) {
            if (isVisibleToUser) {
                mComicsRV.onWindowVisibilityChanged(View.VISIBLE);

            } else {
                mComicsRV.onWindowVisibilityChanged(View.GONE);
            }
        }
    }


    @Override
    public void onBottomDialogItemSelected(int position) {
        if (mComicToShare == null) {
            showMessage(R.string.server_error_general);
            return;
        }

        Integer comicType = mComicToShare.getComicType();

        switch (position) {
            case COMIC_SHARE_TYPE_SAVE_POS:
                if (comicType == ApiHelper.COMIC_TYPES.IMAGE) {
                    createImageDownloadTask();

                } else if (comicType == ApiHelper.COMIC_TYPES.GIF) {
                    createGifDownloadTask(mComicToShare.getComicUrl());
                }

                break;

            case COMIC_SHARE_TYPE_SHARE_POS:
                if (comicType == ApiHelper.COMIC_TYPES.IMAGE) {
                    createImageShareTask();

                } else if (comicType == ApiHelper.COMIC_TYPES.GIF) {
                    createGifShareTask(mComicToShare.getComicUrl());
                }

                break;
        }

        mComicToShare = null;
        mViewToShare = null;
    }

    @Override
    public void onNetworkCallStarted(String apiEndpoint) {

        switch (apiEndpoint) {

            case ApiEndPoint.EP_COMIC_GET_NEW:
                // Making sure refresh layout and progressbar won't appear together
                if (mComicsAdapter.getItemCount() == 0) {
                    showView(mComicsProgressBar);
                }
                break;
        }
    }

    @Override
    public void onNetworkCallReturned(String apiEndpoint, boolean isSuccessful) {

        switch (apiEndpoint) {

            case ApiEndPoint.EP_COMIC_GET_NEW:
                hideView(mComicsProgressBar);
                if (getActivity()instanceof NewMainActivity) {
                    ((NewMainActivity) getActivity()).stopRefreshing();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        mComicsPresenter.onDetach();
        super.onDestroy();
    }
}
