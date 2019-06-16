package com.joyapeak.sarcazon.data.network;

import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramRequest;
import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramResponse;
import com.joyapeak.sarcazon.data.network.model.server.BaseItemsRequest;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryRequest;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicRequest;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentRequest;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardRequest;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardResponse;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationRequest;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherRequest;
import com.joyapeak.sarcazon.data.network.model.server.other.OtherResponse;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoRequest;
import com.joyapeak.sarcazon.data.network.model.server.photo.PhotoResponse;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsRequest;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsResponse;
import com.joyapeak.sarcazon.data.network.model.server.user.UserRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.UserResponse;
import com.joyapeak.sarcazon.data.network.model.server.user.register.RegisterRequest;
import com.joyapeak.sarcazon.data.network.model.server.user.register.RegisterResponse;
import com.joyapeak.sarcazon.data.network.retrofit.RetrofitService;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.utils.CommonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.joyapeak.sarcazon.data.network.ApiEndPoint.EP_COMIC_GET_NEW;
import static com.joyapeak.sarcazon.data.network.ApiEndPoint.EP_COMIC_GET_SPECIFIC;
import static com.joyapeak.sarcazon.data.network.ApiEndPoint.EP_COMIC_POST_URL;
import static com.joyapeak.sarcazon.data.network.ApiEndPoint.EP_COMIC_PUT_PHOTO;

