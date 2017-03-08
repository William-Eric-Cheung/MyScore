package com.vancior.myscore.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.vancior.myscore.MyApplication;
import com.vancior.myscore.R;
import com.vancior.myscore.audio.MatchThread;
import com.vancior.myscore.audio.bean.Chord;
import com.vancior.myscore.audio.util.NoteParser;
import com.vancior.myscore.web.LoadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by H on 2017/3/4.
 */

public class MatchActivity extends AppCompatActivity {

    private static final String TAG = "MatchActivity";

    private PDFView mPDFView;
    private String mLinkUrl;
    private String mFileName;
    private List<Chord> mChordList;
    private MatchThread mMatchThread;
    private Handler mHandler;
    private int mCurrentPage;
    private int mMaxPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_match);

        mLinkUrl = getIntent().getStringExtra("linkUrl");
        mFileName = mLinkUrl.replace('/', '|');
        mCurrentPage = 0;

        mPDFView = (PDFView) findViewById(R.id.id_pdfview);

        this.registerForContextMenu(mPDFView);

        mPDFView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 0, 0, "Add to Favorite");
            }
        });

        ProgressDialog progressDialog = new ProgressDialog(MatchActivity.this);
        progressDialog.setTitle("Fetching files");
        progressDialog.setMessage("Fetching...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        LoadTask loadTask = new LoadTask(progressDialog, mPDFView, this);
        loadTask.execute(mLinkUrl);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mPDFView.jumpTo(mPDFView.getCurrentPage()+1);
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMatchThread != null)
            mMatchThread.setRunning(false);
    }

    public void parseSheet() {
        try {
            File file = new File(MainActivity.CACHESTORAGE, mFileName + ".xml");
//            File file = new File(MainActivity.STORAGE, "fortest.xml");
            InputStream is = new FileInputStream(file);
            NoteParser noteParser = new NoteParser();
            mChordList = noteParser.parse(is);
            for (Chord i : mChordList) {
                Log.d(TAG, "parseSheet: " + i.toString());
            }
            mMatchThread = new MatchThread(mHandler, mChordList);
            mMatchThread.setSensitivity(10.0f);
            mMatchThread.setRunning(true);
            mMatchThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMaxPage(int maxPage) {
        this.mMaxPage = maxPage;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected: ");
        Toast.makeText(MyApplication.getContext(), "Save", Toast.LENGTH_SHORT);
        return super.onContextItemSelected(item);
    }
}
