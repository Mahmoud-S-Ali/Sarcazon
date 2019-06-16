package com.joyapeak.sarcazon.data.network;

import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramRequest;
import com.joyapeak.sarcazon.data.network.model.server.BaseItemsRequest;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryRequest;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentRequest;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardRequest;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationRequest;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.register.RegisterRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

public interface ApiHelper {

    int SMALL_COUNT_PER_REQUEST = 4;
    int MEDIUM_COUNT_PER_REQUEST = 8;
    int LARGE_COUNT_PER_REQUEST = 10;
    int X_LARGE_COUNT_PER_REQUEST = 20;


    public interface NetworkCallStatusCallback {
        void onNetworkCallStarted(String apiEndpoint);
        void onNetworkCallReturned(String apiEndpoint, boolean isSuccessful);
    }

    class ACTION_TYPES {
        public static final String LIKE = "like";
        public static final String DISLIKE = "dislike";
        public static final String REPORT = "report";
    }

    class COMIC_TYPES {

        private COMIC_TYPES() {}

        public static final int IMAGE = 1;
        public static final int GIF = 2;
        public static final int VIDEO_YOUTUBE = 3;
        public static final int VIDEO_FACEBOOK = 4;
        public static final int VIDEO_INSTAGRAM = 5;
        public static final int LEADERBOARD_RESULTS = 6;
    }

    class REPORT_TYPES {

        private REPORT_TYPES(){}

        public static final int NUDITY = 0;
        public static final int HATE = 1;
        public static final int LANGUAGE = 2;
        public static final int VIOLENCE  = 3;
        public static final int SPAM  = 4;
        public static final int MANUAL  = 5;
    }

    class REPORT_CONTENT_TYPES {
        public static final int COMIC = 0;
        public static final int COMMENT = 1;
        public static final int REPLY = 2;
    }

    class CONTENT_REVIEW_ACTIONS {
        public static final int BLOCK = 1;
        public static final int ALLOW = 2;
    }

    class USER_REVIEW_ACTIONS {
        public static final int NOTHING = 0;
        public static final int BLOCK = 1;
        public static final int WARNING = 2;
        public static final int BLOCK_WEEK = 3;
        public static final int BLOCK_MONTH = 4;
    }

    class COMIC_SOURCES {

        private COMIC_SOURCES(){}

        public static final int FEATURED = 0;
        public static final int ALL = 1;
        public static final int SUBSCRIPTIONS = 2;
        public static final int PENDING_FEATURED = 3;
        public static final int PENDING_REVIEW = 4;
        public static final int DEFAULT = FEATURED;
    }

    class NOTIFICATION_TYPES {

        private NOTIFICATION_TYPES(){}

        public static final String NOT_FEATURED_COMICS = "featured";
        public static final String NOT_COMIC_FEATURED = "comic-featured";
        public static final String NOT_COMIC_LIKE = "comic-like";
        public static final String NOT_COMMENT = "comic-comment";
        public static final String NOT_COMMENT_LIKE = "comment-like";
        public static final String NOT_REPLY = "comment-reply";
        public static final String NOT_REPLY_LIKE = "reply-like";
        public static final String NOT_FOLLOW = "follow-follow";
        public static final String NOT_CREATE_LEADER_BOARD = "leaderboard-create_result";
    }

    class CONTROL_FLAG_TYPES {
        public static final int NORMAL = 0;
        public static final int ADMIN = 1;
        public static final int MODERATOR = 2;
    }

    class FEATURE_STATUS_TYPES {
        public static final int NORMAL = 0;
        public static final int PENDING = 1;
        public static final int FEATURED = 2;
    }

    class LEADERBOARD_SOURCES {
        public static final String ALL_TIME = "all_time";
        public static final String WEEKLY = "weekly";
        public static final String PREV_WEEK = "weekly_leaderboard_result";
    }

    String INSTAGRAM_BASE_URL = "https://www.instagram.com/";


    ApiHeader getApiHeader();

    void setApiVersionName(String apiVersionName);

    // User api calls
    void registerNewUser(RegisterRequest.BasicRegisterRequest request, ServerResult result);

