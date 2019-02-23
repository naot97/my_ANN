package com.example.sipln.movieapp.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sipln.movieapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.example.sipln.movieapp.Models.Movie;

public class DetailMovieFragment extends android.support.v4.app.Fragment {

    public static final String TITLE = "Detail";

    public static Movie movie = new Movie();
    private TextView txtName;
    private TextView txtType;
    private TextView txtReleaseDate;
    private TextView txtDescription;
    private TextView txtNumbRate;
    private TextView txtIbmScore;

    public DetailMovieFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View detailView = inflater.inflate(R.layout.fragment_detailmovie, container, false);
        txtName = detailView.findViewById(R.id.movieName);
        txtDescription = detailView.findViewById(R.id.description);
        txtReleaseDate = detailView.findViewById(R.id.releaseDate);
        txtType = detailView.findViewById(R.id.types);
        txtNumbRate = detailView.findViewById(R.id.numRate);
        txtIbmScore = detailView.findViewById(R.id.ibmScore);

        String types = movie.convertStringType();

        txtName.setText(movie.getName());



        txtDescription.setText("Description: "+movie.getDesc());



        double rate = movie.getRate();
        rate = (double) Math.round(rate * 10) / 10;

        txtIbmScore.setText("IMDB: " + rate);
        txtType.setText("Types: " + types);
        txtNumbRate.setText("Numb Rate: " + movie.getNumRate());

        // còn thiếu date với types.
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String releaseDate = df.format(movie.getDate());
        txtReleaseDate.setText("Release Date: " + releaseDate);

        return detailView;
    }
}
