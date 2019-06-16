package com.joyapeak.sarcazon.ui.category;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by User on 12/2/2018.
 */

public interface CategoryMvpPresenter <V extends CategoryMvpView> extends MvpPresenter <V> {

    void getCategories();

    void setSelectedCategory(String category);
}
