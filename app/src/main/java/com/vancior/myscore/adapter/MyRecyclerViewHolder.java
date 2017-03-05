package com.vancior.myscore.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vancior.myscore.R;

/**
 * Created by H on 2017/2/26.
 */

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextViewTitle;
    public TextView mTextViewArthur;
    public ImageView mImageView;

    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        mTextViewTitle = (TextView) itemView.findViewById(R.id.id_title);
        mTextViewArthur = (TextView) itemView.findViewById(R.id.id_arthur);
        mImageView = (ImageView) itemView.findViewById(R.id.id_imageview);
    }
}
