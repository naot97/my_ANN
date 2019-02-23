package com.example.sipln.movieapp.Views.Activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipln.movieapp.Dialogs.CustomDialog;
import com.example.sipln.movieapp.Models.Favorite;
import com.example.sipln.movieapp.Models.Movie;
import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.Presenters.ViewPagerAdapter;
import com.example.sipln.movieapp.R;
import com.example.sipln.movieapp.Views.Fragments.NowFragment;
import com.example.sipln.movieapp.Views.Fragments.PopularFragment;
import com.example.sipln.movieapp.Views.Fragments.TopRatedFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;


public class HomeActivity extends AppCompatActivity {
    private static final int TIME_LIMIT = 1500;
    private static long backPressed;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private BottomNavigationView mbottomNavigationView;
    public static MobileServiceClient mClient;

    public MobileServiceTable<Movie> mMovieTable;
    public MobileServiceTable<Reviewer> mReviewerTable;
    public PopularFragment p;
    private int userIndex;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Button btnLogout;
    private Button btnSignUp;
    private NavigationView navigationView;
    private TextView reviewerName;
    private TextView reviewerBirth;
    private TextView reviewerEmail;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_CAMERA_REQUEST = 72;

    private BroadcastReceiver mBoardCastReceiver;
    private  boolean isFace(){
        if (Reviewer.loggingReviewer.getUerIndex() == 0) {
            Toast.makeText(getBaseContext(), "Please sign up in profile", Toast.LENGTH_SHORT).show();
            return true;
        }
        else return false;
    }
    private BroadcastReceiver mBoardCastReceiver_Directory,
            getmBoardCastReceiver_Camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            mClient = new MobileServiceClient(
                    "https://ttcnpm.azurewebsites.net",
                    this
            );
            mMovieTable = mClient.getTable(Movie.class);
            mReviewerTable = mClient.getTable((Reviewer.class));
            userIndex = getIntent().getExtras().getInt("userIndex");

        } catch (Exception e) {
        }

        try {

            Favorite.getFavoriteTable(mClient);
        } catch (MobileServiceException e) {
            e.printStackTrace();
        }



        /////////////////////////////////////////
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home);
        //////////////////////////////////////////
        mbottomNavigationView = findViewById(R.id.bot_nav);
        addEvent();

        //------------------------------------------------------

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //------------------------------------------------------


        drawerLayout = findViewById(R.id.home_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //--------------------------------------------------------

        navigationView = findViewById(R.id.nv_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (isFace()) return false;
                switch (item.getItemId()) {
                    case R.id.changeAvatar:
                        new CustomDialog(HomeActivity.this).show();
                        break;
                    case R.id.changeLogInfo:
                        HomeActivity.this.startActivityForResult(new Intent(HomeActivity.this, LoginInfoModActivity.class), 2);

                }

                return false;
            }
        });
        View avatar = navigationView.getHeaderView(0).findViewById(R.id.cv_avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFace()) return;
                new CustomDialog(HomeActivity.this).show();
            }
        });
        //-----------------------------------------------------------
        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));

            }
        });
        //---------------------------------------------------------

        setViewPager(1);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //--------------------------------------------------------
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if login by facebook. it will logout
                LoginManager.getInstance().logOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        //-----------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //------------------------------------------------------------

        IntentFilter filter_oepnDirectory_ = new IntentFilter();
        filter_oepnDirectory_.addAction("Directory");
