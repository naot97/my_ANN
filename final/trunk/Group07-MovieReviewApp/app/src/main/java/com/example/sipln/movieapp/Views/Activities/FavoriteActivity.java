package com.example.sipln.movieapp.Views.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sipln.movieapp.Models.Favorite;
import com.example.sipln.movieapp.Models.Movie;
import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.Presenters.RVMovieFavoriteAdapter;
import com.example.sipln.movieapp.R;
import com.facebook.login.LoginManager;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    public static List<Movie> movieList = new ArrayList<>();
    public static List<Favorite> favoriteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RVMovieFavoriteAdapter rvMovieFavoriteAdapter;
    private SwipeRefreshLayout refreshLayout;
    private MobileServiceClient mClient;
    public  MobileServiceTable<Favorite> mFavoriteTable;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);


        movieList = Favorite.movieList;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_favorite_activity);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rvMovieFavoriteAdapter = new RVMovieFavoriteAdapter(movieList);
        recyclerView.setAdapter(rvMovieFavoriteAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshLayout = findViewById(R.id.refreshFavoriteMovies);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cập nhật giao diện (sau khi remove favor ở đâu đó)
                // cập nhật movie lists
                try {
                    update();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });


    }
    void update() throws MalformedURLException {
            mClient = new MobileServiceClient("https://ttcnpm.azurewebsites.net", this);
            mFavoriteTable = mClient.getTable(Favorite.class);
            mFavoriteTable.where().field("userId").eq(Reviewer.loggingReviewer.getUerIndex()).execute(new TableQueryCallback<Favorite>() {
                @Override
                public void onCompleted(List<Favorite> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                    int k=0;
                    movieList.clear();
                    for (int j=0;j<list.size();j++){
                        Movie x = Movie.getMovieBy(list.get(j).getMovieId());
                        movieList.add(x);
                    }

                    movieList = Favorite.movieList;
                    rvMovieFavoriteAdapter.setMovies(movieList);
                    rvMovieFavoriteAdapter.notifyDataSetChanged();

                }
            });
        }


    }
