package com.joyapeak.sarcazon.ui.comicview;

import com.joyapeak.sarcazon.data.network.model.server.comic.ComicResponse;
import com.joyapeak.sarcazon.ui.base.MvpView;

/**
 * Created by Mahmoud Ali on 5/20/2018.
 */

public interface ComicViewMvpView extends MvpView {

    void setupComicFragment(ComicResponse.SingleComic comic);
}
