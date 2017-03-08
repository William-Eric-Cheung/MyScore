package com.vancior.myscore.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;

/**
 * Created by H on 2017/3/8.
 */

public class FavoriteFragment extends MyFragment {

    @Override
    protected void loadSheets() {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Favorite", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String bookmark = cursor.getString(cursor.getColumnIndex("bookmark"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                String linkUrl = cursor.getString(cursor.getColumnIndex("linkurl"));
                String imgUrl = cursor.getString(cursor.getColumnIndex("imgurl"));
                addSheet(bookmark, author, linkUrl, imgUrl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        mStaggeredViewAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
