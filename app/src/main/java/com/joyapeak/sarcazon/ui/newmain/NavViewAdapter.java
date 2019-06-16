package com.joyapeak.sarcazon.ui.newmain;

import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 7/18/2018.
 */

public class NavViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<String> mItemsList;
    private Callback mCallback;

    private int mNewProfileNotificationsCount = 0;

    // private int mSelectedPosition = ApiHelper.COMIC_SOURCES.DEFAULT;
    private final int PROFILE_POSITION = 1;


    public interface Callback {
        void onItemClicked(int position);
    }

    public NavViewAdapter(List<String> itemsList) {
        mItemsList = itemsList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setSelectedComicSource(int selectedComicSource) {
        // mSelectedPosition = selectedComicSource;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_nav_view, parent, false));
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
        return position;
    }

    public void clearItems() {
        mItemsList = null;
        notifyDataSetChanged();
    }

    public void addItems(List<String> items) {
        int startPosition = getItemCount();

        mItemsList.addAll(items);
        notifyItemRangeInserted(startPosition, mItemsList.size());
    }

    public void setNewProfileNotificationCount(int count) {
        mNewProfileNotificationsCount = count;
        notifyDataSetChanged();
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.layout_divider_light)
        View mDividerLayout;

        @BindView(R.id.tv_navViewItem_name)
        TextView mItemNameTV;

        @BindView(R.id.iv_navViewItem_icon)
        ImageView mItemIconIV;

        /*@BindView(R.id.iv_navViewItem_notifIcon)
        ImageView mNotifIconIV;*/

        @BindView(R.id.tv_navViewItem_new_count)
        TextView mNotifCountTV;

        @BindView(R.id.tv_navViewItem_stopWatch)
        TextView mStopWatchTV;

        TypedArray mIconDrawableTypedArr;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mIconDrawableTypedArr = itemView.getContext().getResources().obtainTypedArray(R.array.nav_view_items_icons);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mItemNameTV.setText("");
            // mNotifIconIV.setImageDrawable(null);
            mNotifCountTV.setText("");
            mStopWatchTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            final String itemName = mItemsList.get(position);
            mItemNameTV.setText(itemName);

            /*if (position == mSelectedPosition) {
                mItemNameTV.setTextColor(itemView.getContext().getResources().getColor(R.color.white_op87));

            } else {*/
                mItemNameTV.setTextColor(itemView.getContext().getResources().getColor(R.color.white_op70));
            // }

            int drawableSrcId;
            if (position < mIconDrawableTypedArr.length()) {
                drawableSrcId = mIconDrawableTypedArr.getResourceId(position, 0);

            } else {
                drawableSrcId = R.mipmap.ic_launcher;
            }

            mItemIconIV.setVisibility(View.VISIBLE);
            ImageUtils.loadDrawableIntoView(itemView.getContext(),
                    mItemIconIV,
                    drawableSrcId,
                    false,
                    R.drawable.ic_circle_black57);

            if (position < PROFILE_POSITION) {
                mDividerLayout.setVisibility(View.GONE);
                mNotifCountTV.setVisibility(View.GONE);

            } else {
                switch (position) {

                    case PROFILE_POSITION:
                        if (mNewProfileNotificationsCount > 0) {
                            mNotifCountTV.setText(String.valueOf(mNewProfileNotificationsCount));
                            mNotifCountTV.setVisibility(View.VISIBLE);
                        } else {
                            mNotifCountTV.setVisibility(View.GONE);
                        }
                        break;

                    default:
                        mDividerLayout.setVisibility(View.GONE);
                        mNotifCountTV.setVisibility(View.GONE);
                }

            /*if (position > 1) {
                mItemNameTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }*/
            }
        }


        @Override
        public void onClick(View v) {

            mCallback.onItemClicked(getAdapterPosition());
            int adapterPosition = getAdapterPosition();

            /*if (adapterPosition != CREDITS_POSITION &&
                    adapterPosition != REPORTED_COMMENTS_POSITION &&
                    adapterPosition != TUTORIAL_POSITION &&
                    adapterPosition != PROFILE_POSITION &&
                    adapterPosition != LEADERBOARD_POSITION) {

                mSelectedPosition = adapterPosition;
                notifyDataSetChanged();
            }*/

            if (adapterPosition == PROFILE_POSITION) {
                mNewProfileNotificationsCount = 0;
                notifyDataSetChanged();
            }
        }
    }
}
