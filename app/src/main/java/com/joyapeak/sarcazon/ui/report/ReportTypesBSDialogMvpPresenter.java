package com.joyapeak.sarcazon.ui.report;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 6/25/2018.
 */

public interface ReportTypesBSDialogMvpPresenter<V extends ReportTypesBSDialogMvpView> extends MvpPresenter<V> {

    void reportComic(long comicId, int reportType);
}
