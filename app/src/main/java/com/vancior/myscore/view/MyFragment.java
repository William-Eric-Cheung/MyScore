package com.vancior.myscore.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vancior.myscore.R;
import com.vancior.myscore.activity.MainActivity;
import com.vancior.myscore.activity.MatchActivity;
import com.vancior.myscore.adapter.MyGridViewAdapter;
import com.vancior.myscore.adapter.MyStaggeredViewAdapter;
import com.vancior.myscore.bean.Sheet;
import com.vancior.myscore.web.SearchTask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by H on 2017/2/25.
 */

public class MyFragment extends Fragment
        implements MyStaggeredViewAdapter.OnItemClickListener{

    private static final String TAG = "MyFragment";

    private List<Sheet> sheets;
    private View mView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyStaggeredViewAdapter mStaggeredViewAdapter;
    private MyGridViewAdapter mGridViewAdapter;

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

        sheets = new ArrayList<>();

        configRecyclerView();

        SearchTask task = new SearchTask(this, mStaggeredViewAdapter, mRecyclerView);
//        SearchTask task = new SearchTask(this, mGridViewAdapter, mRecyclerView);
        task.execute("https://musescore.com/sheetmusic?text=&instruments=0&parts=1");
    }

    private void configRecyclerView() {
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
//        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT, GridLayoutManager.VERTICAL, true);
        mStaggeredViewAdapter = new MyStaggeredViewAdapter(getActivity(), sheets);
        mStaggeredViewAdapter.setOnItemClickListener(this);
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

    public void addSheet(String bookmark, String userName, String postTime, String viewNum, String linkUrl, String imgUrl) {
        Sheet sheet = new Sheet(bookmark, userName, postTime, viewNum, linkUrl, imgUrl);
        sheets.add(sheet);
//        mGridViewAdapter.notifyDataSetChanged();
    }

    public void onSearch(String query) {
        if (sheets != null)
            sheets.clear();
//        SearchTask task = new SearchTask(this, mGridViewAdapter, mRecyclerView);
        SearchTask task = new SearchTask(this, mStaggeredViewAdapter, mRecyclerView);
        task.execute("https://musescore.com/sheetmusic?text=" + query + "&instruments=0&parts=1");
    }

    public void onRefresh() {
        if (mRecyclerView != null)
            mRecyclerView.requestLayout();
    }

}