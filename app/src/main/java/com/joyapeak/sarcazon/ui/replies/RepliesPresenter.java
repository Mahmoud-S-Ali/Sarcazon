package com.joyapeak.sarcazon.ui.replies;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.ApiHelper.ACTION_TYPES;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mahmoud Ali on 5/3/2018.
 */

public class RepliesPresenter <V extends RepliesMvpView> extends BasePresenter<V>
            implements RepliesMvpPresenter<V> {

    private HashSet<Long> mLikedRepliesIdsSet;
    private HashSet<Long> mDislikedRepliesIdsSet;

    private HashSet<Long> mReportedRepliesIdsSet;

    private Long mRepliesBaseId = null;
    private Integer mRepliesOffset = 0;


    @Inject
    public RepliesPresenter(DataManager dataManager) {
        super(dataManager);
        mLikedRepliesIdsSet = getDataManager().getLikedRepliesIds();
        mDislikedRepliesIdsSet = getDataManager().getDislikedRepliesIds();

        mReportedRepliesIdsSet = getDataManager().getReportedRepliesIds();
    }

    @Override
    public long getViewerId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public void getComment(final long commentId) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getComment(commentId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().getComment(new CommentRequest.SpecificCommentRequest(commentId),
                new ServerResult() {

                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        CommentResponse.ComicComment comment =
                                (CommentResponse.ComicComment) responseBody;
                        comment.setCommentId(commentId);

                        if (isViewAttached()) {
                            getMvpView().onCommentDataRetrieved(comment);
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
    public void addReply(final long mainCommentId, final String reply) {
        if (!confirmRegistration()) {
            return;
        }

        getDataManager().logCommentAdd();

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                addReply(mainCommentId, reply);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postNewReply(new CommentRequest.ReplyAddRequest(
                getDataManager().getCurrentUserId(),
                mainCommentId, reply
        ), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    mRepliesBaseId = null;
                    mRepliesOffset = 0;

                    getMvpView().updateAfterReplyAdded();
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
    public void getReplies(final long mainCommentId) {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getReplies(mainCommentId);
            }
        };

        if (getMvpView() == null)
            return;

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().getNewReplies(new CommentRequest.NewRepliesRequest(
                        mainCommentId,
                        mRepliesBaseId,
                        mRepliesOffset,
                        ApiHelper.LARGE_COUNT_PER_REQUEST),
                new ServerResult() {

                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        if (isViewAttached()) {
                            CommentResponse.NewRepliesResponse repliesResponse =
                                    (CommentResponse.NewRepliesResponse) responseBody;

                            mRepliesOffset += repliesResponse.getReplies().size();
                            if (mRepliesBaseId == null) {
                                mRepliesBaseId = repliesResponse.getBaseId();
                            }

                            // Don't show any reported replies by this user
                            List<ComicComment> filteredResplies = new ArrayList<>();
                            for (ComicComment comment : repliesResponse.getReplies()) {
                                if (!mReportedRepliesIdsSet.contains(comment.getCommentId())) {
                                    filteredResplies.add(comment);
                                }
                            }

                            setCommentsIsLikedAndDisliked((repliesResponse.getReplies()));
                            getMvpView().addReplies(repliesResponse.getReplies());

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
    private void setCommentsIsLikedAndDisliked(List<ComicComment> comments) {
        for (ComicComment comment : comments) {
            comment.setIsLiked(mLikedRepliesIdsSet.contains(comment.getCommentId())? true : false);
            comment.setIsDisliked(mDislikedRepliesIdsSet.contains(comment.getCommentId())? true : false);
        }
    }

    @Override
    public void likeReply(final long replyId, final boolean actionLike, final boolean isPositiveAction) {
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
                likeReply(replyId, actionLike, isPositiveAction);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        String action = actionLike? ACTION_TYPES.LIKE : ACTION_TYPES.DISLIKE;

        getDataManager().postReplyLike(new CommentRequest.CommentLikeRequest(
                getDataManager().getCurrentUserId(),
                replyId,
                action,
                isPositiveAction,
                mLikedRepliesIdsSet.contains(replyId),
                mDislikedRepliesIdsSet.contains(replyId)), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (actionLike) {
                    updateLikedRepliesIds(replyId, isPositiveAction);
                    if (isPositiveAction) {
                        updateDislikedRepliesIds(replyId, false);
                    }

                } else {
                    updateDislikedRepliesIds(replyId, isPositiveAction);
                    if (isPositiveAction) {
                        updateLikedRepliesIds(replyId, false);
                    }
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
    public void reportReply(final long replyId, final int adapterPosition) {

        if (getMvpView() == null) {
            return;
        }

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                reportReply(replyId, adapterPosition);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postReplyReport(new OtherRequest.ReportRequest(
                replyId, ApiHelper.REPORT_TYPES.LANGUAGE), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                mReportedRepliesIdsSet.add(replyId);
                getDataManager().setReportedCommentsIds(mReportedRepliesIdsSet);

                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().removeReply(adapterPosition);
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
    public void deleteReply(final long replyId, final int replyPosition) {

        if (getMvpView() == null) {
            return;
        }

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                deleteReply(replyId, replyPosition);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postCommentDelete(new CommentRequest.CommentDeleteRequest(
                getDataManager().getCurrentUserId(),
                replyId, "delete"), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }


                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().removeReply(replyPosition);
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
    public void blockReply(final long replyId, final int replyPosition) {

        if (getMvpView() == null) {
            return;
        }

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                blockReply(replyId, replyPosition);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postReplyBlock(replyId, new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().removeReply(replyPosition);
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
    public boolean getIsEngagementUsersEnabled() {
        return CommonUtils.intToBoolean(
                getDataManager().getApiHeader().getProtectedApiHeader().getEngagementUsersEnabled());
    }


    private void updateLikedRepliesIds(long replyId, boolean shouldAdd) {
        if (shouldAdd) {
            mLikedRepliesIdsSet.add(replyId);

        } else {
            mLikedRepliesIdsSet.remove(replyId);
        }

        getDataManager().setLikedRepliesIds(mLikedRepliesIdsSet);
    }
    private void updateDislikedRepliesIds(long replyId, boolean shouldAdd) {
        if (shouldAdd) {
            mDislikedRepliesIdsSet.add(replyId);

        } else {
            mDislikedRepliesIdsSet.remove(replyId);
        }

        getDataManager().setDislikedRepliesIds(mDislikedRepliesIdsSet);
    }

    /*private List<ComicComment> addCommentsTest() {
        ComicComment comment1 = new ComicComment(1L, 1L, "Hi from 1", "Ali", "https://i.annihil.us/u/prod/marvel/i/mg/f/80/537bbeb8de8a3/standard_xlarge.jpg", System.currentTimeMillis(), 10, 1, 2, false, false);
        ComicComment comment2 = new ComicComment(2L, 2L, "Hi from 2", "Mag", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTX9M0hRMd3HUidpXnwNGxGpYg9fqHJlz4XBaJKe3RGjssp6fR_", System.currentTimeMillis(), 11, 2, 3, false, false);
        ComicComment comment3 = new ComicComment(3L, 3L, "Hi from 3", "His", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbe-CmIuvyNCiAs-mvPXNRFMszM3FVypWoBPqYCBZtSKCylam_", System.currentTimeMillis(), 12, 3, 4, false, false);
        ComicComment comment4 = new ComicComment(4L, 4L, "Hi from 4", "Mah", "https://i.ytimg.com/vi/AZ5p09x69DQ/maxresdefault.jpg", System.currentTimeMillis(), 13, 5, 6, false, false);
        ComicComment comment5 = new ComicComment(5L, 5L, "Hi from 5", "Nad", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqv0AxzTzQd9m1ZITAKFlpbJH9ngH4LiP3YxCoZBYd-0Wu7IIc", System.currentTimeMillis(), 14, 7, 8, false, false);
        ComicComment comment6 = new ComicComment(6L, 6L, "Hi from 6", "Fad", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT75Zt57B__pyNWplXb8vAG2i773uZZpgA7n2PFFATm3IhoKiX0", System.currentTimeMillis(), 15, 9, 10, false, false);

        List<ComicComment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        comments.add(comment4);
        comments.add(comment5);
        comments.add(comment6);

        return comments;
    }*/
}
