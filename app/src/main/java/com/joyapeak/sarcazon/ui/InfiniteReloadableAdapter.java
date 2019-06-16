package com.joyapeak.sarcazon.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.joyapeak.sarcazon.ui.base.BaseViewHolder;

/**
 * Created by Mahmoud Ali on 6/18/2018.
 */

public abstract class InfiniteReloadableAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private final int ITEMS_LOADING_THRESHOLD = 5;
    protected int mTotalItemsCount = 0;
    private int mTotalLoadedItems;
    protected int mPrevLoadedItemsCount = -1;
    private int mLastVisibleItemPos;
    protected boolean mIsLoadingNewItems = false;

    private RecyclerView.LayoutManager mLayoutManager;

    public interface InfiniteItemsScrollCallback {
        void onScrollReachedEnd();
    }

    public void addNewItemsOnScrollListener(RecyclerView recyclerView,
                                            final InfiniteItemsScrollCallback callback) {

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (!(layoutManager instanceof LinearLayoutManager) &&
                !(layoutManager instanceof StaggeredGridLayoutManager))
            return;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                handleScrolling(recyclerView, callback);
            }
        });
    }

    public void addNewItemsOnScrollListener(NestedScrollView nestedScrollView,
                                            final RecyclerView recyclerView,
                                            final InfiniteItemsScrollCallback callback) {

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (!(layoutManager instanceof LinearLayoutManager) &&
                !(layoutManager instanceof StaggeredGridLayoutManager))
            return;

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() -
                            v.getMeasuredHeight() - 1000)) &&
                            scrollY > oldScrollY) {

                        handleScrolling(recyclerView, callback);
                    }
                }
            }
        });
    }

    private void handleScrolling(RecyclerView recyclerView, InfiniteItemsScrollCallback callback) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        mTotalLoadedItems = recyclerView.getAdapter().getItemCount();
        if (mTotalLoadedItems == 0) {
            mPrevLoadedItemsCount = -1;
        }

        if (layoutManager instanceof LinearLayoutManager) {
            mLastVisibleItemPos = ((LinearLayoutManager) layoutManager)
                    .findLastCompletelyVisibleItemPosition();

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            lastPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
            mLastVisibleItemPos = Math.max(lastPositions[0], lastPositions[1]);
        }

        if (!mIsLoadingNewItems &&
                mTotalLoadedItems != mPrevLoadedItemsCount &&
                mTotalLoadedItems < mTotalItemsCount &&
                mTotalLoadedItems <= (mLastVisibleItemPos + ITEMS_LOADING_THRESHOLD)) {

            mIsLoadingNewItems = true;
            mPrevLoadedItemsCount = mTotalLoadedItems;
            callback.onScrollReachedEnd();
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void resetIsLoadingNewItems() {
        mIsLoadingNewItems = false;
    }
}
