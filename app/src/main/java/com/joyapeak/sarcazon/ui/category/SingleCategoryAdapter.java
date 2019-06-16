package com.joyapeak.sarcazon.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 12/5/2018.
 *
 * Allows selecting only one category
 */

public class SingleCategoryAdapter extends BaseCategoryAdapter {

    private RadioButton mLastCheckedRB;

    private Callback mCallback;

    public interface Callback {
        void onCategorySelected();
    }

    public SingleCategoryAdapter(List<CategoryResponse.SingleCategory> categoriesList) {
        super(categoriesList);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_category, viewGroup, false));
    }

    public String getSelectedCategory() {
        return mSelectedCategory;
    }

    public class ViewHolder extends BaseCategoryAdapter.ViewHolder {

        @BindView(R.id.radioBtn_category)
        RadioButton mSelectedCategoryRadioBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            super.clear();
            mSelectedCategoryRadioBtn.setChecked(false);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            mSelectedCategoryRadioBtn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {

            mSelectedCategory = mCategoriesList.get(getAdapterPosition()).getName();

            mSelectedCategoryRadioBtn.setChecked(true);
            if (mLastCheckedRB != null) {
                if (!mLastCheckedRB.equals(mSelectedCategoryRadioBtn)) {
                    mLastCheckedRB.setChecked(false);
                }

            } else {
                mCallback.onCategorySelected();
            }

            mLastCheckedRB = mSelectedCategoryRadioBtn;
        }
    }
}
