package com.joyapeak.sarcazon.ui.photosource;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.BasicListAdapter;
import com.joyapeak.sarcazon.ui.base.BaseBottomSheetDialogFragment;
import com.joyapeak.sarcazon.utils.ImageUtils;
import com.joyapeak.sarcazon.utils.MarshmallowPermissions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 5/18/2018.
 */

public class PhotoSourceDialog extends BaseBottomSheetDialogFragment implements PhotoSourceDialogMvpView {

    private static final String TAG = "PhotoSourceDialog";

    @BindView(R.id.rv_dialogBasicList)
    RecyclerView mChoiceRV;

    @Inject
    PhotoSourceDialogMvpPresenter<PhotoSourceDialogMvpView> mPresenter;

    @Inject
    @Named("nonscrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    private String mSelectedPhotoSourceOption;

    private static final int REQ_INTENT_IMAGE_FROM_GALLERY = 0;
    private static final int REQ_INTENT_IMAGE_FROM_CAMERA = 1;

    private PhotoSourceDialogHandler mHandler;

    private String mSelectedImagePath = null;

    private String[] mPhotoSourceOptions;
    private final static String EXTRA_PHOTO_SOURCE_OPTIONS = "EXTRA_PHOTO_SOURCE_OPTIONS";


    public interface PhotoSourceDialogHandler {
        void onPhotoSourcePhotoSelected(String photoPath);
        void onPhotoSourceUrlOptionSelected();
    }


    // Creating new instance
    public static PhotoSourceDialog newInstance(String[] photoSourceOptions) {
        PhotoSourceDialog fragment = new PhotoSourceDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_PHOTO_SOURCE_OPTIONS, photoSourceOptions);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (mHandler == null) {
                mHandler = (PhotoSourceDialogHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_basic_list, container, false);

        mPhotoSourceOptions = getArguments().getStringArray(EXTRA_PHOTO_SOURCE_OPTIONS);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            mPresenter.onAttach(this);
            setUnBinder(ButterKnife.bind(this, view));
        }

        return view;
    }


    @Override
    public String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {

        List<String> photoSourcesList = Arrays.asList(mPhotoSourceOptions);
        BasicListAdapter photoSourceAdapter = new BasicListAdapter(photoSourcesList,
                new BasicListAdapter.Callback() {

            @Override
            public void onItemClicked(int position) {

                mSelectedPhotoSourceOption = mPhotoSourceOptions[position];

                if (mSelectedPhotoSourceOption.equals(getString(R.string.photoSource_gallery))) {
                    startImageSelectionFromLibraryIntent();

                } else if (mSelectedPhotoSourceOption.equals(getString(R.string.photoSource_camera))) {
                    startTakingImageWithCameraIntent();

                } else if (mSelectedPhotoSourceOption.equals(getString(R.string.photoSource_url))) {
                    mHandler.onPhotoSourceUrlOptionSelected();

                } else {
                    mSelectedImagePath = "";
                    getDialog().cancel();
                }
            }
        });

        mChoiceRV.setLayoutManager(mLayoutManager);
        mChoiceRV.setAdapter(photoSourceAdapter);
        mChoiceRV.setHasFixedSize(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQ_INTENT_IMAGE_FROM_GALLERY:
                    mSelectedImagePath = ImageUtils.getLocalImgPathFromIntent(getContext(), intent);
                    break;

                case REQ_INTENT_IMAGE_FROM_CAMERA:
                    break;
            }

            getDialog().cancel();

        } else {
            mSelectedImagePath = null;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case MarshmallowPermissions.STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (mSelectedPhotoSourceOption.equals(getString(R.string.photoSource_camera))) {
                        startTakingImageWithCameraIntent();

                    } else {
                        startImageSelectionFromLibraryIntent();
                    }

                } else {
                    // TODO: Show error message
                }
            }
            break;

            case MarshmallowPermissions.GALLERY_PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startImageSelectionFromLibraryIntent();

                } else {
                    // TODO: Show error message
                }
                break;

            case MarshmallowPermissions.CAMERA_PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTakingImageWithCameraIntent();

                } else {
                    // TODO: Show error message
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    protected void startImageSelectionFromLibraryIntent() {

        if (!MarshmallowPermissions.checkPermissionForStoringData(getContext())) {
            MarshmallowPermissions.requestFragmentPermissionForStoringData(
                    PhotoSourceDialog.this,
                    MarshmallowPermissions.STORAGE_PERMISSION_CODE);

        } else if (!MarshmallowPermissions.checkPermissionForGalleryAccess(getContext())) {
            MarshmallowPermissions.requestFragmentPermissionForGalleryAccess(
                    PhotoSourceDialog.this,
                    MarshmallowPermissions.GALLERY_PERMISSION_CODE);

        } else {
            dispatchSelectImageFromGalleryIntent(PhotoSourceDialog.this,
                    REQ_INTENT_IMAGE_FROM_GALLERY);
        }
    }
    protected void startTakingImageWithCameraIntent() {

        if (!MarshmallowPermissions.checkPermissionForStoringData(getContext())) {
            MarshmallowPermissions.requestFragmentPermissionForStoringData(
                    PhotoSourceDialog.this,
                    MarshmallowPermissions.STORAGE_PERMISSION_CODE);

        } else if (!MarshmallowPermissions.checkPermissionForCamera(getContext())) {
            MarshmallowPermissions.requestFragmentPermissionForCamera(
                    PhotoSourceDialog.this,
                    MarshmallowPermissions.CAMERA_PERMISSION_CODE);

        } else {
            mSelectedImagePath = dispatchTakePhotoWithCameraIntent(getContext(),
                    PhotoSourceDialog.this, REQ_INTENT_IMAGE_FROM_CAMERA);
        }
    }


    private void dispatchSelectImageFromGalleryIntent(Fragment fragment, int requestCode) {

        Intent intent =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        fragment.startActivityForResult(intent, requestCode);
    }
    private String dispatchTakePhotoWithCameraIntent(Context context, Fragment fragment,
                                                           int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String cameraPhotoPath = null;

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = ImageUtils.createImageFileFromCamera();

            } catch (IOException ex) {
                Timber.e(ex.getMessage().toString());
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {

                cameraPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(
                                context,
                                context.getApplicationContext().getPackageName() + ".fileprovider",
                                photoFile));
                fragment.startActivityForResult(takePictureIntent, requestCode);
            }
        }

        return cameraPhotoPath;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            if (mHandler == null && context instanceof PhotoSourceDialogHandler) {
                mHandler = (PhotoSourceDialogHandler) context;
            }

        } catch (ClassCastException e) {
            Timber.e(" must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mSelectedImagePath != null) {
            mHandler.onPhotoSourcePhotoSelected(mSelectedImagePath);
        }
    }

    @Override
    public void onDestroyView() {
        mHandler = null;
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
