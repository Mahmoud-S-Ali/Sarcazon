package com.joyapeak.sarcazon.ui.newmain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.fcm.MyFirebaseMessagingService;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.ui.MyPagerAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.category.CategorySelectionActivity;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.confirmationdialog.ConfirmationDialog;
import com.joyapeak.sarcazon.ui.credits.CreditsActivity;
import com.joyapeak.sarcazon.ui.custom.CustomViewPager;
import com.joyapeak.sarcazon.ui.custom.SmoothActionBarDrawerToggle;
import com.joyapeak.sarcazon.ui.featuredmessage.FeaturedMessageDialog;
import com.joyapeak.sarcazon.ui.leaderboard.LeaderboardActivity;
import com.joyapeak.sarcazon.ui.main.MainMvpPresenter;
import com.joyapeak.sarcazon.ui.main.MainMvpView;
import com.joyapeak.sarcazon.ui.newmain.maincomics.MainComicsFragment;
import com.joyapeak.sarcazon.ui.newupdates.NewUpdatesDialog;
import com.joyapeak.sarcazon.ui.pendingcommentsforreview.PendingCommentsForReviewActivity;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.ui.search.SearchActivity;
import com.joyapeak.sarcazon.ui.tutorial.TutorialActivity;
import com.joyapeak.sarcazon.ui.upload.uploadoptions.UploadOptionsActivity;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.EventBusUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import timber.log.Timber;

