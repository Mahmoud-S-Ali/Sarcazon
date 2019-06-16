package com.joyapeak.sarcazon.ui.search.searchcomics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joyapeak.sarcazon.R;
import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.di.component.ActivityComponent;
import com.joyapeak.sarcazon.ui.InfiniteReloadableAdapter;
import com.joyapeak.sarcazon.ui.base.BaseFragment;
import com.joyapeak.sarcazon.ui.comicview.ComicViewActivity;
import com.joyapeak.sarcazon.ui.profile.profilecomics.ProfileComicsAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Ali on 6/11/2018.
 */

public class SearchComicsFragment extends BaseFragment implements SearchComicsMvpView {

    @BindView(R.id.rv_search_comics)
    RecyclerView mComicsSearchRV;

    @Inject
    SearchComicsMvpPresenter<SearchComicsMvpView> mPresenter;

    @Inject
    ProfileComicsAdapter mComicsAdapter;

    @Inject
    @Named("scrollable_layoutmanager")
    LinearLayoutManager mLayoutManager;


    public static SearchComicsFragment newInstance() {
        SearchComicsFragment fragment = new SearchComicsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_comics, container, false);

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
        setupComicsAdapter();
        setupComicsRV();
    }

    private void setupComicsAdapter() {
        mComicsAdapter.setCallback(new ProfileComicsAdapter.Callback() {
            @Override
            public void onComicClicked(long comicId) {
                startActivity(ComicViewActivity.getStartIntent(getActivity(), comicId, null));
            }
        });
    }
    private void setupComicsRV() {
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mComicsSearchRV.setLayoutManager(mLayoutManager);
        mComicsSearchRV.setAdapter(mComicsAdapter);

        mComicsAdapter.addNewItemsOnScrollListener(mComicsSearchRV,
                new InfiniteReloadableAdapter.InfiniteItemsScrollCallback() {
            @Override
            public void onScrollReachedEnd() {
                // TODO: Call the search api with offset
            }
        });
    }

    public void processSearch(String query) {
        mPresenter.processSearch(query);
    }

    @Override
    public void onSearchResult(List<ComicResponse.ComicInfo> comicsList) {
        // Timber.d(comicsList.toString());
        showMessage("Search returned");
    }
}
