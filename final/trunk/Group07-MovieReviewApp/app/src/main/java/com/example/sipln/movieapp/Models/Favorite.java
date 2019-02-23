package com.example.sipln.movieapp.Models;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

public class Favorite {

    private String id;
    private int favoriteId;

    public int getMovieId() {
        return movieId;
    }

    private int movieId;
    private int userId;

    public Favorite(int favoriteId, int movieId, int userId){
        this.favoriteId = favoriteId;
        this.movieId = movieId;
        this.userId = userId;
    }


    public static MobileServiceTable<Favorite> mFavoriteTable;
    public static List<Movie> movieList = new ArrayList<>();
    public static List<Favorite> favoriteList = new ArrayList<>();

    public static void getFavoriteTable(MobileServiceClient mClient) throws MobileServiceException {


        mFavoriteTable = mClient.getTable(Favorite.class);
        mFavoriteTable.where().field("userId").eq(Reviewer.loggingReviewer.getUerIndex()).execute(new TableQueryCallback<Favorite>() {
            @Override
            public void onCompleted(List<Favorite> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                int k=0;
                favoriteList = list;
                for (int j=0;j<list.size();j++){
                    Movie x = Movie.getMovieBy(list.get(j).getMovieId());
                    movieList.add(x);
                }


            }
        });
    }



}
