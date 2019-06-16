package com.joyapeak.sarcazon.ui.profile.profilecomics;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.profile.ProfileResponse;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseActivity;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.profile.ProfileActivity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 5/6/2018.
 */

public class ProfileComicsFragment extends BaseFragment implements ProfileComicsMvpView {

    public static final String TAG = "ProfileComicsFragment";

    @BindView(R.id.nsv_profile)
    NestedScrollView mParentNSV;

    @BindView(R.id.rv_profile_comics)
    RecyclerView mComicsRV;

    @BindView(R.id.layout_empty_items)
    View mEmptyView;


    @Inject
    ProfileComicsMvpPresenter<ProfileComicsMvpView> mPresenter;

    @Inject
    ProfileComicsAdapter mComicsAdapter;

    @Inject
    @Named("nonscrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;

    private long mUserId;


    public static ProfileComicsFragment newInstance(long userId) {
        ProfileComicsFragment fragment = new ProfileComicsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ProfileActivity.EXTRA_PROFILE_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_comics, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, root));

            mPresenter.onAttach(this);
            setUp(root);

            mUserId = getArguments().getLong(ProfileActivity.EXTRA_PROFILE_USER_ID);
            mPresenter.getUserComics(mUserId);
        }

        return root;
    }

    @Override
    protected void setUp(View view) {
        setupComicsAdapter();
        setupComicsRV();
    }

    private void setupComicsAdapter() {
        mComicsAdapter.setCallback(new ProfileComicsAdapter.Callback() {
            @Override
            public void onComicClicked(long comicId) {
                Intent comicViewIntent = ComicViewActivity.getStartIntent(getActivity(), comicId, null);
                comicViewIntent.putExtra(BaseActivity.EXTRA_PARENT_ACTIVITY, ProfileActivity.class.getSimpleName());
                startActivity(comicViewIntent);
            }
        });

        mComicsAdapter.setEmptyView(mEmptyView);
    }
    private void setupComicsRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mComicsRV.setLayoutManager(staggeredGridLayoutManager);
        mComicsRV.setAdapter(mComicsAdapter);

        mComicsAdapter.addNewItemsOnScrollListener(mParentNSV,
                mComicsRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {

            @Override
            public void onScrollReachedEnd() {
                mPresenter.getUserComics(mUserId);
            }
        });
    }


    @Override
    public void addUserComics(List<ProfileResponse.ProfileComicInfo> comicsList) {
        mComicsAdapter.addComics(comicsList);
        mComicsAdapter.resetIsLoadingNewItems();
    }

    @Override
    public void updateTotalComicsCount(int count) {
        mComicsAdapter.setTotalComicsCount(count);
    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
