package com.joyapeak.sarcazon.ui.newupdates.updatescontent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by test on 10/9/2018.
 */

public class UpdatesContentFragment extends BaseFragment {

    @BindView(R.id.iv_updatesDialog_img)
    ImageView mFeatureImg;

    @BindView(R.id.tv_updatesDialog_featureTitle)
    TextView mFeatureTitleTV;

    @BindView(R.id.tv_updatesDialog_featureDesc)
    TextView mFeatureDescTV;

    private String mFeatureDesc;
    private String mFeatureTitle;
    private int mFeatureDrawableId;

    private static String EXTRA_FEATURE_TITLE = "EXTRA_FEATURE_TITLE";
    private static String EXTRA_FEATURE_DESC = "EXTRA_FEATURE_DESC";
    private static String EXTRA_FEATURE_DRAWABLE_ID = "EXTRA_FEATURE_DRAWABLE_ID";


    public static UpdatesContentFragment newInstance(String title, String desc, int drawableId) {
        UpdatesContentFragment updatesContentFragment = new UpdatesContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FEATURE_TITLE, title);
        bundle.putString(EXTRA_FEATURE_DESC, desc);
        bundle.putInt(EXTRA_FEATURE_DRAWABLE_ID, drawableId);
        updatesContentFragment.setArguments(bundle);
        return updatesContentFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog_updates, container, false);
        ButterKnife.bind(this, root);

        Bundle bundle = getArguments();
        mFeatureTitle = bundle.getString(EXTRA_FEATURE_TITLE);
        mFeatureDesc = bundle.getString(EXTRA_FEATURE_DESC);
        mFeatureDrawableId = bundle.getInt(EXTRA_FEATURE_DRAWABLE_ID);

        setUp(root);

        return root;
    }

    @Override
    protected void setUp(View view) {
        mFeatureTitleTV.setText(mFeatureTitle);
        mFeatureDescTV.setText(mFeatureDesc);
        mFeatureImg.setImageDrawable(ContextCompat.getDrawable(getContext(), mFeatureDrawableId));
    }
}
