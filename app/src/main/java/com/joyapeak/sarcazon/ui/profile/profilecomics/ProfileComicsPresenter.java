package com.joyapeak.sarcazon.ui.profile.profilecomics;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.data.network.retrofit.ServerResult;
import com.joyapeak.sarcazon.ui.base.ApiErrorHandler;
import com.joyapeak.sarcazon.ui.base.BasePresenter;
import com.joyapeak.sarcazon.utils.ApiErrorConstants;
import com.joyapeak.sarcazon.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 5/7/2018.
 */

public class ProfileComicsPresenter <V extends ProfileComicsMvpView> extends BasePresenter <V>
            implements ProfileComicsMvpPresenter <V> {

    private Long mComicsBaseId = null;
    private Integer mComicsOffset = 0;

    @Inject
    public ProfileComicsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getUserComics(final long userId) {

        final ApiErrorHandler handler = new ApiErrorHandler() {
            @Override
            public void onRetry() {
                getUserComics(userId);
            }
        };

        if (!getMvpView().isNetworkConnected()) {
            handleApiError(ApiErrorConstants.CONNECTION_ERROR, handler);
            return;
        }

        if (mComicsOffset == 0) {
            getMvpView().showLoadingDialog();
        }

        Long viewerId = getDataManager().getCurrentUserId();
        Long profileId = userId;
        if (profileId == AppConstants.NULL_INDEX) {
            profileId = getDataManager().getCurrentUserId();
            viewerId = null;
        }

        getDataManager().getProfileComics(new ProfileRequest.ProfileComicsRequest(
                profileId, viewerId, mComicsBaseId,
                mComicsOffset, ApiHelper.X_LARGE_COUNT_PER_REQUEST), new ServerResult() {

            @Override
            public void onSuccess(Object responseBody, int responseCode) {
                Timber.d("Get profile comics successful");
                if (isViewAttached()) {
                    ProfileResponse.ProfileComicsResponse comicsResponse =
                            (ProfileResponse.ProfileComicsResponse) responseBody;

                    if (comicsResponse == null) {
                        if (isViewAttached()) {
                            getMvpView().hideLoadingDialog();
                        }

                        return;
                    }

                    mComicsOffset += comicsResponse.getComics().size();
                    if (mComicsBaseId == null) {
                        mComicsBaseId = comicsResponse.getBaseId();
                    }

                    List<ProfileResponse.ProfileComicInfo> userComics = new ArrayList<>();
                    for (ProfileResponse.ProfileComic comic : comicsResponse.getComics()) {
                        userComics.add(comic.getProfileComicInfo());
                    }

                    if (getMvpView() != null) {
                        if (comicsResponse != null) {
                            Integer totalComicsCount = comicsResponse.getTotalComicsCount();
                            totalComicsCount = totalComicsCount == null? 0 : totalComicsCount;
                            getMvpView().updateTotalComicsCount(totalComicsCount);
                        }
                        getMvpView().addUserComics(userComics);
                        getMvpView().hideLoadingDialog();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Timber.d("Get profile comics failed");
            }
        });

        /*List<ProfileResponse.ProfileComicInfo> userComics = new ArrayList<>();
        List<ComicInfo> comicInfos = addComicsTest();
        for (ComicInfo comicInfo : comicInfos) {
            userComics.add(new ProfileResponse.ProfileComicInfo(comicInfo.getComicId(),
                    comicInfo.getThumbnailUrl(),
                    comicInfo.getComicType()));
        }
        getMvpView().addUserComics(userComics);*/
    }
    /*private List<ComicInfo> addComicsTest() {
        ComicInfo comic1 = new ComicInfo(1L, ApiHelper.COMIC_TYPES.IMAGE, "https://pbs.twimg.com/media/CNSTgU4UsAAX9zt.jpg", "https://4thofjulyimagess.us/wp-content/uploads/2018/06/Best-4th-Of-July-Meme.jpg", 2, 5, 1, false, false, false);
        ComicInfo comic2 = new ComicInfo(2L, ApiHelper.COMIC_TYPES.IMAGE, "http://masralarabia.com/images/thumbs/850/14184238111428574914-11037463_923414771086960_4587689334251406555_n.jpg", "http://quotesnhumor.com/wp-content/uploads/2015/08/Top-50-Funniest-Memes-Collection-memes-funny.jpg", 2, 5, 1, true, false, false);
        ComicInfo comic3 = new ComicInfo(3L, ApiHelper.COMIC_TYPES.IMAGE, "http://masralarabia.com/images/thumbs/850/14184238111428574914-11037463_923414771086960_4587689334251406555_n.jpg", "http://srune.com/media/uploads/18/5723628b550c9.jpg", 2, 5, 2, false, false, false);
        ComicInfo comic4 = new ComicInfo(4L, ApiHelper.COMIC_TYPES.IMAGE, "http://masralarabia.com/images/thumbs/850/14184238111428574914-11037463_923414771086960_4587689334251406555_n.jpg", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsq8JO510MgLvpS0HhDmeVX5z57pXX1rqkQ2rRzHUbL1M-_UtLlA", 2, 5, 1, true, false, false);

        List<ComicInfo> comics = new ArrayList<>();
        comics.add(comic1);
        comics.add(comic2);
        comics.add(comic3);
        comics.add(comic4);

        return comics;
    }*/
}
