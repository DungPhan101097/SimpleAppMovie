package com.example.lap10715.simpleappmovie.uis.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lap10715.simpleappmovie.R;
import com.example.lap10715.simpleappmovie.rest.AdapterModule;
import com.example.lap10715.simpleappmovie.rest.ApiLoader;
import com.example.lap10715.simpleappmovie.rest.DaggerFragmentComponent;
import com.example.lap10715.simpleappmovie.rest.FragmentComponent;
import com.example.lap10715.simpleappmovie.rest.MessageEvent;
import com.example.lap10715.simpleappmovie.uis.adapters.MyAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;

public class PopularFragment extends Fragment {
    @Inject
    MyAdapter mAdapter;
    @Inject
    @Named(value = "now_playing")
    ApiLoader mPopularLoader;

    public PopularFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_popular);

        FragmentComponent component = DaggerFragmentComponent
                .builder()
                .adapterModule(new AdapterModule(getContext(), recyclerView))
                .build();

        component.inject(this);
        mAdapter.setLoader(mPopularLoader);
        recyclerView.setAdapter(mAdapter);
        mAdapter.loadData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void sendAdapter(Integer type){
        if(type == 2){
            MessageEvent event = new MessageEvent(mAdapter);
            EventBus.getDefault().post(event);
        }
    }
}
