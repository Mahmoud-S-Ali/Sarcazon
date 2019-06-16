package com.joyapeak.sarcazon.ui.report;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mahmoud Ali on 6/25/2018.
 */

public class ReportTypesBSDialogPresenter<V extends ReportTypesBSDialogMvpView> extends BasePresenter<V>
        implements ReportTypesBSDialogMvpPresenter<V> {

    @Inject
    public ReportTypesBSDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void reportComic(final long comicId, final int reportType) {
        if (getMvpView() == null) {
            return;
        }

        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                reportComic(comicId, reportType);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().postComicReport(new OtherRequest.ReportRequest(
                comicId,
                reportType), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    getMvpView().onReportSuccess();
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
