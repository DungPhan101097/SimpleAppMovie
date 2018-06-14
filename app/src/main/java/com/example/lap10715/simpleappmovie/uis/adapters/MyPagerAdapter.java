package com.example.lap10715.simpleappmovie.uis.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lap10715.simpleappmovie.uis.fragments.NowPlayingFragment;
import com.example.lap10715.simpleappmovie.uis.fragments.PopularFragment;
import com.example.lap10715.simpleappmovie.uis.fragments.TopRatedFragment;
import com.example.lap10715.simpleappmovie.uis.fragments.UpComingFragment;

import static com.example.lap10715.simpleappmovie.constant.MyConstant.NUMBER_PAGER;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NowPlayingFragment();
            case 1:
                return new TopRatedFragment();
            case 2:
                return new PopularFragment();
            case 3:
                return new UpComingFragment();
            default:
                return new NowPlayingFragment();
        }
    }

    @Override
    public int getCount() {
        return NUMBER_PAGER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Now Playing";
            case 1:
                return "Top Rated";
            case 2:
                return "Popular";
            case 3:
                return "UpComing";
            default:
                return "Now Playing";
        }
    }
}
