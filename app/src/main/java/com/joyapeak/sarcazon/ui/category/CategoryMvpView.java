package com.joyapeak.sarcazon.ui.category;

import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by User on 12/2/2018.
 */

public interface CategoryMvpView extends MvpView {

    void onCategoriesRetrieved(List<CategoryResponse.SingleCategory> categories);
}
