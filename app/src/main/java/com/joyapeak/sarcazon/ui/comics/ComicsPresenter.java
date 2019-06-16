package com.joyapeak.sarcazon.ui.comics;

import android.content.Context;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiEndPoint;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.AppConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import timber.log.Timber;

import static com.joyapeak.sarcazon.utils.ApiErrorConstants.GENERAL_ERROR;

/**
 * Created by test on 10/16/2018.
 */

public class ComicsPresenter <V extends ComicsMvpView> extends BasePresenter <V>
        implements ComicsMvpPresenter <V> {


    private static final String TAG = "ComicsPresenter";

    private Long mComicsBaseId = null;
    private Integer mComicsOffset = 0;
    // private boolean mFirstTimeGrabbingComics = true;

    private HashSet<Long> mViewedComicsIdsSet;
    private HashSet<Long> mReportedComicsIdsSet;

    @Inject
    public ComicsPresenter(DataManager dataManager) {
        super(dataManager);
        mViewedComicsIdsSet = getDataManager().getViewedComicsIds();
        mReportedComicsIdsSet = getDataManager().getReportedComicsIds();
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public void getNewComics(final int comicsSource, final String category) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getNewComics(comicsSource, category);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        if (mComicsOffset == 0) {
            // mFirstTimeGrabbingComics = false;
            informNetworkCallStart(ApiEndPoint.EP_COMIC_GET_NEW);
        }

        getDataManager().getNewComics(
                new ComicRequest.NewComicsRequest(
                        getDataManager().getCurrentUserId(),
                        comicsSource,
                        category,
                        mComicsBaseId,
                        mComicsOffset,
                        ApiHelper.LARGE_COUNT_PER_REQUEST
                ),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {

                        if (isViewAttached()) {
                            if (responseCode != HttpsURLConnection.HTTP_OK) {
                                handleApiError(responseCode, handler);
                                informNetworkCallEnd(ApiEndPoint.EP_COMIC_GET_NEW, false);
                                return;
                            }

                            ComicResponse.NewComicsResponse comicsReturned =
                                    (ComicResponse.NewComicsResponse) responseBody;

                            // TODO: Should be changed later to make sure he watched all top comics
                            if (comicsSource == ApiHelper.COMIC_SOURCES.FEATURED) {
                                getDataManager().setNewFeaturedCount(0);
                            }

                            // Removing any corrupted or reported comics by this user
                            List<ComicResponse.SingleComic> newComics = comicsReturned.getComics();

                            mComicsOffset += newComics.size();
                            if (mComicsBaseId == null) {
                                comicsReturned.getBaseId();
                            }

                            List<ComicResponse.SingleComic> comicsToRemove = new ArrayList<>();

                            for (ComicResponse.SingleComic comic : newComics) {
                                String comicUrl = comic.getComicInfo().getComicUrl();
                                if (comicUrl == null || comicUrl.isEmpty() ||
                                        mReportedComicsIdsSet.contains(comic.getComicInfo().getComicId())) {

                                    comicsToRemove.add(comic);
                                }
                            }

                            newComics.removeAll(comicsToRemove);
                            getMvpView().onComicsRetrieved(newComics);

                            informNetworkCallEnd(ApiEndPoint.EP_COMIC_GET_NEW, true);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            handleApiError(GENERAL_ERROR, handler);
                            informNetworkCallEnd(ApiEndPoint.EP_COMIC_GET_NEW, false);
                        }
                    }
                });
    }

    @Override
    public void likeComic(final long comicId, final boolean likeAction) {
        if (getMvpView() == null) {
            return;
        }

        // Only registered users can like comics
        /*if (!confirmRegistration()) {
            getDataManager().logNewUnregUsersLikeClick();
            return;
        }*/

        int appOpensCount = getDataManager().getAppOpensCount();
        if (appOpensCount > 1) {
            getDataManager().logLikeClick();

        } else {
            if (getDataManager().getCurrentUserLoggedInMode() !=
                    DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
                getDataManager().logNewRegUsersLikeClick();

            } else {
                getDataManager().logNewRegUsersLikeClick();
            }
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                likeComic(comicId, likeAction);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            return;
        }

        // getMvpView().showLoadingDialog();
        String actionType = likeAction ? ApiHelper.ACTION_TYPES.LIKE : ApiHelper.ACTION_TYPES.DISLIKE;
        getDataManager().postComicLike(new ComicRequest.ComicLikeRequest(
                getDataManager().getCurrentUserId(),
                comicId,
                actionType), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    return;
                }

                if (isViewAttached()) {
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

    }

    @Override
    public void deleteComic(final long comicId) {
        if (getMvpView() == null) {
            return;
        }

        if (!getMvpView().isNetworkConnected()) {
            handleApiErrorWithToast(ApiErrorConstants.CONNECTION_ERROR);
            getMvpView().onComicDeleted(false);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicDelete(new ComicRequest.DeleteRequest(
                getDataManager().getCurrentUserId(),
                comicId), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        handleApiErrorWithToast(responseCode);
                        getMvpView().onComicDeleted(false);
                        return;
                    }

                    getMvpView().onComicDeleted(true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    handleApiErrorWithToast(GENERAL_ERROR);
                    getMvpView().onComicDeleted(false);
                }
            }
        });
    }

    // Hide comic, hides the comic from everyone except the owner
    @Override
    public void hideComic(final long comicId) {
        if (getMvpView() == null) {
            return;
        }

        if (!getMvpView().isNetworkConnected()) {
            handleApiErrorWithToast(ApiErrorConstants.CONNECTION_ERROR);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicHide(new ComicRequest.HideRequest(
                getDataManager().getCurrentUserId(),
                comicId), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        handleApiErrorWithToast(responseCode);
                        getMvpView().onComicHid(false);
                        return;
                    }

                    getMvpView().onComicHid(true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    handleApiErrorWithToast(GENERAL_ERROR);
                    getMvpView().onComicHid(false);
                }
            }
        });
    }

    @Override
    public void blockComic(final long comicId) {

        if (getMvpView() == null) {
            return;
        }

        if (!getMvpView().isNetworkConnected()) {
            handleApiErrorWithToast(ApiErrorConstants.CONNECTION_ERROR);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicBlock(comicId, new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        handleApiErrorWithToast(responseCode);
                        getMvpView().onComicBlocked(false);
                        return;
                    }

                    getMvpView().onComicBlocked(true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    handleApiErrorWithToast(GENERAL_ERROR);
                    getMvpView().onComicBlocked(false);
                }
            }
        });
    }

    @Override
    public void featureComic(final long comicId) {

        if (getMvpView() == null) {
            return;
        }

        // Only registered users can like comics
        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                featureComic(comicId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicFeatured(new ComicRequest.AddFeaturedRequest(
                getDataManager().getCurrentUserId(),
                comicId), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().onComicAddedToFeatured();
                    getMvpView().hideLoadingDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                }
            }
        });
    }

    @Override
    public void shareComic(Context context, long comicId, String thumbnailUrl, int comicType) {
        switch (comicType) {
            case ApiHelper.COMIC_TYPES.GIF:
                getMvpView().handleShareComicGif();
                break;

            case ApiHelper.COMIC_TYPES.IMAGE:
                getMvpView().handleShareComicImage();
                break;

            default:
                createVideoLinkForShare(context, comicId, thumbnailUrl);
                break;
        }
    }


    // Facebook video source urls are changed frequently, so we need to keep it updated
    // with the working url
    @Override
    public void updateFacebookComicVidSourceUrl(long comicId, String vidSourceUrl) {

        if (getMvpView() == null) {
            return;
        }

        if (!getMvpView().isNetworkConnected()) {
            return;
        }

        getDataManager().postComicUrl(new ComicRequest.ComicUrlUploadRequest(
                getDataManager().getCurrentUserId(),
                comicId,
                ApiHelper.COMIC_TYPES.VIDEO_FACEBOOK,
                null,
                vidSourceUrl,
                null,
                null,
                null,
                null,
                null,
                null), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    public int getNewFeaturedComicsCount() {
        return getDataManager().getNewFeaturedCount();
    }

    @Override
    public void markComicAsViewed(final long comicId) {

        if (getMvpView() == null) {
            return;
        }

        if (!getMvpView().isNetworkConnected()) {
            return;
        }

        if (mViewedComicsIdsSet.contains(comicId))
            return;

        if (getDataManager().getAppOpensCount() <= 1) {
            getDataManager().logComicViewForNewUsers();

        } else {
            getDataManager().logComicView();
        }

        mViewedComicsIdsSet.add(comicId);
        getDataManager().postComicViewed(new ComicRequest.MarkViewedRequest(
                getDataManager().getCurrentUserId(),
                comicId), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    mViewedComicsIdsSet.remove(comicId);
                    return;
                }

                getDataManager().setViewedComicsIds(mViewedComicsIdsSet);
            }

            @Override
            public void onFailure(Throwable t) {
                mViewedComicsIdsSet.remove(comicId);
            }
        });
    }

    @Override
    public long getLastFeaturedComicId() {
        return getDataManager().getLastFeaturedComicId();
    }

    @Override
    public void updateLastFeaturedComicId(long id) {
        getDataManager().setLastFeaturedComicId(id);
    }

    @Override
    public void releaseFeatured(final String message) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                releaseFeatured(message);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        String topic = null;
        if (getDataManager().getSelectedApiVersionName().equals(AppConstants.API_TEST_VERSION_NAME)) {
            topic = "test";
        }

        getMvpView().showLoadingDialog();
        getDataManager().postReleaseFeaturedComics(new OtherRequest.FeaturedReleaseRequest(message, topic),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        if (isViewAttached()) {
                            getMvpView().onFeaturedComicsReleased();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            getMvpView().hideLoadingDialog();
                            handleApiError(GENERAL_ERROR, handler);
                        }
                    }
                });
    }

    // Saving reported comics locally
    @Override
    public void addReportedComic(long comicId) {
        mReportedComicsIdsSet.add(comicId);
        getDataManager().setReportedComicsIds(mReportedComicsIdsSet);
    }

    // Resetting comics page, when refreshing for example
    @Override
    public void onComicsReset() {
        mComicsBaseId = null;
        mComicsOffset = 0;
    }


    // Branch sharing
    // Video link operations
    private void createVideoLinkForShare(Context context, Long comicId, String thumbnailUrl) {
        generateComicLink(context, comicId, thumbnailUrl, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    if (isViewAttached()) {
                        getMvpView().handleShareVideoUrl(url);
                    }

                } else {
                    if (isViewAttached()) {
                        getMvpView().showMessage("Can't share this content");
                    }
                    Timber.e(error.getMessage().toString());
                }
            }
        });
    }
    private void generateComicLink(Context context, @NonNull Long comicId, String thumbnailUrl,
                                   Branch.BranchLinkCreateListener listener) {

        BranchUniversalObject branchUniversalObj =  createBranchUniversalObject(comicId, thumbnailUrl);
        LinkProperties linkProperties = createLinkProperties();
        branchUniversalObj.generateShortUrl(context, linkProperties, listener);
    }
    private BranchUniversalObject createBranchUniversalObject(Long comicId, String thumbnailUrl) {

        String canonicalIdentifier = "comic_id/" + String.valueOf(comicId);
        String contentTitle = "Shoof el video 3ala Sarcazon";
        String contentDescription = "Sarcazon, el makan el gedeed lel comics";

        ContentMetadata contentMetadata = new ContentMetadata();
        contentMetadata.addCustomMetadata("comic_id", String.valueOf(comicId));

        return new BranchUniversalObject()
                .setCanonicalIdentifier(canonicalIdentifier)
                .setTitle(contentTitle)
                .setContentDescription(contentDescription)
                .setContentImageUrl(thumbnailUrl)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(contentMetadata);
    }
    private LinkProperties createLinkProperties() {

        return new LinkProperties()
                .setFeature("sharing");
        // .setChannel("facebook");
        // .addControlParameter("$desktop_url", "https://joyapeak.com")
        // .addControlParameter("$android_url", "https://www.dropbox.com/home?preview=app-debug.apk")
        // .addControlParameter("$ios_url", "https://joyapeak.com");
    }
}
