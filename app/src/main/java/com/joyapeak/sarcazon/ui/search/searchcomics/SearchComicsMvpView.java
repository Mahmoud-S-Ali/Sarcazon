package com.joyapeak.sarcazon.ui.search.searchcomics;

import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

import java.util.List;

/**
 * Created by Mahmoud Ali on 6/11/2018.
 */

public interface SearchComicsMvpView extends MvpView {

    void onSearchResult(List<ComicResponse.ComicInfo> comicsList);
}