/**
 * Created by Mahmoud Ali on 4/16/2018.
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    private static String sApiVersionName = "";

    private RetrofitService mRetrofitService;

    @Inject
    public AppApiHelper(ApiHeader apiHeader, RetrofitService retrofitService) {
        mApiHeader = apiHeader;
        mRetrofitService = retrofitService;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }


    @Override
    public void setApiVersionName(String apiVersionName) {
        sApiVersionName = apiVersionName;
    }

    // User api calls handling
    @Override
    public void registerNewUser(final RegisterRequest.BasicRegisterRequest request, final ServerResult result) {
        mRetrofitService
                .getUserAPI()
                .postBasicRegister(
                        sApiVersionName,
                        ApiEndPoint.EP_USER_CREATE_NEW,
                        getApiHeader().getPublicApiHeader().getStrMap())

                .enqueue(
                        new Callback<RegisterResponse.BasicRegisterResponse>() {
                            @Override
                            public void onResponse(Call<RegisterResponse.BasicRegisterResponse> call,
                                                   Response<RegisterResponse.BasicRegisterResponse> response) {

                                Timber.d("Register new user returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<RegisterResponse.BasicRegisterResponse> call, Throwable t) {
                                Timber.e("Register new user failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postLogTime(UserRequest.LogTimeRequest request, final ServerResult result) {
        mRetrofitService
                .getUserAPI()
                .postLogTime(
                        sApiVersionName,
                        ApiEndPoint.EP_USER_LOG_TIME,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {

                                Timber.d("Send log time returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Send log time failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postLogin(UserRequest.LoginRequest request, final ServerResult result) {
        mRetrofitService
                .getUserAPI()
                .postLogin(
                        sApiVersionName,
                        ApiEndPoint.EP_USER_POST_LOGIN,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getEmail(),
                        request.getPassword())

                .enqueue(
                        new Callback<UserResponse.LoginResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse.LoginResponse> call,
                                                   Response<UserResponse.LoginResponse> response) {
                                Timber.d("Login returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<UserResponse.LoginResponse> call, Throwable t) {
                                Timber.e("Login failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getEmailExistence(UserRequest.EmailExistenceRequest request, final ServerResult result) {
        mRetrofitService
                .getUserAPI()
                .getEmailExists(
                        sApiVersionName,
                        ApiEndPoint.EP_USER_GET_EMAIL_EXISTENCE,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getEmail())

                .enqueue(
                        new Callback<UserResponse.EmailExistenceResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse.EmailExistenceResponse> call,
                                                   Response<UserResponse.EmailExistenceResponse> response) {
                                Timber.d("Email existence returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<UserResponse.EmailExistenceResponse> call, Throwable t) {
                                Timber.e("Email existence failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getUserControlFlag(UserRequest.UserControlFlagRequest request, final ServerResult result) {

        mRetrofitService
                .getUserAPI()
                .getControlFlag(
                        sApiVersionName,
                        ApiEndPoint.EP_USER_GET_CONTROL_FLAG,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId())

                .enqueue(
                        new Callback<UserResponse.UserControlFlagResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse.UserControlFlagResponse> call,
                                                   Response<UserResponse.UserControlFlagResponse> response) {
                                Timber.d("User Control Flag returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<UserResponse.UserControlFlagResponse> call, Throwable t) {
                                Timber.e("User Control Flag failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Comic api calls handling
    @Override
    public void putComicPhoto(PhotoRequest.PhotoUploadRequest request, final ServerResult result) {

        mRetrofitService
                .getComicAPI()
                .putComicPhoto(
                        sApiVersionName,
                        EP_COMIC_PUT_PHOTO,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getRequestBody(),
                        request.getSizesInBytes())

                .enqueue(
                        new Callback<PhotoResponse.PhotoUploadResponse>() {
                            @Override
                            public void onResponse(Call<PhotoResponse.PhotoUploadResponse> call,
                                                   Response<PhotoResponse.PhotoUploadResponse> response) {
                                Timber.d("Put comic photo returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<PhotoResponse.PhotoUploadResponse> call, Throwable t) {
                                Timber.e("Put comic photo failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicUrl(ComicRequest.ComicUrlUploadRequest request, final ServerResult result) {
        mRetrofitService
                .getComicAPI()
                .postComicUrl(
                        sApiVersionName,
                        EP_COMIC_POST_URL,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId(),
                        request.getComicType(),
                        request.getComicUrl(),
                        request.getComicVideoSourceUrl(),
                        request.getVideoComicId(),
                        request.getComicCaption(),
                        request.getCategory(),
                        request.getTags(),
                        request.getCredits(),
                        request.getAspectRatio())

                .enqueue(
                        new Callback<ComicResponse.ComicUrlUploadResponse>() {
                            @Override
                            public void onResponse(Call<ComicResponse.ComicUrlUploadResponse> call,
                                                   Response<ComicResponse.ComicUrlUploadResponse> response) {

                                Timber.d("Upload comic with url returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ComicResponse.ComicUrlUploadResponse> call, Throwable t) {
                                Timber.e("Upload comic with url failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getNewComics(ComicRequest.NewComicsRequest request, final ServerResult result) {

        mRetrofitService
                .getComicAPI()
                .getNewComics(
                        sApiVersionName,
                        EP_COMIC_GET_NEW,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getType(),
                        request.getCategory(),
                        request.getBaseId(),
                        request.getOffset(),
                        request.getCount())

                .enqueue(
                        new Callback<ComicResponse.NewComicsResponse>() {
                            @Override
                            public void onResponse(Call<ComicResponse.NewComicsResponse> call,
                                                   Response<ComicResponse.NewComicsResponse> response) {
                                Timber.d("Get new comics returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ComicResponse.NewComicsResponse> call, Throwable t) {
                                Timber.d("Get new comics failed");
                                Timber.d(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getSpecificComic(ComicRequest.SpecificComicRequest request, final ServerResult result) {
        mRetrofitService
                .getComicAPI()
                .getSpecificComic(
                        sApiVersionName,
                        EP_COMIC_GET_SPECIFIC,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId())

                .enqueue(
                        new Callback<ComicResponse.SingleComic>() {
                            @Override
                            public void onResponse(Call<ComicResponse.SingleComic> call,
                                                   Response<ComicResponse.SingleComic> response) {
                                Timber.d("Get specific comic returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ComicResponse.SingleComic> call, Throwable t) {
                                Timber.e("Get specific comic failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicLike(ComicRequest.ComicLikeRequest request, final ServerResult result) {

        mRetrofitService
                .getComicAPI()
                .postLike(
                        sApiVersionName,
                        ApiEndPoint.EP_COMIC_LIKE,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId(),
                        request.getAction())

                .enqueue(
                        new Callback<OtherResponse.LikeResponse>() {
                            @Override
                            public void onResponse(Call<OtherResponse.LikeResponse> call,
                                                   Response<OtherResponse.LikeResponse> response) {
                                Timber.d("Post comic like returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<OtherResponse.LikeResponse> call, Throwable t) {
                                Timber.e("Post comic like failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicReport(OtherRequest.ReportRequest request, final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postReport(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_REPORT,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        REPORT_CONTENT_TYPES.COMIC,
                        request.getContentId(),
                        request.getReportType())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Reporting comic returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Reporting comic failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicDelete(ComicRequest.DeleteRequest request, final ServerResult result) {

        mRetrofitService
                .getComicAPI()
                .postDelete(
                        sApiVersionName,
                        ApiEndPoint.EP_COMIC_Delete,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId())

                .enqueue(
                        new Callback<ComicResponse.DeleteResponse>() {
                            @Override
                            public void onResponse(Call<ComicResponse.DeleteResponse> call,
                                                   Response<ComicResponse.DeleteResponse> response) {
                                Timber.d("Deleting comic returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ComicResponse.DeleteResponse> call, Throwable t) {
                                Timber.e("Deleting comic failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicHide(ComicRequest.HideRequest request, final ServerResult result) {
        mRetrofitService
                .getComicAPI()
                .postHide(
                        sApiVersionName,
                        ApiEndPoint.EP_COMIC_Hide,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId())

                .enqueue(
                        new Callback<ComicResponse.HideResponse>() {
                            @Override
                            public void onResponse(Call<ComicResponse.HideResponse> call,
                                                   Response<ComicResponse.HideResponse> response) {
                                Timber.d("Hiding comic returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ComicResponse.HideResponse> call, Throwable t) {
                                Timber.e("Hiding comic failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicBlock(long comicId, final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postReport(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_REPORT,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        REPORT_CONTENT_TYPES.COMIC,
                        comicId,
                        REPORT_TYPES.MANUAL)

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Blocking comic returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Blocking comic failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicFeatured(ComicRequest.AddFeaturedRequest request, final ServerResult result) {

        mRetrofitService
                .getComicAPI()
                .postFeatured(
                        sApiVersionName,
                        ApiEndPoint.EP_COMIC_POST_FEATURED,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId())

                .enqueue(
                        new Callback<ComicResponse.AddFeaturedResponse>() {
                            @Override
                            public void onResponse(Call<ComicResponse.AddFeaturedResponse> call,
                                                   Response<ComicResponse.AddFeaturedResponse> response) {
                                Timber.d("Add Featured comic returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ComicResponse.AddFeaturedResponse> call, Throwable t) {
                                Timber.e("Add Featured comic failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postComicViewed(ComicRequest.MarkViewedRequest request, final ServerResult result) {

        mRetrofitService
                .getComicAPI()
                .postViewed(
                        sApiVersionName,
                        ApiEndPoint.EP_COMIC_POST_VIEWED,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Comic Viewed returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Comic Viewed failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Category api calls
    @Override
    public void getCategories(CategoryRequest.CategoriesRequest request, final ServerResult result) {
        mRetrofitService
                .getCategoryAPI()
                .getCategories(
                        sApiVersionName,
                        ApiEndPoint.EP_COMIC_POST_VIEWED,
                        getApiHeader().getProtectedApiHeader().getStrMap())

                .enqueue(
                        new Callback<CategoryResponse.CategoriesResponse>() {
                            @Override
                            public void onResponse(Call<CategoryResponse.CategoriesResponse> call,
                                                   Response<CategoryResponse.CategoriesResponse> response) {
                                Timber.d("Categories returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<CategoryResponse.CategoriesResponse> call, Throwable t) {
                                Timber.e("Categories failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Comment api calls handling
    @Override
    public void postNewComment(CommentRequest.CommentAddRequest request, final ServerResult result) {

        mRetrofitService
                .getCommentAPI()
                .postNewComment(
                        sApiVersionName,
                        ApiEndPoint.EP_COMMENT_ADD,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getComicId(),
                        request.getComment())

                .enqueue(
                        new Callback<CommentResponse.CommentAddResponse>() {
                            @Override
                            public void onResponse(Call<CommentResponse.CommentAddResponse> call,
                                                   Response<CommentResponse.CommentAddResponse> response) {
                                Timber.d("Adding new comment returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<CommentResponse.CommentAddResponse> call, Throwable t) {
                                Timber.e("Adding new comment failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postCommentLike(CommentRequest.CommentLikeRequest request, final ServerResult result) {

        mRetrofitService
                .getCommentAPI()
                .postCommentLike(
                        sApiVersionName,
                        ApiEndPoint.EP_COMMENT_LIKE,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getCommentId(),
                        request.getAction(),
                        CommonUtils.booleanToInt(request.getIsPositiveAction()),
                        CommonUtils.booleanToInt(request.getIsLiked()),
                        CommonUtils.booleanToInt(request.getIsDisliked()))

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Timber.d("Like comment returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Like comment failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postCommentReport(OtherRequest.ReportRequest request, final ServerResult result) {
        mRetrofitService
                .getOtherAPI()
                .postReport(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_REPORT,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        REPORT_CONTENT_TYPES.COMMENT,
                        request.getContentId(),
                        request.getReportType())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Report comment returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Report comment failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postCommentDelete(CommentRequest.CommentDeleteRequest request, final ServerResult result) {

        mRetrofitService
                .getCommentAPI()
                .postCommentDelete(
                        sApiVersionName,
                        ApiEndPoint.EP_COMMENT_DELETE,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getCommentId())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Delete comment returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Delete comment failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postCommentBlock(long commentId, final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postReport(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_REPORT,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        REPORT_CONTENT_TYPES.COMMENT,
                        commentId,
                        REPORT_TYPES.MANUAL)

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Block comment returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Block comment failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getNewComments(CommentRequest.NewCommentsRequest request, final ServerResult result) {

        mRetrofitService
                .getCommentAPI()
                .getNewComments(
                        sApiVersionName,
                        ApiEndPoint.EP_COMMENT_GET_NEW,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getComicId(),
                        request.getBaseId(),
                        request.getOffset(),
                        request.getCount())

                .enqueue(
                        new Callback<CommentResponse.NewCommentsResponse>() {
                            @Override
                            public void onResponse(Call<CommentResponse.NewCommentsResponse> call,
                                                   Response<CommentResponse.NewCommentsResponse> response) {
                                Timber.d("Get new comments returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<CommentResponse.NewCommentsResponse> call, Throwable t) {
                                Timber.d("Get new comments failed");
                                Timber.d(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getComment(CommentRequest.SpecificCommentRequest request, final ServerResult result) {
        mRetrofitService
                .getCommentAPI()
                .getSpecificComment(
                        sApiVersionName,
                        ApiEndPoint.EP_COMMENT_GET_SPECIFIC,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getCommentId())

                .enqueue(
                        new Callback<CommentResponse.ComicComment>() {
                            @Override
                            public void onResponse(Call<CommentResponse.ComicComment> call,
                                                   Response<CommentResponse.ComicComment> response) {
                                Timber.d("Get comment returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<CommentResponse.ComicComment> call, Throwable t) {
                                Timber.d("Get comment failed");
                                Timber.d(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postNewReply(CommentRequest.ReplyAddRequest request, final ServerResult result) {
        mRetrofitService
                .getCommentAPI()
                .postNewReply(
                        sApiVersionName,
                        ApiEndPoint.EP_REPLY_ADD,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getCommentId(),
                        request.getComment())

                .enqueue(
                        new Callback<CommentResponse.ReplyAddResponse>() {
                            @Override
                            public void onResponse(Call<CommentResponse.ReplyAddResponse> call,
                                                   Response<CommentResponse.ReplyAddResponse> response) {
                                Timber.d("Adding reply returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<CommentResponse.ReplyAddResponse> call, Throwable t) {
                                Timber.e("Adding reply failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getNewReplies(CommentRequest.NewRepliesRequest request, final ServerResult result) {
        mRetrofitService
                .getCommentAPI()
                .getNewReplies(
                        sApiVersionName,
                        ApiEndPoint.EP_REPLY_GET_NEW,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getCommentId(),
                        request.getBaseId(),
                        request.getOffset(),
                        request.getCount())

                .enqueue(
                        new Callback<CommentResponse.NewRepliesResponse>() {
                            @Override
                            public void onResponse(Call<CommentResponse.NewRepliesResponse> call,
                                                   Response<CommentResponse.NewRepliesResponse> response) {
                                Timber.d("Getting new replies returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<CommentResponse.NewRepliesResponse> call, Throwable t) {
                                Timber.e("Getting new replies failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postReplyLike(CommentRequest.CommentLikeRequest request, final ServerResult result) {
        mRetrofitService
                .getCommentAPI()
                .postReplyLike(
                        sApiVersionName,
                        ApiEndPoint.EP_REPLY_POST_LIKE,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getCommentId(),
                        request.getAction(),
                        CommonUtils.booleanToInt(request.getIsPositiveAction()),
                        CommonUtils.booleanToInt(request.getIsLiked()),
                        CommonUtils.booleanToInt(request.getIsDisliked()))

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Timber.d("Like reply returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Like reply failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postReplyReport(OtherRequest.ReportRequest request, final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postReport(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_REPORT,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        REPORT_CONTENT_TYPES.REPLY,
                        request.getContentId(),
                        request.getReportType())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Report reply returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Report reply failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postReplyDelete(CommentRequest.CommentDeleteRequest request, final ServerResult result) {

        mRetrofitService
                .getCommentAPI()
                .postReplyDelete(
                        sApiVersionName,
                        ApiEndPoint.EP_REPLY_POST_DELETE,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getCommentId())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Delete reply returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Delete reply failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postReplyBlock(long replyId, final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postReport(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_REPORT,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        REPORT_CONTENT_TYPES.REPLY,
                        replyId,
                        REPORT_TYPES.MANUAL)

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Block reply returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Block reply failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Profile api calls handling
    @Override
    public void getProfileInfo(ProfileRequest.ProfileInfoRequest request, final ServerResult result) {
        mRetrofitService
                .getProfileAPI()
                .getProfileInfo(
                        sApiVersionName,
                        ApiEndPoint.EP_PROFILE_GET_INFO,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getViewerId())

                .enqueue(
                        new Callback<ProfileResponse.ProfileInfoResponse>() {
                            @Override
                            public void onResponse(Call<ProfileResponse.ProfileInfoResponse> call,
                                                   Response<ProfileResponse.ProfileInfoResponse> response) {
                                Timber.d("Get user info returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ProfileResponse.ProfileInfoResponse> call, Throwable t) {
                                Timber.e("Get user info failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getProfileComics(ProfileRequest.ProfileComicsRequest request, final ServerResult result) {
        mRetrofitService
                .getProfileAPI()
                .getProfileComics(
                        sApiVersionName,
                        ApiEndPoint.EP_PROFILE_GET_COMICS,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getBaseId(),
                        request.getOffset(),
                        request.getCount())

                .enqueue(
                        new Callback<ProfileResponse.ProfileComicsResponse>() {
                            @Override
                            public void onResponse(Call<ProfileResponse.ProfileComicsResponse> call,
                                                   Response<ProfileResponse.ProfileComicsResponse> response) {
                                Timber.d("Get user comics returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<ProfileResponse.ProfileComicsResponse> call, Throwable t) {
                                Timber.e("Get user comics failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postUpdateProfileInfo(ProfileRequest.ProfileUpdateInfoRequest request, final ServerResult result) {
        mRetrofitService
                .getProfileAPI()
                .postProfileUpdateInfo(
                        sApiVersionName,
                        ApiEndPoint.EP_PROFILE_POST_UPDATE_INFO,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getPhotoUrl(),
                        request.getName(),
                        request.getQuote(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getNotificationToken())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Timber.d("Update user info returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Update user info failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postUpdateNotificationToken(ProfileRequest.ProfileUpdateInfoRequest request, final ServerResult result) {
        postUpdateProfileInfo(request, result);
    }

    @Override
    public void putUpdateProfilePhoto(PhotoRequest.PhotoUploadRequest request, final ServerResult result) {
        mRetrofitService
                .getProfileAPI()
                .putProfilePhoto(
                        sApiVersionName,
                        ApiEndPoint.EP_PROFILE_PUT_UPDATE_PHOTO,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getRequestBody(),
                        request.getSizesInBytes())

                .enqueue(
                        new Callback<PhotoResponse.PhotoUploadResponse>() {
                            @Override
                            public void onResponse(Call<PhotoResponse.PhotoUploadResponse> call,
                                                   Response<PhotoResponse.PhotoUploadResponse> response) {
                                Timber.d("Put user photo returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<PhotoResponse.PhotoUploadResponse> call, Throwable t) {
                                Timber.e("Put user photo failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Subs api calls handling
    @Override
    public void postSubscribe(SubsRequest.SubRequest request, final ServerResult result) {
        mRetrofitService
                .getSubsAPI()
                .postSub(
                        sApiVersionName,
                        ApiEndPoint.EP_SUB_POST_SUBSCRIBE,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getSubId(),
                        request.getValue())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Post subscribe returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Post subscribe failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    private void getSubs(SubsRequest.UserSubsRequest request, final ServerResult result, String endPoint) {
        mRetrofitService
                .getSubsAPI()
                .getSubs(
                        sApiVersionName,
                        endPoint,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId(),
                        request.getViewerId(),
                        request.getBaseId(),
                        request.getOffset(),
                        request.getCount())

                .enqueue(
                        new Callback<SubsResponse.UserSubsResponse>() {
                            @Override
                            public void onResponse(Call<SubsResponse.UserSubsResponse> call,
                                                   Response<SubsResponse.UserSubsResponse> response) {
                                Timber.d("Get subs returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<SubsResponse.UserSubsResponse> call, Throwable t) {
                                Timber.e("Get subs failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getSubscribers(SubsRequest.UserSubsRequest request, ServerResult result) {
        getSubs(request, result, ApiEndPoint.EP_SUB_GET_SUBSCRIBERS);
    }

    @Override
    public void getSubscriptions(SubsRequest.UserSubsRequest request, ServerResult result) {
        getSubs(request, result, ApiEndPoint.EP_SUB_GET_SUBSCRIPTIONS);
    }

    @Override
    public void getNotifications(NotificationRequest.ServerNotificationsRequest request, final ServerResult result) {
        mRetrofitService
                .getNotificationAPI()
                .getNotifications(
                        sApiVersionName,
                        ApiEndPoint.EP_NOTIFICATION,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getUserId())

                .enqueue(
                        new Callback<NotificationResponse.ServerNotificationResponse>() {
                            @Override
                            public void onResponse(Call<NotificationResponse.ServerNotificationResponse> call,
                                                   Response<NotificationResponse.ServerNotificationResponse> response) {
                                Timber.d("Get notifications returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<NotificationResponse.ServerNotificationResponse> call, Throwable t) {
                                Timber.e("Get notifications failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Leaderboard
    @Override
    public void getLeaderboard(LeaderboardRequest.LeaderboardInfoRequest request, final ServerResult result) {

        mRetrofitService
                .getLeaderboardAPI()
                .getLeaderboardInfo(
                        sApiVersionName,
                        ApiEndPoint.EP_LEADERBOARD,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getLeaderboardSource())

                .enqueue(
                        new Callback<LeaderboardResponse.LeaderboardInfoResponse>() {
                            @Override
                            public void onResponse(Call<LeaderboardResponse.LeaderboardInfoResponse> call,
                                                   Response<LeaderboardResponse.LeaderboardInfoResponse> response) {
                                Timber.d("Get leaderboard returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<LeaderboardResponse.LeaderboardInfoResponse> call, Throwable t) {
                                Timber.e("Get leaderboard failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Instagram video
    @Override
    public void getInstagramVideoData(InstagramRequest.InstagramVideoRequest request, final ServerResult result) {
        mRetrofitService
                .getInstagramAPI()
                .getInstagramVideoData(
                        request.getVideoId(),
                        1)

                .enqueue(
                        new Callback<InstagramResponse.InstagramVideoResponse>() {
                            @Override
                            public void onResponse(Call<InstagramResponse.InstagramVideoResponse> call,
                                                   Response<InstagramResponse.InstagramVideoResponse> response) {
                                Timber.d("Get instagram video data returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<InstagramResponse.InstagramVideoResponse> call, Throwable t) {
                                Timber.e("Get subs failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postReleaseFeaturedComics(OtherRequest.FeaturedReleaseRequest request, final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postFeaturedRelease(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_POST_RELEASE_FEATURED,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getMessage(),
                        request.getTopic())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Release featured comics returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Release featured comics failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void postNextFeaturedWaitingHours(OtherRequest.NextFeaturedWaitingHoursRequest request,
                                             final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postNextFeaturedWaitingHours(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_POST_FEATURED_WAITING_HOURS,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getHours())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Next featured waiting hours returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Next featured waiting hours failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }


    // Other
    @Override
    public void postMarkReviewed(OtherRequest.MarkReviewedRequest request, final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .postMarkReviewed(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_POST_MARK_REVIEWED,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getReportId(),
                        request.getReviewAction(),
                        request.getUserAction())

                .enqueue(
                        new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call,
                                                   Response<Void> response) {
                                Timber.d("Mark reviewed returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Timber.e("Mark reviewed failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }

    @Override
    public void getPendingCommentsForReview(BaseItemsRequest request,
                                            final ServerResult result) {

        mRetrofitService
                .getOtherAPI()
                .getPendingCommentsForReview(
                        sApiVersionName,
                        ApiEndPoint.EP_OTHER_GET_PENDING_COMMENTS_FOR_REVIEW,
                        getApiHeader().getProtectedApiHeader().getStrMap(),
                        request.getBaseId(),
                        request.getOffset(),
                        request.getCount())

                .enqueue(
                        new Callback<CommentResponse.NewCommentsResponse>() {
                            @Override
                            public void onResponse(Call<CommentResponse.NewCommentsResponse> call,
                                                   Response<CommentResponse.NewCommentsResponse> response) {
                                Timber.d("Get pending comments for review returned with status: " + response.code());
                                Timber.d(response.body() == null? "" : response.body().toString());
                                result.onSuccess(response.body(), response.code());
                            }

                            @Override
                            public void onFailure(Call<CommentResponse.NewCommentsResponse> call, Throwable t) {
                                Timber.e("Get pending comments for review failed");
                                Timber.e(t.getMessage().toString());
                                result.onFailure(t);
                            }
                        }
                );
    }
}
