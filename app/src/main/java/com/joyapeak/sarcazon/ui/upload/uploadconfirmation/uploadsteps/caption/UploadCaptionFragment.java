package com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.caption;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.joyapeak.sarcazon.GlideApp;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.async.ImageCompression;
import com.joyapeak.sarcazon.async.ViewImageGeneration;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.helper.FBVideoSourceGrabber;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.popupmessage.MessageDialog;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationActivity;
import com.joyapeak.sarcazon.ui.youtube.YoutubeActivity;
import com.joyapeak.sarcazon.utils.FileUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by User on 11/27/2018.
 */

public class UploadCaptionFragment extends BaseFragment implements
        MessageDialog.MessageDialogHandler,
        FBVideoSourceGrabber.FBVideoSourceHandler{

    public static final String TAG = "UploadCaptionFragment";

    @BindView(R.id.layout_uploadConfirmation_credits)
    View mCreditsLayout;

    @BindView(R.id.et_uploadConfirmation_credits)
    EditText mCreditsET;

    @BindView(R.id.layout_uploadConfirmation_comicRoot)
    RelativeLayout mComicRootLayout;

    @BindView(R.id.layout_uploadConfirmation_caption)
    View mCaptionLayout;

    @BindView(R.id.layout_comicUploadConfirmation_comicBody)
    LinearLayout mComicBodyLayout;

    @BindView(R.id.et_uploadConfirmation_caption)
    MaterialEditText mCaptionET;

    @BindView(R.id.layout_comic_contentImage)
    RelativeLayout mImageContentLayout;

    @BindView(R.id.iv_comic_contentImage)
    ImageView mContentImageIV;

    @BindView(R.id.ib_comic_youtube)
    ImageButton mYoutubeIB;

    @BindView(R.id.layout_comic_video)
    View mVideoComicLayout;

    @BindView(R.id.vv_comic_contentVideo)
    VideoView mVideoView;

    @BindView(R.id.webview_comic_fbHack)
    WebView mFBWebView;

    @BindView(R.id.progressbar_comic_loading)
    ProgressBar mContentLoadingProgressbar;

    private Integer mComicType;
    private String mComicPath;
    private final static String EXTRA_COMIC_TYPE = "EXTRA_COMIC_TYPE";
    private final static String EXTRA_COMIC_PATH = "EXTRA_COMIC_PATH";

    private String mVidSourceUrl;

    private ViewImageGeneration mImageDownloadTask;
    private ImageCompression mImageCompressionTask;

    private MediaPlayer mMediaPlayer;

    @Inject
    FBVideoSourceGrabber mFBVideoSourceGrabber;


    public static UploadCaptionFragment newInstance(int comicType, String comicPath) {
        UploadCaptionFragment uploadCaptionFragment = new UploadCaptionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_COMIC_TYPE, comicType);
        bundle.putString(EXTRA_COMIC_PATH, comicPath);
        uploadCaptionFragment.setArguments(bundle);
        return uploadCaptionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload_caption, container, false);
        ButterKnife.bind(this, root);

        Bundle bundle = getArguments();
        mComicType = mComicType == null? bundle.getInt(EXTRA_COMIC_TYPE) : mComicType;
        mComicPath = mComicPath == null? bundle.getString(EXTRA_COMIC_PATH) : mComicPath;

        setUp(root);

        return root;
    }


    @Override
    public void onStop() {
        if (mImageDownloadTask != null) {
            mImageDownloadTask.cancelTask();
        }

        if (mImageCompressionTask != null) {
            mImageCompressionTask.cancelImageCompressionTask();
        }

        super.onStop();
    }


    @Override
    protected void setUp(View view) {
        setupComicView();
    }

    public String getCaption() {
        return mCaptionET.getText().toString();
    }
    public String getCredits() {
        return mCreditsET.getText().toString();
    }
    public String getFBVideoSourceUrl() {
        return mVidSourceUrl;
    }

    public void processData(){
        switch (mComicType) {
            case ApiHelper.COMIC_TYPES.IMAGE:
                startImageDownloadTask();
                break;

            default:
                ((UploadConfirmationActivity) getActivity()).onCaptionFragmentDataProcessed(null);
                break;
        }
    }


    private void setupComicView() {
        switch (mComicType) {
            case ApiHelper.COMIC_TYPES.VIDEO_FACEBOOK:
                if (mFBVideoSourceGrabber == null) {
                    mFBVideoSourceGrabber = new FBVideoSourceGrabber();
                }

                mFBVideoSourceGrabber.setHandler(this);
                mFBVideoSourceGrabber.setupFBVideoGrabberWebView(getContext(), mFBWebView, mComicPath);
                break;

            case ApiHelper.COMIC_TYPES.VIDEO_INSTAGRAM:
                mVideoComicLayout.setVisibility(View.VISIBLE);
                mCaptionLayout.setVisibility(View.VISIBLE);
                ((UploadConfirmationActivity) getActivity()).getInstagramVideoData(mComicPath);
                break;

            case ApiHelper.COMIC_TYPES.VIDEO_YOUTUBE:
                mYoutubeIB.setVisibility(View.VISIBLE);
                mCaptionLayout.setVisibility(View.VISIBLE);
                mImageContentLayout.setVisibility(View.VISIBLE);

                ImageUtils.loadImageUrlIntoView(getContext(),
                        mContentImageIV,
                        YoutubeActivity.getYoutubeThumbnail(mComicPath),
                        false,
                        -1);

                hideLoadingDialog();
                break;

            case ApiHelper.COMIC_TYPES.IMAGE:
                mImageContentLayout.setVisibility(View.VISIBLE);
                mCreditsLayout.setVisibility(View.VISIBLE);

                startImageCompressionToBitmapTask(mComicPath);
                break;

            case ApiHelper.COMIC_TYPES.GIF: // Image or Gif
                mImageContentLayout.setVisibility(View.VISIBLE);

                mImageContentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if(mImageContentLayout.getVisibility() == View.VISIBLE)
                                {
                                    GlideApp.with(getContext())
                                            .load(mComicPath)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                                            Target<Drawable> target, boolean isFirstResource) {
                                                    hideLoadingDialog();
                                                    showErrorMsgDialog(getString(R.string.msg_error_image));
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model,
                                                                               Target<Drawable> target,
                                                                               DataSource dataSource, boolean isFirstResource) {
                                                    hideLoadingDialog();
                                                    mCaptionLayout.setVisibility(View.VISIBLE);
                                                    return false;
                                                }
                                            })
                                            .into(mContentImageIV);
                                }
                            }
                        });

                break;
        }
    }

    public void initVideoView(String videoUrl) {
        videoUrl = videoUrl == null? "" : videoUrl;

        /*try {
            Uri url = Uri.parse(videoUrl);
            MediaController controller = new MediaController(this);
            mVideoView.setMediaController(controller);
            mVideoView.setVideoURI(url);
            mVideoView.start();

        } catch (Exception ex) {
            Timber.e("Facebook URI video error: " + ex.getMessage().toString());
        }*/
        mVideoView.setVideoPath(videoUrl);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Timber.d("Video prepared");
                hideLoadingDialog();
                mMediaPlayer = mp;
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showErrorMsgDialog(getString(R.string.msg_error_video_url));
                return true;
            }
        });
    }


    public void startImageDownloadTask() {
        mCaptionET.clearFocus();
        mCaptionET.setCursorVisible(false);

        if (mImageDownloadTask == null) {
            mImageDownloadTask = new ViewImageGeneration(getContext(), new ViewImageGeneration.ViewImageGenerationHandler() {

                        @Override
                        public void onImageGenerated(Bitmap bitmap) {
                            mImageDownloadTask = null;

                            mCaptionET.setCursorVisible(true);

                            if (bitmap == null) {
                                MessageDialog dialog = MessageDialog.newInstance(getString(R.string.free_some_space));
                                dialog.show(getActivity().getSupportFragmentManager(), dialog.getMyDialogTag());

                            } else {
                                startImageCompressionToByteArrTask(FileUtils.getCachedImagePath(getContext()));
                            }
                        }
                    });

            /*View viewToDownloadAsImage;
            if (getCaption() == null || getCaption().isEmpty()) {
                viewToDownloadAsImage = mContentImageIV;

            } else {
                viewToDownloadAsImage = mComicRootLayout;
            }*/

            mImageDownloadTask.startNewTask(mContentImageIV, true);
        }
    }
    private void startImageCompressionToByteArrTask(String imagePath) {
        if (mImageCompressionTask == null) {
            mImageCompressionTask = new ImageCompression(getContext(),
                    new ImageCompression.ImageCompressionHandler() {

                        @Override
                        public void onCompressionCompleted(byte[][] filesByteArrays) {
                            mImageCompressionTask = null;
                            ((UploadConfirmationActivity) getActivity()).onCaptionFragmentDataProcessed(filesByteArrays);
                        }

                        @Override
                        public void onCompressionCompleted(Bitmap bitmap) {
                        }
                    });

            mImageCompressionTask.startNewImageCompressionToByteArrTask(imagePath);
        }
    }
    private void startImageCompressionToBitmapTask(String imagePath) {
        if (mImageCompressionTask == null) {
            mImageCompressionTask = new ImageCompression(getContext(),
                    new ImageCompression.ImageCompressionHandler() {

                        @Override
                        public void onCompressionCompleted(byte[][] filesByteArrays) {
                        }

                        @Override
                        public void onCompressionCompleted(Bitmap bitmap) {
                            mImageCompressionTask = null;
                            if (bitmap == null) {
                                showErrorMsgDialog(getString(R.string.msg_error_image));
                                return;
                            }

                            hideLoadingDialog();
                            mCaptionLayout.setVisibility(View.VISIBLE);
                            mContentImageIV.setImageBitmap(bitmap);
                        }
                    });

            mImageCompressionTask.startNewImageCompressionToBitmapTask(imagePath);
        }
    }


    public double getContentAspectRatio() {
        double width;
        double height;

        if (mComicType == ApiHelper.COMIC_TYPES.IMAGE && !getCaption().isEmpty()) {
            width = (double) mComicRootLayout.getMeasuredWidth();
            height = (double) mComicRootLayout.getMeasuredHeight();

        } else {
            width = (double) mComicBodyLayout.getMeasuredWidth();
            height = (double) mComicBodyLayout.getMeasuredHeight();
        }

        return height == 0? 0 : width / height;
    }

    private void hideContentLoading() {
        if (mContentLoadingProgressbar.getVisibility() == View.VISIBLE) {
            mContentLoadingProgressbar.setVisibility(View.GONE);
        }
    }
    private void showContentLoading() {
        if (mContentLoadingProgressbar.getVisibility() != View.VISIBLE) {
            mContentLoadingProgressbar.setVisibility(View.VISIBLE);
        }
    }


    private void showErrorMsgDialog(String msg) {
        MessageDialog dialog = MessageDialog.newInstance(msg);
        dialog.show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onFBVideoSourceUrlReady(final String sourceUrl) {
        mVidSourceUrl = sourceUrl;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCaptionLayout.setVisibility(View.VISIBLE);
                mVideoComicLayout.setVisibility(View.VISIBLE);
                initVideoView(sourceUrl);
            }
        });
    }

    @Override
    public void onErrorMsgOkClicked() {

    }
}