    void postLogTime(UserRequest.LogTimeRequest request, ServerResult result);

    void postLogin(UserRequest.LoginRequest request, ServerResult result);

    void getEmailExistence(UserRequest.EmailExistenceRequest request, ServerResult result);

    void getUserControlFlag(UserRequest.UserControlFlagRequest request, ServerResult result);


    // Comic api calls
    void putComicPhoto(PhotoRequest.PhotoUploadRequest request, ServerResult result);

    void postComicUrl(ComicRequest.ComicUrlUploadRequest request, ServerResult result);

    void getNewComics(ComicRequest.NewComicsRequest request, ServerResult result);

    void getSpecificComic(ComicRequest.SpecificComicRequest request, ServerResult result);

    void postComicLike(ComicRequest.ComicLikeRequest request, ServerResult result);

    void postComicDelete(ComicRequest.DeleteRequest request, ServerResult result);

    void postComicHide(ComicRequest.HideRequest request, ServerResult result);

    void postComicReport(OtherRequest.ReportRequest request, ServerResult result);

    void postComicBlock(long comicId, ServerResult result);

    void postComicFeatured(ComicRequest.AddFeaturedRequest request, ServerResult result);

    void postComicViewed(ComicRequest.MarkViewedRequest request, ServerResult result);


    // Category api calls
    void getCategories(CategoryRequest.CategoriesRequest request, ServerResult result);


    // Comment api calls
    void postNewComment(CommentRequest.CommentAddRequest request, ServerResult result);

    void getNewComments(CommentRequest.NewCommentsRequest request, ServerResult result);

    void getComment(CommentRequest.SpecificCommentRequest request, ServerResult result);

    void postCommentLike(CommentRequest.CommentLikeRequest request, ServerResult result);

    void postCommentDelete(CommentRequest.CommentDeleteRequest request, ServerResult result);

    void postCommentReport(OtherRequest.ReportRequest request, ServerResult result);

    void postCommentBlock(long commentId, ServerResult result);


    // Reply api calls
    void postNewReply(CommentRequest.ReplyAddRequest request, ServerResult result);

    void getNewReplies(CommentRequest.NewRepliesRequest request, ServerResult result);

    void postReplyLike(CommentRequest.CommentLikeRequest request, ServerResult result);

    void postReplyDelete(CommentRequest.CommentDeleteRequest request, ServerResult result);

    void postReplyReport(OtherRequest.ReportRequest request, ServerResult result);

    void postReplyBlock(long replyId, ServerResult result);


    // Profile api calls
    void getProfileInfo(ProfileRequest.ProfileInfoRequest request, ServerResult result);

    void getProfileComics(ProfileRequest.ProfileComicsRequest request, ServerResult result);

    void postUpdateProfileInfo(ProfileRequest.ProfileUpdateInfoRequest request, ServerResult result);

    void postUpdateNotificationToken(ProfileRequest.ProfileUpdateInfoRequest request, ServerResult result);

    void putUpdateProfilePhoto(PhotoRequest.PhotoUploadRequest request, ServerResult result);


    // Subs api calls
    void postSubscribe(SubsRequest.SubRequest request, final ServerResult result);

    void getSubscribers(SubsRequest.UserSubsRequest request, final ServerResult result);

    void getSubscriptions(SubsRequest.UserSubsRequest request, final ServerResult result);


    // Notifications
    void getNotifications(NotificationRequest.ServerNotificationsRequest request, ServerResult result);


    // Leaderboard
    void getLeaderboard(LeaderboardRequest.LeaderboardInfoRequest request, ServerResult result);


    // Instagram video
    void getInstagramVideoData(InstagramRequest.InstagramVideoRequest request, ServerResult result);


    // Other
    void postReleaseFeaturedComics(OtherRequest.FeaturedReleaseRequest request, ServerResult result);

    void postNextFeaturedWaitingHours(OtherRequest.NextFeaturedWaitingHoursRequest request, ServerResult result);

    void postMarkReviewed(OtherRequest.MarkReviewedRequest request, ServerResult result);

    void getPendingCommentsForReview(BaseItemsRequest request, ServerResult result);
}
