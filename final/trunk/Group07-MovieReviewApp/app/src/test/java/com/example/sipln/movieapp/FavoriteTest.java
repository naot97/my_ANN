package com.example.sipln.movieapp;

import com.example.sipln.movieapp.Models.Favorite;

import junit.framework.TestCase;

public class FavoriteTest extends TestCase{

    private Favorite favorite;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        favorite = new Favorite(1, 1, 1);

    }
}
