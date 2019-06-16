package com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.tags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseFragment;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 11/27/2018.
 */

public class UploadTagsFragment extends BaseFragment {

    public static final String TAG = "UploadTagsFragment";

    @BindView(R.id.rv_tags)
    RecyclerView mTagsRV;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    @Inject
    TagsAdapter mTagsAdapter;


    public static UploadTagsFragment newInstance() {
        UploadTagsFragment fragment = new UploadTagsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload_tags, container, false);

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            setUnBinder(ButterKnife.bind(this, root));

            setUp(root);
        }

        return root;
    }

    @Override
    protected void setUp(View view) {
        setupTagsRV();
    }

    private void setupTagsRV() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTagsRV.setLayoutManager(mLayoutManager);
        mTagsRV.setAdapter(mTagsAdapter);
        mTagsRV.setHasFixedSize(true);
    }

    public String getTags() {
        String[] tagsArr = mTagsAdapter.getTags();
        StringBuilder tagsStrBuilder = new StringBuilder();

        for (String str : tagsArr) {
            if (str != null && !str.isEmpty()) {
                tagsStrBuilder.append(str + ",");
            }
        }

        String tags = tagsStrBuilder.toString();
        return tags == null || tags.isEmpty()? null : tags;
    }
}
