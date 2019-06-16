package com.joyapeak.sarcazon.ui.profile;

import android.content.Context;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsRequest;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 5/6/2018.
 */

public class ProfilePresenter<V extends ProfileMvpView> extends BasePresenter<V>
            implements ProfileMvpPresenter<V> {

    @Inject
    public ProfilePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewInitialized(long userId) {
        boolean isOwner;

        if (userId == AppConstants.NULL_INDEX) {
            isOwner = true;

        } else {
            Long ownerUserId = getDataManager().getCurrentUserId();
            if (ownerUserId == null) {
                isOwner = false;

            } else {
                isOwner = ownerUserId.equals(userId);
            }
        }

        getMvpView().handleAfterViewInitialization(isOwner);
    }

    @Override
    public long getOwnerId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public void getProfileInfo(final long userId) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getProfileInfo(userId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();

        Long viewerId = getDataManager().getCurrentUserId();
        Long profileId = userId;
        if (profileId == AppConstants.NULL_INDEX) {
            profileId = getDataManager().getCurrentUserId();
            viewerId = null;
        }

        final boolean isOwner = viewerId == null || viewerId == profileId;

        getDataManager().getProfileInfo(new ProfileRequest.ProfileInfoRequest(
                profileId, viewerId), new ServerResult() {
            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    ProfileResponse.ProfileInfoResponse profileInfoResponse =
                            (ProfileResponse.ProfileInfoResponse) responseBody;

                    if (isOwner) {
                        profileInfoResponse.setIsSubscribed(null);
                    }

                    getMvpView().updateProfileInfo(profileInfoResponse);
                    getMvpView().hideLoadingDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                }
            }
        });

        /*// TODO: For Testing, delete later
        ProfileResponse.ProfileInfoResponse response = new ProfileResponse.ProfileInfoResponse(
                "Mahmoud",
                "https://pre00.deviantart.net/a1a8/th/pre/f/2013/202/1/5/mortal_kombat_symbol_by_yurtigo-d6eg67j.png",
                "http://digitalspyuk.cdnds.net/18/12/980x490/landscape-1521646648-avengers-assemble-series.jpg",
                "Arnabna fe manwar anwar we arnab anwar fe manwarna",
                220,
                15,
                4
        );

        getMvpView().updateProfileInfo(response);*/
    }

    @Override
    public void subscribe(final long subId, final Boolean shouldSubscribe) {
        if (!confirmRegistration()) {
            return;
        }

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                subscribe(subId, shouldSubscribe);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        getMvpView().showLoadingDialog();

        long userId = getDataManager().getCurrentUserId();
        getDataManager().postSubscribe(new SubsRequest.SubRequest(userId, subId,
                CommonUtils.booleanToInt(shouldSubscribe)), new ServerResult() {
            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    handleApiError(responseCode, handler);
                    return;
                }

                if (isViewAttached()) {
                    getMvpView().updateFollowStatus(shouldSubscribe);
                    getMvpView().hideLoadingDialog();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (isViewAttached()) {
                    getMvpView().hideLoadingDialog();
                    handleApiError(ApiErrorConstants.GENERAL_ERROR, handler);
                }
            }
        });
    }

    @Override
    public void shareProfile(Context context, long userId, String thumbnailUrl) {
        createProfileLinkForShare(context, userId, thumbnailUrl);
    }

    @Override
    public void logout() {
        setUserAsLoggedOut();
        getMvpView().onUserSetAsLoggedOut();
    }

    // Branch sharing
    // Video link operations
    private void createProfileLinkForShare(Context context, Long profileId, String thumbnailUrl) {
        getMvpView().showLoadingDialog();
        generateProfileLink(context, profileId, thumbnailUrl, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (getMvpView() != null) {
                    if (error == null) {
                        getMvpView().handleProfileShare(url);

                    } else {
                        getMvpView().showMessage("Can't share this content");
                        Timber.e(error.getMessage().toString());
                    }

                    getMvpView().hideLoadingDialog();
                }
            }
        });
    }
    private void generateProfileLink(Context context, @NonNull Long profileId, String thumbnailUrl,
                                     Branch.BranchLinkCreateListener listener) {

        BranchUniversalObject branchUniversalObj =  createBranchUniversalObject(profileId, thumbnailUrl);
        LinkProperties linkProperties = createLinkProperties();
        branchUniversalObj.generateShortUrl(context, linkProperties, listener);
    }
    private BranchUniversalObject createBranchUniversalObject(Long profileId, String thumbnailUrl) {

        String canonicalIdentifier = "profile_id/" + String.valueOf(profileId);
        String contentTitle = "Saf7ety 3ala Sarcazon";
        String contentDescription = "Sarcazon, el makan el geded lel comics";

        ContentMetadata contentMetadata = new ContentMetadata();
        contentMetadata.addCustomMetadata("profile_id", String.valueOf(profileId));

        return new BranchUniversalObject()
                .setCanonicalIdentifier(canonicalIdentifier)
                .setTitle(contentTitle)
                .setContentDescription(contentDescription)
                .setContentImageUrl(thumbnailUrl)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(contentMetadata);
    }
    private LinkProperties createLinkProperties() {

        return new LinkProperties()
                .setFeature("sharing");
        // .setChannel("facebook");
        // .addControlParameter("$desktop_url", "https://joyapeak.com")
        // .addControlParameter("$android_url", "https://www.dropbox.com/home?preview=app-debug.apk")
        // .addControlParameter("$ios_url", "https://joyapeak.com");
    }
}
