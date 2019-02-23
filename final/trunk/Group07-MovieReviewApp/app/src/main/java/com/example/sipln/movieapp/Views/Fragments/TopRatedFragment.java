package com.example.sipln.movieapp.Views.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sipln.movieapp.R;
import com.example.sipln.movieapp.Presenters.RVMovieAdapter;
import com.example.sipln.movieapp.Models.Movie;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import android.support.v7.widget.DefaultItemAnimator;


public class TopRatedFragment extends Fragment {

    public static final String TITLE = "Top Rated";

    public static List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RVMovieAdapter rvMovieAdapter;
    private SwipeRefreshLayout refreshLayout;

    public MobileServiceTable<Movie> mMovieTable ;

    public TopRatedFragment(){

    }

        public static TopRatedFragment newInstance(MobileServiceTable<Movie> mMovieTable) {
        TopRatedFragment fragment = new TopRatedFragment();
        fragment.mMovieTable = mMovieTable;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialMovieData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        movieList = Movie.topRatedListMovie;
        View rootHomeView = inflater.inflate(R.layout.fragment_toprated, container, false);

        refreshLayout = rootHomeView.findViewById(R.id.refreshTopRatedMovies);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do something


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },2000);


            }
        });


        recyclerView = (RecyclerView) rootHomeView.findViewById(R.id.recycler_view_toprated);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        rvMovieAdapter = new RVMovieAdapter(movieList);
        recyclerView.setAdapter(rvMovieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootHomeView;
    }

    private void initialMovieData() {
//
//        try {
//
//            mMovieTable.orderBy("rate", QueryOrder.Descending).execute(new com.microsoft.windowsazure.mobileservices.table.TableQueryCallback<Movie>() {
//                @Override
//                public void onCompleted(List<Movie> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
//                    movieList = list;
//                }
//            });
//
//
//        }
//        catch (Exception e){}
    }
}