package com.joyapeak.sarcazon.ui.comic;

import android.content.Context;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.ApiHelper.ACTION_TYPES;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.CommentAddResponse;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.NewCommentsResponse;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;

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

/**
 * Created by Mahmoud Ali on 4/17/2018.
 */

public class SingleComicPresenter<V extends SingleComicMvpView> extends BasePresenter<V>
        implements SingleComicMvpPresenter<V> {

    private HashSet<Long> mLikedCommentsIdsSet;
    private HashSet<Long> mDislikedCommentsIdsSet;

    private HashSet<Long> mReportedCommentsIdsSet;

    private Long mCommentsBaseId = null;
    private Integer mCommentsOffset = 0;


    @Inject
    public SingleComicPresenter(DataManager dataManager) {
        super(dataManager);
        mLikedCommentsIdsSet = getDataManager().getLikedCommentsIds();
        mDislikedCommentsIdsSet = getDataManager().getDislikedCommentsIds();
        mReportedCommentsIdsSet = getDataManager().getReportedCommentsIds();
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public long getViewerId() {
        return getDataManager().getCurrentUserId();
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
        getMvpView().onComicLikeResult(likeAction, true);

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onComicLikeResult(likeAction, false);
            // handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        // getMvpView().showLoadingDialog();
        String actionType = likeAction ? ACTION_TYPES.LIKE : ACTION_TYPES.DISLIKE;
        getDataManager().postComicLike(new ComicRequest.ComicLikeRequest(
                getDataManager().getCurrentUserId(),
                comicId,
                actionType), new ServerResult() {

                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {

                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            // handleApiError(responseCode, handler);
                            getMvpView().onComicLikeResult(likeAction, false);
                            return;
                        }

                        if (isViewAttached()) {
                            // getMvpView().onComicLikeResult(likeAction, true);
                            // getMvpView().hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            // getMvpView().hideLoadingDialog();
                            // handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                        }
                    }
                });

    }

    @Override
    public void deleteComic(final long comicId) {
        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                deleteComic(comicId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicDelete(new ComicRequest.DeleteRequest(
                getDataManager().getCurrentUserId(),
                comicId), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().removeComic();
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
    public void hideComic(final long comicId) {
        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                hideComic(comicId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicHide(new ComicRequest.HideRequest(
                getDataManager().getCurrentUserId(),
                comicId), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().removeComic();
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
    public void blockComic(final long comicId) {

        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                blockComic(comicId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicBlock(comicId, new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().removeComic();
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
    public void markComicAsReviewed(final long comicId, final int contentReportAction,
                                    final Integer userReportAction) {

        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                markComicAsReviewed(comicId, contentReportAction, userReportAction);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postMarkReviewed(new OtherRequest.MarkReviewedRequest(
                comicId,
                contentReportAction,
                userReportAction), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().removeComic();
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
    public void getNewComments(final long comicId) {
        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getNewComments(comicId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        if (mCommentsOffset == 0) {
            getMvpView().showLoadingDialog();
        }

        getDataManager().getNewComments(new CommentRequest.NewCommentsRequest(
                comicId,
                mCommentsBaseId,
                mCommentsOffset,
                ApiHelper.LARGE_COUNT_PER_REQUEST), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    NewCommentsResponse commentsResponse = (NewCommentsResponse) responseBody;

                    mCommentsOffset += commentsResponse.getComments().size();
                    if (mCommentsBaseId == null) {
                        mCommentsBaseId = commentsResponse.getBaseId();
                    }

                    // Don't show any reported comments by this user
                    List<ComicComment> filteredComments = new ArrayList<>();
                    for (ComicComment comment : commentsResponse.getComments()) {
                        if (!mReportedCommentsIdsSet.contains(comment.getCommentId())) {
                            filteredComments.add(comment);
                        }
                    }

                    setCommentsIsLikedAndDisliked(filteredComments);
                    getMvpView().addComments(filteredComments);

                    int totalCommentsCnt = commentsResponse.getTotalCommentsCnt() == null? 0 :
                            commentsResponse.getTotalCommentsCnt();

                    getMvpView().updateCommentsCount(totalCommentsCnt);

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

        // getMvpView().addComments(addCommentsTest(startPos, 10));
    }

    @Override
    public void onCommentsRemoved() {
        mCommentsOffset = 0;
        mCommentsBaseId = null;
    }

    private void setCommentsIsLikedAndDisliked(List<ComicComment> comments) {
        for (ComicComment comment : comments) {
            comment.setIsLiked(mLikedCommentsIdsSet.contains(comment.getCommentId())? true : false);
            comment.setIsDisliked(mDislikedCommentsIdsSet.contains(comment.getCommentId())? true : false);
        }
    }

    /*private List<ComicComment> addCommentsTest(int startPos, int count) {

        List<ComicComment> comments = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ComicComment comment = new ComicComment(
                    Long.valueOf(i + startPos),
                    Long.valueOf(i + startPos),
                    String.valueOf(i + startPos),
                    "commenter " + (i + startPos),
                    "",
                    System.currentTimeMillis(),
                    i + startPos,
                    startPos - 1,
                    2,
                    false,
                    false);

            comments.add(comment);
        }

        return comments;
    }*/

    @Override
    public void likeComment(final long commentId, final boolean actionLike, final boolean isPositiveAction) {
        if (getMvpView() == null) {
            return;
        }

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
                likeComment(commentId, actionLike, isPositiveAction);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        String action = actionLike ? ACTION_TYPES.LIKE : ACTION_TYPES.DISLIKE;

        getDataManager().postCommentLike(new CommentRequest.CommentLikeRequest(
                getDataManager().getCurrentUserId(),
                commentId,
                action,
                isPositiveAction,
                mLikedCommentsIdsSet.contains(commentId),
                mDislikedCommentsIdsSet.contains(commentId)), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (actionLike) {
                    updateLikedCommentsIds(commentId, isPositiveAction);

                } else {
                    updateDislikedCommentsIds(commentId, isPositiveAction);
                }

                if (isViewAttached()) {
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
    public void reportComment(final long commentId, final int adapterPosition) {
        if (getMvpView() == null) {
            return;
        }

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                reportComment(commentId, adapterPosition);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postCommentReport(new OtherRequest.ReportRequest(
                commentId, ApiHelper.REPORT_TYPES.LANGUAGE), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                mReportedCommentsIdsSet.add(commentId);
                getDataManager().setReportedCommentsIds(mReportedCommentsIdsSet);

                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().removeComment(adapterPosition);
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
    public void deleteComment(final long commentId, final int commentPosition) {

        if (getMvpView() == null) {
            return;
        }

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                deleteComment(commentId, commentPosition);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postCommentDelete(new CommentRequest.CommentDeleteRequest(
                getDataManager().getCurrentUserId(),
                commentId, "delete"), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }


                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().removeComment(commentPosition);
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
    public void blockComment(final long commentId, final int commentPosition) {

        if (getMvpView() == null) {
            return;
        }

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                blockComment(commentId, commentPosition);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postCommentBlock(commentId, new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }


                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().removeComment(commentPosition);
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

    private void updateLikedCommentsIds(long commentId, boolean shouldAdd) {
        if (shouldAdd) {
            mLikedCommentsIdsSet.add(commentId);

            if (mDislikedCommentsIdsSet.contains(commentId)) {
                mDislikedCommentsIdsSet.remove(commentId);
                getDataManager().setDislikedCommentsIds(mDislikedCommentsIdsSet);
            }

        } else {
            mLikedCommentsIdsSet.remove(commentId);
        }

        getDataManager().setLikedCommentsIds(mLikedCommentsIdsSet);
    }
    private void updateDislikedCommentsIds(long commentId, boolean shouldAdd) {
        if (shouldAdd) {
            mDislikedCommentsIdsSet.add(commentId);

            if (mLikedCommentsIdsSet.contains(commentId)) {
                mLikedCommentsIdsSet.remove(commentId);
                getDataManager().setLikedCommentsIds(mLikedCommentsIdsSet);
            }

        } else {
            mDislikedCommentsIdsSet.remove(commentId);
        }

        getDataManager().setDislikedCommentsIds(mDislikedCommentsIdsSet);
    }

    @Override
    public void addComment(final long comicId, final String comment) {
        if (getMvpView() == null) {
            return;
        }

        getDataManager().logCommentAdd();

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                addComment(comicId, comment);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postNewComment(new CommentRequest.CommentAddRequest(
                getDataManager().getCurrentUserId(),
                comicId,
                comment), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                CommentAddResponse commentAddResponse = (CommentAddResponse) responseBody;

                if (isViewAttached()) {
                    mCommentsBaseId = null;
                    mCommentsOffset = 0;

                    getMvpView().updateAfterCommentAdded();
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
