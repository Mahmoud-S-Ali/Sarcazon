package com.joyapeak.sarcazon.ui.replies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.GeneralChoicesDialog;
import com.joyapeak.sarcazon.ui.comments.GeneralCommentsAdapter;
import com.joyapeak.sarcazon.ui.newmain.NewMainActivity;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepliesActivity extends BaseActivity implements RepliesMvpView {

    @BindView(R.id.appbar_general)
    AppBarLayout mAppbar;

    @BindView(R.id.tv_toolbar_general_title)
    TextView mToolbarTitleTV;

    @BindView(R.id.iv_commentItem_commenterPhoto)
    ImageView mMainCommenterPhotoIV;

    @BindView(R.id.tv_commentItem_commenterName)
    TextView mMainCommenterNameTV;

    @BindView(R.id.tv_commentItem_time)
    TextView mMainCommenterTimeTV;

    @BindView(R.id.tv_commentItem_body)
    TextView mMainCommentBodyTV;

    @BindView(R.id.ib_commentItem_commentOptions)
    ImageButton mMainCommentOptionsIB;

    @BindView(R.id.ib_commentItem_likeIcon)
    ImageButton mMainCommentLikeIB;

    @BindView(R.id.tv_commentItem_likesCount)
    TextView mMainCommentLikeTV;

    @BindView(R.id.ib_commentItem_dislikeIcon)
    ImageButton mMainCommentDislikeIB;

    @BindView(R.id.rv_replies)
    RecyclerView mRepliesRV;

    @BindView(R.id.layout_empty_items)
    View mEmptyView;

    @BindView(R.id.et_comments_newComment)
    EditText mNewReplyET;


    @Inject
    RepliesMvpPresenter<RepliesMvpView> mPresenter;

    @Inject
    GeneralCommentsAdapter mRepliesAdapter;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    private ComicComment mMainComment;
    private final static String EXTRA_MAIN_COMMENT = "EXTRA_MAIN_COMMENT";

    private final static String EXTRA_COMMENT_ID = "EXTRA_COMMENT_ID";


    public static Intent getStartIntent(Context context, ComicComment mainComment) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MAIN_COMMENT, mainComment);
        return getStartIntent(context, bundle);
    }
    public static Intent getStartIntent(Context context, Long commentId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_COMMENT_ID, commentId);
        return getStartIntent(context, bundle);
    }
    private static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, RepliesActivity.class);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);
        setUnBinder(ButterKnife.bind(this));

        mParentActivityName = getIntent().getStringExtra(EXTRA_PARENT_ACTIVITY);

        getActivityComponent().inject(this);
        mPresenter.onAttach(this);

        setUp();
    }

    private void setUp() {
        setupGeneralToolbar(mAppbar, "");
        setupRepliesAdapter();
        setupRepliesRV();

        mMainComment = getIntent().getExtras().getParcelable(EXTRA_MAIN_COMMENT);
        mNewReplyET.setHint(R.string.reply_hint);

        if (mMainComment != null) {
            updateMainCommentViews();
            getReplies();

        } else {
            Long commentId = getIntent().getExtras().getLong(EXTRA_COMMENT_ID);
            if (commentId != null) {
                mPresenter.getComment(commentId);
            }
        }
    }

    private void updateMainCommentViews() {
        mMainCommentLikeIB.setVisibility(View.GONE);
        mMainCommentDislikeIB.setVisibility(View.GONE);
        mMainCommentLikeTV.setVisibility(View.GONE);
        mMainCommentOptionsIB.setVisibility(View.GONE);

        mMainCommenterNameTV.setText(mMainComment.getCommenterName());
        mMainCommenterTimeTV.setText(CommonUtils.getFriendlyShortDateString(mMainComment.getCommentDate()));
        mMainCommentBodyTV.setText(mMainComment.getComment());

        ImageUtils.loadImageUrlIntoView(getApplicationContext(),
                mMainCommenterPhotoIV,
                mMainComment.getCommenterPhotoUrl(),
                true,
                R.drawable.im_default);
    }
    private void setupRepliesAdapter() {
        if (mRepliesAdapter != null) {
            mRepliesAdapter.setCallback(new GeneralCommentsAdapter.Callback() {
                @Override
                public void onProfileClicked(long userId) {
                    startActivity(ProfileActivity.getStartIntent(RepliesActivity.this, userId));
                }

                @Override
                public void onLikeClicked(long commentId, boolean isPositiveAction) {
                    mPresenter.likeReply(commentId, true, isPositiveAction);
                }

                @Override
                public void onDislikedClicked(long commentId, boolean isPositiveAction) {
                    mPresenter.likeReply(commentId, false, isPositiveAction);
                }

                @Override
                public void onCommentOptionsClicked(final int replyPosition, final long commentId,
                                                    boolean isOwner) {

                    final String[] replyOptions;
                    boolean isAdmin = mPresenter.getUserControlFlag() == ApiHelper.CONTROL_FLAG_TYPES.ADMIN;

                    if (isAdmin) {
                        replyOptions = getResources().getStringArray(R.array.options_admin);

                    } else if (isOwner) {
                        replyOptions = getResources().getStringArray(R.array.comment_options_owner);

                    } else {
                        replyOptions = getResources().getStringArray(R.array.comment_options_general);
                    }

                    GeneralChoicesDialog dialog = GeneralChoicesDialog.newInstance(replyOptions);
                    dialog.setHandler(
                            new GeneralChoicesDialog.GeneralChoicesDialogHandler() {

                                @Override
                                public void onItemSelected(int position) {
                                    switch (replyOptions[position]) {
                                        case AppConstants.OPTION_REPORT:
                                            mPresenter.reportReply(commentId, replyPosition);
                                            break;

                                        case AppConstants.OPTION_DELETE:
                                            mPresenter.deleteReply(commentId, replyPosition);
                                            break;

                                        case AppConstants.OPTION_BLOCK:
                                            mPresenter.blockReply(commentId, replyPosition);
                                            break;
                                    }
                                }
                            });

                    dialog.show(getSupportFragmentManager(), dialog.getMyDialogTag());
                }

                @Override
                public void onReplyClicked(ComicComment commentItem) {
                    showMessage("Reply clicked");
                }
            });

            mRepliesAdapter.setViewerId(mPresenter.getViewerId());
            mRepliesAdapter.setEmptyView(mEmptyView);
        }
    }
    private void setupRepliesRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRepliesRV.setLayoutManager(mLayoutManager);
        mRepliesRV.setAdapter(mRepliesAdapter);

        mRepliesAdapter.addNewItemsOnScrollListener(mRepliesRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {
            @Override
            public void onScrollReachedEnd() {
                getReplies();
            }
        });
    }

    private void getReplies() {
        mPresenter.getReplies(mMainComment.getCommentId());
    }


    @OnClick(R.id.ib_comments_commentAdd)
    public void onAddReplyClicked() {
        String reply = mNewReplyET.getText().toString();
        if (reply.isEmpty()) {
            mNewReplyET.setError(getString(R.string.reply_error_empty));
            return;
        }

        mPresenter.addReply(mMainComment.getCommentId(), reply);
        hideKeyboard();
    }


    @Override
    public void onCommentDataRetrieved(ComicComment comment) {
        mMainComment = comment;
        updateToolbarTitleWithRepliesCount();
        updateMainCommentViews();
        getReplies();
    }

    @Override
    public void removeReply(int replyPosition) {
        mRepliesAdapter.removeComment(replyPosition);
    }

    @Override
    public void addReplies(List<ComicComment> replies) {
        if (mRepliesAdapter.getItemCount() == 0) {
            mNewReplyET.requestFocus();
        }

        mRepliesAdapter.resetIsLoadingNewItems();
        mRepliesAdapter.addComments(replies);

        updateToolbarTitleWithRepliesCount();
    }

    private void updateToolbarTitleWithRepliesCount() {
        mToolbarTitleTV.setText(getString(R.string.replies_header, String.valueOf(mMainComment.getRepliesCount())));
    }

    @Override
    public void updateAfterReplyAdded() {
        mRepliesAdapter.clearAllComments();
        getReplies();

        mNewReplyET.setText("");
        mNewReplyET.clearFocus();

        mMainComment.setRepliesCount(mMainComment.getRepliesCount() + 1);
        updateToolbarTitleWithRepliesCount();
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
