package com.joyapeak.sarcazon.ui.search.searchusers;

import com.joyapeak.sarcazon.data.network.model.server.search.SearchResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 6/13/2018.
 */

public interface SearchUsersMvpView extends MvpView {

    void onSearchResult(List<SearchResponse.SingleUserSearch> usersList);
}
