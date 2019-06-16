package com.joyapeak.sarcazon.ui.profilesettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.async.ImageCompression;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileRequest;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.photosource.PhotoSourceDialog;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.EditTextUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.joyapeak.sarcazon.ui.profile.ProfileActivity.EXTRA_PROFILE_INFO;

public class ProfileSettingsActivity extends BaseActivity
        implements ProfileSettingsMvpView, PhotoSourceDialog.PhotoSourceDialogHandler {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.iv_profileSettings_photo)
    ImageView mUserPhotoIV;

    @BindView(R.id.et_profileSettings_name)
    TextView mProfileNameET;

    @BindView(R.id.et_profileSettings_quote)
    TextView mProfileQuoteET;

    @BindView(R.id.et_profileSettings_email)
    TextView mProfileEmailET;

    @BindView(R.id.et_profileSettings_password)
    TextView mProfilePasswordET;


    @Inject
    ProfileSettingsMvpPresenter<ProfileSettingsMvpView> mPresenter;

    private ProfileResponse.ProfileInfoResponse mProfileInfo;

    private String mProfilePhotoPath;
    private String mProfilePhotoUrl;
    private ImageCompression mImageCompressionTask;

    private String mName;
    private String mQuote;
    private String mEmail;
    private String mPassword;


    public static Intent getStartIntent(Context context, ProfileResponse.ProfileInfoResponse profileInfo) {
        Intent intent = new Intent(context, ProfileSettingsActivity.class);
        intent.putExtra(EXTRA_PROFILE_INFO, profileInfo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        mProfileInfo = getIntent().getParcelableExtra(EXTRA_PROFILE_INFO);
        mProfileInfo = mProfileInfo == null? new ProfileResponse.ProfileInfoResponse() : mProfileInfo;

        setupGeneralToolbar(mAppbar, getString(R.string.title_activity_profile_settings));
        mPresenter.onViewInitialized();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mImageCompressionTask != null) {
            mImageCompressionTask.cancelImageCompressionTask();
        }
    }

    @OnClick(R.id.iv_profileSettings_photo)
    public void onProfilePhotoClicked() {
        showPhotoSourceBSDialog();
    }


    private void showPhotoSourceBSDialog() {
        PhotoSourceDialog photoSourceBSDialog = PhotoSourceDialog.newInstance(
                getResources().getStringArray(R.array.photo_sources));

        photoSourceBSDialog.show(getSupportFragmentManager(), photoSourceBSDialog.getMyDialogTag());
    }

    @Override
    public void onPhotoSourcePhotoSelected(String photoPath) {
        mProfilePhotoPath = photoPath;
        ImageUtils.loadImageFileIntoView(this,
                mUserPhotoIV,
                ImageUtils.getImageFileFromPath(photoPath),
                true,
                R.drawable.im_default);
    }

    @Override
    public void onPhotoSourceUrlOptionSelected() {
    }


    @OnClick(R.id.btn_toolbar_done)
    public void onConfirmDataUpdateClicked() {
        checkAllFieldsValidation();
    }

    private void checkAllFieldsValidation() {
        mName = mProfileNameET.getText().toString();
        mQuote = mProfileQuoteET.getText().toString();
        mEmail = mProfileEmailET.getText().toString();
        mPassword = mProfilePasswordET.getText().toString();

        mPresenter.checkFieldsValidation(new ProfileRequest.ProfileUpdateInfoRequest(null,
                null,
                mName,
                null,
                mEmail,
                mPassword));
    }

    private void updateProfileInfo() {
        if (mProfilePhotoPath != null && mProfilePhotoUrl == null) {
            if (!mProfilePhotoPath.isEmpty()) {
                startImageCompressionTask(mProfilePhotoPath);
                return;

            } else {
                mProfilePhotoUrl = mProfilePhotoPath;
            }
        }

        mProfileInfo.setName(mName);
        mProfileInfo.setQuote(mQuote);

        if (mProfilePhotoUrl != null && !mProfilePhotoUrl.isEmpty()) {
            mProfileInfo.setProfilePhotoUrl(mProfilePhotoUrl);
            mProfileInfo.setThumbnailUrl(mProfilePhotoUrl);
        }

        mPresenter.updateUserData(new ProfileRequest.ProfileUpdateInfoRequest(null,
                mProfilePhotoUrl,
                mName,
                mQuote,
                mEmail,
                mPassword));
    }

    @Override
    public void updateUserInfoViews(String email) {

        if (email != null && !email.isEmpty()) {
            mProfileEmailET.setText(email);
            mProfileEmailET.setEnabled(false);

            mProfilePasswordET.setText("12345678");
            mProfilePasswordET.setEnabled(false);

            if (email.contains(AppConstants.EMAIL_FACEBOOK_CONCAT)) {
                mProfileEmailET.setVisibility(View.INVISIBLE);
                mProfilePasswordET.setVisibility(View.INVISIBLE);
            }

            String name = mProfileInfo.getName() == null ? "" : mProfileInfo.getName();
            String quote = mProfileInfo.getQuote() == null ? "" : mProfileInfo.getQuote();

            mProfileNameET.setText(name);
            mProfileQuoteET.setText(quote);
        }

        ImageUtils.loadImageUrlIntoView(this,
                mUserPhotoIV,
                mProfileInfo.getProfilePhotoUrl(),
                true,
                R.drawable.im_default);
    }

    @Override
    public void onEmailExistenceResult(boolean exists) {
        if (exists) {
            mProfileEmailET.setError(getString(R.string.error_email_exists));

        } else {
            updateProfileInfo();
        }
    }

    @Override
    public void onProfilePhotoUploaded(String photoUrl) {
        mProfilePhotoUrl = photoUrl;
        updateProfileInfo();
    }


    @Override
    public void showNameError(int nameStatus) {
        switch (nameStatus) {
            case EditTextUtils.NAME_STATUS_EMPTY:
                mProfileNameET.setError(getString(R.string.error_name_empty));
                break;

            case EditTextUtils.NAME_STATUS_INVALID:
                mProfileNameET.setError(getString(R.string.error_name_invalid));
                break;
        }
    }

    @Override
    public void showEmailError(int emailErrorCode) {
        switch (emailErrorCode) {
            case EditTextUtils.EMAIL_STATUS_EMPTY:
                mProfileEmailET.setError(getString(R.string.error_email_field_empty));
                break;

            case EditTextUtils.EMAIL_STATUS_INVALID:
                mProfileEmailET.setError(getString(R.string.error_email_invalid));
                break;
        }
    }

    @Override
    public void showPasswordError(int passwordErrorCode) {
        switch (passwordErrorCode) {
            case EditTextUtils.PASSWORD_STATUS_LENGTH_SHORT:
                mProfilePasswordET.setError(getString(R.string.error_password_short,
                        String.valueOf(EditTextUtils.PASSWORD_MIN_LENGTH)));
                break;

            case EditTextUtils.PASSWORD_STATUS_INVALID:
                mProfilePasswordET.setError(getString(R.string.error_password_invalid));
                break;
        }
    }

    @Override
    public void onUserInfoUpdated() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_PROFILE_INFO, mProfileInfo);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void startImageCompressionTask(String imagePath) {
        if (mImageCompressionTask == null) {
            mImageCompressionTask = new ImageCompression(this, new ImageCompression.ImageCompressionHandler() {
                @Override
                public void onCompressionCompleted(byte[][] filesByteArrays) {
                    mPresenter.uploadFiles(filesByteArrays);
                    mImageCompressionTask = null;
                }

                @Override
                public void onCompressionCompleted(Bitmap bitmap) {
                }
            });

            mImageCompressionTask.startNewImageCompressionToByteArrTask(imagePath);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
