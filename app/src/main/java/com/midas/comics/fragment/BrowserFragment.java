package com.midas.comics.fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.midas.comics.R;
import com.midas.comics.activity.ReaderActivity;
import com.midas.comics.managers.Utils;
import com.midas.comics.parsers.Parser;
import com.midas.comics.parsers.ParserFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class BrowserFragment extends Fragment
        implements AdapterView.OnItemClickListener {
    private final static String STATE_CURRENT_DIR = "stateCurrentDir";

    private ListView mListView;
    private File mCurrentDir;
    private File mRootDir = new File("/");
    private File[] mSubdirs = new File[]{};
    private TextView mDirTextView;

    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentDir = (File) savedInstanceState.getSerializable(STATE_CURRENT_DIR);
        }
        else {
            mCurrentDir = Environment.getExternalStorageDirectory();
        }

//        getActivity().setTitle(R.string.menu_browser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_browser, container, false);

        mToolbar = root.findViewById(R.id.toolbar);
//        ViewGroup toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
//        ViewGroup breadcrumbLayout = (ViewGroup) inflater.inflate(R.layout.breadcrumb, toolbar, false);
//        toolbar.addView(breadcrumbLayout);
//        mDirTextView = (TextView) breadcrumbLayout.findViewById(R.id.dir_textview);
        mDirTextView = (TextView) root.findViewById(R.id.dir_textview);

        setCurrentDir(mCurrentDir);

        mListView = (ListView) root.findViewById(R.id.listview_browser);
        mListView.setAdapter(new DirectoryAdapter());
        mListView.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            mToolbar.setTitle(R.string.menu_browser);
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            final ActionBar toggle = appCompatActivity.getSupportActionBar();
            if (toggle != null) {
                toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white);
                toggle.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_CURRENT_DIR, mCurrentDir);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
//        ViewGroup toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
//        ViewGroup breadcrumb = (ViewGroup) mToolbar.findViewById(R.id.breadcrumb_layout);
//        mToolbar.removeView(breadcrumb);
        super.onDestroyView();
    }

    private void setCurrentDir(File dir) {

        Log.d("ruby", "set current dir:"+ dir.getAbsolutePath());
        mCurrentDir = dir;
        mCurrentDir = new File(String.valueOf(dir.getAbsoluteFile()));
        for(File file : mCurrentDir.listFiles()) {
            Log.d("ruby", "file:"+ file.getName());

        }
        ArrayList<File> subdirs = new ArrayList<>();
        if (!mCurrentDir.getAbsolutePath().equals(mRootDir.getAbsolutePath())) {
            subdirs.add(mCurrentDir.getParentFile());
        }
        File[] files = mCurrentDir.listFiles();
        for(File file : files) {
            Log.d("ruby", "file:"+ file.getName());

        }
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() || Utils.isArchive(f.getName())) {
                    subdirs.add(f);
                }
            }
        }
        Collections.sort(subdirs);
        mSubdirs = subdirs.toArray(new File[subdirs.size()]);

        if (mListView != null) {
            mListView.invalidateViews();
        }

        mDirTextView.setText(dir.getAbsolutePath());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = mSubdirs[position];
        if (file.isDirectory()) {
            // check if directory is folder-based comic
            Parser p = ParserFactory.create(file);
            if (p == null) {
                setCurrentDir(file);
                return;
            }
        }

        Intent intent = new Intent(getActivity(), ReaderActivity.class);
        intent.putExtra(ReaderFragment.PARAM_HANDLER, file);
        intent.putExtra(ReaderFragment.PARAM_MODE, ReaderFragment.Mode.MODE_BROWSER);
        startActivity(intent);
    }

    private void setIcon(View convertView, File file) {
        ImageView view = (ImageView) convertView.findViewById(R.id.directory_row_icon);
        int colorRes = R.color.circle_grey;
        if (file.isDirectory()) {
            view.setImageResource(R.drawable.ic_folder_white_24dp);
        }
        else {
            view.setImageResource(R.drawable.ic_file_document_box_white_24dp);

            String name = file.getName();
            if (Utils.isZip(name)) {
                colorRes = R.color.circle_green;
            }
            else if (Utils.isRar(name)) {
                colorRes = R.color.circle_red;
            }
        }

        GradientDrawable shape = (GradientDrawable) view.getBackground();
        shape.setColor(getResources().getColor(colorRes));
    }

    private final class DirectoryAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSubdirs.length;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mSubdirs[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_directory, parent, false);
            }

            File file = mSubdirs[position];
            TextView textView = (TextView) convertView.findViewById(R.id.directory_row_text);

            if (position == 0 && !mCurrentDir.getAbsolutePath().equals(mRootDir.getAbsolutePath())) {
                textView.setText("..");
            }
            else {
                textView.setText(file.getName());
            }

            setIcon(convertView, file);

            return convertView;
        }
    }
}