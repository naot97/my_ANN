package com.example.sipln.movieapp.Views.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sipln.movieapp.Models.Reviewer;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


public class FacebookService {
    private static final String TAG = "123uiui";
    private static  String first_name= null;
    private static  String last_name= null;
    private static  String email= null;

    public static void set_LoginFacebookButton(LoginButton loginButton, CallbackManager callbackManager, Intent myIntent,Context context, MainActivity main) {

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent_ToHome = new Intent(main, HomeActivity.class);
                intent_ToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Reviewer.loggingReviewer = new Reviewer(0,"","",email,true,first_name,last_name,"01/01/2018");
                main.startActivity(intent_ToHome);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Log.d(TAG, "Login attempt failed.");
            }
        });
    }

    private static Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : " + e.toString());
        }
        return bundle;
    }
}
