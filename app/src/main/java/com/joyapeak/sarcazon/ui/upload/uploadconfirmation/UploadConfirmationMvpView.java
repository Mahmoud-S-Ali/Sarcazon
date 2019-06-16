package com.joyapeak.sarcazon.ui.upload.uploadconfirmation;

import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 7/5/2018.
 */

public interface UploadConfirmationMvpView extends MvpView {

    void onComicUrlUploadResult(boolean isSuccessful, Long comicId);

    void onInstagramVideoDataRetrieved(boolean successful, InstagramResponse.InstagramVideoResponse videoData);

    void onPhotoFilesUploaded(String photoUrl);
}
