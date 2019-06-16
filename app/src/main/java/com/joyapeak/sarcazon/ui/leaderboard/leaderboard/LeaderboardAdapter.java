package com.joyapeak.sarcazon.ui.leaderboard.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.leaderboard.LeaderboardResponse.LeaderboardUserItem;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by test on 9/30/2018.
 */

public class LeaderboardAdapter extends InfiniteReloadableAdapter {

    private List<LeaderboardUserItem> mItemsList;
    private View mEmptyView;
    private Callback mCallback;

    public interface Callback {
        void onItemClicked(int position, long userId);
    }

    public LeaderboardAdapter(List<LeaderboardUserItem> items) {
        mItemsList = items;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeaderboardAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_leaderboard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mItemsList == null? 0 : mItemsList.size();
    }


    @Override
    public long getItemId(int position) {
        if (mItemsList != null) {
            return mItemsList.get(position).getUserId();
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

    public void addUsers(List<LeaderboardUserItem> users) {
        int startPosition = getItemCount();
        mItemsList.addAll(users);
        notifyItemRangeInserted(startPosition, mItemsList.size());

        updateEmptyViewVisibility();
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_leaderboard_order)
        TextView mRankTV;

        @BindView(R.id.iv_leaderboardItem_userPhoto)
        ImageView mUserPhotoIV;

        @BindView(R.id.tv_leaderboardItem_userName)
        TextView mUserNameTV;

        @BindView(R.id.tv_leaderboardItem_followersCnt)
        TextView mFollowersCntTV;

        @BindView(R.id.tv_leaderboardItem_likesCnt)
        TextView mLikesCountTV;

        private Context mContext;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mRankTV.setText("");
            mUserPhotoIV.setImageDrawable(null);
            mUserNameTV.setText("");
            mFollowersCntTV.setText("");
            mLikesCountTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            LeaderboardUserItem leaderboardInfo = mItemsList.get(getAdapterPosition());

            mRankTV.setText(String.valueOf(leaderboardInfo.getRank()) + ".");
            mUserNameTV.setText(leaderboardInfo.getName());

            ImageUtils.loadImageUrlIntoView(mContext,
                    mUserPhotoIV,
                    leaderboardInfo.getPhotoUrl(),
                    true,
                    R.drawable.im_default);

            mFollowersCntTV.setText(mContext.getString(R.string.followers_count,
                    String.valueOf(leaderboardInfo.getFollowersCount())));
            mLikesCountTV.setText(String.valueOf(leaderboardInfo.getLikesCount()));
        }

        @Override
        public void onClick(View v) {
            Long userId = mItemsList.get(getAdapterPosition()).getUserId();
            mCallback.onItemClicked(getAdapterPosition(), userId);
        }
    }
}
