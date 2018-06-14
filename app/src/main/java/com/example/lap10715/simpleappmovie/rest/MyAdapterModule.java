package com.example.lap10715.simpleappmovie.rest;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.lap10715.simpleappmovie.model.Movie;
import com.example.lap10715.simpleappmovie.rest.ApiClient;
import com.example.lap10715.simpleappmovie.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MyAdapterModule {
    private Context context;

    public MyAdapterModule(Context context) {
        this.context = context;
    }

    @Provides
    public LinearLayoutManager provideLinearLayoutManager(){
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(){
        return ApiClient.getClient();
    }

    @Provides
    public List<Movie> provideList(){
        return new ArrayList<>();
    }

    @Provides
    public ApiInterface provideApi(Retrofit retrofit){
        return retrofit.create(ApiInterface.class);
    }
}
