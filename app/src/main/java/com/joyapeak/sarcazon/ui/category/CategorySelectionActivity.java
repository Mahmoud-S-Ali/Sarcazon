package com.joyapeak.sarcazon.ui.category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/*
* Assigning each post to a specific category or
* filtering posts according to a specific category
* */

public class CategorySelectionActivity extends BaseActivity implements CategoryMvpView {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbarLayout;

    @BindView(R.id.main_top_panel_categories)
    SlidingUpPanelLayout mCategoriesPanelLayout;

    @BindView(R.id.rv_categories)
    RecyclerView mCategoriesRV;

    @BindView(R.id.btn_toolbar_done)
    Button mDoneBtn;

    private boolean mPanelWasCollapsed = true;

    // This will be used to show or hide views depending on whether it's an edit mode or
    // selection one
    private boolean mEditModeEnabled = false;

    @Inject
    CategoryMvpPresenter<CategoryMvpView> mCategoryPresenter;

    @Inject
    MultipleCategoryAdapter mCategoryAdapter;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CategorySelectionActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));

        mCategoryPresenter.onAttach(this);
        setUp();

        mCategoryPresenter.getCategories();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            handleFinish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        handleFinish();
    }

    // handle closing the categories page
    private void handleFinish() {
        mCategoriesPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void setUp() {
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupGeneralToolbar(mAppbarLayout, getString(R.string.categories));
        mDoneBtn.setVisibility(View.GONE);
        // refreshEditMode();

        setupCategoriesAdapter();
        setupCategoryRV();

        setupCategoriesPanelLayout();
    }

    //
    private void refreshEditMode() {

        if (mEditModeEnabled) {
            mDoneBtn.setText(R.string.done);
            mDoneBtn.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        } else {
            mDoneBtn.setText(R.string.edit);
            mDoneBtn.setTextColor(ContextCompat.getColor(this, R.color.white_op70));
        }

        mCategoryAdapter.setEditMode(mEditModeEnabled);
    }
    private void setupCategoriesAdapter() {
        mCategoryAdapter.setCallback(new MultipleCategoryAdapter.Callback() {
            @Override
            public void onCategorySelected(String category) {
                if (mCategoryPresenter.getSelectedCategory() == null ||
                        !mCategoryPresenter.getSelectedCategory().equals(category)) {

                    mCategoryPresenter.setSelectedCategory(category);
                    setResult(Activity.RESULT_OK);
                    handleFinish();
                }
            }
        });

        mCategoryAdapter.setSelectedCategory(mCategoryPresenter.getSelectedCategory());
    }
    private void setupCategoryRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mCategoriesRV.setLayoutManager(mLayoutManager);
        mCategoriesRV.setAdapter(mCategoryAdapter);
        mCategoriesRV.setHasFixedSize(true);
    }

    // Panel Layout is a library used for smooth opening and closing an activity or fragment
    // by swiping up or down when reaching the end of the page
    private void setupCategoriesPanelLayout() {
        mCategoriesPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // Timber.d("Comments slide offset = " + String.valueOf(slideOffset));
            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                // Timber.d("Comments state changed from : " + previousState + " ...to : " + newState);

                if (mPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mPanelWasCollapsed = false;

                    mCategoryPresenter.getCategories();

                } else if (!mPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
        mCategoriesPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoriesPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        mCategoriesPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @OnClick(R.id.btn_toolbar_done)
    public void onDoneClicked() {
        if (mEditModeEnabled) {
            Timber.d(mCategoryAdapter.getSelectedCategories().toString());
        }

        mEditModeEnabled = !mEditModeEnabled;
        refreshEditMode();
    }


    @Override
    public void onCategoriesRetrieved(List<CategoryResponse.SingleCategory> categories) {
        mCategoryAdapter.setItems(categories);
        mCategoriesRV.scrollToPosition(0);
    }

    @Override
    protected void onDestroy() {
        mCategoryPresenter.onDetach();
        super.onDestroy();
    }
}
