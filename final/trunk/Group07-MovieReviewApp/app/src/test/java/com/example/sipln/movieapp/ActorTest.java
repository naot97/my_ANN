package com.example.sipln.movieapp;

import com.example.sipln.movieapp.Models.Actor;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ActorTest extends TestCase{
    private Actor actor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        actor = new Actor("", "", 0);
    }

    public void test_Name_Gettter_Setter(){
        actor.setName("ActorName");
        Assert.assertEquals(actor.getName(), "ActorName");
    }

    public void test_Desc_Gettter_Setter(){
        actor.setDesc("Actor Discriptions");
        Assert.assertEquals(actor.getDesc(), "Actor Discriptions");
    }

    public void test_PhotoId_Gettter_Setter(){
        actor.setDesc("Actor PhotoId");
        Assert.assertEquals(actor.getDesc(), "Actor PhotoId");
    }



}
