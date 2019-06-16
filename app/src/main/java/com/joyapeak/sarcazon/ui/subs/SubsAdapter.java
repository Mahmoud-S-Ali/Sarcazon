package com.joyapeak.sarcazon.ui.subs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.subs.SubsResponse;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mahmoud Ali on 5/9/2018.
 */

public class SubsAdapter extends InfiniteReloadableAdapter {

    private List<SubsResponse.UserSub> mSubsList;

    private Long mViewerId;
    private View mEmptyView;
    private Callback mCallback;

    public interface Callback {
        void onSubItemClicked(long userId);
        void onSubscribeClicked(ViewHolder holder, long userId, boolean shouldSubscribe);
    }

    public SubsAdapter(List<SubsResponse.UserSub> subsList) {
        mSubsList = subsList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setViewerId(long userId) {
        mViewerId = userId;
    }
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_follow, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mSubsList == null? 0 : mSubsList.size();
    }

    @Override
    public long getItemId(int position) {
        if (mSubsList != null) {
            return mSubsList.get(position).getUserId();
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

    public void setTotalSubsCount(int totalSubsCount) {
        mTotalItemsCount = totalSubsCount;
    }

    public void addSubs(List<SubsResponse.UserSub> subs) {
        int startPosition = getItemCount();
        mSubsList.addAll(subs);
        notifyItemRangeInserted(startPosition, mSubsList.size());

        updateEmptyViewVisibility();
    }

    public void updateSubscribeStatus(Context context,  ViewHolder holder, boolean isSubscribed) {
        mSubsList.get(holder.getAdapterPosition()).setIsSubscribed(isSubscribed);
        updateSubscribeView(context, holder, isSubscribed);
    }
    private void updateSubscribeView(Context context,  ViewHolder holder, boolean isSubscribed) {
        if (holder != null) {
            if (isSubscribed) {
                holder.mSubBtn.setText(R.string.unfollow);
                holder.mSubBtn.setTextColor(ContextCompat.getColor(context, R.color.white_op50));
            } else {
                holder.mSubBtn.setText(R.string.follow);
                holder.mSubBtn.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            }
        }
    }


    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.civ_sub_userPhoto)
        ImageView mSubUserIV;

        @BindView(R.id.tv_sub_userName)
        TextView mSubUserTV;

        @BindView(R.id.btn_sub_button)
        Button mSubBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mSubUserIV.setImageDrawable(null);
            mSubUserTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            final SubsResponse.UserSub sub = mSubsList.get(position);

            ImageUtils.loadImageUrlIntoView(itemView.getContext(),
                    mSubUserIV,
                    sub.getPhotoUrl(),
                    true,
                    R.drawable.im_default);

            mSubUserTV.setText(sub.getName());

            if (sub.getUserId().equals(mViewerId)) {
                mSubBtn.setVisibility(View.GONE);
                return;
            }

            updateSubscribeView(itemView.getContext(), ViewHolder.this, sub.getIsSubscribed());
        }

        @OnClick(R.id.btn_sub_button)
        public void onSubBtnClicked() {
            final SubsResponse.UserSub sub = mSubsList.get(getAdapterPosition());

            mCallback.onSubscribeClicked(ViewHolder.this, sub.getUserId(), !sub.getIsSubscribed());
        }

        @Override
        public void onClick(View v) {
            mCallback.onSubItemClicked(mSubsList.get(getAdapterPosition()).getUserId());
        }
    }
}
