package com.joyapeak.sarcazon.ui.pendingcommentsforreview;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.BaseItemsRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.NewCommentsResponse;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by test on 8/27/2018.
 */

public class PendingCommentsForReviewPresenter<V extends PendingCommentsForReviewMvpView> extends BasePresenter<V>
        implements PendingCommentsForReviewMvpPresenter<V> {

    private Long mCommentsBaseId = null;
    private Integer mCommentsOffset = 0;


    @Inject
    public PendingCommentsForReviewPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getPendingCommentsForReviews() {

        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getPendingCommentsForReviews();
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        if (mCommentsOffset == 0) {
            getMvpView().showLoadingDialog();
        }

        getDataManager().getPendingCommentsForReview(new BaseItemsRequest(
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

                    getMvpView().addComments(commentsResponse.getComments());

                    int totalCommentsCnt = commentsResponse.getTotalCommentsCnt() == null ? 0 :
                            commentsResponse.getTotalCommentsCnt();

                    getMvpView().updateTotalCommentsCount(totalCommentsCnt);
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
    public void postMarkReviewed(final long commentId, final int reviewAction,
                                 final Integer userAction, final int adapterPosition) {

        if (getMvpView() == null) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                postMarkReviewed(commentId, reviewAction, userAction, adapterPosition);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postMarkReviewed(new OtherRequest.MarkReviewedRequest(
                commentId,
                reviewAction,
                userAction), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().updateAfterMarkReviewed(adapterPosition);
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
