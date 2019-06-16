package com.joyapeak.sarcazon.ui.upload.uploadconfirmation;

import com.joyapeak.sarcazon.ui.base.MvpPresenter;

/**
 * Created by Mahmoud Ali on 7/5/2018.
 */

public interface UploadConfirmationMvpPresenter <V extends UploadConfirmationMvpView> extends MvpPresenter<V> {

    void uploadComicUrl(int comicType, String comicUrl, String comicVideoSourceUrl,
                        String caption, String category, String tags, String credits, Double aspectRatio);

    boolean getIsEngagementUsersEnabled();

    void uploadFiles(byte[][] filesByteArrays);

    void getInstagramVideoData(String videoUrl);
}
