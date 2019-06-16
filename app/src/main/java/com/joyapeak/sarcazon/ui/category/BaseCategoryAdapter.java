package com.joyapeak.sarcazon.ui.category;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse.SingleCategory;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 12/2/2018.
 */

public abstract class BaseCategoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<SingleCategory> mCategoriesList;

    protected String mSelectedCategory;


    public BaseCategoryAdapter(List<SingleCategory> categoriesList) {
        this.mCategoriesList = categoriesList;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        viewHolder.onBind(position);
    }


    public void setItems(List<SingleCategory> categories) {
        this.mCategoriesList = categories;
        notifyItemRangeInserted(0, getItemCount());
    }

    public void setSelectedCategory(String category) {
        mSelectedCategory = category;
    }
    public String getSelectedCategory() {
        return mSelectedCategory;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mCategoriesList == null? 0 : mCategoriesList.size();
    }

    public abstract class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_category_image)
        ImageView mCategoryIV;

        @BindView(R.id.tv_category_name)
        TextView mCategoryNameTV;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            ImageUtils.loadDrawableIntoView(itemView.getContext(), mCategoryIV,
                    R.drawable.im_default, true, R.drawable.im_default);

            mCategoryNameTV.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            SingleCategory category = mCategoriesList.get(position);

            mCategoryNameTV.setText(category.getName());
            ImageUtils.loadImageUrlIntoView(itemView.getContext(), mCategoryIV,
                    category.getPhotoUrl(), true, R.drawable.im_default);
        }

        @Override
        public abstract void onClick(View v);
    }
}
