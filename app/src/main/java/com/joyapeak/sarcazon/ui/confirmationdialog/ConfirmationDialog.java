package com.joyapeak.sarcazon.ui.confirmationdialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by test on 8/20/2018.
 */

public class ConfirmationDialog extends BaseDialog {

    private static final String TAG = "ConfirmationDialog";

    @BindView(R.id.tv_dialog_msg)
    TextView mMsgTV;

    private String mMsg;
    private final static String EXTRA_MSG = "EXTRA_MSG";

    private ConfirmationDialogHandler mHandler;

    public interface ConfirmationDialogHandler {
        void onConfirmationDialogYesClicked();
        void onConfirmationDialogNoClicked();
    }


    public static ConfirmationDialog newInstance(String msg) {
        ConfirmationDialog dialog = new ConfirmationDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_MSG, msg);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (mHandler == null) {
                mHandler = (ConfirmationDialogHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_confirmation, container, false);
        setUnBinder(ButterKnife.bind(this, view));

        mMsg = getArguments().getString(EXTRA_MSG);
        return view;
    }

    @Override
    public String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {
        mMsgTV.setText(mMsg);
    }

    @OnClick(R.id.btn_dialog_yes)
    public void onYesClicked() {
        mHandler.onConfirmationDialogYesClicked();
        dismiss();
    }

    @OnClick(R.id.btn_dialog_no)
    public void onNoClicked() {
        mHandler.onConfirmationDialogNoClicked();
        dismiss();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (mHandler == null && context instanceof ConfirmationDialogHandler) {
                mHandler = (ConfirmationDialogHandler) context;
            }

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }
}
