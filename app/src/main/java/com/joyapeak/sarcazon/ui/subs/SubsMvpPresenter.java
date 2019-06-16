package com.joyapeak.sarcazon.ui.subs;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public interface SubsMvpPresenter<V extends SubsMvpView> extends MvpPresenter<V> {

    long getViewerId();

    void subscribe(long subId, boolean shouldSubscribe);

    void getSubscribers(long userId);

    void getSubscriptions(long userId);
}
