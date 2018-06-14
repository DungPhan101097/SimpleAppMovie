package com.example.lap10715.simpleappmovie.rest;

import com.example.lap10715.simpleappmovie.uis.adapters.MyAdapter;

public class MessageEvent {
    private MyAdapter mAdapter;

    public MessageEvent(MyAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public MyAdapter getmAdapter() {
        return mAdapter;
    }
}
