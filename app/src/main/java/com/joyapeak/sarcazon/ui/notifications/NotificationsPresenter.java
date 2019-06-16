package com.joyapeak.sarcazon.ui.notifications;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationRequest;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mahmoud Ali on 7/1/2018.
 */

public class NotificationsPresenter <V extends NotificationsMvpView> extends BasePresenter<V>
        implements NotificationsMvpPresenter<V> {

    private final int MAX_NOTIFICATIONS_SIZE = 250;

    @Inject
    public NotificationsPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void getNotifications() {
        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getNotifications();
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            //handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        //getMvpView().showLoadingDialog();

        long userId = getDataManager().getCurrentUserId();
        getDataManager().getNotifications(new NotificationRequest.ServerNotificationsRequest(userId),
                new ServerResult() {
                    @Override
                    public void onSuccess(Object responseBody, int responseCode) {
                        if (responseCode != HttpsURLConnection.HTTP_OK) {
                            // handleApiError(responseCode, handler);
                            return;
                        }

                        NotificationResponse.ServerNotificationResponse notificationsResponse =
                                (NotificationResponse.ServerNotificationResponse) responseBody;

                        // Setting all returned notifications as new
                        List<ServerNotification> newNotifications = notificationsResponse.getNotifications();
                        for (ServerNotification notification : newNotifications) {
                            notification.setIsNew(true);
                        }

                        int newNotificationsCount = newNotifications.size();
                        ArrayList<ServerNotification> savedNotifications = getDataManager().getSavedNotifications();

                        // Making sure the list doesn't exceed the max size, or remove from beginning
                        int availableSize = MAX_NOTIFICATIONS_SIZE - savedNotifications.size();
                        if (newNotificationsCount - availableSize > 0) {
                            savedNotifications = new ArrayList<ServerNotification> (savedNotifications.subList(
                                    newNotificationsCount - availableSize, savedNotifications.size()));
                        }

                        // Adding unread saved notifications count to the returned count
                        for (int i = savedNotifications.size() - 1; i >= 0; i--) {
                            ServerNotification notification = savedNotifications.get(i);
                            if (notification.getIsNew() == null || !notification.getIsNew())
                                break;

                            newNotificationsCount++;
                        }

                        // Adding returned notifications to the saved ones
                        savedNotifications.addAll(notificationsResponse.getNotifications());
                        getDataManager().setNotifications(savedNotifications);

                        //Reverse notifications to get the most recent at beginning
                        Collections.reverse(savedNotifications);

                        if (isViewAttached()) {
                            getMvpView().onNotificationsConsumed(savedNotifications, newNotificationsCount);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (isViewAttached()) {
                            getMvpView().hideLoadingDialog();
                            // handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                        }
                    }
                });
    }

    @Override
    public void setAllNotificationsAsOld() {
        ArrayList<ServerNotification> savedNotifications = getDataManager().getSavedNotifications();

        for (int i = savedNotifications.size() - 1; i >= 0; i--) {
            ServerNotification notification = savedNotifications.get(i);
            if (notification.getIsNew() == null || !notification.getIsNew())
                break;

            notification.setIsNew(false);
        }

        getDataManager().setNotifications(savedNotifications);
    }
}
