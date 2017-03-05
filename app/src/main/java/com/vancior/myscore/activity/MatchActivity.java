package com.vancior.myscore.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.github.barteksc.pdfviewer.PDFView;
import com.vancior.myscore.R;
import com.vancior.myscore.web.LoadTask;

/**
 * Created by H on 2017/3/4.
 */

public class MatchActivity extends AppCompatActivity {

    private static final String TAG = "MatchActivity";

    private PDFView mPDFView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_match);

        mPDFView = (PDFView) findViewById(R.id.id_pdfview);
//        mPDFView.fromAsset("MySecretBase.pdf");

        ProgressDialog progressDialog = new ProgressDialog(MatchActivity.this);
        progressDialog.setTitle("Fetching files");
        progressDialog.setMessage("Fetching...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        LoadTask loadTask = new LoadTask(progressDialog, mPDFView, (DownloadManager) getSystemService(DOWNLOAD_SERVICE), MatchActivity.this);
        loadTask.execute(getIntent().getStringExtra("linkUrl"));

    }
}
