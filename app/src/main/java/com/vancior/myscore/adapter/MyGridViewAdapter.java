package com.vancior.myscore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.vancior.myscore.R;
import com.vancior.myscore.bean.Sheet;
import com.vancior.myscore.view.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 2017/2/27.
 */

public class MyGridViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    private static final String TAG = "MyGridViewAdapter";

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    public MyStaggeredViewAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MyStaggeredViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public MyFragment mFragment;
    public List<Sheet> mDatas;
    public List<Integer> mHeights;
    public LayoutInflater mLayoutInflater;

    public MyGridViewAdapter(Context mContext, List<Sheet> sheets) {
        this.mContext = mContext;
        this.mDatas = sheets;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
        MyRecyclerViewHolder mRecyclerViewHolder = new MyRecyclerViewHolder(mView);
        return mRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        //TODO: modify
//        ViewGroup.LayoutParams mLayoutParams = holder.mTextView.getLayoutParams();
//        mLayoutParams.height = mHeights.get(position);
//        holder.mTextView.setLayoutParams(mLayoutParams);
        holder.mTextViewTitle.setText(mDatas.get(position).getBookmark());
        holder.mTextViewArthur.setText(mDatas.get(position).getUserName());
        //TODO:
//        String imgFile = MainActivity.STORAGE + "/" + mDatas.get(position).getBookmark() + "score.jpeg";
//        Log.d(TAG, "onBindViewHolder: " + imgFile);
//        Bitmap bitmap = BitmapFactory.decodeFile(imgFile);
//        holder.mImageView.setImageBitmap(bitmap);
//        holder.mImageView.setImageURI(Uri.parse(mDatas.get(position).getImgUrl()));
        Glide.with(mContext)
                .load(mDatas.get(position).getImgUrl())
                .crossFade()
                .fitCenter()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
