package com.joyapeak.sarcazon.ui.upload.urlpaste;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Mahmoud Ali on 7/5/2018.
 */

public class UrlPasteDialog extends BaseDialog implements UrlPasteDialogMvpView {

    private static final String TAG = "UrlPasteDialog";

    @BindView(R.id.tv_urlPasteDialog_title)
    TextView mHintTitleTV;

    @BindView(R.id.et_urlPasteDialog)
    MaterialEditText mUrlPasteET;

    @BindView(R.id.btn_done)
    Button mDoneBtn;

    @BindView(R.id.btn_cancel)
    Button mCancelBtn;

    @Inject
    UrlPasteDialogMvpPresenter<UrlPasteDialogMvpView> mPresenter;

    private String mComicUrl;
    private Integer mUrlType;
    private final static String EXTRA_URL_TYPE_URL = "EXTRA_URL_TYPE_URL";

    private UrlPasteDialogHandler mHandler;

    public static final int URL_ERROR_EMPTY = 1;
    public static final int URL_ERROR_VIDEO_SOURCE = 2;
    public static final int URL_ERROR_GIF = 3;


    public interface UrlPasteDialogHandler {
        void onUrlPasteDoneClicked(int comicType, String comicUrl);
    }


    public static UrlPasteDialog newInstance(Integer comicType) {
        UrlPasteDialog dialog = new UrlPasteDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_URL_TYPE_URL, comicType);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (mHandler == null) {
                mHandler = (UrlPasteDialogHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_url_paste, container, false);

        mUrlType = getArguments().getInt(EXTRA_URL_TYPE_URL);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));

            mPresenter.onAttach(this);
        }

        return view;
    }

    @Override
    protected String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {
        switch (mUrlType) {
            case ApiHelper.COMIC_TYPES.GIF:
                mHintTitleTV.setText(getString(R.string.url_paste_gif_hint_title));
                break;

            default:
                mHintTitleTV.setText(getString(R.string.url_paste_video_hint_title));
                break;
        }

        mDoneBtn.setText(getString(R.string.next));
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClicked() {
        getDialog().dismiss();
    }

    @OnClick(R.id.btn_done)
    public void onDoneClicked() {
        mComicUrl = mUrlPasteET.getText().toString();
        mPresenter.onNextClicked(mUrlType, mComicUrl);
    }


    @Override
    public void handleUrlStatus(boolean isValidUrl, Integer errorCode) {
        if (isValidUrl) {
            hideKeyboard();
            int comicType = mPresenter.getComicTypeFromUrl(mComicUrl);
            mHandler.onUrlPasteDoneClicked(comicType, mComicUrl);
            getDialog().dismiss();

        } else {
            String errorMsg;
            switch (errorCode) {
                case URL_ERROR_EMPTY:
                    errorMsg = getString(R.string.url_error_empty);
                    break;

                case URL_ERROR_VIDEO_SOURCE:
                    errorMsg = getString(R.string.url_error_video_source);
                    break;

                case URL_ERROR_GIF:
                default:
                    errorMsg = getString(R.string.url_error_general);
                    break;
            }

            mUrlPasteET.setError(errorMsg);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (mHandler == null && context instanceof UrlPasteDialogHandler) {
                mHandler = (UrlPasteDialogHandler) context;
            }

        } catch (ClassCastException e) {
            Timber.e(" must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
