package com.nkanaev.comics.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.nkanaev.comics.R;
import com.nkanaev.comics.fragment.AboutFragment;
import com.nkanaev.comics.fragment.BrowserFragment;
import com.nkanaev.comics.fragment.HeaderFragment;
import com.nkanaev.comics.fragment.LibraryFragment;
import com.nkanaev.comics.managers.LocalCoverHandler;
import com.nkanaev.comics.managers.Scanner;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, NavigationView.OnNavigationItemSelectedListener {
    private final static String STATE_CURRENT_MENU_ITEM = "STATE_CURRENT_MENU_ITEM";
    private static final String TAG = "ruby";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentNavItem;
    private Picasso mPicasso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        isStoragePermissionGranted();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

//        if (Utils.isLollipopOrLater()) {
//            toolbar.setElevation(8);
//        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPicasso = new Picasso.Builder(this)
                .addRequestHandler(new LocalCoverHandler(this))
                .build();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout,
//                R.string.drawer_open, R.string.drawer_close);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        Scanner.getInstance().scanLibrary();

        if (savedInstanceState == null) {
            setFragment(new LibraryFragment());
//            setNavBar(navigationView);
            mCurrentNavItem = R.id.drawer_menu_library;
        } else {
            onBackStackChanged();  // force-call method to ensure indicator is shown properly
            mCurrentNavItem = savedInstanceState.getInt(STATE_CURRENT_MENU_ITEM);
        }
//        navigationView.getMenu().findItem(mCurrentNavItem).setChecked(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_MENU_ITEM, mCurrentNavItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    private void setNavBar(NavigationView view) {
//    private void setNavBar() {
//        view.findViewById()
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.header, new HeaderFragment())
                .commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void pushFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean popFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }


    @Override
    public void onBackStackChanged() {
//        mDrawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
    }

//    @Override
//    public void onBackPressed() {
//        if (!popFragment()) {
//            finish();
//        }
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        if (!popFragment()) {
//            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
//                mDrawerLayout.closeDrawers();
//            else
//                mDrawerLayout.openDrawer(GravityCompat.START);
//        }
//        return super.onSupportNavigateUp();
//    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (mCurrentNavItem == menuItem.getItemId()) {
            mDrawerLayout.closeDrawers();
            return true;
        }

        switch (menuItem.getItemId()) {
            case R.id.drawer_menu_library:
                setFragment(new LibraryFragment());
                mCurrentNavItem = menuItem.getItemId();
//                menuItem.setChecked(true);
                break;
            case R.id.drawer_menu_browser:
                setFragment(new BrowserFragment());
                mCurrentNavItem = menuItem.getItemId();
//                menuItem.setChecked(true);
                break;
            case R.id.drawer_menu_about:
                startActivity(new Intent(this, AboutActivity.class));
//                setTitle(R.string.menu_about);
//                setFragment(new AboutFragment());
                break;
        }

        mDrawerLayout.closeDrawers();
        return true;
    }
}
