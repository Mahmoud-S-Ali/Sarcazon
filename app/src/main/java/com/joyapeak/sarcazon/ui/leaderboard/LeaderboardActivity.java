package com.joyapeak.sarcazon.ui.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.ui.MyPagerAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.leaderboard.leaderboard.LeaderboardFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class LeaderboardActivity extends BaseActivity {

    @BindView(R.id.appbar_leaderboard)
    AppBarLayout mAppbar;

    @BindView(R.id.viewpager_leaderboard)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout_leaderboard)
    TabLayout mTabLayout;

    @Inject
    MyPagerAdapter mViewPagerAdapter;

    private boolean mForPrevWeek = false;
    private final static String EXTRAS_FOR_PREV_WEEK = "EXTRAS_FOR_PREV_WEEK";


    public static Intent getStartIntent(Context context, boolean forPrevWeek) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRAS_FOR_PREV_WEEK, forPrevWeek);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        if (getIntent().getExtras() != null) {
            mForPrevWeek = getIntent().getExtras().getBoolean(EXTRAS_FOR_PREV_WEEK, false);
        }

        setUp();
    }

    private void setUp() {
        setupGeneralToolbar(mAppbar, getString(R.string.leaderboard));
        setupViewPager();
        setupTabLayout();
    }
    private void setupViewPager() {
        if (mForPrevWeek) {
            mViewPagerAdapter.addFragment(LeaderboardFragment.newInstance(ApiHelper.LEADERBOARD_SOURCES.PREV_WEEK),
                    getString(R.string.leaderboard_tab_weekly));

        } else {
            mViewPagerAdapter.addFragment(LeaderboardFragment.newInstance(ApiHelper.LEADERBOARD_SOURCES.WEEKLY),
                    getString(R.string.leaderboard_tab_weekly));

            mViewPagerAdapter.addFragment(LeaderboardFragment.newInstance(ApiHelper.LEADERBOARD_SOURCES.ALL_TIME),
                    getString(R.string.leaderboard_tab_all_time));
        }

        mViewPager.setAdapter(mViewPagerAdapter);
        // mViewPager.setOffscreenPageLimit(VIEW_PAGER_OFFSET_LIMIT);
    }
    private void setupTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.leaderboard_tab_weekly)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.leaderboard_tab_all_time)));

        mTabLayout.setupWithViewPager(mViewPager);

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
}
