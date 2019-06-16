package com.joyapeak.sarcazon.data.network;

/**
 * Created by Mahmoud Ali on 4/29/2018.
 */

public class ApiEndPoint {

    public static final String EP_USER_CREATE_NEW = "user/createNewUser";

    public static final String EP_USER_LOG_TIME = "user/logTime";

    public static final String EP_USER_POST_LOGIN = "user/login";

    public static final String EP_USER_GET_EMAIL_EXISTENCE = "user/email_exists";

    public static final String EP_USER_GET_CONTROL_FLAG = "user/getControlFlag";

    public static final String EP_PROFILE_POST_UPDATE_INFO = "user/updateInfo";

    public static final String EP_PROFILE_PUT_UPDATE_PHOTO = "user/uploadPhoto";

    public static final String EP_PROFILE_GET_INFO = "user/getInfo";

    public static final String EP_PROFILE_GET_COMICS = "user/getComics";

    public static final String EP_COMIC_PUT_PHOTO = "comic/uploadComic";

    public static final String EP_COMIC_POST_URL = "comic/createNewComic";

    public static final String EP_COMIC_POST_FEATURED = "featured/addcomic";

    public static final String EP_COMIC_POST_VIEWED = "comic/viewComic";

    public static final String EP_COMIC_GET_NEW = "comic/getComic";

    public static final String EP_COMIC_GET_SPECIFIC = "comic/getComic";

    public static final String EP_COMIC_LIKE = "comic/like";

    public static final String EP_COMIC_Delete = "comic/delete";

    public static final String EP_COMIC_Hide = "comic/hide";

    public static final String EP_COMMENT_ADD = "comment/newComment";

    public static final String EP_COMMENT_GET_NEW = "comment/getComments";

    public static final String EP_COMMENT_GET_SPECIFIC = "comment/getComment";

    public static final String EP_COMMENT_LIKE = "comment/like";

    public static final String EP_COMMENT_DELETE = "comment/delete";

    public static final String EP_REPLY_ADD = "reply/newReply";

    public static final String EP_REPLY_GET_NEW = "reply/getReplies";

    public static final String EP_REPLY_POST_LIKE = "reply/like";

    public static final String EP_REPLY_POST_DELETE = "reply/delete";

    public static final String EP_SUB_POST_SUBSCRIBE = "follow/subscribe";

    public static final String EP_SUB_GET_SUBSCRIBERS = "follow/getSubscribers";

    public static final String EP_SUB_GET_SUBSCRIPTIONS = "follow/getSubscriptions";

    public static final String EP_LEADERBOARD = "leaderboard/leaderboard";

    public static final String EP_GET_CATEGORIES = "category/getCategories";

    public static final String EP_NOTIFICATION = "activity/consumeActivity";

    public static final String EP_OTHER_REPORT = "report/reportContent";

    public static final String EP_OTHER_POST_MARK_REVIEWED = "report/markReviewed";

    public static final String EP_OTHER_POST_RELEASE_FEATURED = "featured/release";

    public static final String EP_OTHER_POST_FEATURED_WAITING_HOURS = "featured/nextWaitingHours";

    public static final String EP_OTHER_GET_PENDING_COMMENTS_FOR_REVIEW = "report/getPendingCommentsForReview";
}
