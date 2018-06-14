package com.example.lap10715.simpleappmovie.rest;

import com.example.lap10715.simpleappmovie.uis.adapters.MyAdapter;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {MyAdapterModule.class})
public interface Component {
    void inject(MyAdapter adapter);

}
