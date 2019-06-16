package com.joyapeak.sarcazon.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.ui.MyPagerAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.custom.CustomViewPager;
import com.joyapeak.sarcazon.ui.newmain.NewMainActivity;
import com.joyapeak.sarcazon.ui.profile.profilecomics.ProfileComicsFragment;
import com.joyapeak.sarcazon.ui.profile.profilenotifications.ProfileNotificationsFragment;
import com.joyapeak.sarcazon.ui.profilesettings.ProfileSettingsActivity;
import com.joyapeak.sarcazon.ui.share.link.ShareLinkDialog;
import com.joyapeak.sarcazon.ui.subs.SubsActivity;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ProfileActivity extends BaseActivity implements ProfileMvpView {

    @BindView(R.id.appbar_profile)
    AppBarLayout mAppbar;

    @BindView(R.id.iv_profile_userImage)
    ImageView mUserImageIV;

    @BindView(R.id.tv_profile_name)
    TextView mUserNameTV;

    @BindView(R.id.tv_profile_followersCount)
    TextView mFollowersCountTV;

    @BindView(R.id.tv_profile_followingsCount)
    TextView mFollowingsCountTV;

    @BindView(R.id.tv_profile_totalLikesCount)
    TextView mTotalLikesCountTV;

    @BindView(R.id.layout_divider_light)
    FrameLayout mDividerLayout;

    @BindView(R.id.tv_profile_quote)
    TextView mQuoteTV;

    @BindView(R.id.btn_profile_follow)
    Button mFollowBtn;

    @BindView(R.id.viewpager_profile)
    CustomViewPager mViewPager;

    @BindView(R.id.tab_layout_profile)
    TabLayout mTabLayout;

    private final String PROFILE_OPTION_SHARE = "Share";
    private final String PROFILE_OPTION_EDIT = "Edit";
    private final String PROFILE_OPTION_REPORT = "Report";
    private final String PROFILE_OPTION_LOGOUT = "Logout";


    @Inject
    MyPagerAdapter mViewPagerAdapter;

    @Inject
    ProfileMvpPresenter<ProfileMvpView> mPresenter;

    private static final int VIEW_PAGER_OFFSET_LIMIT = 4;


    private ProfileResponse.ProfileInfoResponse mProfileInfo;

    private enum TAB_POSITIONS {
        NOTIFICATIONS,
        COMICS
    };

    private long mUserId;
    private boolean mIsOwnerUser;
    public static final String EXTRA_PROFILE_USER_ID = "EXTRA_PROFILE_USER_ID";

    private static final int REQ_CODE_PROFILE_SETTINGS = 100;
    public static final String EXTRA_PROFILE_INFO = "EXTRA_PROFILE_INFO";

    private final int REQ_CODE_SUBS = 200;
    public static final String EXTRA_FOLLOWINGS_COUNT_CHANGE = "EXTRA_FOLLOWINGS_COUNT_CHANGE";

    public static Intent getStartIntent(Context context, long userId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_PROFILE_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUnBinder(ButterKnife.bind(this));

        mParentActivityName = getIntent().getStringExtra(EXTRA_PARENT_ACTIVITY);

        mUserId = getIntent().getLongExtra(EXTRA_PROFILE_USER_ID, AppConstants.NULL_INDEX);

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        setUp();

        mPresenter.onViewInitialized(mUserId);
    }

    private void setUp() {
        setupGeneralToolbar(mAppbar, "");
    }
    private void setupViewPager() {
        if (mIsOwnerUser) {
            mViewPagerAdapter.addFragment(ProfileNotificationsFragment.newInstance(), String.valueOf(mViewPagerAdapter.getCount()));
        }
        mViewPagerAdapter.addFragment(ProfileComicsFragment.newInstance(mUserId), String.valueOf(mViewPagerAdapter.getCount()));

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(VIEW_PAGER_OFFSET_LIMIT);
    }
    private void setupTabLayout() {
        if (mIsOwnerUser) {
            mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.profile_tab_notifications)));
        }
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.profile_tab_comics)));

        // mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPos = tab.getPosition();
                mViewPager.setCurrentItem(tabPos);
                Timber.d("Tab selected: " + tabPos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Timber.d("Tab unselected: " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_PROFILE_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    ProfileResponse.ProfileInfoResponse profileInfo
                            = data.getParcelableExtra(EXTRA_PROFILE_INFO);

                    if (profileInfo != null && !mProfileInfo.equals(profileInfo)) {
                        mProfileInfo = profileInfo;
                        updateProfileInfo(mProfileInfo);
                    }
                }
                break;

            case REQ_CODE_SUBS:
                if (resultCode == Activity.RESULT_OK) {
                    int changeInFollowingsCount = data.getIntExtra(EXTRA_FOLLOWINGS_COUNT_CHANGE,
                            0);
                    updateFollowingsCount(changeInFollowingsCount);
                }
                break;
        }
    }

    @OnClick(R.id.layout_profile_followings)
    public void onFollowingsClicked() {
        openSubsActivity(false);
    }

    @OnClick(R.id.layout_profile_followers)
    public void onFollowersClicked() {
        openSubsActivity(true);
    }

    @OnClick(R.id.btn_profile_follow)
    public void onFollowClicked() {
        mPresenter.subscribe(mUserId, !mProfileInfo.getIsSubscribed());
    }

    @OnClick(R.id.ib_toolbar_profile_options)
    public void onProfileOptionsClicked() {
        final String[] profileOptionsArr = mIsOwnerUser?
                getResources().getStringArray(R.array.profile_options_owner) :
                getResources().getStringArray(R.array.profile_options_general);

        GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(profileOptionsArr);
        dialog.setHandler(
                new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                    @Override
                    public void onItemSelected(int position) {
                        switch (profileOptionsArr[position]) {
                            case PROFILE_OPTION_SHARE:
                                long userId = mUserId == AppConstants.NULL_INDEX? mPresenter.getOwnerId() : mUserId;
                                mPresenter.shareProfile(ProfileActivity.this,
                                        userId, mProfileInfo.getProfilePhotoUrl());
                                break;

                            case PROFILE_OPTION_EDIT:
                                openProfileSettingsActivity();
                                break;

                            case PROFILE_OPTION_REPORT:
                                showMessage("Report");
                                break;

                            case PROFILE_OPTION_LOGOUT:
                                mPresenter.logout();
                                break;
                        }
                    }
                });

        dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
    }

    /*@OnClick(R.id.ib_profile_share)
    public void onShareProfileClicked() {
        mPresenter.shareProfile(getApplicationContext(), mUserId);
    }

    @OnClick(R.id.tv_profile_logout)
    public void onLogoutProfileClicked() {
        mPresenter.logout();
    }*/


    public void openSubsActivity(boolean isFollowers) {
        int followersCount = mProfileInfo == null? 0 : mProfileInfo.getFollowersCount();
        startActivityForResult(
                SubsActivity.getStartIntent(this, mUserId, isFollowers, followersCount),
                REQ_CODE_SUBS);
    }
    private void openProfileSettingsActivity() {
        Intent i = ProfileSettingsActivity.getStartIntent(getApplicationContext(), mProfileInfo);
        i.putExtra(EXTRA_PARENT_ACTIVITY, ProfileActivity.class.getSimpleName());
        startActivityForResult(i, REQ_CODE_PROFILE_SETTINGS);
    }

    @Override
    public void handleAfterViewInitialization(boolean isOwner) {
        mIsOwnerUser = isOwner;

        setupViewPager();

        if (mIsOwnerUser) {
            setupTabLayout();
            /*mToolbarProfileOptionsIB.setVisibility(View.VISIBLE);
            mProfileLogoutTV.setVisibility(View.VISIBLE);*/
        } else {
            mTabLayout.setVisibility(View.GONE);
        }

        mPresenter.getProfileInfo(mUserId);
    }

    @Override
    public void updateProfileInfo(ProfileResponse.ProfileInfoResponse profileInfo) {
        if (profileInfo != null) {
            mProfileInfo = profileInfo;

            ImageUtils.loadImageUrlIntoView(this,
                    mUserImageIV,
                    mProfileInfo.getThumbnailUrl(),
                    true,
                    R.drawable.im_default);

            mUserNameTV.setText(mProfileInfo.getName());
            mFollowersCountTV.setText(CommonUtils.getShortNumberRepresentation(mProfileInfo.getFollowersCount()));
            mFollowingsCountTV.setText(CommonUtils.getShortNumberRepresentation(mProfileInfo.getFollowingsCount()));
            mTotalLikesCountTV.setText(CommonUtils.getShortNumberRepresentation(mProfileInfo.getTotalLikes()));

            String quote = profileInfo.getQuote();
            if (quote == null || quote.isEmpty()) {
                mQuoteTV.setVisibility(View.GONE);
                mDividerLayout.setVisibility(View.GONE);

            } else {
                mQuoteTV.setVisibility(View.VISIBLE);
                mDividerLayout.setVisibility(View.VISIBLE);
                mQuoteTV.setText(quote);
            }

            updateFollowView();
        }
    }

    @Override
    public void updateFollowStatus(boolean isFollowed) {
        mProfileInfo.setIsSubscribed(isFollowed);
        updateFollowView();

        updateFollowersCount(isFollowed? mProfileInfo.getFollowersCount() + 1 : mProfileInfo.getFollowersCount() - 1);
    }

    @Override
    public void handleProfileShare(String url) {
        ShareLinkDialog dialog = ShareLinkDialog.newInstance(url);
        dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
    }

    @Override
    public void onUserSetAsLoggedOut() {
        finish();
        openActivityOnLogout();
    }

    private void updateFollowView() {

        // In case user has opened his own profile
        if (mIsOwnerUser) {
            mFollowBtn.setVisibility(View.GONE);
            return;
        }

        if (mProfileInfo == null || mProfileInfo.getIsSubscribed() == null) {
            return;
        }

        mFollowBtn.setVisibility(View.VISIBLE);
        boolean isFollowed = mProfileInfo.getIsSubscribed();
        if (isFollowed) {
            mFollowBtn.setText(getString(R.string.unfollow));
            mFollowBtn.setTextColor(ContextCompat.getColor(this, R.color.white_op50));

        } else {
            mFollowBtn.setText(getString(R.string.follow));
            mFollowBtn.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    private void updateFollowingsCount(int changeInFollowingsCount) {
        if (mProfileInfo != null && mProfileInfo.getFollowingsCount() != null) {
            int newFollowingsCount = mProfileInfo.getFollowingsCount() + changeInFollowingsCount;
            mProfileInfo.setFollowingsCount(newFollowingsCount);
            mFollowingsCountTV.setText(CommonUtils.getShortNumberRepresentation(newFollowingsCount));
        }
    }
    private void updateFollowersCount(int followersCount) {
        mProfileInfo.setFollowersCount(followersCount);
        mFollowersCountTV.setText(CommonUtils.getShortNumberRepresentation(followersCount));
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
        Intent i = null;

        if (mParentActivityName != null && mParentActivityName.equals(ProfileActivity.class.getSimpleName())) {
            i = new Intent(this, ProfileActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        } else {
            i = new Intent(this, NewMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        return i;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
