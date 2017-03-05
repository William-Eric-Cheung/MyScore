package com.vancior.myscore.web;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vancior.myscore.activity.MainActivity;
import com.vancior.myscore.adapter.MyGridViewAdapter;
import com.vancior.myscore.adapter.MyStaggeredViewAdapter;
import com.vancior.myscore.bean.Sheet;
import com.vancior.myscore.view.MyFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by H on 2017/2/26.
 */

public class SearchTask extends AsyncTask<String, Integer, List<Sheet>> {

    private static final String TAG = "SearchTask";
    private MyStaggeredViewAdapter mStaggeredViewAdapter;
    private MyGridViewAdapter mGridViewAdapter;
    private MyFragment mFragment;
    private RecyclerView mRecyclerView;

    public SearchTask(MyFragment mFragment, MyStaggeredViewAdapter mStaggeredViewAdapter, RecyclerView mRecyclerView) {
        this.mStaggeredViewAdapter = mStaggeredViewAdapter;
        this.mFragment = mFragment;
        this.mRecyclerView = mRecyclerView;
    }

    public SearchTask(MyGridViewAdapter mGridViewAdapter) {
        this.mGridViewAdapter = mGridViewAdapter;
    }

    public SearchTask(MyFragment mFragment, MyGridViewAdapter mGridViewAdapter, RecyclerView mRecyclerView) {
        this.mFragment = mFragment;
        this.mGridViewAdapter = mGridViewAdapter;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    protected List<Sheet> doInBackground(String... params) {

        try {
            Document document = Jsoup.connect(params[0]).get();

            OkHttpClient mOkHttpClient = new OkHttpClient();

            List<Sheet> sheets = new ArrayList<>();
            Elements elements = document.getElementsByAttributeValue("role", "article");
            Elements metaElements = null;
            String linkUrl, userName, postTime, viewNum;
            for (Element score : elements) {
                final String bookmark = score.getElementsByAttributeValue("rel", "bookmark").text().replace('/', '|');
                final String imgUrl = score.getElementsByTag("img").attr("srcset").split("\\?")[0].
                        replace("-w310-h434-", "-w400-h560-");
                linkUrl = "https://musescore.com" + score.getElementsByAttributeValue("rel", "bookmark").attr("href");
                userName = score.getElementsByClass("user name").text();
                metaElements = score.getElementsByClass("meta").get(0).getElementsByTag("span");
                postTime = metaElements.get(0).text();
                viewNum = metaElements.get(1).text();

                mFragment.addSheet(bookmark, userName, postTime, viewNum, linkUrl, imgUrl);

//                Sheet sheet = new Sheet(bookmark, userName, postTime, viewNum, link, imgUrl);
//                sheets.add(sheet);

/*
                Request request = new Request.Builder().url(imgUrl).build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + imgUrl);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            is = response.body().byteStream();

                            File file = new File(MainActivity.STORAGE, bookmark + "score.jpeg");

                            fos = new FileOutputStream(file);
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                            Log.d(TAG, "onResponse: success");
                        } catch (Exception e) {
                            Log.d(TAG, "onResponse: " + imgUrl);
                            e.printStackTrace();
                        } finally {
                            if (is != null)
                                is.close();
                            if (fos != null)
                                fos.close();
                        }
                    }
                });

*/
//                mStaggeredViewAdapter.mDatas.add(sheet);
//                mGridViewAdapter.mDatas.add(sheet);

            }
//            publishProgress(1);

//            Log.d(TAG, "doInBackground: " + document.toString());

//            mFragment.onRefresh();
            return sheets;
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: failed");
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<Sheet> sheets) {
//        mStaggeredViewAdapter.notifyDataSetChanged();
//        mGridViewAdapter.notifyDataSetChanged();
        mRecyclerView.requestLayout();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "onProgressUpdate: ");
//        mGridViewAdapter.notifyDataSetChanged();
    }
}