public class NewMainActivity extends BaseActivity implements
        MainMvpView,
        ConfirmationDialog.ConfirmationDialogHandler,
        FeaturedMessageDialog.FeaturedMessageDialogHandler,
        SwipeRefreshLayout.OnRefreshListener,
        TabLayout.OnTabSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_toolbar_main_title)
    TextView mToolbarTitleTV;

    @BindView(R.id.ib_toolbar_main_pending_featured_options)
    ImageButton mPendingFeaturedOptionsIB;

    @BindView(R.id.iv_toolbar_main_notifIndicator)
    ImageView mToolbarNotifIndicatorIV;

    @BindView(R.id.swipeRefresh_main)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tab_layout_main)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager_new_main)
    CustomViewPager mViewPager;

    @BindView(R.id.ib_toolbar_main_plus)
    ImageButton mAddComicIB;

    @Inject
    MyPagerAdapter mViewPagerAdapter;

    @BindView(R.id.drawer_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navView_main)
    NavigationView mNavView;

    @BindArray(R.array.nav_view_items)
    String[] mNavViewComicSourceItems;

    @BindArray(R.array.pending_featured_options)
    String[] mPendingFeaturedOptions;

    SmoothActionBarDrawerToggle mDrawerToggle;

    private boolean mIsAdmin = false;

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    private final int NEWEST_COMICS_PAGE_POS = 0;
    private final int FEATURED_COMICS_PAGE_POS = 1;
    private final int PENDING_FEATURED_COMICS_PAGE_POS = 2;


    private Switch mNavTestSwitch;
    private Switch mNavEngagementUsersSwitch;
    private ImageView mNavUserPhotoIV;
    // private ImageView mNavProfileNotifIV;
    private TextView mNavUserNameTV;
    private RecyclerView mNavItemsRV;

    private final int NAV_ITEM_LEADERBOARD_POS = 0;
    private final int NAV_ITEM_PROFILE_POS = 1;
    private final int NAV_ITEM_CREDITS_POS = 2;
    private final int NAV_ITEM_FACEBOOK_POS = 3;
    private final int NAV_ITEM_MESSAGE_POS = 4;
    private final int NAV_ITEM_RATE_POS = 5;
    private final int NAV_ITEM_TUTORIAL_POS = 6;
    private final int NAV_ITEM_PENDING_FEATURED_POS = 7;
    private final int NAV_ITEM_REPORTED_COMICS_POS = 8;
    private final int NAV_ITEM_REPORTED_COMMENTS_POS = 9;

    private final int REQ_CODE_CATEGORY_ACTIVITY = 1;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mNavViewLayoutManager;

    @Inject
    NavViewAdapter mNavViewAdapter;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, NewMainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        mIsAdmin = mPresenter.getUserControlFlag() == ApiHelper.CONTROL_FLAG_TYPES.ADMIN;
        Timber.d("User control flag" + mPresenter.getUserControlFlag());

        mPresenter.onViewInitialized();
        setUp();
    }

    private void setUp() {
        updateToolbarTitleWithSelectedCategory();
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setupViewPager();
        setupTabLayout();

        setupDrawer();
        setupNavView();

        if (mPresenter.getNewFeaturedComicsCount() > 0) {
            switchToFeaturedPage();
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new SmoothActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mToolbarNotifIndicatorIV.setVisibility(View.GONE);
                mPresenter.logNavigationDrawerClick();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }
    private void setupNavView() {
        // View headerView = mNavView.getHeaderView(0);
        LinearLayout addNewLayout = mNavView.findViewById(R.id.layout_navView_addNew);
        mNavTestSwitch = mNavView.findViewById(R.id.switch_navView_testEnable);
        mNavEngagementUsersSwitch = mNavView.findViewById(R.id.switch_navView_engagementUsers);
        mNavUserPhotoIV = mNavView.findViewById(R.id.iv_navView_userPhoto);
        // mNavProfileNotifIV = mNavView.findViewById(R.id.iv_navView_profileNotifIcon);
        mNavUserNameTV = mNavView.findViewById(R.id.tv_navView_userName);
        mNavItemsRV = mNavView.findViewById(R.id.rv_navView);

        addNewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mNavView);
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.onUploadComicClicked();
                    }
                });
            }
        });

        View.OnClickListener onProfileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mNavView);
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        // mNavProfileNotifIV.setVisibility(View.GONE);
                        mPresenter.onOptionsProfileClicked();
                    }
                });
            }
        };
        mNavUserPhotoIV.setOnClickListener(onProfileClickListener);
        mNavUserNameTV.setOnClickListener(onProfileClickListener);

        // Show extra options for admin users
        if (mIsAdmin) {
            mNavTestSwitch.setVisibility(View.VISIBLE);
            if (mPresenter.isTestEnabled()) {
                mNavTestSwitch.setChecked(true);
            }

            mNavEngagementUsersSwitch.setVisibility(View.VISIBLE);

            mNavTestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    mPresenter.onTestSwitchStateChanged(isChecked);
                    mDrawerLayout.closeDrawer(mNavView);
                    mDrawerToggle.runWhenIdle(new Runnable() {
                        @Override
                        public void run() {
                            refreshAllComics();
                        }
                    });
                }
            });

            mNavEngagementUsersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    mPresenter.onEngagementUsersSwitchChanged(isChecked);
                    if (isChecked) {
                        showMessage("Engagement users enabled");

                    } else {
                        showMessage("Engagement users disabled");
                    }
                }
            });
        }

        setupNavViewAdapter();
        setupNavViewRV();
    }
    private void setupViewPager() {
        mViewPagerAdapter.addFragment(MainComicsFragment.newInstance(ApiHelper.COMIC_SOURCES.ALL),
                getString(R.string.tab_newest));

        mViewPagerAdapter.addFragment(MainComicsFragment.newInstance(ApiHelper.COMIC_SOURCES.FEATURED),
                getString(R.string.tab_featured));

        if (mPresenter.getIsAdmin()) {
            mViewPagerAdapter.addFragment(MainComicsFragment.newInstance(ApiHelper.COMIC_SOURCES.PENDING_FEATURED),
                    getString(R.string.tab_pending_featured));
        }

        mViewPager.setAdapter(mViewPagerAdapter);
    }
    private void setupTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_newest)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_featured)));

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void setupNavViewAdapter() {
        /*if (mIsAdmin) {
            mNavViewAdapter.addItems(Arrays.asList(mNavViewComicSourceItemsAdmin));

        } else {*/
            mNavViewAdapter.addItems(Arrays.asList(mNavViewComicSourceItems));
        // }

        mNavViewAdapter.setCallback(new NavViewAdapter.Callback() {
            @Override
            public void onItemClicked(int position) {

                mDrawerLayout.closeDrawer(mNavView);

                switch (position) {
                    case NAV_ITEM_PROFILE_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.onOptionsProfileClicked();
                            }
                        });
                        break;


                    case NAV_ITEM_LEADERBOARD_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                openLeaderboardActivity();
                            }
                        });

                        break;

                    case NAV_ITEM_REPORTED_COMMENTS_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                openReportedCommentsActivity();
                            }
                        });

                        break;

                    case NAV_ITEM_CREDITS_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                openCreditsActivity();
                            }
                        });

                        break;

                    case NAV_ITEM_FACEBOOK_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                openUrlLink(CommonUtils.getFBPageUrl(NewMainActivity.this));
                            }
                        });

                        break;

                    case NAV_ITEM_MESSAGE_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                openUrlLink(CommonUtils.getFBPageUrl(NewMainActivity.this));
                            }
                        });

                        break;

                    case NAV_ITEM_RATE_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                openUrlLink(AppConstants.STORE_LINK);
                            }
                        });

                        break;

                    case NAV_ITEM_TUTORIAL_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                openTutorialActivity();
                            }
                        });

                        break;
                }
            }
        });

        mNavViewAdapter.setSelectedComicSource(getSelectedComicsSource());
    }
    private void setupNavViewRV() {
        mNavViewLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNavItemsRV.setLayoutManager(mNavViewLayoutManager);
        mNavItemsRV.setAdapter(mNavViewAdapter);
        mNavItemsRV.setHasFixedSize(true);
    }

    private void updateToolbarTitleWithSelectedCategory() {
        // mToolbarTitleTV.setText(mPresenter.getSelectedCategory().toLowerCase());
        mToolbarTitleTV.setText(getString(R.string.app_name));
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSession();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.updateNavViewUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_CATEGORY_ACTIVITY:
                    updateToolbarTitleWithSelectedCategory();
                    refreshComics(NEWEST_COMICS_PAGE_POS);
                    refreshComics(FEATURED_COMICS_PAGE_POS);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);

        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            if (intentExtras.containsKey(MyFirebaseMessagingService.EXTRA_NOTIFICATION_ACTION_TYPE)) {
                String notificationType = intentExtras.getString(MyFirebaseMessagingService.EXTRA_NOTIFICATION_ACTION_TYPE);
                switch (notificationType) {
                    case ApiHelper.NOTIFICATION_TYPES.NOT_FEATURED_COMICS:
                        switchToFeaturedPage();
                        break;
                }
            }
        }
    }

    private void initSession() {
        Branch branch = Branch.getInstance();

        // Branch init
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                    Timber.i("BRANCH SDK: " + referringParams.toString());
                    try {
                        if (referringParams != null) {
                            if (referringParams.has("comic_id")) {
                                Long comicId = Long.valueOf(referringParams.getString("comic_id"));
                                // openComicViewActivity(comicId);
                            }

                            if (referringParams.has("profile_id")) {
                                Long profileId = Long.valueOf(referringParams.getString("profile_id"));
                                // openProfileActivity(profileId);
                            }
                        }

                    }catch (JSONException ex) {
                        Timber.e(ex.getMessage().toString());
                    }

                } else {
                    Timber.i("BRANCH SDK: " + error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }


    @OnClick(R.id.ib_toolbar_main_plus)
    public void onComicAddClicked() {
        mPresenter.onUploadComicClicked();
    }


    @Override
    public void onConfirmationDialogYesClicked() {
        finish();
    }

    @Override
    public void onConfirmationDialogNoClicked() {
    }

    @Override
    public void updateNavViewUserInfo(String name, String photoUrl) {
        if (mNavUserPhotoIV != null) {
            ImageUtils.loadImageUrlIntoView(this, mNavUserPhotoIV, photoUrl,
                    true, R.drawable.im_default);
        }

        if (mNavUserNameTV != null) {
            mNavUserNameTV.setText(name == null || name.isEmpty() ? getString(R.string.default_name) : name);
        }
    }

    @Override
    public int getSelectedComicsSource() {
        switch (mViewPager.getCurrentItem()) {
            case FEATURED_COMICS_PAGE_POS:
                return ApiHelper.COMIC_SOURCES.FEATURED;

            default:
                return ApiHelper.COMIC_SOURCES.ALL;
        }
    }

    @Override
    public void onFeaturedComicsReleased() {
        refreshComics(PENDING_FEATURED_COMICS_PAGE_POS);
    }

    /*@Override
    public void onLogTimeSent() {
        requestNewComics();
    }*/

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int tabPos = tab.getPosition();
        mViewPager.setCurrentItem(tabPos);

        if (mPresenter.getIsAdmin() && tabPos == PENDING_FEATURED_COMICS_PAGE_POS) {
            mPendingFeaturedOptionsIB.setVisibility(View.VISIBLE);

        } else {
            mPendingFeaturedOptionsIB.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Timber.d("Tab unselected: " + tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Timber.d("Tab Reselected: " + tab.getPosition());
        Fragment fragment = mViewPagerAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null) {
            ((MainComicsFragment) fragment).scrollToTop();
        }
    }


    @Override
    public void onRefresh() {
        refreshComics(mViewPager.getCurrentItem());
    }

    public void stopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void addNewComics(List<ComicResponse.SingleComic> comics) {
    }

    @Override
    public void openProfilePage() {
        openProfileActivity(null);
    }

    @Override
    public void openComicUploadPage() {
        openComicUploadActivity();
    }

    @Override
    public void informUserWithUpdates() {
        NewUpdatesDialog dialog = NewUpdatesDialog.newInstance();
        dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
    }

    @Override
    public void onNotificationsConsumed(List<ServerNotification> allNotifications, int newNotificationsCount) {
        if (newNotificationsCount > 0) {

            // TODO: should also handle subs comics notifications
            mToolbarNotifIndicatorIV.setVisibility(View.VISIBLE);
            mNavViewAdapter.setNewProfileNotificationCount(newNotificationsCount);
        }

        mPresenter.sendLogTime();
    }


    @OnClick(R.id.ib_toolbar_main_pending_featured_options)
    public void onPendingFeaturedOptionsClicked() {
        final GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(mPendingFeaturedOptions);
        dialog.setHandler(
                new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                    @Override
                    public void onItemSelected(int position) {

                        if (mPendingFeaturedOptions[position].equals(getString(R.string.release))) {

                            FeaturedMessageDialog featuredMessageDialog = FeaturedMessageDialog.newInstance();
                            featuredMessageDialog.show(getSupportFragmentManager(),
                                    featuredMessageDialog.getMyDialogTag());

                        } else {
                            // Set next waiting hours
                            showMessage("Set waiting hours");
                        }
                    }
                });

        dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
    }

    private void openProfileActivity(Long profileId) {
        startActivity(ProfileActivity.getStartIntent(this,
                profileId == null? AppConstants.NULL_INDEX : profileId));
    }
    private void openComicUploadActivity() {
        startActivity(UploadOptionsActivity.getStartIntent(this));
    }
    private void openComicViewActivity(Long comicId) {
        startActivity(ComicViewActivity.getStartIntent(this, comicId, null));
    }
    private void openSearchActivity() {
        startActivity(SearchActivity.getStartIntent(this));
    }
    private void openCreditsActivity() {
        startActivity(CreditsActivity.getStartIntent(this));
    }
    private void openTutorialActivity() {
        startActivity(TutorialActivity.newInstance(this));
    }
    private void openReportedCommentsActivity() {
        startActivity(PendingCommentsForReviewActivity.getStartIntent(NewMainActivity.this));
    }
    private void openLeaderboardActivity() {
        startActivity(LeaderboardActivity.getStartIntent(NewMainActivity.this,false));
    }
    private void openCategoriesActivity() {
        startActivityForResult(CategorySelectionActivity.getStartIntent(this), REQ_CODE_CATEGORY_ACTIVITY);
        overridePendingTransition(R.anim.slide_from_up, 0);
    }

    private void refreshAllComics() {
        for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
            refreshComics(i);
        }
    }
    private void refreshComics(int pagePos) {
        if (mViewPagerAdapter.getItem(pagePos) instanceof MainComicsFragment) {
            MainComicsFragment mainComicsFragment = (MainComicsFragment) mViewPagerAdapter.getItem(pagePos);
            mainComicsFragment.resetComics();
        }
    }

    private void switchToNewestPage() {
        mViewPager.setCurrentItem(NEWEST_COMICS_PAGE_POS);
    }
    private void switchToFeaturedPage() {
        mViewPager.setCurrentItem(FEATURED_COMICS_PAGE_POS);
    }

    @Override
    public void onReleaseFeaturedDoneClicked(String message) {
        mPresenter.releaseFeatured(message);
    }


    @OnClick(R.id.layout_toolbar_categories)
    public void onToolbarTitleClicked() {
        openCategoriesActivity();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusUtils.EventTokenRefreshed event) {
        mPresenter.updateUserNotificationToken();
    };


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
