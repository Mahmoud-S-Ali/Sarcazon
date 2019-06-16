package com.joyapeak.sarcazon.ui.upload.uploadoptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.photosource.PhotoSourceDialog;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationActivity;
import com.joyapeak.sarcazon.ui.upload.urlpaste.UrlPasteDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class UploadOptionsActivity extends BaseActivity implements
        UploadOptionsMvpView,
        PhotoSourceDialog.PhotoSourceDialogHandler,
        UrlPasteDialog.UrlPasteDialogHandler {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindArray(R.array.gif_sources)
    String[] mGifSourcesArr;

    @Inject
    UploadOptionsMvpPresenter<UploadOptionsMvpView> mPresenter;

    private Integer mSelectedType;
    private final String STATE_SELECTED_TYPE = "STATE_SELECTED_TYPE";

    private final int REQ_CODE_CONFIRMATION_ACTIVITY = 0;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, UploadOptionsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SELECTED_TYPE)) {
                mSelectedType = savedInstanceState.getInt(STATE_SELECTED_TYPE);
            }
        }

        setUp();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedType != null) {
            outState.putInt(STATE_SELECTED_TYPE, mSelectedType);
        }
    }

    private void setUp() {
        setupGeneralToolbar(mAppbar, getString(R.string.upload_header));
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQ_CODE_CONFIRMATION_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    openUploadConfirmationActivity(mSelectedType, result.getUri().getPath());

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Timber.e(result.getError());
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.layout_upload_image)
    public void onUploadImageClicked() {
        mSelectedType = ApiHelper.COMIC_TYPES.IMAGE;
        showPhotoSourceBSDialog();
    }

    @OnClick(R.id.layout_upload_gif)
    public void onUploadGifClicked() {
        mSelectedType = ApiHelper.COMIC_TYPES.GIF;
        // showGifSourcesDialog();
        showUrlPasteDialog(mSelectedType);
    }

    @OnClick(R.id.layout_upload_video)
    public void onUploadVideoClicked() {
        mSelectedType = ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE;
        showUrlPasteDialog(mSelectedType);
    }

    private void showGifSourcesDialog() {
        final PhotoSourceDialog dialog = PhotoSourceDialog.newInstance(mGifSourcesArr);
        dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
    }
    private void showUrlPasteDialog(int urlType) {
        UrlPasteDialog dialog = UrlPasteDialog.newInstance(urlType);
        dialog.show(getSupportFragmentManager());
    }


    private void showPhotoSourceBSDialog() {
        PhotoSourceDialog photoSourceBSDialog = PhotoSourceDialog.newInstance(
                getResources().getStringArray(R.array.photo_sources_without_remove));

        photoSourceBSDialog.show(getSupportFragmentManager(), photoSourceBSDialog.getTag());
    }

    @Override
    public void onPhotoSourcePhotoSelected(String photoPath) {

        switch (mSelectedType) {
            case ApiHelper.COMIC_TYPES.GIF:
                openUploadConfirmationActivity(mSelectedType, photoPath);
                break;

            default:
                Uri photoUri = Uri.fromFile(new File(photoPath));
                CropImage.activity(photoUri)
                        .setInitialCropWindowPaddingRatio(0)
                        .setBorderCornerThickness(10)
                        .setBorderCornerOffset(0)
                        .setBorderLineThickness(4)
                        .setBorderCornerOffset(4)
                        .setBorderCornerColor(ContextCompat.getColor(this, R.color.colorAccent))
                        .setBorderLineColor(ContextCompat.getColor(this, R.color.grey_light))
                        .start(this);
        }
    }

    @Override
    public void onPhotoSourceUrlOptionSelected() {
        if (mSelectedType == ApiHelper.COMIC_TYPES.GIF) {
            showUrlPasteDialog(mSelectedType);
        }
    }


    private void openUploadConfirmationActivity(int comicType, String comicPath) {
        startActivityForResult(UploadConfirmationActivity.getStartIntent(this, comicType, comicPath),
                REQ_CODE_CONFIRMATION_ACTIVITY);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onUrlPasteDoneClicked(int comicType, String comicUrl) {
        openUploadConfirmationActivity(comicType, comicUrl);
    }
}
