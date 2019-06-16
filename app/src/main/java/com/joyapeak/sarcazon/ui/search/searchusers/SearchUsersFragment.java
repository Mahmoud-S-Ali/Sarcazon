package com.joyapeak.sarcazon.ui.search.searchusers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.search.SearchResponse;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.UsersAdapter;
import com.joyapeak.sarcazon.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 6/13/2018.
 */

public class SearchUsersFragment extends BaseFragment implements SearchUsersMvpView {

    @BindView(R.id.rv_search_users)
    RecyclerView mUsersSearchRV;

    @Inject
    SearchUsersMvpPresenter<SearchUsersMvpView> mPresenter;

    @Inject
    UsersAdapter mUsersAdapter;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;



    public static SearchUsersFragment newInstance() {
        SearchUsersFragment fragment = new SearchUsersFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_users, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            ButterKnife.bind(this, root);

            mPresenter.onAttach(this);

            setUp(root);
        }

        return root;
    }

    @Override
    protected void setUp(View view) {
        setupUsersAdapter();
        setupUsersRV();
    }

    private void setupUsersAdapter() {
        mUsersAdapter.setCallback(new UsersAdapter.Callback() {
            @Override
            public void onUserClicked(Long userId) {
                showMessage("User clicked");
            }
        });
    }
    private void setupUsersRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mUsersSearchRV.setLayoutManager(mLayoutManager);
        mUsersSearchRV.setAdapter(mUsersAdapter);

        mUsersAdapter.addNewItemsOnScrollListener(mUsersSearchRV, new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {
            @Override
            public void onScrollReachedEnd() {
                // TODO: Request new users with offset
                showMessage("Should load new users");
            }
        });
    }

    public void processSearch(String query) {
        mPresenter.processSearch(query);
    }

    @Override
    public void onSearchResult(List<SearchResponse.SingleUserSearch> usersList) {
        showMessage("Users returned");
    }
}
