package com.example.sipln.movieapp;

import com.example.sipln.movieapp.Models.Reviewer;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ReviewerTest extends TestCase {

    private Reviewer reviewer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        reviewer = new Reviewer(1, "Username", "Password", "abc@gmail.com", true, "Firstname", "Lastname", "10/10/2018");
    }

    public void test_getUsername(){
        Assert.assertEquals(reviewer.getUserName(), "Username");
    }

}
