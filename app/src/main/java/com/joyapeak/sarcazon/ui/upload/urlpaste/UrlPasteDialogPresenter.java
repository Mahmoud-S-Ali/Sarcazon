package com.joyapeak.sarcazon.ui.upload.urlpaste;

import com.joyapeak.sarcazon.data.DataManager;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Mahmoud Ali on 7/5/2018.
 */

public class UrlPasteDialogPresenter<V extends UrlPasteDialogMvpView> extends BasePresenter<V>
        implements UrlPasteDialogMvpPresenter<V> {

    private final static String INSTAGRAM_BASE_VIDEO_URL = "instagram.com/p/";
    private final static String YOUTUBE_BASE_VIDEO_URL_1 = "youtube.com/watch?v=";
    private final static String YOUTUBE_BASE_VIDEO_URL_2 = "youtu.be/";
    private final static String FACEBOOK_BASE_VIDEO_URL = "facebook.com/";

    @Inject
    public UrlPasteDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void onNextClicked(int comicType, String comicUrl) {

        if (comicUrl == null || comicUrl.isEmpty()) {
            getMvpView().handleUrlStatus(false, UrlPasteDialog.URL_ERROR_EMPTY);
            return;
        }

        switch (comicType) {
            case ApiHelper.COMIC_TYPES.VIDEO_FACEBOOK:
            case ApiHelper.COMIC_TYPES.VIDEO_INSTAGRAM:
            case ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE:
                if (comicUrl.contains(INSTAGRAM_BASE_VIDEO_URL) ||
                        comicUrl.contains(YOUTUBE_BASE_VIDEO_URL_1) ||
                        comicUrl.contains(YOUTUBE_BASE_VIDEO_URL_2) ||
                        comicUrl.contains(FACEBOOK_BASE_VIDEO_URL)) {

                    getMvpView().handleUrlStatus(true, null);

                } else {
                    getMvpView().handleUrlStatus(false, UrlPasteDialog.URL_ERROR_VIDEO_SOURCE);
                }

                return;
        }

        getMvpView().handleUrlStatus(true, null);
    }

    @Override
    public int getComicTypeFromUrl(String comicUrl) {

        if (comicUrl.contains(INSTAGRAM_BASE_VIDEO_URL))
            return ApiHelper.COMIC_TYPES.VIDEO_INSTAGRAM;

        if (comicUrl.contains(YOUTUBE_BASE_VIDEO_URL_1) || comicUrl.contains(YOUTUBE_BASE_VIDEO_URL_2))
            return ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE;

        if (comicUrl.contains(FACEBOOK_BASE_VIDEO_URL))
            return ApiHelper.COMIC_TYPES.VIDEO_FACEBOOK;

        return ApiHelper.COMIC_TYPES.GIF;
    }
}
