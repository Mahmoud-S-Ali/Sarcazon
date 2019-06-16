package com.joyapeak.sarcazon.ui.upload.uploadconfirmation;

import android.net.Uri;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramRequest;
import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramResponse;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoRequest;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.ui.youtube.YoutubeActivity;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.FileUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;
import com.joyapeak.sarcazon.utils.UrlUtils;

import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 7/5/2018.
 */

public class UploadConfirmationPresenter<V extends UploadConfirmationMvpView> extends BasePresenter<V>
        implements UploadConfirmationMvpPresenter<V> {

    @Inject
    public UploadConfirmationPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void uploadComicUrl(final int comicType, final String comicUrl, final String comicVideoSourceUrl,
                               final String caption, final String category, final String tags, final String credits,
                               final Double aspectRatio) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                uploadComicUrl(comicType, comicUrl, comicVideoSourceUrl, caption, category, tags, credits, aspectRatio);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            getMvpView().onComicUrlUploadResult(false, null);
            return;
        }

        getMvpView().showLoadingDialog();

        String videoComicId = null;
        switch (comicType) {
            case ApiHelper.COMIC_TYPES.VIDEO_FACEBOOK:
                videoComicId = extractFacebookVideoIdFromUrl(comicUrl);
                break;

            case ApiHelper.COMIC_TYPES.VIDEO_INSTAGRAM:
                videoComicId = extractInstagramVideoIdFromUrl(comicUrl);
                break;

            case ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE:
                videoComicId = extractYoutubeVideoIdFromUrl(comicUrl);
                break;
        }

        Timber.d("Video comic id = " + videoComicId);

        getDataManager().postComicUrl(new ComicRequest.ComicUrlUploadRequest(
                getDataManager().getCurrentUserId(),
                null,
                comicType,
                comicUrl,
                comicVideoSourceUrl,
                videoComicId,
                caption,
                "all",
                tags,
                credits,
                aspectRatio), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    if (isViewAttached()) {
                        handleApiError(responseCode, handler);
                        getMvpView().onComicUrlUploadResult(false, null);
                    }
                    return;
                }

                getDataManager().logComicUpload();

                if (isViewAttached()) {
                    ComicResponse.ComicUrlUploadResponse uploadResponse =
                            (ComicResponse.ComicUrlUploadResponse) responseBody;

                    getMvpView().onComicUrlUploadResult(true, uploadResponse.getComicId());
                    getMvpView().hideLoadingDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().onComicUrlUploadResult(false, null);
                    handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                }
            }
        });
    }

    @Override
    public boolean getIsEngagementUsersEnabled() {
        return CommonUtils.intToBoolean(
                getDataManager().getApiHeader().getProtectedApiHeader().getEngagementUsersEnabled());
    }

    @Override
    public void uploadFiles(final byte[][] filesByteArrays) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                uploadFiles(filesByteArrays);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();

        FileUtils.CombinedFilesByteArr combinedBitmapsByteArr =
                FileUtils.getCombinedBitmapsByteArr(filesByteArrays);

        getDataManager().putComicPhoto(
                new PhotoRequest.PhotoUploadRequest(
                        ImageUtils.getRequestBodyFromByteArr(
                                combinedBitmapsByteArr.getResultByteArr()),
                        combinedBitmapsByteArr.getSizesInStrFormat()),
                new ServerResult() {

                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        if (isViewAttached()) {
                            PhotoResponse.PhotoUploadResponse photoResponse =
                                    (PhotoResponse.PhotoUploadResponse) responseBody;

                            getMvpView().onPhotoFilesUploaded(photoResponse.getPhotoUrl());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            getMvpView().hideLoadingDialog();
                            handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                        }
                    }
                }
        );
    }

    @Override
    public void getInstagramVideoData(final String videoUrl) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getInstagramVideoData(videoUrl);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getDataManager().getInstagramVideoData(new InstagramRequest.InstagramVideoRequest(
                        extractInstagramVideoIdFromUrl(videoUrl)), new ServerResult() {

                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            getMvpView().onInstagramVideoDataRetrieved(false, null);
                            return;
                        }

                        if (isViewAttached()) {
                            InstagramResponse.InstagramVideoResponse videoResponse =
                                    (InstagramResponse.InstagramVideoResponse) responseBody;

                            getMvpView().onInstagramVideoDataRetrieved(true, videoResponse);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            getMvpView().onInstagramVideoDataRetrieved(false, null);
                        }
                    }
                }
        );
    }


    private String extractInstagramVideoIdFromUrl(String url) {
        List<String> pathSegments = Uri.parse(url).getPathSegments();
        return pathSegments.size() > 0 ? pathSegments.get(1) : null;
    }

    private String extractFacebookVideoIdFromUrl(String url) {
        String videoId = UrlUtils.getKeyValueFromUrl(url, "story_fbid");
        if (videoId != null) {
            return videoId;
        }

        List<String> pathSegments = Uri.parse(url).getPathSegments();
        return pathSegments.size() > 1 ? pathSegments.get(2) : null;
    }

    private String extractYoutubeVideoIdFromUrl(String url) {
        return YoutubeActivity.getYoutubeVideoId(url);
    }
}
