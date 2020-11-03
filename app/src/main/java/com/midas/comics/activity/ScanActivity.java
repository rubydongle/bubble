package com.midas.comics.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.midas.comics.Constants;
import com.midas.comics.MainApplication;
import com.midas.comics.R;
import com.midas.comics.managers.Scanner;
import com.midas.comics.view.DirectorySelectDialog;
import com.midas.comics.view.RadarView;

import java.io.File;


public class ScanActivity extends AppCompatActivity implements DirectorySelectDialog.OnDirectorySelectListener {

    Button scanButton;
    RadarView radarView;

    Handler mUpdateHandler;

    private DirectorySelectDialog mDirectorySelectDialog;

    private void startScanGames() {
        Scanner.getInstance().scanLibrary();
//        RomScanner2 romScanner = new RomScanner2(Constants.getFCGameExtensions(), Constants.getArchiveExtensions(), this, this, true, null);
//        romScanner.start();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_comics);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        radarView = findViewById(R.id.radar_view);
//        radarView.setCircleColor(new int[]{R.color.app_blue, R.color.app_green, R.color.app_green_dark});
//        radarView.setCircleColor(new int[]{R.color.app_blue, R.color.app_red, R.color.app_green});//R.color.app_blue, R.color.app_green, R.color.app_green_dark});
//        radarView.setScanColor(new int[]{R.color.app_blue_dark, R.color.app_red_dark, R.color.app_green_dark});// radarView.getCircleColor()});//R.color.app_blue, R.color.app_blue_dark});
        scanButton = findViewById(R.id.start_scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanGames();
                radarView.start();
            }
        });

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constants.MESSAGE_MEDIA_UPDATE_FINISHED:
                        radarView.stop();
                        break;
                    case Constants.MESSAGE_MEDIA_UPDATED:
                        //show find message
                        break;
                }
            }
        };
        Scanner.getInstance().addUpdateHandler(mUpdateHandler);

        mDirectorySelectDialog = new DirectorySelectDialog(this);
        mDirectorySelectDialog.setCurrentDirectory(Environment.getExternalStorageDirectory());
        mDirectorySelectDialog.setOnDirectorySelectListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Scanner.getInstance().removeUpdateHandler(mUpdateHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_select_scan_folder:
                mDirectorySelectDialog.show();
//                ToastUtils.showShort("select scan folder");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDirectorySelect(File file) {
        SharedPreferences preferences = MainApplication.getPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.SETTINGS_LIBRARY_DIR, file.getAbsolutePath());
        editor.apply();

        Scanner.getInstance().forceScanLibrary();
//        showEmptyMessage(false);
//        setLoading(true);
    }

}