//        filter.addAction("Camera");
        mBoardCastReceiver_Directory = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Directory")) {
                    chooseAvatarFromDirectory();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mBoardCastReceiver_Directory, filter_oepnDirectory_);

        IntentFilter filter_oepnCamera = new IntentFilter();
        filter_oepnCamera = new IntentFilter();
        filter_oepnCamera.addAction("Camera");
        getmBoardCastReceiver_Camera = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("Camera")) {
                    takePhoto();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(getmBoardCastReceiver_Camera, filter_oepnCamera);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.signInAnonymously();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                capture();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LoginManager.getInstance().logOut();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBoardCastReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBoardCastReceiver_Directory);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getmBoardCastReceiver_Camera);

    }


    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();

    }


    // ham nay nhan event khi user Tap vao 3 icon bottom vnd
    private void addEvent() {

        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_actor:
                        startActivity(new Intent(HomeActivity.this, ActorActivity.class));
                        break;

                    case R.id.mn_fav:
                        if (isFace()) return false;
                        startActivity(new Intent(HomeActivity.this, FavoriteActivity.class));
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        //event cho search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    Movie.reNew();

                    int i = 0;
                    while (i < Movie.popularListMovie.size()) {
                        if (!Movie.popularListMovie.get(i).getName().toLowerCase().contains(newText.toLowerCase()))
                            Movie.popularListMovie.remove(i);
                        else i++;
                    }

                    i = 0;
                    while (i < Movie.topRatedListMovie.size()) {
                        if (!Movie.topRatedListMovie.get(i).getName().toLowerCase().contains(newText.toLowerCase()))
                            Movie.topRatedListMovie.remove(i);
                        else i++;
                    }

                    i = 0;
                    while (i < Movie.nowListMovie.size()) {
                        if (!Movie.nowListMovie.get(i).getName().toLowerCase().contains(newText.toLowerCase()))
                            Movie.nowListMovie.remove(i);
                        else i++;
                    }

                    setViewPager(mViewPager.getCurrentItem());
                    return true;
                } catch (Exception e) {
                    return true;
                }
            }
        });
        //navigationView.callOnClick();

        updateInfo();

        return true;
    }

    private void updateInfo() {
        ImageView gender = findViewById(R.id.img_gender);
        try {
            reviewerName = findViewById(R.id.reviewer_name);
            reviewerName.setText(Reviewer.loggingReviewer.getFirstName() + " " + Reviewer.loggingReviewer.getLastName());
            reviewerEmail = findViewById(R.id.reviewer_email);
            reviewerEmail.setText(Reviewer.loggingReviewer.getEmail());
            reviewerBirth = findViewById(R.id.reviewer_birthday);
            reviewerBirth.setText(new SimpleDateFormat("dd/MM/yyyy").format(Reviewer.loggingReviewer.getBirthDay()));
            if (Reviewer.loggingReviewer.getMan())
                gender.setImageResource(R.drawable.masculine);
            else
                gender.setImageResource(R.drawable.femenine);
            updateAvatar(Uri.parse(Reviewer.loggingReviewer.getAvatar()));
        }
        catch(Exception e) {}
    }

    private void setViewPager(int defa) {

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(PopularFragment.newInstance(mMovieTable, userIndex), PopularFragment.TITLE);//toan
        mViewPagerAdapter.addFragment(NowFragment.newInstance(mMovieTable), NowFragment.TITLE);
        mViewPagerAdapter.addFragment(TopRatedFragment.newInstance(mMovieTable), TopRatedFragment.TITLE);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(defa);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap.recycle();
                if (filePath != null) {
                    final StorageReference ref = storageReference.child("photos/" + UUID.randomUUID().toString());
                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("url", ref.getDownloadUrl().toString());
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Reviewer.loggingReviewer.setAvatar(uri.toString());
                                            mReviewerTable.update(Reviewer.loggingReviewer);
                                            Log.e("uri", uri.toString());
                                            updateAvatar(uri);
                                        }
                                    });
                                }
                            });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == PICK_CAMERA_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Log.e("ERROR", filePath.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap.recycle();
                if (filePath != null) {
                    final StorageReference ref = storageReference.child("photos/" + UUID.randomUUID().toString());
                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.e("url", ref.getDownloadUrl().toString());
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Reviewer.loggingReviewer.setAvatar(uri.toString()) ;
                                            mReviewerTable.update(Reviewer.loggingReviewer);
                                            Log.e("uri", uri.toString());
                                            updateAvatar(uri);
                                        }
                                    });
                                }
                            });
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }



    // ham setup back 2 lan se exit app--vnd
    @Override
    public void onBackPressed() {
        if (TIME_LIMIT + backPressed > System.currentTimeMillis()) {
            LoginManager.getInstance().logOut();
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Press back twice to exit", Toast.LENGTH_SHORT).show();
        }

        backPressed=System.currentTimeMillis();

    }

    private void updateAvatar(Uri uri) {
        Log.e("Avatar", "OK");
        SimpleDraweeView avatar = navigationView.getHeaderView(0).findViewById(R.id.reviewer_photo);
        avatar.setImageURI(uri);

    }

    public void chooseAvatarFromDirectory(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        HomeActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        }
        else {
            capture();
        }

    }

    public void capture()
    {
        Calendar cal = Calendar.getInstance();
        File file = new File(this.getFilesDir(), (cal.getTimeInMillis() + ".jpg"));

        try{
            if (file.exists())
                file.delete();
            else
                file.createNewFile();

        }catch (IOException ex) {
            ex.printStackTrace();
        }

//        Uri capturedImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName(), file);
        Uri capturedImageUri = Uri.fromFile(file);
        Log.e("URI", capturedImageUri.toString());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
            startActivityForResult(takePictureIntent, PICK_CAMERA_REQUEST);
        }
    }


}
