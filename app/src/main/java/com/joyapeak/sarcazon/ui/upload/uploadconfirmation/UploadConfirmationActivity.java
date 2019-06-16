package com.joyapeak.sarcazon.ui.upload.uploadconfirmation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.other.instagram.InstagramResponse;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.popupmessage.MessageDialog;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.caption.UploadCaptionFragment;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.tags.UploadTagsFragment;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class UploadConfirmationActivity extends BaseActivity implements UploadConfirmationMvpView,
        MessageDialog.MessageDialogHandler {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.btn_toolbar_done)
    Button mNextBtn;

    @Inject
    UploadConfirmationMvpPresenter<UploadConfirmationMvpView> mPresenter;

    private final static String EXTRA_COMIC_TYPE = "EXTRA_COMIC_TYPE";
    private final static String EXTRA_COMIC_PATH = "EXTRA_COMIC_PATH";

    private ComicToUploadInfo mComicToUploadInfo;
    private String STATE_COMIC_TO_UPLOAD_INFO = "STATE_COMIC_TO_UPLOAD_INFO";

    private long mLastDoneClickTime = 0;

    private FragmentManager mFragmentManager;

    private final int FRAGMENT_CAPTION_POSITION = 0;
    // private final int FRAGMENT_CATEGORY_POSITION = 1;
    private final int FRAGMENT_TAGS_POSITION = 1;

    public static Intent getStartIntent(Context context, Integer comicType, String comicPath) {
        Intent intent = new Intent(context, UploadConfirmationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_COMIC_TYPE, comicType);
        bundle.putString(EXTRA_COMIC_PATH, comicPath);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_confirmation);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_COMIC_TO_UPLOAD_INFO)) {
                mComicToUploadInfo = savedInstanceState.getParcelable(STATE_COMIC_TO_UPLOAD_INFO);
            }

        } else {
            Bundle extras = getIntent().getExtras();
            mComicToUploadInfo = new ComicToUploadInfo();
            mComicToUploadInfo.setComicType(extras.getInt(EXTRA_COMIC_TYPE));
            mComicToUploadInfo.setComicPath(extras.getString(EXTRA_COMIC_PATH));

            addFragment(UploadCaptionFragment.newInstance(
                        mComicToUploadInfo.getComicType(),
                        mComicToUploadInfo.getComicPath()),
                    UploadCaptionFragment.TAG);
        }

        setUp();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mComicToUploadInfo != null) {
            outState.putParcelable(STATE_COMIC_TO_UPLOAD_INFO, mComicToUploadInfo);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() == 1) {
            mFragmentManager.popBackStack();
            super.onBackPressed();

        } else {
            super.onBackPressed();
            setNextBtnName(getString(R.string.next));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mFragmentManager.getBackStackEntryCount() > 1) {
                removeFragment();
                setNextBtnName(getString(R.string.next));
                return true;
            }
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void setUp() {
        setupGeneralToolbar(mAppbar, "");
        setNextBtnName(getString(R.string.next));
    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mFragmentManager.getBackStackEntryCount() > 1) {
            ft.setCustomAnimations(R.anim.translate_enter_from_right, 0,
                    R.anim.translate_enter_from_left, 0);
        }
        ft.replace(R.id.container_upload_confirmation, fragment, tag);
        ft.addToBackStack(tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.commit();
    }
    private void removeFragment() {
        mFragmentManager.popBackStack();
    }

    public void setNextBtnAsEnabled(boolean enabled) {
        if (enabled) {
            mNextBtn.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        } else {
            mNextBtn.setTextColor(ContextCompat.getColor(this, R.color.white_op25));
        }
    }
    public void setNextBtnName(String name) {
        mNextBtn.setText(name);
    }

    private void showErrorMsgDialog(String msg) {
        MessageDialog dialog = MessageDialog.newInstance(msg);
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public void onErrorMsgOkClicked() {

    }

    @OnClick(R.id.btn_toolbar_done)
    public void onDoneClicked() {

        int currentFragmentsCount = mFragmentManager.getBackStackEntryCount();
        switch (currentFragmentsCount - 1) {

            case FRAGMENT_CAPTION_POSITION:
                hideKeyboard();
                showLoadingDialog();

                UploadCaptionFragment captionFragment =
                        (UploadCaptionFragment) mFragmentManager.findFragmentByTag(UploadCaptionFragment.TAG);

                captionFragment.processData();
                setNextBtnAsEnabled(true);
                break;

            /*case FRAGMENT_CATEGORY_POSITION:
                UploadCategoryFragment categoryFragment =
                        (UploadCategoryFragment) mFragmentManager.findFragmentByTag(UploadCategoryFragment.TAG);

                String category = categoryFragment.getSelectedCategory();

                if (category != null) {
                    mComicToUploadInfo.setCategory(category);
                    addFragment(UploadTagsFragment.newInstance(), UploadTagsFragment.TAG);
                    setNextBtnName(getString(R.string.done));

                } else {
                    showMessage("Lazem tekhtar category");
                }

                break;*/

            case FRAGMENT_TAGS_POSITION:
                UploadTagsFragment tagsFragment =
                        (UploadTagsFragment) mFragmentManager.findFragmentByTag(UploadTagsFragment.TAG);

                mComicToUploadInfo.setTags(tagsFragment.getTags());
                Timber.d("Tags = " + tagsFragment.getTags());

                if (System.currentTimeMillis() - mLastDoneClickTime < 2000) {
                    showMessage("Uploading post, please wait");
                    return;
                }

                mLastDoneClickTime = System.currentTimeMillis();
                showLoadingDialog();
                hideKeyboard();

                // Making sure focus from edittexts has been removed
                switch (mComicToUploadInfo.getComicType()) {
                    case ApiHelper.COMIC_TYPES.IMAGE:
                        mPresenter.uploadFiles((mComicToUploadInfo.getPhotoByteArr()));
                        break;

                    default:
                        mPresenter.uploadComicUrl(mComicToUploadInfo.getComicType(),
                                mComicToUploadInfo.getComicPath(),
                                mComicToUploadInfo.getVidSourceUrl(),
                                mComicToUploadInfo.getCaption(),
                                mComicToUploadInfo.getCategory(),
                                mComicToUploadInfo.getTags(),
                                mComicToUploadInfo.getCredits(),
                                mComicToUploadInfo.getAspectRatio());
                        break;
                }
                break;
        }
    }


    @Override
    public void onComicUrlUploadResult(boolean isSuccessful, Long comicId) {
        if (isSuccessful) {
            openComicViewActivity(comicId);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void openComicViewActivity(Long comicId) {
        startActivity(ComicViewActivity.getStartIntent(getApplicationContext(), comicId, null));
    }

    public void getInstagramVideoData(String comicPath) {
        mPresenter.getInstagramVideoData(comicPath);
    }

    public void onCaptionFragmentDataProcessed(byte[][] imageByteArr) {
        hideLoadingDialog();

        UploadCaptionFragment captionFragment =
                (UploadCaptionFragment) mFragmentManager.findFragmentByTag(UploadCaptionFragment.TAG);

        if (imageByteArr != null) {
            mComicToUploadInfo.setPhotoByteArr(imageByteArr);
        }

        mComicToUploadInfo.setCaption(captionFragment.getCaption());
        mComicToUploadInfo.setCredits(captionFragment.getCredits());
        mComicToUploadInfo.setAspectRatio(captionFragment.getContentAspectRatio());
        mComicToUploadInfo.setVidSourceUrl(captionFragment.getFBVideoSourceUrl());

        addFragment(UploadTagsFragment.newInstance(), UploadTagsFragment.TAG);
    }

    @Override
    public void onInstagramVideoDataRetrieved(boolean successful,
                                              InstagramResponse.InstagramVideoResponse videoData) {

        UploadCaptionFragment uploadCaptionFragment =
                (UploadCaptionFragment) getSupportFragmentManager().findFragmentByTag(UploadCaptionFragment.TAG);

        if (!successful || uploadCaptionFragment == null) {
            hideLoadingDialog();
            showErrorMsgDialog(getString(R.string.msg_error_video_url));
            return;
        }

        uploadCaptionFragment.initVideoView(videoData.getVideoUrl());
    }

    @Override
    public void onPhotoFilesUploaded(String photoUrl) {
        mPresenter.uploadComicUrl(ApiHelper.COMIC_TYPES.IMAGE,
                photoUrl,
                null,
                mComicToUploadInfo.getCaption(),
                mComicToUploadInfo.getCategory(),
                mComicToUploadInfo.getTags(),
                mComicToUploadInfo.getCredits(),
                mComicToUploadInfo.getAspectRatio());
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }


    private static class ComicToUploadInfo implements Parcelable {

        private int comicType;
        private String comicPath;
        private String vidSourceUrl;
        private byte[][] photoByteArr;
        private String caption;
        private String credits;
        private String category;
        private String tags;
        private Double aspectRatio;

        public ComicToUploadInfo() {
        }

        public int getComicType() {
            return comicType;
        }
        public void setComicType(int comicType) {
            this.comicType = comicType;
        }

        public String getComicPath() {
            return comicPath;
        }
        public void setComicPath(String comicPath) {
            this.comicPath = comicPath;
        }

        public String getVidSourceUrl() {
            return vidSourceUrl;
        }
        public void setVidSourceUrl(String vidSourceUrl) {
            this.vidSourceUrl = vidSourceUrl;
        }

        public byte[][] getPhotoByteArr() {
            return photoByteArr;
        }
        public void setPhotoByteArr(byte[][] photoByteArr) {
            this.photoByteArr = photoByteArr;
        }

        public String getCaption() {
            return caption;
        }
        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getCredits() {
            return credits;
        }
        public void setCredits(String credits) {
            this.credits = credits;
        }

        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }

        public String getTags() {
            return tags;
        }
        public void setTags(String tags) {
            this.tags = tags;
        }

        public Double getAspectRatio() {
            return aspectRatio;
        }
        public void setAspectRatio(Double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        @Override
        public String toString() {
            return "ComicToUploadInfo{" +
                    "comicType=" + comicType +
                    ", comicPath='" + comicPath + '\'' +
                    ", vidSourceUrl='" + vidSourceUrl + '\'' +
                    ", photoByteArr=" + Arrays.toString(photoByteArr) +
                    ", caption='" + caption + '\'' +
                    ", credits='" + credits + '\'' +
                    ", category='" + category + '\'' +
                    ", tags='" + tags + '\'' +
                    ", aspectRatio=" + aspectRatio +
                    '}';
        }

        protected ComicToUploadInfo(Parcel in) {
            comicType = in.readInt();
            comicPath = in.readString();
            vidSourceUrl = in.readString();
            caption = in.readString();
            credits = in.readString();
            category = in.readString();
            tags = in.readString();
            aspectRatio = in.readByte() == 0x00 ? null : in.readDouble();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(comicType);
            dest.writeString(comicPath);
            dest.writeString(vidSourceUrl);
            dest.writeString(caption);
            dest.writeString(credits);
            dest.writeString(category);
            dest.writeString(tags);
            if (aspectRatio == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeDouble(aspectRatio);
            }
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<ComicToUploadInfo> CREATOR = new Parcelable.Creator<ComicToUploadInfo>() {
            @Override
            public ComicToUploadInfo createFromParcel(Parcel in) {
                return new ComicToUploadInfo(in);
            }

            @Override
            public ComicToUploadInfo[] newArray(int size) {
                return new ComicToUploadInfo[size];
            }
        };
    }
}
