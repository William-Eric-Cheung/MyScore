package com.vancior.myscore.view;

import com.vancior.myscore.web.SearchTask;

/**
 * Created by H on 2017/3/8.
 */

public class OnlineFragment extends MyFragment {

    @Override
    protected void loadSheets() {
        SearchTask task = new SearchTask(this, mStaggeredViewAdapter, mSwipeRefreshLayout);
        task.execute("https://musescore.com/sheetmusic?text=&instruments=0&parts=1");
    }

    public void onSearch(String query) {
        if (sheets != null)
            sheets.clear();
//        SearchTask task = new SearchTask(this, mGridViewAdapter, mRecyclerView);
        SearchTask task = new SearchTask(this, mStaggeredViewAdapter, mSwipeRefreshLayout);
        task.execute("https://musescore.com/sheetmusic?text=" + query + "&instruments=0&parts=1");
    }
}
