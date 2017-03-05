package com.vancior.myscore.web;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.vancior.myscore.activity.MainActivity;
import com.vancior.myscore.activity.MatchActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by H on 2017/3/4.
 */

public class LoadTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "LoadTask";

    private ProgressDialog mProgressDialog;
    private PDFView mPDFView;
    private DownloadManager mDownloadManager;
    private Context mContext;
    private File mPDFFile;

    public LoadTask(ProgressDialog mProgressDialog, PDFView mPDFView, DownloadManager mDownloadManager, Context mContext) {
        this.mProgressDialog = mProgressDialog;
        this.mPDFView = mPDFView;
        this.mDownloadManager = mDownloadManager;
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            final String linkUrl = strings[0];
            final String fileName = linkUrl.replace('/', '|');

            Log.d(TAG, "doInBackground: " + linkUrl);
            Document document = Jsoup.connect(linkUrl).get();
//            Log.d(TAG, "doInBackground: " + document.getElementsByTag("script").get(3));
//            Log.d(TAG, "doInBackground: " + document.getElementsByAttributeValue("type", "application/json").html());
            JSONObject jsonObject = new JSONObject(document.getElementsByAttributeValue("type", "application/json").html());
            JSONObject infoObject = jsonObject.getJSONObject("score_player").getJSONObject("json");
            String secret = infoObject.getString("secret");
            String id = infoObject.getString("id");
            Log.d(TAG, "doInBackground: " + secret + " " + id);
            String zipUrl = "http://static.musescore.com/" + id + "/" + secret + "/score.mxl";
            String pdfUrl = "http://static.musescore.com/" + id + "/" + secret + "/score.pdf";

//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
//            request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOCUMENTS, fileName + ".pdf");
//            long downloadId = mDownloadManager.enqueue(request);

//            File file = new File()
//            mPDFView.fromFile()

            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder().url(pdfUrl).build();
            Response response = okHttpClient.newCall(request).execute();
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len;
            FileOutputStream fos = null;
            try {
                is = response.body().byteStream();

                File file = new File(MainActivity.STORAGE, fileName + ".pdf");

                if (!file.exists()) {
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                }
                Log.d(TAG, "onResponse: pdf success");
                mPDFFile = file;

            } catch (Exception e) {
                Log.d(TAG, "onResponse: pdf failure");
                e.printStackTrace();
            } finally {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            }

//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.d(TAG, "onFailure: pdf fail");
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    InputStream is = null;
//                    byte[] buf = new byte[2048];
//                    int len;
//                    FileOutputStream fos = null;
//                    try {
//                        is = response.body().byteStream();
//
//                        File file = new File(MainActivity.STORAGE, fileName + ".pdf");
//
//                        if (!file.exists()) {
//                            fos = new FileOutputStream(file);
//                            while ((len = is.read(buf)) != -1) {
//                                fos.write(buf, 0, len);
//                            }
//                            fos.flush();
//                        }
//                        Log.d(TAG, "onResponse: pdf success");
//
//                        mPDFView.fromFile(file)
//                                .enableSwipe(false)
//                                .swipeHorizontal(true)
//                                .load();
//                        mPDFView.loadPages();
//                    } catch (Exception e) {
//                        Log.d(TAG, "onResponse: pdf failure");
//                        e.printStackTrace();
//                    } finally {
//                        if (is != null)
//                            is.close();
//                        if (fos != null)
//                            fos.close();
//                    }
//                }
//            });

            request = new Request.Builder().url(zipUrl).build();

            is = null;
            buf = new byte[2048];
            fos = null;
            try {
                is = response.body().byteStream();

                File file = new File(MainActivity.STORAGE, fileName + ".zip");

                if (!file.exists()) {
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                }
                Log.d(TAG, "onResponse: zip success");

                unZipFile(file, fileName);
                file.delete();

            } catch (Exception e) {
                Log.d(TAG, "onResponse: zip failure");
                e.printStackTrace();
            } finally {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        mPDFView.fromFile(mPDFFile)
                .enableSwipe(false)
                .swipeHorizontal(true)
                .load();
        mProgressDialog.dismiss();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: download success");
        }
    };

    private void unZipFile(File file, String fileName) {

        try {
            ZipFile zipFile = new ZipFile(file);
            File outFile = null;
            ZipInputStream zipIs = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = null;
            InputStream is = null;
            OutputStream os = null;
            while ((entry = zipIs.getNextEntry()) != null) {
                Log.d(TAG, "unZipFile: " + entry.getName());
                if (!entry.getName().contains("META_INF")) {
                    outFile = new File(MainActivity.STORAGE, fileName + ".xml");
                    if (!outFile.getParentFile().exists()) {
                        outFile.getParentFile().mkdir();
                    }
                    if (!outFile.exists()) {
                        outFile.createNewFile();
                    }
                    is = zipFile.getInputStream(entry);
                    os = new FileOutputStream(outFile);
                    byte[] buf = new byte[2048];
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                }
            }
            if (is != null)
                is.close();
            if (os != null)
                os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
