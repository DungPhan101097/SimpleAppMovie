package com.example.lap10715.simpleappmovie.rest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.lap10715.simpleappmovie.uis.adapters.MyAdapter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class AdapterModule {
    private Context mContext;
    private RecyclerView mRecyclerView;

    public AdapterModule(Context mContext, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
    }

    @Provides
    @Named(value = "popular")
    public ApiLoader providePopularLoader(){
        return new PopularLoader();
    }

    @Provides
    @Named(value = "up_coming")
    public ApiLoader provideUpComingLoader(){
        return new UpComingLoader();
    }

    @Provides
    @Named(value = "now_playing")
    public ApiLoader provideNowPlayingLoader(){
        return new NowPlayingLoader();
    }

    @Provides
    @Named(value = "top_rated")
    public ApiLoader provideTopRatedLoader(){
        return new TopRatedLoader();
    }

    @Provides
    public MyAdapter provideAdapter(){
        return new MyAdapter(mContext, mRecyclerView);
    }

}
