package com.joyapeak.sarcazon.ui.category;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse.SingleCategory;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by User on 12/2/2018.
 */

public class CategoryPresenter <V extends CategoryMvpView> extends BasePresenter <V> implements
    CategoryMvpPresenter <V> {

    private static List<SingleCategory> sCategories;

    @Inject
    public CategoryPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getCategories() {

        List<SingleCategory> categories = new ArrayList<>();
        categories.add(new SingleCategory("Football", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));
        categories.add(new SingleCategory("Animals", "https://www.designboom.com/wp-content/uploads/2016/10/things-i-have-drawn-child-drawing-photoshop-reality-db-600.jpg"));
        categories.add(new SingleCategory("Cars", "https://amp.businessinsider.com/images/5aabc7bbc72ac12f008b4609-750-563.jpg"));
        categories.add(new SingleCategory("Funny", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLaEH8F8eToiRxvAs1q878xduAHQIqfLaujX5q1mNouCjA4_5y"));
        categories.add(new SingleCategory("Sports", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));
        categories.add(new SingleCategory("Wallpapers", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));
        categories.add(new SingleCategory("Travel", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));
        categories.add(new SingleCategory("Games", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));
        categories.add(new SingleCategory("Art", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));
        categories.add(new SingleCategory("Football", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));
        categories.add(new SingleCategory("Football", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Football_%28soccer_ball%29.svg/2000px-Football_%28soccer_ball%29.svg.png"));

        getMvpView().onCategoriesRetrieved(categories);

        /*if (getMvpView() == null) {
            return;
        }

        if (sCategories != null && sCategories.size() > 0) {
            getMvpView().onCategoriesRetrieved(sCategories);
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getCategories();
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();
        getDataManager().getCategories(new CategoryRequest.CategoriesRequest(), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (isViewAttached()) {
                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        handleApiError(responseCode, handler);
                        return;
                    }

                    CategoriesResponse categoriesResponse = (CategoriesResponse) responseBody;
                    sCategories = categoriesResponse.getCategories();
                    getMvpView().onCategoriesRetrieved(sCategories);
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
        });*/
    }

    @Override
    public void setSelectedCategory(String category) {
        getDataManager().setSelectedCategory(category);
    }
}
