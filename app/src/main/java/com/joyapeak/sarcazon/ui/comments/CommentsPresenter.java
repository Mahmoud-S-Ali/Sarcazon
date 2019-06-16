package com.joyapeak.sarcazon.ui.comments;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.ApiHelper.ACTION_TYPES;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.CommentAddResponse;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
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

/**
 * Created by test on 11/1/2018.
 */

public class CommentsPresenter <V extends CommentsMvpView> extends BasePresenter<V>
        implements CommentsMvpPresenter<V> {

    private HashSet<Long> mLikedCommentsIdsSet;
    private HashSet<Long> mDislikedCommentsIdsSet;

    private HashSet<Long> mReportedCommentsIdsSet;

    private Long mCommentsBaseId = null;
    private Integer mCommentsOffset = 0;

    @Inject
    public CommentsPresenter(DataManager dataManager) {
        super(dataManager);
        mLikedCommentsIdsSet = getDataManager().getLikedCommentsIds();
        mDislikedCommentsIdsSet = getDataManager().getDislikedCommentsIds();
        mReportedCommentsIdsSet = getDataManager().getReportedCommentsIds();
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
}
