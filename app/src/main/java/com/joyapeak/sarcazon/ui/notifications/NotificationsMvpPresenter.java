package com.joyapeak.sarcazon.ui.notifications;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 7/1/2018.
 */

public interface NotificationsMvpPresenter <V extends NotificationsMvpView> extends MvpPresenter<V> {

    void getNotifications();

    void setAllNotificationsAsOld();
}
