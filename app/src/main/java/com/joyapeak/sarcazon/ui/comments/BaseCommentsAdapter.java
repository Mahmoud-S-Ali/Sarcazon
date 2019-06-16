package com.joyapeak.sarcazon.ui.comments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.AppConstants;
import com.joyapeak.sarcazon.utils.CommonUtils;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by test on 8/27/2018.
 */

public abstract class BaseCommentsAdapter extends InfiniteReloadableAdapter {

    protected List<ComicComment> mCommentsList;
    private View mEmptyView;

    protected long mViewerId = AppConstants.NULL_INDEX;

    public BaseCommentsAdapter(List<ComicComment> commentsList) {
        mCommentsList = commentsList;
    }
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mCommentsList == null? 0 : mCommentsList.size();
    }

    @Override
    public long getItemId(int position) {
        if (mCommentsList != null && mCommentsList.size() > 0) {
            Long commentId = mCommentsList.get(position).getCommentId();
            return commentId == null? position : commentId;
        }
        return position;
    }

    private void updateEmptyViewVisibility() {
        if (mEmptyView != null) {
            if (getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);

            } else {
                mEmptyView.setVisibility(View.GONE);
            }
        }
    }
    public void setViewerId(long viewerId) {
        mViewerId = viewerId;
    }

    public void setTotalCommentsCount(int count) {
        if (mTotalItemsCount == 0) {
            mTotalItemsCount = count;
        }
    }
    public void addComments(List<ComicComment> comments) {
        int startPosition = getItemCount();
        mCommentsList.addAll(comments);
        notifyItemRangeInserted(startPosition, mCommentsList.size());

        updateEmptyViewVisibility();
    }
    public void clearAllComments() {
        int oldSize = getItemCount();
        mCommentsList.clear();
        mTotalItemsCount = 0;
        notifyItemRangeRemoved(0, oldSize);
    }
    public void removeComment(int position) {
        if (mCommentsList != null && position >= 0 && position < mCommentsList.size()) {
            mCommentsList.remove(position);
            notifyItemRemoved(position);

            updateEmptyViewVisibility();
        }
    }


    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_commentItem_commenterPhoto)
        ImageView mCommenterPhotoIV;

        @BindView(R.id.tv_commentItem_body)
        TextView mCommentBodyTV;

        @BindView(R.id.tv_commentItem_commenterName)
        TextView mCommenterNameTV;

        @BindView(R.id.tv_commentItem_time)
        TextView mCommentTimeTV;


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
        }
    }
}
