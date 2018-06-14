package com.example.lap10715.simpleappmovie.rest;

import com.example.lap10715.simpleappmovie.uis.fragments.NowPlayingFragment;
import com.example.lap10715.simpleappmovie.uis.fragments.PopularFragment;
import com.example.lap10715.simpleappmovie.uis.fragments.TopRatedFragment;
import com.example.lap10715.simpleappmovie.uis.fragments.UpComingFragment;

import dagger.Component;

@Component(modules = {AdapterModule.class})
public interface FragmentComponent {

    void inject(NowPlayingFragment fragment);
    void inject(UpComingFragment fragment);
    void inject(TopRatedFragment fragment);
    void inject(PopularFragment fragment);
}
