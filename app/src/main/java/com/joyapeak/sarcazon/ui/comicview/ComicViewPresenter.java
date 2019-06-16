package com.joyapeak.sarcazon.ui.comicview;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mahmoud Ali on 5/20/2018.
 */

public class ComicViewPresenter<V extends ComicViewMvpView> extends BasePresenter<V>
        implements ComicViewMvpPresenter<V> {

    @Inject
    public ComicViewPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getComicWithId(final Long comicId) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getComicWithId(comicId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().getSpecificComic(new ComicRequest.SpecificComicRequest(
                getDataManager().getCurrentUserId(),
                        comicId),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            handleApiError(responseCode, handler);
                            return;
                        }

                        if (isViewAttached()) {
                            ComicResponse.SingleComic comicResponse = (ComicResponse.SingleComic) responseBody;
                            getMvpView().setupComicFragment(comicResponse);
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
