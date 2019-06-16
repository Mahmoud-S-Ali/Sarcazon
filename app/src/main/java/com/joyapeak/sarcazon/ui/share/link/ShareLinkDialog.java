package com.joyapeak.sarcazon.ui.share.link;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseBottomSheetDialogFragment;
import com.joyapeak.sarcazon.ui.share.ShareAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 5/14/2018.
 */

public class ShareLinkDialog extends BaseBottomSheetDialogFragment implements ShareLinkDialogMvpView {

    private static final String TAG = "ShareLinkDialog";

    @BindView(R.id.rv_dialog_shareChoices)
    protected RecyclerView mChoiceRV;

    @Inject
    ShareLinkDialogMvpPresenter<ShareLinkDialogMvpView> mPresenter;

    private String mLinkUrl;
    private final static String EXTRA_LINK_URL = "EXTRA_LINK_URL";


    public static ShareLinkDialog newInstance(String linkUrl) {
        ShareLinkDialog fragment = new ShareLinkDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_LINK_URL, linkUrl);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_share_choices, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            mPresenter.onAttach(this);
            setUnBinder(ButterKnife.bind(this, view));
        }

        mLinkUrl = getArguments().getString(EXTRA_LINK_URL);
        return view;
    }

    @Override
    public String getMyDialogTag() {
        return TAG;
    }


    @Override
    public void startShareIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showMessengerDialog(ShareLinkContent content) {
        MessageDialog.show(getBaseActivity(), content);
    }

    @Override
    public void showFacebookShareDialog(ShareLinkContent content) {
        ShareDialog.show(getBaseActivity(), content);
    }

    @Override
    protected void setUp(View view) {

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        final List resolveInfos = getBaseActivity().getPackageManager()
                .queryIntentActivities(shareIntent, 0);

        ShareAdapter adapter = new ShareAdapter(resolveInfos, new ShareAdapter.Callback() {
            @Override
            public void onShareSourceClicked(ResolveInfo resolveInfoItem) {
                mPresenter.onSourceShareSelected(shareIntent, resolveInfoItem, mLinkUrl);
            }
        });
        adapter.setHasStableIds(true);

        mChoiceRV.setLayoutManager(new GridLayoutManager(getBaseActivity(),
                getBaseActivity().getResources().getInteger(R.integer.shareChoices_grid_spanCount)));
        mChoiceRV.setAdapter(adapter);
        mChoiceRV.setHasFixedSize(true);
    }


    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}