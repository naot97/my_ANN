package com.example.sipln.movieapp.Views.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sipln.movieapp.R;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.List;

import com.example.sipln.movieapp.Models.Movie;

import com.example.sipln.movieapp.Presenters.RVMovieAdapter;


public class PopularFragment extends Fragment {

    public static final String TITLE = "Popular";
    public static List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RVMovieAdapter rvMovieAdapter;
    public MobileServiceTable<Movie> mMovieTable ;
    int userIndex;
    private SwipeRefreshLayout refreshLayout;


    public PopularFragment() {

    }
    public static PopularFragment newInstance(MobileServiceTable<Movie> mMovieTable, int userIndex) {
        PopularFragment fragment = new PopularFragment();
        fragment.userIndex = userIndex;
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

        try {
            movieList = Movie.popularListMovie;
            View rootHomeView = inflater.inflate(R.layout.fragment_popular, container, false);

            refreshLayout = rootHomeView.findViewById(R.id.refreshPopularMovies);
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



            recyclerView = (RecyclerView) rootHomeView.findViewById(R.id.recycler_view_popular);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            rvMovieAdapter = new RVMovieAdapter(movieList);
            rvMovieAdapter.userIndex = userIndex;
            recyclerView.setAdapter(rvMovieAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            return rootHomeView;

        }
        catch (Exception e){
            return  null;
        }
    }
    private void initialMovieData() {
        //toan
        try {
           /* mMovieTable.orderBy("numRate", QueryOrder.Descending).execute(new TableQueryCallback<Movie>() {
                @Override
                public void onCompleted(List<Movie> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                    //movieList = list;
                }
            });*/

        //toa

        /*    SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        movieList.add(
                new Movie(1,"BATTLEFIELD EARTH",
                        "After enslavement and near extermination by an alien race in the year 3000, humanity begins to fight back.",
                        R.drawable.battlefile_earth,"https://www.youtube.com/watch?v=XhNuXvlCTTc",2.5,699,sdf.parse("12 05 2000"), " Action-Adventure-SciFi","https://resizing.flixster.com/R-nH-aoasELb5wtJg4IyoY8FufM=/206x305/v1.bTsxMTIwNDU4MztqOzE3NzY5OzEyMDA7MTIwMDsxNjAw"));
        movieList.add(
                new Movie(2,"COLD MOUNTAIN",
                        "In the waning days of the American Civil War, a wounded soldier embarks on a perilous journey back home to Cold Mountain, NorthCarolina to reunite with his sweetheart.",
                        R.drawable.cold_mountain,"https://www.youtube.com/watch?v=uXGtunJ9Jqk",7.2,1274,sdf.parse("25 12 2003"),"Adventure-Drama-History-Romance-War","http://images.vkool.net/fposter/thumb/i66.tinypic.com/34dfyft.jpg") );
        movieList.add(
                new Movie(3,"Cats & Dogs",
                        "A look at the top-secret, high-tech espionage war going on between cats and dogs, of which their human owners are blissfullyunaware.",
                        R.drawable.cat_dog,"https://www.youtube.com/watch?v=dIVRE_0qduA",5.1,505, sdf.parse("04 07 2001")," Action-Comedy-Family-Fantasy","http://www.phimmoi.com/wp-content/uploads/2013/11/cuoc-chien-cho-meo-kitty-galore-bao-thu.jpg"));
        movieList.add(
                new Movie(4,"La vita è bella",
                        "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp.",
                        R.drawable.life_is_beautiful,"https://www.youtube.com/watch?v=4MpZyOdx4cs",8.6,5054,sdf.parse("12 02 1997")," Comedy-Drama-War","https://i.pinimg.com/originals/01/68/76/016876093150b8145575c60cd0c834f8.jpg"));
        movieList.add(
                new Movie(5,"Pirates of the Caribbean: Dead Man's Chest (2006)",
                        "Jack Sparrow races to recover the heart of Davy Jones to avoid enslaving his soul to Jones' service, as other friends and foes seek the heart for their own agenda as well.",
                        R.drawable.privates_caribbean,"https://www.youtube.com/watch?v=iPKbf7H8qC8",7.3,5833,sdf.parse("07 07 2006")," Action-Adventure-Fantasy","http://image.phimmoi.net/film/482/poster.medium.jpg"));
        //toan
        for(int i=0;i<=movieList.size();i++){
           mMovieTable.insert(movieList.get(i));
        }*/
        //toan
        }
        catch (Exception e){}

//        try {
//            mMovieTable.orderBy("numRate", QueryOrder.Descending).execute(new TableQueryCallback<Movie>() {
//                @Override
//                public void onCompleted(List<Movie> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
//                    movieList = list;
//                }
//            });
//        }
//        catch (Exception e){}

    }
}