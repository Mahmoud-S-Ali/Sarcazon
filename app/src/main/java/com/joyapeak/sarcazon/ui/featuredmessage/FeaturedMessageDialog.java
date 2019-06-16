package com.joyapeak.sarcazon.ui.featuredmessage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by test on 9/10/2018.
 */

public class FeaturedMessageDialog extends BaseDialog {

    private static final String TAG = "FeaturedMessageDialog";

    @BindView(R.id.et_featuredMessage)
    MaterialEditText mFeaturedMessageET;

    @BindView(R.id.btn_done)
    Button mDoneBtn;

    @BindView(R.id.btn_cancel)
    Button mCancelBtn;

    private FeaturedMessageDialogHandler mHandler;

    public interface FeaturedMessageDialogHandler {
        void onReleaseFeaturedDoneClicked(String message);
    }


    public static FeaturedMessageDialog newInstance() {
        FeaturedMessageDialog dialog = new FeaturedMessageDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (mHandler == null) {
                mHandler = (FeaturedMessageDialogHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_featured_message, container, false);
        setUnBinder(ButterKnife.bind(this, view));

        return view;
    }

    @Override
    public String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {
        mFeaturedMessageET.setText(getString(R.string.featured_msg_default));
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClicked() {
        getDialog().dismiss();
    }

    @OnClick(R.id.btn_done)
    public void onDoneClicked() {
        String message = mFeaturedMessageET.getText().toString();
        if (message.isEmpty()) {
            mFeaturedMessageET.setError("Message can't be empty");

        } else {
            mHandler.onReleaseFeaturedDoneClicked(mFeaturedMessageET.getText().toString());
            hideKeyboard();
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (mHandler == null) {
                mHandler = (FeaturedMessageDialogHandler) context;
            }

        } catch (ClassCastException e) {
            Timber.e(" must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
