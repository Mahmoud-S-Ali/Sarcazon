package com.joyapeak.sarcazon.ui.comicview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.comic.SingleComicFragment;
import com.joyapeak.sarcazon.ui.newmain.NewMainActivity;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.utils.AppConstants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* Full comic view page
* */

public class ComicViewActivity extends BaseActivity implements ComicViewMvpView {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.tv_toolbar_general_title)
    TextView mToolbarTitleTV;

    @Inject
    ComicViewMvpPresenter<ComicViewMvpView> mPresenter;

    private Long mComicId;
    private final static String EXTRA_COMIC_ID = "EXTRA_COMIC_ID";

    // Used to open comments page if not null
    private Long mCommentId;
    private final static String EXTRA_COMMENT_ID = "EXTRA_COMMENT_ID";

    public static Intent getStartIntent(Context context, Long comicId, Long commentId) {
        Intent intent = new Intent(context, ComicViewActivity.class);
        intent.putExtra(EXTRA_COMIC_ID, comicId);
        if (commentId != null) {
            intent.putExtra(EXTRA_COMMENT_ID, commentId);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_view);
        setUnBinder(ButterKnife.bind(this));

        mParentActivityName = getIntent().getStringExtra(EXTRA_PARENT_ACTIVITY);

        mComicId = getIntent().getLongExtra(EXTRA_COMIC_ID, -1);
        mCommentId = getIntent().getLongExtra(EXTRA_COMMENT_ID, -1);

        getActivityComponent().inject(this);

        mPresenter.onAttach(this);

        if (savedInstanceState == null) {
            mPresenter.getComicWithId(mComicId);
        }

        setupGeneralToolbar(mAppbar, getString(R.string.comic_view_header));
    }

    @Override
    public void setupComicFragment(ComicResponse.SingleComic comic) {
        if (comic == null) {
            showMessage("Oops, couldn't open this comic");
            finish();
            return;
        }

        SingleComicFragment comicFragment = SingleComicFragment.newInstance(comic, 0);

        getSupportFragmentManager().beginTransaction().add(R.id.comicView_container,
                comicFragment, SingleComicFragment.TAG).commitAllowingStateLoss();

        getSupportFragmentManager().executePendingTransactions();

        if (mCommentId != null && mCommentId != AppConstants.NULL_INDEX) {
            comicFragment.expandCommentsPanel();
        }
    }


    public void updateAppbarToNormal() {
        mToolbarTitleTV.setText(getString(R.string.comic_view_header));
    }

    public void updateAppbarToCommentsPage(int count) {
        mToolbarTitleTV.setText(getString(R.string.comments_header, String.valueOf(count)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SingleComicFragment comicFragment =
                        (SingleComicFragment) getSupportFragmentManager().findFragmentByTag(SingleComicFragment.TAG);

                if (comicFragment != null && comicFragment.collapseAnyExpandedPanels()) {
                    return true;
                }
        }
        return false;
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
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

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
