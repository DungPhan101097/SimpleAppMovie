package com.example.lap10715.simpleappmovie.uis.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lap10715.simpleappmovie.GlideApp;
import com.example.lap10715.simpleappmovie.R;
import com.example.lap10715.simpleappmovie.constant.MyConstant;
import com.example.lap10715.simpleappmovie.model.Movie;
import com.example.lap10715.simpleappmovie.model.MoviesResponse;
import com.example.lap10715.simpleappmovie.rest.ApiInterface;
import com.example.lap10715.simpleappmovie.rest.ApiLoader;
import com.example.lap10715.simpleappmovie.rest.Component;
import com.example.lap10715.simpleappmovie.rest.DaggerComponent;
import com.example.lap10715.simpleappmovie.rest.MyAdapterModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{
    private final int LOADING = 1001;
    private final int CARD = 1002;
    private final int MAXIMUM = 10;

    private Context mContext;

    @Inject
    List<Movie> mMovies;
    @Inject
    List<Movie> mMoviesFilter;
    @Inject
    LinearLayoutManager mLayoutManager;
    private boolean mIsLoading = false;
    private ApiLoader mApiLoader;
    @Inject
    ApiInterface mApiInterface;
    private int mPage = 1;
    private int mMaxpage = 1;
    private int mThreshold = 0;


    public MyAdapter(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;
        Component component = DaggerComponent
                .builder()
                .myAdapterModule(new MyAdapterModule(mContext))
                .build();

        component.inject(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // Tinh so item trong List
                int totalItems = mThreshold;
                // Tinh item cuoi cung nhin thay
                int lastVisiableItem = mLayoutManager.findLastVisibleItemPosition();

                // Neu con mot item o coi thi tien hanh load.
                if(totalItems > 0 && !mIsLoading && totalItems <=lastVisiableItem + 1){
                    // Load them data bang cach tang so trang len.
                    if(mMoviesFilter  == mMovies && mThreshold == mMovies.size() ){
                        mPage++;
                        if(mMaxpage == mPage){
                            mMoviesFilter.add(null);
                            notifyItemInserted(mThreshold);
                        }
                        loadData();
                    }
                    else{
                        int oldThres = mThreshold;
                        mThreshold = (mMoviesFilter.size() - mThreshold) > MAXIMUM?
                                mThreshold + MAXIMUM : mMoviesFilter.size();
                        notifyItemRangeInserted(oldThres, mThreshold - oldThres);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mMoviesFilter.add(null);
        mThreshold = 1;
    }


    @Override
    public int getItemViewType(int position) {
        if(mMoviesFilter.get(position) == null){
            return LOADING;
        }
        return CARD;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == LOADING){
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.loading_layout,parent, false);
            return new LoadingHolder(view);
        }
        else{
            CardView cardView = (CardView) LayoutInflater.from(mContext)
                    .inflate(R.layout.movie_item_card_view, parent, false);
            return new MyHolder(cardView);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = mMoviesFilter.get(position);
        if(holder instanceof MyHolder){
            MyHolder myHolder = (MyHolder)holder;
            myHolder.movieTitle.setText(movie.getTitle());
            myHolder.release.setText(movie.getReleaseDate());
            myHolder.rate.setText("Rated: "  + movie.getVoteAverage());

            String overview = movie.getOverview();
            if(!overview.isEmpty()){
                if(overview.length() > 100){
                    overview = overview.substring(0, 100) + " ...";
                }
                myHolder.overview.setText(overview);
            }

            String imagePath = MyConstant.IMAGE_PATH + movie.getPosterPath();
            GlideApp.with(mContext)
                    .load(Uri.parse(imagePath))
                    .placeholder(R.drawable.placeholder)
                    .into(myHolder.poster);

            // Set click lister for each item.

        }
    }

    @Override
    public int getItemCount() {
        return mThreshold;
    }

    public void setLoader(ApiLoader loader){
        this.mApiLoader = loader;
    }

    public void loadData(){
        // dang load data
        if(!mIsLoading && mPage <= mMaxpage){
            mIsLoading = true;
            Call<MoviesResponse> call = mApiLoader.load(mApiInterface, mPage);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movieList = response.body().getResults();
                    mMaxpage  = response.body().getTotalPages();
                    if(mMovies == null || mMovies.size() == 0){
                        // Load moi
                        mMovies = movieList;
                        mMoviesFilter = mMovies;
                        mThreshold = mMoviesFilter.size() > MAXIMUM ?
                                MAXIMUM : mMoviesFilter.size();
                        notifyDataSetChanged();
                    }
                    else{
                        // Load them data
                        int size = mMoviesFilter.size()  -1;
                        mMoviesFilter.remove(size);
                        notifyItemRemoved(size);

                        mMovies.addAll(movieList);
                        mMoviesFilter = mMovies;
                        int oldThres = mThreshold;
                        mThreshold = movieList.size() >  MAXIMUM?
                                mThreshold + MAXIMUM : mThreshold + movieList.size();
                        notifyItemRangeInserted(oldThres, mThreshold - oldThres);
                    }
                    mIsLoading = false;
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    mIsLoading = false;
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                List<Movie> queryList ;
                if(!query.isEmpty()){
                    queryList  = mMovies;
                }
                else {
                    queryList = new ArrayList<>();
                    for(Movie movie : mMovies){
                        if(movie.getTitle().toLowerCase().contains(query)){
                            queryList.add(movie);
                        }
                    }
                }
                FilterResults filterResults  = new FilterResults();
                filterResults.values = queryList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mMoviesFilter = (List<Movie>) results.values;
                mThreshold = mMoviesFilter.size() > MAXIMUM ?
                        MAXIMUM: mMoviesFilter.size();
                notifyDataSetChanged();
            }
        };

    }

    class LoadingHolder extends  RecyclerView.ViewHolder{

        public LoadingHolder(View itemView) {
            super(itemView);
            ProgressBar pbLoading = itemView.findViewById(R.id.pb_loading);
            pbLoading.setIndeterminate(true);
        }
    }

    class MyHolder extends  RecyclerView.ViewHolder{

        public ImageView poster;
        public TextView release;
        public TextView movieTitle;
        public TextView overview;
        public CardView card;
        public TextView rate;

        public MyHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView;
            poster = itemView.findViewById(R.id.iv_poster);
            release = itemView.findViewById(R.id.tv_release);
            movieTitle = itemView.findViewById(R.id.tv_title);
            overview = itemView.findViewById(R.id.tv_overview);
            rate = itemView.findViewById(R.id.tv_rate);

        }
    }
}
