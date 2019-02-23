package com.example.sipln.movieapp.Views.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sipln.movieapp.Models.Movie;
import com.example.sipln.movieapp.Presenters.RVMovieAdapter;
import com.example.sipln.movieapp.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;

import java.util.ArrayList;
import java.util.List;


public class NowFragment extends Fragment {

    public static final String TITLE = "Now";
    public static List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RVMovieAdapter rvMovieAdapter;
    private SwipeRefreshLayout refreshLayout;
    public MobileServiceTable<Movie> mMovieTable ;
    private LinearLayoutManager layoutManager1;


    // load more:
    private final int THREAT_SHOT = 4;
    private boolean isLoading = false;
    public int countMoviesCurrent = 6;
    public int maxCountMovies;

    public NowFragment() {

    }

    public static NowFragment newInstance(MobileServiceTable<Movie> mMovieTable) {
        NowFragment fragment = new NowFragment();
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


        movieList = Movie.nowListMovie;

//        for (int i =0;i<6;i++){
//            movieList.add(Movie.nowListMovie.get(i));
//        }
//        maxCountMovies = Movie.nowListMovie.size();


        View rootHomeView = inflater.inflate(R.layout.fragment_now, container, false);
        refreshLayout = rootHomeView.findViewById(R.id.refreshNowMovies);


        // refresh thôi:
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });



        recyclerView = (RecyclerView) rootHomeView.findViewById(R.id.recycler_view_now);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        //layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        // số movies cần load
        int curNumMovies = RVMovieAdapter.numCurrentMovies;
        // set scroll event:
//        layoutManager1 = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//
//                if (!isLoading && layoutManager1.getItemCount()-1 == layoutManager1.findLastVisibleItemPosition()){
//                    countMoviesCurrent = layoutManager1.getItemCount();
//                    loadMoreData();
//                }
//
//            }
//        });

        rvMovieAdapter = new RVMovieAdapter(movieList);
        recyclerView.setAdapter(rvMovieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootHomeView;


    }

    // init loadmore function:
//    public void loadMoreData(){
//        int temp = 4;
//        if(maxCountMovies-countMoviesCurrent<4){
//            temp=maxCountMovies-countMoviesCurrent;
//        }
//
//        for (int i=0;i<temp;i++){
//            movieList.add(Movie.movieList.get(i+countMoviesCurrent));
//        }
//
//        //int a = layoutManager1.getItemCount();
//        // set lại listData cho recylerAdapter.
//
//        try {
//            Thread.sleep(3000);
//            rvMovieAdapter.setMovies(movieList);
//            rvMovieAdapter.notifyDataSetChanged();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//    }

}