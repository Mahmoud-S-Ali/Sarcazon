package com.joyapeak.sarcazon.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.search.SearchResponse.SingleUserSearch;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 6/13/2018.
 */

public class UsersAdapter extends InfiniteReloadableAdapter {

    private List<SingleUserSearch> mUsersList;
    private Callback mCallback;


    public interface Callback {
        void onUserClicked(Long userId);
    }

    public UsersAdapter(List<SingleUserSearch> usersList) {
        mUsersList = usersList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mUsersList == null? 0 : mUsersList.size();
    }

    @Override
    public long getItemId(int position) {
        if (mUsersList != null && mUsersList.size() > 0) {
            return mUsersList.get(position).getUserId();
        }
        return position;
    }

    public void setTotalUsersCount(int count) {
        mTotalItemsCount = count;
    }
    public void addUsers(List<SingleUserSearch> users) {
        int startPosition = getItemCount();
        mUsersList.addAll(users);
        notifyItemRangeInserted(startPosition, mUsersList.size());
    }
    public void clearAllUsers() {
        int oldSize = getItemCount();
        mUsersList.clear();
        mTotalItemsCount = 0;
        notifyItemRangeRemoved(0, oldSize);
    }


    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_user_photo)
        ImageView mUserIV;

        @BindView(R.id.tv_user_name)
        TextView mNameTV;

        @BindView(R.id.tv_user_comicsCount)
        TextView mComicsCntTV;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mUserIV.setImageDrawable(null);
            mNameTV.setText("");
            mComicsCntTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            final SingleUserSearch user = mUsersList.get(position);

            ImageUtils.loadImageUrlIntoView(itemView.getContext(),
                    mUserIV,
                    user.getUserPhotoUrl(),
                    true,
                    R.drawable.im_default);

            mNameTV.setText(user.getUserName());
            mComicsCntTV.setText(user.getUserComicsCnt());
        }

        @Override
        public void onClick(View v) {
            mCallback.onUserClicked(mUsersList.get(getAdapterPosition()).getUserId());
        }
    }
}
