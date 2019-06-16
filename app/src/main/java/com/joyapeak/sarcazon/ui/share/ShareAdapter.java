package com.joyapeak.sarcazon.ui.share;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 4/25/2018.
 */

public class ShareAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<ResolveInfo> mResolveInfoList;
    private Callback mCallback;

    public interface Callback {
        void onShareSourceClicked(ResolveInfo resolveInfoItem);
    }


    public ShareAdapter(List<ResolveInfo> resolveInfoList, Callback callback) {
        mResolveInfoList = resolveInfoList;
        mCallback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_share_choice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mResolveInfoList == null? 0 : mResolveInfoList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_shareItem_icon)
        ImageView mShareIconIV;

        @BindView(R.id.tv_shareItem_itemName)
        TextView mItemNameTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mShareIconIV.setImageDrawable(null);
            mItemNameTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            ResolveInfo resolveInfo = mResolveInfoList.get(position);
            Context context = itemView.getContext();

            String label =
                    resolveInfo.activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();

            Drawable itemIcon =
                    resolveInfo.activityInfo.applicationInfo.loadIcon(context.getPackageManager());

            mShareIconIV.setImageDrawable(itemIcon);
            mItemNameTV.setText(label);
        }

        @Override
        public void onClick(View v) {
            mCallback.onShareSourceClicked(mResolveInfoList.get(getAdapterPosition()));
        }
    }
}
