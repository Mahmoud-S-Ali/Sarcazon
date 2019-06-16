package com.joyapeak.sarcazon.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 6/25/2018.
 */

public class BasicListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<String> mItemsList;
    private Callback mCallback;

    public interface Callback {
        void onItemClicked(int position);
    }

    public BasicListAdapter(List<String> items, Callback callback) {
        mItemsList = items;
        mCallback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_basic_list, parent, false));
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

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_basicDialogItem)
        TextView mItemTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mItemTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            String itemName = mItemsList.get(position);
            mItemTV.setText(itemName);
        }

        @Override
        public void onClick(View v) {
            mCallback.onItemClicked(getAdapterPosition());
        }
    }
}
