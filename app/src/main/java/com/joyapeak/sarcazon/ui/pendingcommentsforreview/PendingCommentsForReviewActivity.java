package com.joyapeak.sarcazon.ui.pendingcommentsforreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.comments.ReportedCommentsAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PendingCommentsForReviewActivity extends BaseActivity implements PendingCommentsForReviewMvpView {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.rv_reported_comments)
    RecyclerView mReportedCommentsRV;

    @BindView(R.id.layout_empty_items)
    View mEmptyLayout;

    @BindArray(R.array.user_review_actions)
    String[] mUserActions;

    @Named("scrollable_layoutmanager")
    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    ReportedCommentsAdapter mReportedCommentsAdapter;

    @Inject
    PendingCommentsForReviewMvpPresenter<PendingCommentsForReviewMvpView> mPresenter;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, PendingCommentsForReviewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_comments);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        setUp();
        mPresenter.getPendingCommentsForReviews();
    }

    private void setUp() {
        setupGeneralToolbar(mAppbar, getString(R.string.reported_comments));
        setupReportedCommentsAdapter();
        setupReportedCommentsRV();
    }
    private void setupReportedCommentsAdapter() {
        mReportedCommentsAdapter.setCallback(new ReportedCommentsAdapter.Callback() {
            @Override
            public void onAllowClicked(long commentId, int adapterPosition) {
                mPresenter.postMarkReviewed(commentId, ApiHelper.CONTENT_REVIEW_ACTIONS.ALLOW,
                        null, adapterPosition);
            }

            @Override
            public void onBlockClicked(final long commentId, final int adapterPosition) {
                GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(mUserActions);
                dialog.setHandler(
                        new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                            @Override
                            public void onItemSelected(int position) {
                                String selectedAction = mUserActions[position];
                                int userReviewAction;

                                if (selectedAction.equals(getString(R.string.nothing))) {
                                    userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.NOTHING;

                                } else if (selectedAction.equals(getString(R.string.warn))) {
                                    userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.WARNING;

                                } else if (selectedAction.equals(getString(R.string.block_week))) {
                                    userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.BLOCK_WEEK;

                                } else if (selectedAction.equals(getString(R.string.block_month))) {
                                    userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.BLOCK_MONTH;

                                } else {
                                    userReviewAction = ApiHelper.USER_REVIEW_ACTIONS.BLOCK;
                                }

                                mPresenter.postMarkReviewed(commentId, ApiHelper.CONTENT_REVIEW_ACTIONS.BLOCK,
                                        userReviewAction, adapterPosition);
                            }
                        });

                dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
            }
        });

        mReportedCommentsAdapter.setEmptyView(mEmptyLayout);
    }
    private void setupReportedCommentsRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mReportedCommentsRV.setLayoutManager(mLayoutManager);
        mReportedCommentsRV.setAdapter(mReportedCommentsAdapter);
    }

    @Override
    public void updateTotalCommentsCount(int count) {
        mReportedCommentsAdapter.setTotalCommentsCount(count);
    }

    @Override
    public void addComments(List<CommentResponse.ComicComment> comments) {
        mReportedCommentsAdapter.addComments(comments);
    }

    @Override
    public void updateAfterMarkReviewed(int adapterPosition) {
        mReportedCommentsAdapter.removeComment(adapterPosition);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
