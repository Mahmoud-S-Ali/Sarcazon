package com.joyapeak.sarcazon.ui.profile.profilenotifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.notification.NotificationResponse.ServerNotification;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mahmoud Ali on 6/27/2018.
 */

public class ProfileNotificationsAdapter extends InfiniteReloadableAdapter {

    private List<ServerNotification> mItemsList;
    private View mEmptyView;
    private Callback mCallback;

    public interface Callback {
        void onItemClicked(int position, ServerNotification notificationData);
        void onProfileClicked(long profileId);
        void onComicClicked(long comicId);
    }

    public ProfileNotificationsAdapter(List<ServerNotification> items) {
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
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mItemsList == null? 0 : mItemsList.size();
    }

    public void setTotalNotificationsCount(int count) {
        mTotalItemsCount = count;
    }

    /*public int getItemPositionInList(int adapterPosition) {
        return getItemCount() - adapterPosition - 1;
    }*/

    @Override
    public long getItemId(int position) {
        // TODO: Should user notification id instead
        return position;//mItemsList.get(position).getComicId();
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

    public void addNotifications(List<ServerNotification> notifications) {
        int startPosition = getItemCount();
        mItemsList.addAll(notifications);
        notifyItemRangeInserted(startPosition, mItemsList.size());

        updateEmptyViewVisibility();
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.layout_notification_root)
        RelativeLayout mNotificationRootLayout;

        @BindView(R.id.iv_notification_userPhoto)
        ImageView mUserPhotoIV;

        @BindView(R.id.iv_notification_comicPhoto)
        ImageView mComicPhotoIV;

        @BindView(R.id.tv_notification_userName)
        TextView mUserNameTV;

        @BindView(R.id.tv_notification_and)
        TextView mAndTV;

        @BindView(R.id.tv_notification_others)
        TextView mOthersTV;

        @BindView(R.id.tv_notification_action)
        TextView mActionTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mUserPhotoIV.setImageDrawable(null);
            mComicPhotoIV.setImageDrawable(null);

            mUserNameTV.setText("");
            mAndTV.setText("");
            mOthersTV.setText("");
            mActionTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            ServerNotification notification = mItemsList.get(getAdapterPosition());
            String actionType = notification.getType() + "-" + notification.getAction();

            if (notification.getIsNew() != null && notification.getIsNew()) {
                mNotificationRootLayout.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.backgroundLighter));

            } else {
                mNotificationRootLayout.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.transparent));
            }

            switch (actionType) {
                case ApiHelper.NOTIFICATION_TYPES.NOT_COMIC_FEATURED:
                    instantiateComicFeaturedNotification(notification);
                    break;

                case ApiHelper.NOTIFICATION_TYPES.NOT_COMIC_LIKE:
                    instantiateComicLikeNotification(notification);
                    break;

                case ApiHelper.NOTIFICATION_TYPES.NOT_COMMENT:
                    instantiateCommentNotification(notification);
                    break;

                case ApiHelper.NOTIFICATION_TYPES.NOT_COMMENT_LIKE:
                    instantiateCommentLikeNotification(notification);
                    break;

                case ApiHelper.NOTIFICATION_TYPES.NOT_REPLY:
                    instantiateReplyNotification(notification);
                    break;

                case ApiHelper.NOTIFICATION_TYPES.NOT_REPLY_LIKE:
                    instantiateReplyLikeNotification(notification);
                    break;

                case ApiHelper.NOTIFICATION_TYPES.NOT_FOLLOW:
                    instantiateSubNotification(notification);
                    break;
            }
        }

        @OnClick({R.id.iv_notification_userPhoto, R.id.tv_notification_userName})
        public void onProfileClicked() {
            ServerNotification serverNotification = mItemsList.get(getAdapterPosition());
            if (serverNotification.getUserName() == null || serverNotification.getUserName().isEmpty()) {
                Toast.makeText(
                        itemView.getContext(),
                        itemView.getContext().getString(R.string.user_not_registered),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            long userId = serverNotification.getUserId();
            mCallback.onProfileClicked(userId);
        }

        @OnClick(R.id.iv_notification_comicPhoto)
        public void onComicPhotoClicked() {
            long comicId = mItemsList.get(getAdapterPosition()).getComicId();
            mCallback.onComicClicked(comicId);
        }


        private void instantiateComicFeaturedNotification(ServerNotification notification){
            setAction(itemView.getContext().getString(R.string.notif_action_comic_featured));
            setComicData(notification.getComicPhotoUrl());

            ImageUtils.loadDrawableIntoView(
                    itemView.getContext(),
                    mUserPhotoIV,
                    R.mipmap.ic_launcher,
                    true,
                    R.drawable.im_default);
        }
        private void instantiateComicLikeNotification(ServerNotification notification){
            setComicData(notification.getComicPhotoUrl());
            setUserData(notification.getUserName(), notification.getUserPhotoUrl(), notification.getCount());
            setAction(itemView.getContext().getString(R.string.notif_action_comic_like));
        }
        private void instantiateCommentNotification(ServerNotification notification){
            setComicData(notification.getComicPhotoUrl());
            setUserData(notification.getUserName(), notification.getUserPhotoUrl(), notification.getCount());

            String action = itemView.getContext().getString(R.string.notif_action_comment) +
                    " \"" + notification.getComment() + " \"";
            setAction(action);
        }
        private void instantiateCommentLikeNotification(ServerNotification notification){
            setComicData(notification.getComicPhotoUrl());
            setUserData(notification.getUserName(), notification.getUserPhotoUrl(), notification.getCount());

            String action = itemView.getContext().getString(R.string.notif_action_comment_like) +
                    " \"" + notification.getComment() + " \"";
            setAction(action);
        }
        private void instantiateReplyNotification(ServerNotification notification){
            setComicData(notification.getComicPhotoUrl());
            setUserData(notification.getUserName(), notification.getUserPhotoUrl(), notification.getCount());

            String action = itemView.getContext().getString(R.string.notif_action_reply) +
                    " \"" + notification.getReply() + " \"";
            setAction(action);
        }
        private void instantiateReplyLikeNotification(ServerNotification notification){
            setComicData(notification.getComicPhotoUrl());
            setUserData(notification.getUserName(), notification.getUserPhotoUrl(), notification.getCount());

            String action = itemView.getContext().getString(R.string.notif_action_reply_like) +
                    " \"" + notification.getReply() + " \"";
            setAction(action);
        }
        private void instantiateSubNotification(ServerNotification notification){
            setUserData(notification.getUserName(), notification.getUserPhotoUrl(), notification.getCount());

            String action = itemView.getContext().getString(R.string.notif_action_sub);
            setAction(action);
        }

        private void setUserData(String userName, String photoUrl, int usersCount) {
            mUserPhotoIV.setVisibility(View.VISIBLE);
            mUserNameTV.setVisibility(View.VISIBLE);
            mAndTV.setVisibility(View.VISIBLE);
            mOthersTV.setVisibility(View.VISIBLE);

            if (userName == null || userName.isEmpty()) {
                userName = itemView.getContext().getString(R.string.someone);
            }
            mUserNameTV.setText(userName);
            if (usersCount > 1) {
                mAndTV.setText(itemView.getContext().getString(R.string.and));
                mOthersTV.setText(itemView.getContext().getString(R.string.count_others,
                        String.valueOf(usersCount - 1)));
            }

            ImageUtils.loadImageUrlIntoView(
                    itemView.getContext(),
                    mUserPhotoIV,
                    photoUrl,
                    true,
                    R.drawable.im_default);
        }
        private void setComicData(String comicPhotoUrl) {
            mComicPhotoIV.setVisibility(View.VISIBLE);

            ImageUtils.loadImageUrlIntoView(
                    itemView.getContext(),
                    mComicPhotoIV,
                    comicPhotoUrl,
                    false,
                    -1);
        }
        private void setAction(String action) {
            mActionTV.setVisibility(View.VISIBLE);
            mActionTV.setText(action);
        }

        private void updateNotificationIsNewStatus(int adapterPosition) {
            ServerNotification notification = mItemsList.get(adapterPosition);
            if (notification.getIsNew() != null && notification.getIsNew()) {
                notification.setIsNew(false);
                notifyItemChanged(adapterPosition);
            }
        }

        @Override
        public void onClick(View v) {
            updateNotificationIsNewStatus(getAdapterPosition());
            ServerNotification notificationData =
                    mItemsList.get(getAdapterPosition());

            mCallback.onItemClicked(getAdapterPosition(), notificationData);
        }
    }
}
