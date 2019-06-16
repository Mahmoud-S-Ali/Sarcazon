package com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.category.CategoryMvpPresenter;
import com.joyapeak.sarcazon.ui.category.CategoryMvpView;
import com.joyapeak.sarcazon.ui.category.SingleCategoryAdapter;
import com.joyapeak.sarcazon.ui.upload.uploadconfirmation.UploadConfirmationActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 11/27/2018.
 */

public class UploadCategoryFragment extends BaseFragment implements CategoryMvpView {

    public static final String TAG = "UploadCategoryFragment";

    @BindView(R.id.rv_post_category)
    RecyclerView mPostCategoryRV;

    @Inject
    SingleCategoryAdapter mPostCategoryAdapter;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    @Inject
    CategoryMvpPresenter<CategoryMvpView> mCategoryPresenter;


    public static UploadCategoryFragment newInstance() {
        UploadCategoryFragment fragment = new UploadCategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload_category, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, root));

            mCategoryPresenter.onAttach(this);

            setUp(root);

            mCategoryPresenter.getCategories();
        }

        return root;
    }

    @Override
    protected void setUp(View view) {
        setupPostCategoryAdapter();
        setupPostCategoryRV();
    }

    private void setupPostCategoryAdapter() {
        mPostCategoryAdapter.setCallback(new SingleCategoryAdapter.Callback() {
            @Override
            public void onCategorySelected() {
                ((UploadConfirmationActivity) getActivity()).setNextBtnAsEnabled(true);
            }
        });
    }
    private void setupPostCategoryRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mPostCategoryRV.setLayoutManager(mLayoutManager);
        mPostCategoryRV.setAdapter(mPostCategoryAdapter);
        mPostCategoryRV.setHasFixedSize(true);
    }

    public String getSelectedCategory() {
        return mPostCategoryAdapter.getSelectedCategory();
    }

    @Override
    public void onCategoriesRetrieved(List<CategoryResponse.SingleCategory> categories) {
        mPostCategoryAdapter.setItems(categories);
    }

    @Override
    public void onDestroy() {
        mCategoryPresenter.onDetach();
        super.onDestroy();
    }
}
