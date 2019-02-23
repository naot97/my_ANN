package com.example.sipln.movieapp;

import com.example.sipln.movieapp.Models.Comment;

import junit.framework.TestCase;

import org.junit.Assert;

import java.util.Date;

public class CommentTest extends TestCase {
    private Comment comment;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        comment = new Comment(0, "Deadline tới nơi rồi", 0, 001, new Date("20/05/2018"), new Date("25/05/2018"));
    }

    public void test_GetDateComment(){
        Assert.assertEquals(comment.getDateComment(), new Date("20/05/2018"));
    }

    public void test_GetUserIndex(){
        Assert.assertEquals(comment.getUserIndex(), 0);
    }

    public void test_GetContent(){
        Assert.assertEquals(comment.getContent(), "Deadline tới nơi rồi");
    }

}
