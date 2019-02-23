package com.example.sipln.movieapp.Models;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Movie {

    private String id;
    private int movieId;
    private String name;
    private String desc;
    private int numRate;
    private double rate;
    private Date date;
    private String type;
    private String linkImage;
    private String linkTrailer;

    // get,set:
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public Date getDate() {
        return date;
    }
    public int getNumRate() {
        return numRate;
    }
    public void setNumRate(int numRate) {
        this.numRate = numRate;
    }
    public int getMovieId() {
        return movieId;
    }
    public String getLinkImage() {
        return linkImage;
    }
    public String getLinkTrailer() { return linkTrailer;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    // constructor
    public Movie(){ }
    public Movie(int movieId,String name, String desc, int photoId,
                 String linkTrailer, double rate, int numRate, Date date, String type, String linkImage) {
        this.name = name;
        this.movieId =  movieId;
        this.desc = desc;
        this.linkTrailer = linkTrailer;
        this.rate = rate;
        this.numRate = numRate;
        this.date = date;
        this.type = type;
        this.linkImage = linkImage;
    }



    // normal,popular,toprated,now:
    public static List<Movie> movieList ;
    public static List<Movie> popularListMovie = new ArrayList<>();
    public static List<Movie> topRatedListMovie = new ArrayList<>();
    public  static List<Movie> nowListMovie = new ArrayList<>();


    public static MobileServiceTable<Movie> mMovieTable;
    public static void reNew(){
        topRatedListMovie = new ArrayList<>(movieList);
        popularListMovie = new ArrayList<>(movieList);
        nowListMovie = new ArrayList<>(movieList);

        Collections.sort(nowListMovie, new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (o2.date.after(o1.date)) return 1;
                else return -1;
            }
        });

        Collections.sort(popularListMovie, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie, Movie t1) {
                return  t1.numRate - movie.numRate;
            }
        });

        Collections.sort(topRatedListMovie, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie, Movie t1) {
                return  (int)t1.rate - (int)movie.rate;
            }
        });
    }
    public static void getMovieTable(MobileServiceClient mClient){
        mMovieTable = mClient.getTable(Movie.class);
        try {
            mMovieTable.execute(new TableQueryCallback<Movie>() {
                @Override
                public void onCompleted(List<Movie> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                    movieList = list;
                    reNew();

                }
            });
        } catch (MobileServiceException e) {
            e.printStackTrace();
        }
    }
    public  String convertStringType(){
        String[] arrayTypes;
        arrayTypes = this.type.split("-");
        String s = new String();
        for(int i =0; i< arrayTypes.length;i++){
            s += arrayTypes[i];
            if (i != arrayTypes.length-1){
                s+= ", ";
            }
            else{
                s+=".";
            }
        }
        return s;
    }
    public static Movie getMovieBy(int movieId){

        for(Movie movie : movieList){
            if(movie.movieId == movieId)
                return movie;
        }
        return null;
    }

}
