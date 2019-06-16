package com.joyapeak.sarcazon.ui.main;

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

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.fcm.MyFirebaseMessagingService;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.ui.MyPagerAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.category.CategoryMvpPresenter;
import com.joyapeak.sarcazon.ui.category.CategoryMvpView;
import com.joyapeak.sarcazon.ui.comic.SingleComicFragment;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.confirmationdialog.ConfirmationDialog;
import com.joyapeak.sarcazon.ui.credits.CreditsActivity;
import com.joyapeak.sarcazon.ui.custom.CustomViewPager;
import com.joyapeak.sarcazon.ui.featuredmessage.FeaturedMessageDialog;
import com.joyapeak.sarcazon.ui.leaderboard.LeaderboardActivity;
import com.joyapeak.sarcazon.ui.main.featuredend.FeatureEndFragment;
import com.joyapeak.sarcazon.ui.newmain.NavViewAdapter;
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

public class MainActivity extends BaseActivity implements
        MainMvpView,
        CategoryMvpView,
        ConfirmationDialog.ConfirmationDialogHandler,
        FeaturedMessageDialog.FeaturedMessageDialogHandler {

    @BindView(R.id.appbar_main)
    AppBarLayout mMainAppbar;

    @BindView(R.id.appbar_general)
    AppBarLayout mGeneralAppbar;

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    @BindView(R.id.iv_toolbar_main_notifIndicator)
    ImageView mToolbarNotifIndicatorIV;

    @BindView(R.id.tv_toolbar_main_title)
    TextView mMainToolbarTitleTV;

    @BindView(R.id.tv_toolbar_general_title)
    TextView mSecondaryToolbarTitleTV;

    @BindView(R.id.ib_toolbar_main_plus)
    ImageButton mAddComicIB;

    @BindView(R.id.ib_toolbar_main_pending_featured_options)
    ImageButton mPendingFeaturedOptionsIB;

    @BindView(R.id.ib_toolbar_general_back)
    ImageButton mNavigationBackIB;

    @BindView(R.id.viewpager_main)
    CustomViewPager mViewpager;

    @BindView(R.id.drawer_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navView_main)
    NavigationView mNavView;

    @BindView(R.id.layout_empty_items)
    View mEmptyLayout;

    @BindArray(R.array.nav_view_items)
    String[] mNavViewComicSourceItems;

    @BindArray(R.array.nav_view_items_admin)
    String[] mNavViewComicSourceItemsAdmin;

    @BindArray(R.array.pending_featured_options)
    String[] mPendingFeaturedOptions;

    SmoothActionBarDrawerToggle mDrawerToggle;

    private Switch mNavTestSwitch;
    private Switch mNavEngagementUsersSwitch;
    private ImageView mNavUserPhotoIV;
    // private ImageView mNavProfileNotifIV;
    private TextView mNavUserNameTV;
    private RecyclerView mNavItemsRV;

    private final int NAV_ITEM_FEATURED_POS = 0;
    private final int NAV_ITEM_ALL_POS = 1;
    private final int NAV_ITEM_LEADERBOARD_POS = 2;
    private final int NAV_ITEM_PROFILE_POS = 3;
    private final int NAV_ITEM_CREDITS_POS = 4;
    private final int NAV_ITEM_TUTORIAL_POS = 5;
    private final int NAV_ITEM_PENDING_FEATURED_POS = 6;
    private final int NAV_ITEM_REPORTED_COMICS_POS = 7;
    private final int NAV_ITEM_REPORTED_COMMENTS_POS = 8;
    private final int NAV_ITEM_SUBSCRIPTIONS_POS = 50;

    private int mSelectedNavDrawerPos = NAV_ITEM_FEATURED_POS;
    private final String EXTRA_SELECTED_DRAWER_POS = "EXTRA_SELECTED_DRAWER_POS";

    private Long mLastFeaturedComicId;
    private final int MAX_NEW_FEATURED_END = 25;

    boolean mIsAdmin = false;

    @Inject
    MyPagerAdapter mViewPagerAdapter;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mNavViewLayoutManager;

    @Inject
    MainMvpPresenter<MainMvpView> mMainPresenter;

    @Inject
    CategoryMvpPresenter<CategoryMvpView> mCategoriesPresenter;

    @Inject
    NavViewAdapter mNavViewAdapter;


    private Integer mSelectedComicsSource = null;
    private boolean mIsGrabbingComics = false;
    private int mPrevFocusedPagePos = 0;

    private final static int VIEW_PAGER_OFFSET_LIMIT = 2;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_SELECTED_DRAWER_POS)) {
                mSelectedNavDrawerPos = savedInstanceState.getInt(EXTRA_SELECTED_DRAWER_POS);
            }
        }

        getActivityComponent().inject(this);
        mMainPresenter.onAttach(this);
        mCategoriesPresenter.onAttach(this);

        mIsAdmin = mMainPresenter.getUserControlFlag() == ApiHelper.CONTROL_FLAG_TYPES.ADMIN;
        setUp();

        mLastFeaturedComicId = mMainPresenter.getLastFeaturedComicId();
        mMainPresenter.onViewInitialized();

        Timber.d("Activity OnCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SELECTED_DRAWER_POS, mSelectedNavDrawerPos);
    }

    private void setUp() {
        setupToolbar();
        setupViewPager();

        setupDrawer();
        setupNavView();
    }


    private void setupDrawer() {
        mDrawerToggle = new SmoothActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mToolbarNotifIndicatorIV.setVisibility(View.GONE);
                mMainPresenter.logNavigationDrawerClick();
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
                        mMainPresenter.onUploadComicClicked();
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
                        mMainPresenter.onOptionsProfileClicked();
                    }
                });
            }
        };
        mNavUserPhotoIV.setOnClickListener(onProfileClickListener);
        mNavUserNameTV.setOnClickListener(onProfileClickListener);

        // Show extra options for admin users
        if (mIsAdmin) {
            mNavTestSwitch.setVisibility(View.VISIBLE);
            if (mMainPresenter.isTestEnabled()) {
                mNavTestSwitch.setChecked(true);
            }

            mNavEngagementUsersSwitch.setVisibility(View.VISIBLE);

            mNavTestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    mMainPresenter.onTestSwitchStateChanged(isChecked);
                    mDrawerLayout.closeDrawer(mNavView);
                    mDrawerToggle.runWhenIdle(new Runnable() {
                        @Override
                        public void run() {
                            removeAllComics();
                            requestNewComics();
                        }
                    });
                }
            });

            mNavEngagementUsersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                    mMainPresenter.onEngagementUsersSwitchChanged(isChecked);
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
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        updateToolbarViews(NAV_ITEM_FEATURED_POS);
    }
    private void setupViewPager() {
        mViewpager.setAdapter(mViewPagerAdapter);
        mViewpager.setOffscreenPageLimit(VIEW_PAGER_OFFSET_LIMIT);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Timber.d("Page selected at pos: " + position);
                // Timber.d("Page selected prev at pos: " + mPrevFocusedPagePos);
                // Timber.d("page selected current pos: " + getFocusedFragmentPos());

                Object prevFragment = mViewPagerAdapter.getItem(mPrevFocusedPagePos);
                Object currentFragment = mViewPagerAdapter.getItem(position);

                if (mPrevFocusedPagePos != position) {
                    if (mPrevFocusedPagePos != AppConstants.NULL_INDEX) {
                        if (prevFragment instanceof SingleComicFragment) {

                            SingleComicFragment prevComicFragment =
                                    ((SingleComicFragment) prevFragment);

                            if (prevComicFragment != null) {
                                prevComicFragment.onFragmentFocusChanged();
                            }
                        }
                    }

                    if (currentFragment instanceof SingleComicFragment) {
                        SingleComicFragment currentComicFragment = (SingleComicFragment) currentFragment;

                        if (currentComicFragment != null) {
                            currentComicFragment.onFragmentFocusChanged();
                            Long comicId = currentComicFragment.getComicId();

                            if (comicId != null) {
                                mMainPresenter.markComicAsViewed(currentComicFragment.getComicId());
                            }
                        }
                    }

                    updateAppbarToNormal();
                    mPrevFocusedPagePos = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int currentItemPosition = getFocusedFragmentPos();
                    Timber.d("Idle state position = " + String.valueOf(currentItemPosition));

                    if (!mIsGrabbingComics) {
                        if (currentItemPosition != 0 &&
                                currentItemPosition >= mViewPagerAdapter.getCount() - 5) {
                            requestNewComics();
                        }
                    }
                }
            }
        });
    }

    private void setupNavViewAdapter() {
        if (mIsAdmin) {
            mNavViewAdapter.addItems(Arrays.asList(mNavViewComicSourceItemsAdmin));

        } else {
            mNavViewAdapter.addItems(Arrays.asList(mNavViewComicSourceItems));
        }

        mNavViewAdapter.setCallback(new NavViewAdapter.Callback() {
            @Override
            public void onItemClicked(int position) {

                mDrawerLayout.closeDrawer(mNavView);

                switch (position) {
                    case NAV_ITEM_PROFILE_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                mMainPresenter.onOptionsProfileClicked();
                            }
                        });
                        break;

                    case NAV_ITEM_FEATURED_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                switchToComicSource(ApiHelper.COMIC_SOURCES.FEATURED);
                            }
                        });

                        break;

                    case NAV_ITEM_SUBSCRIPTIONS_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                mSelectedComicsSource = ApiHelper.COMIC_SOURCES.SUBSCRIPTIONS;
                                onComicSourceChanged();
                            }
                        });

                        break;

                    case NAV_ITEM_ALL_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                switchToComicSource(ApiHelper.COMIC_SOURCES.ALL);
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

                    case NAV_ITEM_PENDING_FEATURED_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                switchToComicSource(ApiHelper.COMIC_SOURCES.PENDING_FEATURED);
                            }
                        });

                        break;

                    case NAV_ITEM_REPORTED_COMICS_POS:
                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                switchToComicSource(ApiHelper.COMIC_SOURCES.PENDING_REVIEW);
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

    private void switchToComicSource(int comicSource) {

        mSelectedComicsSource = comicSource;
        switch (mSelectedComicsSource) {
            case ApiHelper.COMIC_SOURCES.ALL:
                updateToolbarViews(NAV_ITEM_ALL_POS);
                break;

            case ApiHelper.COMIC_SOURCES.FEATURED:
                mLastFeaturedComicId = mMainPresenter.getLastFeaturedComicId();
                updateToolbarViews(NAV_ITEM_FEATURED_POS);
                break;

            case ApiHelper.COMIC_SOURCES.PENDING_FEATURED:
                updateToolbarViews(NAV_ITEM_PENDING_FEATURED_POS);
                break;

            case ApiHelper.COMIC_SOURCES.PENDING_REVIEW:
                updateToolbarViews(NAV_ITEM_REPORTED_COMICS_POS);
                break;
        }

        onComicSourceChanged();
    }
    private void onComicSourceChanged() {
        removeAllComics();
        requestNewComics();
    }

    private void setupNavViewRV() {
        mNavViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNavItemsRV.setLayoutManager(mNavViewLayoutManager);
        mNavItemsRV.setAdapter(mNavViewAdapter);
        mNavItemsRV.setHasFixedSize(true);
    }

    private void requestNewComics() {
        mIsGrabbingComics = true;
        mMainPresenter.getNewComics(getSelectedComicsSource());
    }

    private void updateToolbarViews(int selectedNavDrawerItemPos) {
        if (mIsAdmin) {
            mMainToolbarTitleTV.setText(mNavViewComicSourceItemsAdmin[selectedNavDrawerItemPos]);

            if (getSelectedComicsSource() == ApiHelper.COMIC_SOURCES.PENDING_FEATURED) {
                mPendingFeaturedOptionsIB.setVisibility(View.VISIBLE);
                mAddComicIB.setVisibility(View.GONE);

            } else {
                mPendingFeaturedOptionsIB.setVisibility(View.GONE);
                mAddComicIB.setVisibility(View.VISIBLE);
            }

        } else {
            mMainToolbarTitleTV.setText(mNavViewComicSourceItems[getSelectedComicsSource()]);
        }
    }
    public void updateAppbarToNormal() {
        mGeneralAppbar.setVisibility(View.INVISIBLE);
        mNavigationBackIB.setVisibility(View.INVISIBLE);
        mMainAppbar.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
    public void updateAppbarToCommentsPage(int commentsCount) {
        if (mMainAppbar.getVisibility() == View.VISIBLE) {
            mMainAppbar.setVisibility(View.INVISIBLE);
            mGeneralAppbar.setVisibility(View.VISIBLE);
            mNavigationBackIB.setVisibility(View.VISIBLE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        mSecondaryToolbarTitleTV.setText(getString(R.string.comments_header,
                CommonUtils.getShortNumberRepresentation(commentsCount)));
    }
    public void updateAppbarToPosterSummaryPage() {
        mMainAppbar.setVisibility(View.INVISIBLE);
        mGeneralAppbar.setVisibility(View.VISIBLE);
        mNavigationBackIB.setVisibility(View.VISIBLE);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mSecondaryToolbarTitleTV.setText("");
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
        mMainPresenter.updateNavViewUserInfo();
    }

    @Override
    public void onBackPressed() {

        if (mPrevFocusedPagePos != AppConstants.NULL_INDEX &&
                mViewPagerAdapter.getCount() > mPrevFocusedPagePos) {

            Object prevFocusedFragment = mViewPagerAdapter.getItem(mPrevFocusedPagePos);

            if (prevFocusedFragment instanceof SingleComicFragment) {
                SingleComicFragment fragment = (SingleComicFragment) mViewPagerAdapter.getItem(mPrevFocusedPagePos);
                if (fragment != null) {
                    if (fragment.collapseAnyExpandedPanels()) {
                        return;
                    }
                }
            }
        }

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        ConfirmationDialog dialog = ConfirmationDialog.newInstance(getString(R.string.exit_confirmation));
        dialog.show(getSupportFragmentManager());
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
                        switchToComicSource(ApiHelper.COMIC_SOURCES.FEATURED);
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
                                openComicViewActivity(comicId);
                            }

                            if (referringParams.has("profile_id")) {
                                Long profileId = Long.valueOf(referringParams.getString("profile_id"));
                                openProfileActivity(profileId);
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


    @Override
    public void onCategoriesRetrieved(List<CategoryResponse.SingleCategory> categories) {
        // TODO: Update nav view with categories
        requestNewComics();
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
            ImageUtils.loadImageUrlIntoView(this, mNavUserPhotoIV, photoUrl, true, R.drawable.im_default);
        }

        if (mNavUserNameTV != null) {
            mNavUserNameTV.setText(name == null || name.isEmpty() ? getString(R.string.default_name) : name);
        }
    }

    @Override
    public int getSelectedComicsSource() {
        if (mSelectedComicsSource == null) {
            if (mMainPresenter.getNewFeaturedComicsCount() == 0) {
                mSelectedComicsSource = ApiHelper.COMIC_SOURCES.ALL;

            } else {
                mSelectedComicsSource = ApiHelper.COMIC_SOURCES.DEFAULT;
            }
        }

        return mSelectedComicsSource;
    }

    @Override
    public void onFeaturedComicsReleased() {
        removeAllComics();
        requestNewComics();
    }

    private void updateEmptyLayoutVisibility() {
        if (mViewPagerAdapter.getCount() == 0) {
            mEmptyLayout.setVisibility(View.VISIBLE);

        } else {
            mEmptyLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void addNewComics(List<ComicResponse.SingleComic> comics) {

        if (mSelectedComicsSource == ApiHelper.COMIC_SOURCES.FEATURED && mViewPagerAdapter.getCount() == 0) {
            mMainPresenter.updateLastFeaturedComicId(comics.get(0).getComicInfo().getComicId());
        }

        for (ComicResponse.SingleComic comic : comics) {

            // Showing Featured End page
            if (mSelectedComicsSource == ApiHelper.COMIC_SOURCES.FEATURED &&
                    mLastFeaturedComicId != null &&
                    mViewPagerAdapter.getCount() != 0 &&
                    (mLastFeaturedComicId.equals(comic.getComicInfo().getComicId()) ||
                    mViewPagerAdapter.getCount() == MAX_NEW_FEATURED_END)) {

                mViewPagerAdapter.addFragment(FeatureEndFragment.newInstance(mViewPagerAdapter.getCount()),
                        String.valueOf(mViewPagerAdapter.getCount()));

                mLastFeaturedComicId = null;
            }

            mViewPagerAdapter.addFragment(SingleComicFragment.newInstance(comic, mViewPagerAdapter.getCount()),
                    String.valueOf(mViewPagerAdapter.getCount()));
        }

        mViewPagerAdapter.notifyDataSetChanged();
        mIsGrabbingComics = false;

        updateEmptyLayoutVisibility();
    }

    private void removeAllComics() {
        int currentFocusedFragPos = getFocusedFragmentPos();
        mViewpager.setCurrentItem(0);
        mViewpager.removeAllViews();

        mViewPagerAdapter.clearAll(mViewpager, currentFocusedFragPos, VIEW_PAGER_OFFSET_LIMIT);

        mPrevFocusedPagePos = 0;
        mMainPresenter.onComicsSourceChanged();
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

        mMainPresenter.sendLogTime();
    }


    public int getFocusedFragmentPos() {
        return mViewpager.getCurrentItem();
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
        startActivity(PendingCommentsForReviewActivity.getStartIntent(MainActivity.this));
    }
    private void openLeaderboardActivity() {
        startActivity(LeaderboardActivity.getStartIntent(MainActivity.this, false));
    }


    public void addNewReportedComic(long comicId) {
        mMainPresenter.addReportedComic(comicId);

        /*int currentItemPosition = mViewpager.getCurrentItem();
        int newItemPosition = currentItemPosition == mViewPagerAdapter.getCount()?
                currentItemPosition - 1 : currentItemPosition + 1;

        mViewpager.setCurrentItem(newItemPosition, true);*/
    }

    public void switchToNewestComics() {
        mSelectedComicsSource = ApiHelper.COMIC_SOURCES.ALL;
        onComicSourceChanged();
        updateToolbarViews(NAV_ITEM_ALL_POS);
    }

    @OnClick(R.id.tv_toolbar_main_title)
    public void onMainToolbarTitleClicked() {
        onComicSourceChanged();
    }

    @OnClick(R.id.ib_toolbar_main_plus)
    public void onComicAddClicked() {
        mMainPresenter.onUploadComicClicked();
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

    @Override
    public void onReleaseFeaturedDoneClicked(String message) {
        mMainPresenter.releaseFeatured(message);
    }


    @OnClick(R.id.ib_toolbar_general_back)
    public void onNavigationBackClicked() {
        if (mViewPagerAdapter.getItem(getFocusedFragmentPos()) instanceof SingleComicFragment) {
            SingleComicFragment comicFragment = (SingleComicFragment) mViewPagerAdapter.getItem(getFocusedFragmentPos());
            if (comicFragment != null) {
                comicFragment.collapseAnyExpandedPanels();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusUtils.EventTokenRefreshed event) {
        mMainPresenter.updateUserNotificationToken();
    };


    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                           Toolbar toolbar, int openDrawerContentDescRes,
                                           int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

    @Override
    protected void onDestroy() {
        mMainPresenter.onDetach();
        mCategoriesPresenter.onDetach();
        super.onDestroy();
    }
}
