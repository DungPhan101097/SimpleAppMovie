package com.example.lap10715.simpleappmovie.rest;

import com.example.lap10715.simpleappmovie.model.MoviesResponse;

import retrofit2.Call;

public interface ApiLoader {
    Call<MoviesResponse> load(ApiInterface api, int page);
}
