package com.joyapeak.sarcazon.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.MyPagerAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.search.searchcomics.SearchComicsFragment;
import com.joyapeak.sarcazon.ui.search.searchusers.SearchUsersFragment;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SearchActivity extends BaseActivity implements SearchMvpView {

    @BindView(R.id.et_search)
    EditText mSearchET;

    @BindView(R.id.viewpager_search)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout_search)
    TabLayout mTabLayout;

    @Inject
    SearchMvpPresenter<SearchMvpView> mPresenter;

    @Inject
    MyPagerAdapter mPagerAdapter;

    private TAB_POSITIONS mSelectedTabPos;
    private enum TAB_POSITIONS {
        COMICS,
        USERS
    };


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        setUp();

        mPresenter.onAttach(this);
    }

    private void setUp() {
        initSearchView();
        setupViewPager();
        setupTabLayout();
    }

    private void initSearchView() {

        // TODO: Implement swtichmap
        RxSearchObservable.fromView(mSearchET)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String query) throws Exception {
                        if (query.isEmpty())
                            return false;

                        return true;
                    }
                })
                .distinctUntilChanged()
                /*.switchMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String query) throws Exception {
                        return processSearch(query);
                    }
                })*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSearchObserver());
    }
    private void setupViewPager() {
        mPagerAdapter.addFragment(SearchComicsFragment.newInstance(), "");
        mPagerAdapter.addFragment(SearchUsersFragment.newInstance(), "");

        mViewPager.setAdapter(mPagerAdapter);
        // mViewPager.setOffscreenPageLimit(MyPagerAdapter.VIEW_PAGER_OFFSET_LIMIT);
    }
    private void setupTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.search_tab_comics)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.search_tab_users)));

        // mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPos = tab.getPosition();
                mSelectedTabPos = tabPos == 0? TAB_POSITIONS.COMICS : TAB_POSITIONS.USERS;
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

        mSelectedTabPos = TAB_POSITIONS.COMICS;
    }

    private Observer<String> getSearchObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                processSearch(s);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                hideKeyboard();
                mSearchET.clearFocus();
            }
        };
    }
    private void processSearch(String query) {
        if (mSelectedTabPos == TAB_POSITIONS.COMICS) {
            SearchComicsFragment fragment =
                    (SearchComicsFragment) mPagerAdapter.getItem(mSelectedTabPos.ordinal());

            fragment.processSearch(query);

        } else {
            showMessage("Users fragment");
        }
    }
}
