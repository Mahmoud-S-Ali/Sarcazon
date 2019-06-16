package com.joyapeak.sarcazon.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.category.CategoryResponse.SingleCategory;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 12/5/2018.
 *
 * Allows selecting more than one category
 */

public class MultipleCategoryAdapter extends BaseCategoryAdapter {

    private List<String> mSelectedCategories;
    private boolean mEditModeEnabled = false;

    public MultipleCategoryAdapter(List<SingleCategory> categoriesList,
                                   List<String> mSelectedCategories) {
        super(categoriesList);
        this.mSelectedCategories = mSelectedCategories;
    }

    private Callback mCallback;

    public interface Callback {
        void onCategorySelected(String category);
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

    public void setEditMode(boolean enabled) {
        mEditModeEnabled = enabled;
        notifyItemRangeChanged(0, getItemCount());
    }


    public List<String> getSelectedCategories() {
        return mSelectedCategories;
    }

    public class ViewHolder extends BaseCategoryAdapter.ViewHolder {

        @BindView(R.id.checkBox_category)
        CheckBox mCategoryCB;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            super.clear();
            mCategoryCB.setChecked(false);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            if (mEditModeEnabled) {
                mCategoryCB.setVisibility(View.VISIBLE);

            } else {
                mCategoryCB.setVisibility(View.GONE);
            }

            if ((mSelectedCategory == null && position == 0) ||
                    (mSelectedCategory != null && mSelectedCategory.equals(mCategoryNameTV.getText().toString()))) {

                mCategoryNameTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));

            } else {
                mCategoryNameTV.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white_op87));
            }
        }

        @Override
        public void onClick(View v) {

            String selectedCategory = mCategoriesList.get(getAdapterPosition()).getName();

            if (mEditModeEnabled) {
                if (mCategoryCB.isChecked()) {
                    mCategoryCB.setChecked(false);
                    mSelectedCategories.remove(selectedCategory);

                } else {
                    mCategoryCB.setChecked(true);
                    mSelectedCategories.add(selectedCategory);
                }

            } else {
                mCallback.onCategorySelected(selectedCategory);
            }
        }
    }
}
