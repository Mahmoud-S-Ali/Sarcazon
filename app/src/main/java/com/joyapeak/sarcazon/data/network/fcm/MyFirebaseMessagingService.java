package com.joyapeak.sarcazon.data.network.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.prefs.AppPreferencesHelper;
import com.joyapeak.sarcazon.ui.leaderboard.LeaderboardActivity;
import com.joyapeak.sarcazon.ui.newmain.NewMainActivity;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.utils.EventBusUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

/**
 * Handles all app notifications
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public final static String FIREBASE_TOPIC_ALL = "all";
    public final static String FIREBASE_TOPIC_TEST = "test";

    private final static int ID_NOTIFICATION_COMIC_GENERAL = 0;
    private final static int ID_NOTIFICATION_COMIC_FEATURED = 1;
    private final static int ID_NOTIFICATION_COMIC_LIKE = 2;
    private final static int ID_NOTIFICATION_COMMENT = 2;
    private final static int ID_NOTIFICATION_COMMENT_LIKE = 2;
    private final static int ID_NOTIFICATION_REPLY = 2;
    private final static int ID_NOTIFICATION_REPLY_LIKE = 2;
    private final static int ID_NOTIFICATION_SUB = 2;

    public static final String EXTRA_NOTIFICATION_ACTION_TYPE = "EXTRA_NOTIFICATION_ACTION_TYPE";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Timber.d("Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        // Send an event with the refreshed token
        EventBus.getDefault().post(new EventBusUtils.EventTokenRefreshed());
    }

    // Firebase subscription
    public static void setSubscribeToFirebaseTopic(String topicName, boolean shouldSubscribe) {
        if (shouldSubscribe) {
            FirebaseMessaging.getInstance().subscribeToTopic(topicName);

        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName);
        }
    }



    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                Timber.d("Message data payload: " + remoteMessage.getData());

                String type = String.valueOf(remoteMessage.getData().get("type"));
                String action = String.valueOf(remoteMessage.getData().get("action"));
                String content = String.valueOf(remoteMessage.getData().get("msg"));

                String actionType = "";
                if (type != null) {
                    actionType += type;
                    if (action != null) {
                        actionType += "-" + action;
                    }
                }

                switch (actionType) {
                    case ApiHelper.NOTIFICATION_TYPES.NOT_CREATE_LEADER_BOARD:
                        startActivity(LeaderboardActivity.getStartIntent(this, true));
                        break;

                    case ApiHelper.NOTIFICATION_TYPES.NOT_COMIC_FEATURED:
                        sendComicFeaturedNotification();
                        break;

                    case ApiHelper.NOTIFICATION_TYPES.NOT_COMIC_LIKE:
                        sendComicLikeNotification();
                        break;

                    case ApiHelper.NOTIFICATION_TYPES.NOT_COMMENT:
                        sendCommentNotification();
                        break;

                    case ApiHelper.NOTIFICATION_TYPES.NOT_COMMENT_LIKE:
                        sendCommentLikeNotification();
                        break;

                    case ApiHelper.NOTIFICATION_TYPES.NOT_REPLY:
                        sendReplyNotification();
                        break;

                    case ApiHelper.NOTIFICATION_TYPES.NOT_REPLY_LIKE:
                        sendReplyLikeNotification();
                        break;

                    case ApiHelper.NOTIFICATION_TYPES.NOT_FOLLOW:
                        sendSubNotification();
                        break;

                    default:
                        // TODO: Featured notifications should be unique and not under general
                        AppPreferencesHelper appPreferencesHelper = new AppPreferencesHelper(this);
                        appPreferencesHelper.setNewFeaturedCount(1);

                        sendGeneralNotification(content);
                        break;
                }

            } catch (Exception ex) {
                Timber.e(ex.getMessage());
            }
        }
    }
    // [END receive_message]


    private void sendGeneralNotification(String msg) {

        sendNotification(ID_NOTIFICATION_COMIC_GENERAL,
                "General",
                "general",
                msg,
                getIntentToMainPage(ID_NOTIFICATION_COMIC_GENERAL));
    }
    private void sendComicFeaturedNotification() {

        sendNotification(ID_NOTIFICATION_COMIC_FEATURED,
                "Comic Featured",
                "comic_featured",
                getString(R.string.notif_comic_featured),
                getIntentToProfilePage(ID_NOTIFICATION_COMIC_FEATURED));
    }
    private void sendComicLikeNotification() {

        sendNotification(ID_NOTIFICATION_COMIC_LIKE,
                "Comic Like",
                "comic_like",
                getString(R.string.notif_comic_like),
                getIntentToProfilePage(ID_NOTIFICATION_COMIC_LIKE));
    }
    private void sendCommentNotification() {

        sendNotification(ID_NOTIFICATION_COMMENT,
                "Comment",
                "comment",
                getString(R.string.notif_comment),
                getIntentToProfilePage(ID_NOTIFICATION_COMMENT));
    }
    private void sendCommentLikeNotification() {

        sendNotification(ID_NOTIFICATION_COMMENT_LIKE,
                "Comment Like",
                "comment_like",
                getString(R.string.notif_comment_like),
                getIntentToProfilePage(ID_NOTIFICATION_COMMENT_LIKE));
    }
    private void sendReplyNotification() {

        sendNotification(ID_NOTIFICATION_REPLY,
                "Reply",
                "reply",
                getString(R.string.notif_reply),
                getIntentToProfilePage(ID_NOTIFICATION_REPLY));
    }
    private void sendReplyLikeNotification() {

        sendNotification(ID_NOTIFICATION_REPLY_LIKE,
                "Reply Like",
                "reply_like",
                getString(R.string.notif_reply_like),
                getIntentToProfilePage(ID_NOTIFICATION_REPLY_LIKE));
    }
    private void sendSubNotification() {

        sendNotification(ID_NOTIFICATION_SUB,
                "Subscribe",
                "subscribe",
                getString(R.string.notif_sub),
                getIntentToProfilePage(ID_NOTIFICATION_SUB));
    }

    private void sendNotification(int notificationId, String channelId, String channelName,
                                  String msg, PendingIntent pendingIntent) {

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int smallIconId = R.drawable.ic_notification;

        Bitmap largeIconBitmap = ImageUtils.drawableToBitmap(ContextCompat.getDrawable(
                this, R.mipmap.ic_launcher));

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(smallIconId)
                .setLargeIcon(largeIconBitmap)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[] {0, 200, 100, 200, 0})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH));
        }

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private PendingIntent getIntentToMainPage(int notificationId) {
        Intent mainIntent = new Intent(this, NewMainActivity.class);
        mainIntent.setAction(Intent.ACTION_DEFAULT);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // TODO: Should only be for featured comics and removed from any other general notifications
        mainIntent.putExtra(EXTRA_NOTIFICATION_ACTION_TYPE, ApiHelper.NOTIFICATION_TYPES.NOT_FEATURED_COMICS);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, notificationId,
                new Intent[] {mainIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
    private PendingIntent getIntentToProfilePage(int notificationId) {
        Intent mainIntent = new Intent(this, NewMainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent notificationsIntent = new Intent(this, ProfileActivity.class);
        notificationsIntent.setAction(Intent.ACTION_DEFAULT);
        notificationsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivities(this, notificationId,
                new Intent[] {mainIntent, notificationsIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
