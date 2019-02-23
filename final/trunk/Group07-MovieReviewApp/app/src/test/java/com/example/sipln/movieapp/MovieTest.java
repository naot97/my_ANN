package com.example.sipln.movieapp;

import com.example.sipln.movieapp.Models.Movie;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Date;

public class MovieTest extends TestCase{

    private Movie movie;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        movie = new Movie(1, "MovieName", "MovieDesc", 1, "", 5.0, 2, new Date("21/05/2018"), "romantic","" );
    }

    public void test_getMovieId(){
        Assert.assertEquals(movie.getMovieId(), 1);
    }

    public void test_getMovieName(){
        Assert.assertEquals(movie.getName(), "MovieName");
    }

    public void test_getMovieDesc(){
        Assert.assertEquals(movie.getDesc(), "MovieDesc");
    }

}
