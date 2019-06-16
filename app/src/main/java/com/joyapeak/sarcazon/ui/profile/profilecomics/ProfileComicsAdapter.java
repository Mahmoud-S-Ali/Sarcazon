package com.joyapeak.sarcazon.ui.profile.profilecomics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.ApiHelper;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse.ProfileComicInfo;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseViewHolder;
import com.joyapeak.sarcazon.ui.custom.CustomHeightImageView;
import com.joyapeak.sarcazon.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 5/7/2018.
 */

public class ProfileComicsAdapter extends InfiniteReloadableAdapter {

    private List<ProfileComicInfo> mComicsList;
    private View mEmptyView;
    private ProfileComicsAdapter.Callback mCallback;

    public interface Callback {
        void onComicClicked(long comicId);
    }

    public ProfileComicsAdapter(List<ProfileComicInfo> comicsList) {
        mComicsList = comicsList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_comic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mComicsList == null? 0 : mComicsList.size();
    }

    @Override
    public long getItemId(int position) {
        if (mComicsList != null) {
            return mComicsList.get(position).getComicId();
        }

        return position;
    }

    public void setTotalComicsCount(int count) {
        if (mTotalItemsCount == 0) {
            mTotalItemsCount = count;
        }
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

    public void addComics(List<ProfileComicInfo> comics) {
        int startPosition = getItemCount();
        mComicsList.addAll(comics);
        notifyItemRangeInserted(startPosition, mComicsList.size());

        updateEmptyViewVisibility();
    }

    public class ViewHolder extends BaseViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_comicItem)
        CustomHeightImageView mComicIV;

        @BindView(R.id.iv_comicItem_playIcon)
        ImageView mPlayIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void clear() {
            mComicIV.setImageDrawable(null);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            ProfileComicInfo comic = mComicsList.get(position);
            mComicIV.setAspectRatio(comic.getAspectRatio());

            ImageUtils.loadImageUrlIntoView(itemView.getContext(),
                    mComicIV,
                    comic.getComicThumbnail(),
                    false,
                    -1);

            int comicType = comic.getComicType();
            if (comicType != ApiHelper.COMIC_TYPES.IMAGE && comicType != ApiHelper.COMIC_TYPES.GIF) {
                mPlayIcon.setVisibility(View.VISIBLE);
            } else {
                mPlayIcon.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            mCallback.onComicClicked(mComicsList.get(getAdapterPosition()).getComicId());
        }
    }
}
