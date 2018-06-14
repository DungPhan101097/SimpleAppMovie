package com.example.lap10715.simpleappmovie.rest;

import com.example.lap10715.simpleappmovie.constant.MyConstant;
import com.example.lap10715.simpleappmovie.model.MoviesResponse;

import retrofit2.Call;

public class NowPlayingLoader implements ApiLoader {
    @Override
    public Call<MoviesResponse> load(ApiInterface api, int page) {
        return api.getNowPlayingMovies(page, MyConstant.API_KEY);
    }
}
