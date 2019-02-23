package com.example.sipln.movieapp.Views.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.sipln.movieapp.Models.Actor;
import com.example.sipln.movieapp.Presenters.ActorAdapter;
import com.example.sipln.movieapp.Presenters.ViewPagerAdapter;
import com.example.sipln.movieapp.R;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// create by ND
public class ActorActivity extends AppCompatActivity {
    GridView gridView_Actor;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private Toolbar mToolbar;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        mToolbar = findViewById(R.id.toolbar_actor);
        setSupportActionBar(mToolbar);


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        List<String> nameActors = new ArrayList<>();
        List<String> imgActors = new ArrayList<>();
        for(Actor actor : Actor.currentActorList){
            nameActors.add(actor.getActorName());
            imgActors.add(actor.getActorImage());
        }


        gridView_Actor = findViewById(R.id.grv_Actor);
        ActorAdapter actorAdapter = new ActorAdapter(this,nameActors,imgActors);
        gridView_Actor.setAdapter(actorAdapter);



    }
    //inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actor_toolbar, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // event cho search
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_actor).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    Actor.reNew();
                    int i = 0;
                    while (i < Actor.currentActorList.size()) {
                        if (!Actor.currentActorList.get(i).getActorName().toLowerCase().contains(newText.toLowerCase()))
                            Actor.currentActorList.remove(i);
                        else i++;
                        List<String> nameActors = new ArrayList<>();
                        List<String> imgActors = new ArrayList<>();
                        for(Actor actor : Actor.currentActorList){
                            nameActors.add(actor.getActorName());
                            imgActors.add(actor.getActorImage());
                        }
                        gridView_Actor = findViewById(R.id.grv_Actor);
                        ActorAdapter actorAdapter = new ActorAdapter(getBaseContext(),nameActors,imgActors);
                        gridView_Actor.setAdapter(actorAdapter);


                    }


                    return true;
                } catch (Exception e) {
                    return true;

                }

            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    private void setViewPager(int defa) {

        mViewPager = findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(defa);


    }

    // Bắt sự kiện cho các item Menu tren thanh toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_actor:
                gridView_Actor.setVisibility(View.VISIBLE);
                break;
            case android.R.id.home:
                Intent intent = new Intent(ActorActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();

    }


}
