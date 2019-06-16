package com.joyapeak.sarcazon.ui.share.photo;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.MessageDialog;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseBottomSheetDialogFragment;
import com.joyapeak.sarcazon.ui.share.ShareAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 */

public class SharePhotoDialog extends BaseBottomSheetDialogFragment implements SharePhotoDialogMvpView {

    private static final String TAG = "SharePhotoDialog";

    @BindView(R.id.rv_dialog_shareChoices)
    protected RecyclerView mChoiceRV;

    @Inject
    SharePhotoDialogMvpPresenter<SharePhotoDialogMvpView> mPresenter;

    private final static String EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI";
    private final static String EXTRA_IMAGE_TYPE = "EXTRA_IMAGE_TYPE";


    public static SharePhotoDialog newInstance(Uri imageUri, boolean isGif) {
        SharePhotoDialog fragment = new SharePhotoDialog();
        Bundle bundle = new Bundle();

        bundle.putParcelable(EXTRA_IMAGE_URI, imageUri);
        bundle.putBoolean(EXTRA_IMAGE_TYPE, isGif);

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

        return view;
    }


    @Override
    public String getMyDialogTag() {
        return TAG;
    }

    @Override
    protected void setUp(View view) {

        final Uri imageUriStr = getArguments().getParcelable(EXTRA_IMAGE_URI);
        final boolean isImageTypeGif = getArguments().getBoolean(EXTRA_IMAGE_TYPE);

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (isImageTypeGif) {
            shareIntent.setType("image/GIF");

        } else {
            shareIntent.setType("image/PNG");
        }

        final List resolveInfos = getBaseActivity().getPackageManager()
                .queryIntentActivities(shareIntent, 0);

        ShareAdapter adapter = new ShareAdapter(resolveInfos, new ShareAdapter.Callback() {
            @Override
            public void onShareSourceClicked(ResolveInfo resolveInfoItem) {
                mPresenter.onSourceShareSelected(shareIntent,
                        resolveInfoItem,
                        imageUriStr);
            }
        });
        adapter.setHasStableIds(true);

        mChoiceRV.setLayoutManager(new GridLayoutManager(getBaseActivity(),
                getBaseActivity().getResources().getInteger(R.integer.shareChoices_grid_spanCount)));
        mChoiceRV.setAdapter(adapter);
        mChoiceRV.setHasFixedSize(true);
    }

    @Override
    public void startShareIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showMessengerDialog(SharePhotoContent content) {
        MessageDialog.show(getBaseActivity(), content);
    }

    @Override
    public void showFacebookShareDialog(SharePhotoContent content) {
        com.facebook.share.widget.ShareDialog shareDialog =
                new com.facebook.share.widget.ShareDialog(getBaseActivity());

        shareDialog.show(content, com.facebook.share.widget.ShareDialog.Mode.AUTOMATIC);
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
