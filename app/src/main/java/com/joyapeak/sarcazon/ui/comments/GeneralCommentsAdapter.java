package com.joyapeak.sarcazon.ui.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mahmoud Ali on 5/1/2018.
 */

public class GeneralCommentsAdapter extends BaseCommentsAdapter {

    private Callback mCallback;

    public interface Callback {
        void onProfileClicked(long userId);
        void onLikeClicked(long commentId, boolean isPositiveAction);
        void onDislikedClicked(long commentId, boolean isPositiveAction);
        void onCommentOptionsClicked(int position, long commentId, boolean isOwner);
        void onReplyClicked(ComicComment commentItem);
    }

    public GeneralCommentsAdapter(List<ComicComment> commentsList) {
        super(commentsList);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    public class ViewHolder extends BaseCommentsAdapter.ViewHolder {

        @BindView(R.id.tv_commentItem_likesCount)
        TextView mLikesCountTV;

        @BindView(R.id.tv_commentItem_dislikesCount)
        TextView mDislikesCountTV;

        @BindView(R.id.tv_commentItem_reply)
        TextView mReplyTV;

        @BindView(R.id.tv_commentItem_repliesCount)
        TextView mRepliesCountTV;

        @BindView(R.id.ib_commentItem_repliesIcon)
        ImageButton mRepliesIconIB;

        @BindView(R.id.ib_commentItem_likeIcon)
        ImageButton mLikeIconIB;

        @BindView(R.id.ib_commentItem_dislikeIcon)
        ImageButton mDislikeIconIB;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            mCommenterPhotoIV.setImageDrawable(null);
            mCommentBodyTV.setText("");
            mCommenterNameTV.setText("");
            mCommentTimeTV.setText("");
            mRepliesCountTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            final ComicComment commentItem = mCommentsList.get(position);

            ImageUtils.loadImageUrlIntoView(itemView.getContext(),
                    mCommenterPhotoIV,
                    commentItem.getCommenterThumbnailUrl(),
                    true,
                    R.drawable.im_default);

            mCommentBodyTV.setText(commentItem.getComment());
            mCommenterNameTV.setText(commentItem.getCommenterName());
            mCommentTimeTV.setText(CommonUtils.getFriendlyShortDateString(commentItem.getCommentDate()));
            mRepliesCountTV.setText(CommonUtils.getShortNumberRepresentation(commentItem.getRepliesCount()));

            updateLikeViews(ViewHolder.this, commentItem);
            updateDislikeViews(ViewHolder.this, commentItem);

            boolean isReply = commentItem.getRepliesCount() == null;
            if (isReply || commentItem.getRepliesCount() == null ||
                    commentItem.getRepliesCount() == 0) {
                setRepliesVisibility(false, !isReply);

            } else {
                setRepliesVisibility(true, !isReply);
            }

            if (getIsOwner(commentItem.getCommenterId())) {
                mCommenterNameTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent2));

            } else {
                mCommenterNameTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white_op50));
            }
        }

        private boolean getIsOwner(long commenterId) {
            return commenterId == mViewerId;
        }

        private void setRepliesVisibility(boolean visible, boolean showReplyText) {
            if (visible) {
                mRepliesCountTV.setVisibility(View.VISIBLE);
                mRepliesIconIB.setVisibility(View.VISIBLE);

                if (showReplyText) {
                    mReplyTV.setVisibility(View.GONE);
                }

            } else {
                mRepliesCountTV.setVisibility(View.GONE);
                mRepliesIconIB.setVisibility(View.GONE);

                if (showReplyText) {
                    mReplyTV.setVisibility(View.VISIBLE);
                }
            }
        }

        private void updateLikeViews(ViewHolder holder, ComicComment comment) {
            if (comment.getIsLiked()) {
                holder.mLikeIconIB.setImageResource(R.drawable.ic_like_active);
                /*holder.mLikesCountTV.setTextColor(ContextCompat.getColor(itemView.getContext(),
                        R.color.colorAccent));*/
            } else {
                holder.mLikeIconIB.setImageResource(R.drawable.ic_like_white_50);
                /*holder.mLikesCountTV.setTextColor(ContextCompat.getColor(itemView.getContext(),
                        R.color.white_op50));*/
            }

            holder.mLikesCountTV.setText(
                    CommonUtils.getShortNumberRepresentation(
                            comment.getLikesCount() - comment.getDislikesCount()));
        }
        private void updateDislikeViews(ViewHolder holder, ComicComment comment) {
            if (comment.getIsDisliked()) {
                holder.mDislikeIconIB.setImageResource(R.drawable.ic_dislike_active);
                /*holder.mLikesCountTV.setTextColor(ContextCompat.getColor(itemView.getContext(),
                        R.color.red));*/
            } else {
                holder.mDislikeIconIB.setImageResource(R.drawable.ic_dislike_white_50);
                /*holder.mLikesCountTV.setTextColor(ContextCompat.getColor(itemView.getContext(),
                        R.color.white_op50));*/
            }

            // holder.mDislikesCountTV.setText(String.valueOf(comment.getDislikesCount()));
        }


        @OnClick({R.id.iv_commentItem_commenterPhoto, R.id.tv_commentItem_commenterName})
        public void onProfileClicked() {
            ComicComment commentItem = mCommentsList.get(getAdapterPosition());
            mCallback.onProfileClicked(commentItem.getCommenterId());
        }

        @OnClick(R.id.ib_commentItem_commentOptions)
        public void onCommentOptionsClicked() {
            int position = getAdapterPosition();
            ComicComment commentItem = mCommentsList.get(position);

            mCallback.onCommentOptionsClicked(position, commentItem.getCommentId(),
                    getIsOwner(commentItem.getCommenterId()));
        }

        @OnClick(R.id.ib_commentItem_likeIcon)
        public void onLikeClicked() {
            ComicComment commentItem = mCommentsList.get(getAdapterPosition());

            commentItem.setIsLiked(!commentItem.getIsLiked());
            int currentLikesCount = commentItem.getLikesCount();
            commentItem.setLikesCount(commentItem.getIsLiked()?
                    currentLikesCount + 1 : currentLikesCount - 1);

            if (commentItem.getIsLiked() && commentItem.getIsDisliked()) {
                commentItem.setIsDisliked(false);
                commentItem.setDislikesCount(commentItem.getDislikesCount() - 1);
                updateDislikeViews(ViewHolder.this, commentItem);
            }

            updateLikeViews(ViewHolder.this, commentItem);
            mCallback.onLikeClicked(commentItem.getCommentId(), commentItem.getIsLiked());
        }

        @OnClick(R.id.ib_commentItem_dislikeIcon)
        public void onDislikeClicked() {
            ComicComment commentItem = mCommentsList.get(getAdapterPosition());

            commentItem.setIsDisliked(!commentItem.getIsDisliked());
            int currentDislikesCount = commentItem.getDislikesCount();
            commentItem.setDislikesCount(commentItem.getIsDisliked()?
                    currentDislikesCount + 1 : currentDislikesCount - 1);

            if (commentItem.getIsDisliked() && commentItem.getIsLiked()) {
                commentItem.setIsLiked(false);
                commentItem.setLikesCount(commentItem.getLikesCount() - 1);
            }

            updateLikeViews(ViewHolder.this, commentItem);
            updateDislikeViews(ViewHolder.this, commentItem);
            mCallback.onDislikedClicked(commentItem.getCommentId(), commentItem.getIsDisliked());
        }

        @OnClick({R.id.ib_commentItem_repliesIcon, R.id.tv_commentItem_repliesCount, R.id.tv_commentItem_reply})
        public void onRepliesClicked() {
            ComicComment commentItem = mCommentsList.get(getAdapterPosition());
            mCallback.onReplyClicked(commentItem);
        }
    }
}
