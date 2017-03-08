package com.vancior.myscore.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.vancior.myscore.R;
import com.vancior.myscore.activity.MainActivity;
import com.vancior.myscore.bean.Sheet;
import com.vancior.myscore.view.MyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H on 2017/2/25.
 */

public class MyStaggeredViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder>{

    private static final String TAG = "MyStaggeredViewAdapter";

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    public interface OnItemLongClickListener {

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public Context mContext;
    public List<Sheet> mDatas;
    public LayoutInflater mLayoutInflater;

    public MyStaggeredViewAdapter(Context mContext, List<Sheet> sheets) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = sheets;
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

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    return true;
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
                .placeholder(R.drawable.placeholder)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
