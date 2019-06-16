package com.joyapeak.sarcazon.ui.subs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsResponse;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubsActivity extends BaseActivity implements SubsMvpView {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.tv_toolbar_general_title)
    TextView mToolbarTitleTV;

    @BindView(R.id.rv_subs)
    RecyclerView mSubsRV;

    @BindView(R.id.layout_empty_items)
    View mEmptyView;

    @Inject
    SubsMvpPresenter<SubsMvpView> mPresenter;

    @Inject
    SubsAdapter mSubsAdapter;


    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    private SubsAdapter.ViewHolder mSubsHolder;

    private static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private static final String EXTRA_SUBS_TYPE = "EXTRA_SUBS_TYPE";
    private static final String EXTRA_SUBS_COUNT = "EXTRA_SUBS_COUNT";

    private long mUserId;
    private boolean mIsSubscribers;
    private int mSubsCount = 0;

    private int mChangeInSubscriptionsCount = 0;


    public static Intent getStartIntent(Context context, long userId, boolean isSubscribers,
                                        int subsCount) {
        Intent intent = new Intent(context, SubsActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_SUBS_COUNT, subsCount);
        intent.putExtra(EXTRA_SUBS_TYPE, isSubscribers);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);
        setUnBinder(ButterKnife.bind(this));

        if (getIntent() != null) {
            mUserId = getIntent().getLongExtra(EXTRA_USER_ID, AppConstants.NULL_INDEX);
            mIsSubscribers = getIntent().getBooleanExtra(EXTRA_SUBS_TYPE, false);
            mSubsCount = getIntent().getIntExtra(EXTRA_SUBS_COUNT, 0);
        }

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        setUp();
        getSubs();
    }

    @Override
    public void onBackPressed() {
        prepareActivityForFinish();
        super.onBackPressed();
    }

    private void prepareActivityForFinish() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ProfileActivity.EXTRA_FOLLOWINGS_COUNT_CHANGE, mChangeInSubscriptionsCount);
        setResult(Activity.RESULT_OK, resultIntent);
    }

    private void getSubs() {
        if (mIsSubscribers) {
            mPresenter.getSubscribers(mUserId);

        } else {
            mPresenter.getSubscriptions(mUserId);
        }
    }

    private void setUp() {
        setupGeneralToolbar(mAppbar, getToolbarTitle(mSubsCount));
        // updateSubsCountView();

        setupSubsAdapter();
        setupSubsRV();
    }
    private void setupSubsAdapter() {
        mSubsAdapter.setCallback(new SubsAdapter.Callback() {
            @Override
            public void onSubItemClicked(long userId) {
                startActivity(ProfileActivity.getStartIntent(SubsActivity.this, userId));
            }

            @Override
            public void onSubscribeClicked(SubsAdapter.ViewHolder holder, long userId,
                                           boolean shouldSubscribe) {
                if (mSubsHolder == null) {
                    mSubsHolder = holder;
                    mPresenter.subscribe(userId, shouldSubscribe);
                }
            }
        });

        mSubsAdapter.setTotalSubsCount(mSubsCount);
        mSubsAdapter.setViewerId(mPresenter.getViewerId());
        mSubsAdapter.setEmptyView(mEmptyView);
    }
    private void setupSubsRV() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSubsRV.setLayoutManager(mLayoutManager);
        mSubsRV.setAdapter(mSubsAdapter);

        mSubsAdapter.addNewItemsOnScrollListener(mSubsRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {

            @Override
            public void onScrollReachedEnd() {
                getSubs();
            }
        });
    }

    private String getToolbarTitle(int count) {
        return mIsSubscribers? getString(R.string.followers_header, CommonUtils.getShortNumberRepresentation(count)) :
                getString(R.string.followings_header, CommonUtils.getShortNumberRepresentation(count));
    }

    private void updateSubsCountView() {
        mToolbarTitleTV.setText(getToolbarTitle(mSubsCount));
    }

    private void updateSubscriptionsCount(boolean hasSubscribed) {
        int subscriptionResult = hasSubscribed? 1 : -1;

        mChangeInSubscriptionsCount += subscriptionResult;

        if (!mIsSubscribers) {
            mSubsCount += subscriptionResult;
            updateSubsCountView();
        }
    }


    @Override
    public void addSubsList(List<SubsResponse.UserSub> subsList) {
        mSubsAdapter.addSubs(subsList);
        mSubsAdapter.resetIsLoadingNewItems();
    }

    @Override
    public void updateSubscribeStatus(boolean hasSubscribed) {
        if (mSubsHolder != null) {
            mSubsAdapter.updateSubscribeStatus(this, mSubsHolder, hasSubscribed);
            updateSubscriptionsCount(hasSubscribed);
            mSubsHolder = null;
        }
    }


    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImpl();
    }

    @Override
    public Intent getParentActivityIntent() {
        return getParentActivityIntentImpl();
    }


    private Intent getParentActivityIntentImpl() {
        Intent i = new Intent(this, ProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return i;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
