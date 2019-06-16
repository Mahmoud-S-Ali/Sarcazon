package com.joyapeak.sarcazon.ui.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.ui.replies.RepliesActivity;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsActivity extends BaseActivity implements CommentsMvpView {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.tv_toolbar_general_title)
    TextView mToolbarTitleTV;

    @BindView(R.id.ib_toolbar_general_back)
    ImageButton mToolbarBackIB;

    @BindView(R.id.main_bottom_panel_comments)
    SlidingUpPanelLayout mCommentsPanelLayout;

    @BindView(R.id.rv_comments)
    RecyclerView mCommentsRV;

    @BindView(R.id.layout_empty_items)
    View mEmptyCommentsView;

    @BindView(R.id.et_comments_newComment)
    EditText mNewCommentET;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    @Inject
    GeneralCommentsAdapter mCommentsAdapter;

    @Inject
    CommentsMvpPresenter<CommentsMvpView> mPresenter;

    private Long mComicId;
    private final static String EXTRA_COMIC_ID = "EXTRA_COMIC_ID";

    private boolean mCommentsPanelWasCollapsed = true;


    public static Intent getStartIntent(Context context, long comicId) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(EXTRA_COMIC_ID, comicId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        mPresenter.onAttach(this);

        mComicId = getIntent().getLongExtra(EXTRA_COMIC_ID, -1L);

        setUp();
    }

    private void setUp() {
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupAppbar();

        setupCommentsAdapter();
        setupCommentsRV();

        setupCommentsPanelLayout();
    }

    private void setupAppbar() {
        mToolbarBackIB.setVisibility(View.VISIBLE);
        mToolbarTitleTV.setText(getString(R.string.comments_header,
                CommonUtils.getShortNumberRepresentation(0)));

        setupGeneralToolbar(mAppbar, getString(R.string.comments_header));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    private void setupCommentsAdapter() {
        if (mCommentsAdapter != null) {
            mCommentsAdapter.setCallback(new GeneralCommentsAdapter.Callback() {
                @Override
                public void onProfileClicked(long userId) {
                    openProfileActivity(userId);
                }

                @Override
                public void onLikeClicked(long commentId, boolean isPositiveAction) {
                    mPresenter.likeComment(commentId, true, isPositiveAction);
                }

                @Override
                public void onDislikedClicked(long commentId, boolean isPositiveAction) {
                    mPresenter.likeComment(commentId, false, isPositiveAction);
                }

                @Override
                public void onCommentOptionsClicked(final int commentPosition,
                                                    final long commentId, boolean isOwner) {

                    // Comment options changes according to user credentials: owner, admin, normal
                    final String[] commentOptions;

                    if (mPresenter.getIsAdmin()) {
                        commentOptions = getResources().getStringArray(R.array.options_admin);

                    } else if (isOwner) {
                        commentOptions = getResources().getStringArray(R.array.comment_options_owner);

                    } else {
                        commentOptions = getResources().getStringArray(R.array.comment_options_general);
                    }

                    GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(commentOptions);
                    dialog.setHandler(
                            new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                                @Override
                                public void onItemSelected(int position) {
                                    switch (commentOptions[position]) {
                                        case AppConstants.OPTION_REPORT:
                                            mPresenter.reportComment(commentId, commentPosition);
                                            break;

                                        case AppConstants.OPTION_DELETE:
                                            mPresenter.deleteComment(commentId, commentPosition);
                                            break;

                                        case AppConstants.OPTION_BLOCK:
                                            mPresenter.blockComment(commentId, commentPosition);
                                            break;
                                    }
                                }
                            });

                    dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
                }

                @Override
                public void onReplyClicked(CommentResponse.ComicComment commentItem) {
                    openRepliesActivity(commentItem);
                }
            });

            mCommentsAdapter.setEmptyView(mEmptyCommentsView);
            mCommentsAdapter.setViewerId(mPresenter.getCurrentUserId());
        }
    }
    private void setupCommentsRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mCommentsRV.setLayoutManager(mLayoutManager);
        mCommentsRV.setAdapter(mCommentsAdapter);

        mCommentsAdapter.addNewItemsOnScrollListener(mCommentsRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {

                    @Override
                    public void onScrollReachedEnd() {
                        mPresenter.getNewComments(mComicId);
                    }
                });
    }

    private void setupCommentsPanelLayout() {
        mCommentsPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // Timber.d("Comments slide offset = " + String.valueOf(slideOffset));
            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                // Timber.d("Comments state changed from : " + previousState + " ...to : " + newState);

                if (mCommentsPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mCommentsPanelWasCollapsed = false;

                    mPresenter.getNewComments(mComicId);

                } else if (!mCommentsPanelWasCollapsed && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
        mCommentsPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommentsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        mCommentsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }


    private void openProfileActivity(long userId) {
        startActivity(ProfileActivity.getStartIntent(this, userId));
    }
    private void openRepliesActivity(CommentResponse.ComicComment comment) {
        startActivity(RepliesActivity.getStartIntent(this, comment));
    }

    @Override
    public void onBackPressed() {
        onToolbarBackClicked();
    }

    @OnClick(R.id.ib_toolbar_general_back)
    public void onToolbarBackClicked() {
        mCommentsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @OnClick(R.id.ib_comments_commentAdd)
    public void onAddCommentClicked() {
        String comment = mNewCommentET.getText().toString();
        if (comment.isEmpty()) {
            mNewCommentET.setError(getString(R.string.comment_error_empty));
            return;
        }

        mPresenter.addComment(mComicId, comment);
        hideKeyboard();
    }


    @Override
    public void addComments(List<CommentResponse.ComicComment> comments) {
        if (mCommentsPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mCommentsAdapter.resetIsLoadingNewItems();
            mCommentsAdapter.addComments(comments);
        }
    }

    @Override
    public void updateCommentsCount(int count) {
        // mComicData.getComicInfo().setCommentsCount(count);
        // updateCommentsCountView(count);
        mToolbarTitleTV.setText(getString(R.string.comments_header,
                CommonUtils.getShortNumberRepresentation(count)));

        mCommentsAdapter.setTotalCommentsCount(count);
    }

    @Override
    public void updateAfterCommentAdded() {
        mCommentsAdapter.clearAllComments();
        mPresenter.getNewComments(mComicId);

        mNewCommentET.setText("");
        mNewCommentET.clearFocus();
    }

    @Override
    public void removeComment(int adapterPosition) {
        mCommentsAdapter.removeComment(adapterPosition);
    }
}
