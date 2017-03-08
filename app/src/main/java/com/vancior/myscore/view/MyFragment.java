package com.vancior.myscore.view;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vancior.myscore.MyApplication;
import com.vancior.myscore.R;
import com.vancior.myscore.activity.MainActivity;
import com.vancior.myscore.activity.MatchActivity;
import com.vancior.myscore.adapter.MyGridViewAdapter;
import com.vancior.myscore.adapter.MyStaggeredViewAdapter;
import com.vancior.myscore.bean.Sheet;
import com.vancior.myscore.database.MyDatabaseHelper;
import com.vancior.myscore.web.SearchTask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by H on 2017/2/25.
 */

public class MyFragment extends Fragment
        implements MyStaggeredViewAdapter.OnItemClickListener,
        MyStaggeredViewAdapter.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MyFragment";

    protected List<Sheet> sheets;
    private View mView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected MyStaggeredViewAdapter mStaggeredViewAdapter;
    protected MyDatabaseHelper mMyDatabaseHelper;

    //TODO: change SPAN_COUNT according to the screen size
    private static final int SPAN_COUNT = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_main, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_swiperefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        sheets = new ArrayList<>();
        mMyDatabaseHelper = new MyDatabaseHelper(MyApplication.getContext(), "MyScore.db", null, 2);

        configRecyclerView();
        loadSheets();
    }

    private void configRecyclerView() {
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
//        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, true);
        mStaggeredViewAdapter = new MyStaggeredViewAdapter(getActivity(), sheets);
        mStaggeredViewAdapter.setOnItemClickListener(this);
        mStaggeredViewAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mStaggeredViewAdapter);
//        mGridViewAdapter = new MyGridViewAdapter(getActivity(), sheets);
//        mGridViewAdapter.setOnItemClickListener(this);
//        mRecyclerView.setAdapter(mGridViewAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: " + sheets.get(position).getLinkUrl());
        Intent intent = new Intent(getActivity(), MatchActivity.class);
        intent.putExtra("linkUrl", sheets.get(position).getLinkUrl());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        Log.d(TAG, "onItemLongClick: " + sheets.get(position).getLinkUrl());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Notify");
        builder.setMessage("Add to Favorite?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("bookmark", sheets.get(position).getBookmark());
                values.put("author", sheets.get(position).getUserName());
                values.put("linkurl", sheets.get(position).getLinkUrl());
                values.put("imgurl", sheets.get(position).getImgUrl());
                db.insert("Favorite", null, values);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onRefresh() {
        loadSheets();
    }

    public void addSheet(String bookmark, String userName, String linkUrl, String imgUrl) {
        Sheet sheet = new Sheet(bookmark, userName, linkUrl, imgUrl);
        sheets.add(sheet);
//        mGridViewAdapter.notifyDataSetChanged();
    }

    public void onSearch(String query) {}

    protected void loadSheets() {}

}