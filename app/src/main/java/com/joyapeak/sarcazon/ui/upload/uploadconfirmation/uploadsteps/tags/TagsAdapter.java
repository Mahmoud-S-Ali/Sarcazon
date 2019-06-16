package com.joyapeak.sarcazon.ui.upload.uploadconfirmation.uploadsteps.tags;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.utils.AppConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 11/29/2018.
 */

public class TagsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final int mMaxTagsCount = 4;
    private String[] mTagsArr = new String[mMaxTagsCount];

    public TagsAdapter() {
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TagsAdapter.ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_tag, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        viewHolder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mMaxTagsCount;
    }

    public String[] getTags() {
        return mTagsArr;
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.et_tag)
        EditText mTagET;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
            mTagET.setText("");
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            mTagET.setHint(itemView.getContext().getString(R.string.tag) + (getAdapterPosition() + 1));
            mTagET.setFilters(new InputFilter[]{
                    new TagsInputFilter(),
                    new InputFilter.LengthFilter(itemView.getContext().getResources().getInteger(R.integer.tags_max_length))});

            mTagET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mTagsArr[getAdapterPosition()] = mTagET.getText().toString();
                }
            });
        }
    }

    private static class TagsInputFilter implements InputFilter {

        Pattern mPattern;

        public TagsInputFilter() {
            mPattern = Pattern.compile(AppConstants.TAGS_PATTERN);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest.toString() + source.toString());

            if(!matcher.matches())
                return "";

            return null;
        }

    }
}
