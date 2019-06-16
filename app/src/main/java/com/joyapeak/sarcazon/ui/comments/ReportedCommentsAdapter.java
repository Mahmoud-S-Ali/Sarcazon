package com.joyapeak.sarcazon.ui.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.comment.CommentResponse.ComicComment;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mahmoud Ali on 5/1/2018.
 */

public class ReportedCommentsAdapter extends BaseCommentsAdapter {

    private Callback mCallback;

    public interface Callback {
        void onAllowClicked(long commentId, int adapterPosition);
        void onBlockClicked(long commentId, int adapterPosition);
    }

    public ReportedCommentsAdapter(List<ComicComment> commentsList) {
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
                        R.layout.item_comment_reported, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    public class ViewHolder extends BaseCommentsAdapter.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
        }

        @OnClick(R.id.btn_pendingReview_allow)
        public void onAllowClicked() {
            ComicComment commentItem = mCommentsList.get(getAdapterPosition());
            mCallback.onAllowClicked(commentItem.getCommentId(), getAdapterPosition());
        }

        @OnClick(R.id.btn_reportedComment_block)
        public void onBlockClicked() {
            ComicComment commentItem = mCommentsList.get(getAdapterPosition());
            mCallback.onBlockClicked(commentItem.getCommentId(), getAdapterPosition());
        }
    }
}
