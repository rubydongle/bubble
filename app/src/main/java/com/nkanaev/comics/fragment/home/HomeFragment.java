package com.nkanaev.comics.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nkanaev.comics.R;
import com.nkanaev.comics.fragment.home.inner.LocalComicsFragment;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    ViewPager mViewPager;
    TabLayout mTabLayout;
    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = root.findViewById(R.id.m_viewpager);
        mTabLayout = root.findViewById(R.id.tabs);
        mToolbar = root.findViewById(R.id.toolbar);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            mToolbar.setTitle(R.string.app_name);
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            final ActionBar toggle = appCompatActivity.getSupportActionBar();
            if (toggle != null) {
                toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white);
                toggle.setDisplayHomeAsUpEnabled(true);
            }
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(5);
    }

    private void setupViewPager(ViewPager viewPager) {
        HomePageAdapter homePageAdapter = new HomePageAdapter(getChildFragmentManager());
        homePageAdapter.addFragment(new LocalComicsFragment(), getString(R.string.tab_local_comics));
        homePageAdapter.addFragment(new LocalComicsFragment(), getString(R.string.tab_reading_comics));
        homePageAdapter.addFragment(new LocalComicsFragment(), getString(R.string.tab_unread_comics));
        homePageAdapter.addFragment(new LocalComicsFragment(), getString(R.string.tab_read_comics));
        homePageAdapter.addFragment(new LocalComicsFragment(), getString(R.string.tab_favourite_comics));
//        homePageAdapter.addFragment(new LocalGamesFragment(), getContext().getString(R.string.home_tab_local_games));
//        homePageAdapter.addFragment(new LatestPlayedGamesFragment(), getContext().getString(R.string.home_tab_latest_played_games));
//        homePageAdapter.addFragment(new MostPlayedGamesFragment(), getContext().getString(R.string.home_tab_most_played_games));

        viewPager.setAdapter(homePageAdapter);
    }

}