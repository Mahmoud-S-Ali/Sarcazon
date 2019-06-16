package com.joyapeak.sarcazon.ui.popupmessage;

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
 * Created by Mahmoud Ali on 7/7/2018.
 */

public class MessageDialog extends BaseDialog {

    private static final String TAG = "MessageDialog";

    @BindView(R.id.tv_dialog_msg)
    TextView mMsgTV;

    private String mMsg;
    private final static String EXTRA_MSG = "EXTRA_MSG";

    private MessageDialogHandler mHandler;

    public interface MessageDialogHandler {
        void onErrorMsgOkClicked();
    }


    public static MessageDialog newInstance(String msg) {
        MessageDialog dialog = new MessageDialog();
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
                mHandler = (MessageDialogHandler) getTargetFragment();
            }

        } catch (ClassCastException e) {
            Timber.e("Calling Fragment must implement OnAddFriendListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_msg, container, false);
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

    @OnClick(R.id.btn_dialog_ok)
    public void onOkClicked() {
        mHandler.onErrorMsgOkClicked();
        getDialog().dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (mHandler == null && context instanceof MessageDialogHandler) {
                mHandler = (MessageDialogHandler) context;
            }

        } catch (ClassCastException e) {
            Timber.e(" must implement OnArticleSelectedListener");
        }
    }
}
