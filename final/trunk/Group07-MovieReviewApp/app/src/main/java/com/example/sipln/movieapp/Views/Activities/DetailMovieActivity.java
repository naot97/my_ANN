package com.example.sipln.movieapp.Views.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipln.movieapp.Models.Favorite;
import com.example.sipln.movieapp.Models.MobileService;
import com.example.sipln.movieapp.Models.Movie;
import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.Presenters.ViewPagerAdapter;
import com.example.sipln.movieapp.R;
import com.example.sipln.movieapp.Views.Fragments.CommentFragment;
import com.example.sipln.movieapp.Views.Fragments.DetailMovieFragment;
import com.facebook.login.LoginManager;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DetailMovieActivity extends AppCompatActivity {

    RemoteViews remoteViews;
    Button btnYoutube;
    Button btnFavo;
    ImageView imageMovie;
    TextView txtNameMovie;
    int movieId;
    int userIndex;
    MobileService mobileService = new MobileService(this);
    MobileServiceTable<Favorite> mFavoTable;
    boolean isFavo ;
    Favorite favo = null ;
    int favoIndex =0;


    // show rating pop up
    private Button  btnRate;            // rating button in detailActivity
    private Dialog myDialog;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private Movie currentMovie;



    void check2(){
        boolean isCheck = false;

            for (int i=0;i<Favorite.movieList.size();i++){
                if(movieId == Favorite.movieList.get(i).getMovieId()){
                    btnFavo.setText("LIKED");
                    favo = Favorite.favoriteList.get(i);
                    favoIndex = i;
                    isFavo = true;
                    isCheck=true;
                    break;
                }
                else{
                    btnFavo.setText("LIKE");
                    isFavo = false;
                    isCheck=true;
                }
            }
            if (!isCheck){
                btnFavo.setText("LIKE");
                isFavo = false;
                isCheck=true;
            }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        // init()
        init();

        // set ViewPager
        setViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        mFavoTable = mobileService.client.getTable(Favorite.class);


    favo = null;
    Intent intent = getIntent();
    movieId = intent.getIntExtra("movieId",0);
    btnFavo = (Button)findViewById(R.id.btnFavo);
    currentMovie = Movie.getMovieBy(movieId);


    // kiểm tra xem phim này có nằm trong danh sách yêu thích hay không?
    check2();

    imageMovie = (ImageView) findViewById(R.id.imgMovieDetail);
    txtNameMovie = (TextView) findViewById(R.id.txtMovieName);

    new GetImageFromURL(imageMovie).execute(currentMovie.getLinkImage());
    txtNameMovie.setText(currentMovie.getName());




    btnYoutube = (Button)findViewById(R.id.btnViewTrailer);
    btnYoutube.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentMovie.getLinkTrailer()));
            startActivity(intent);
        }
    });

    btnFavo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                if (Reviewer.loggingReviewer.getUerIndex() == 0) {
                    Toast.makeText(getBaseContext(), "Please sign up in profile", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isFavo) {
                    mFavoTable.delete(favo);
                    // xóa favor khỏi listMovies, xóa khỏi listFavor luôN:
                    Favorite.movieList.remove(favoIndex);
                    Favorite.favoriteList.remove(favoIndex);

                    favo = null;
                    btnFavo.setText("LIKE");
                    isFavo = false;

                } else {
                    mFavoTable.execute(new TableQueryCallback<Favorite>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onCompleted(List<Favorite> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                                    int favoId = list.size() + 1;
                                    favo = new Favorite(favoId,movieId,Reviewer.loggingReviewer.getUerIndex());
                                    mFavoTable.insert(favo);


                                    Favorite.movieList.add(Movie.getMovieBy(favo.getMovieId()));
                                    Favorite.favoriteList.add(favo);

                                    btnFavo.setText("LIKED");
                                    isFavo = true;
                                }
                            });
                }
            }
            catch (Exception e){}
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PopularFragment.movieList.get(k).linkTrailer));
            //startActivity(intent);
        }
    });

        // click to RATING.
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupRate();
            }
        });

}

    private void init(){
        btnYoutube = (Button)findViewById(R.id.btnViewTrailer);
        mViewPager = (ViewPager) findViewById(R.id.viewpagerDetailMovie);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        imageMovie = (ImageView) findViewById(R.id.imgMovieDetail);
        txtNameMovie = (TextView) findViewById(R.id.txtMovieName);
        mTabLayout = (TabLayout) findViewById(R.id.tablayoutDetailMovie);
        // popup
        btnRate = (Button) findViewById(R.id.btnRating);
        myDialog = new Dialog(this);
    }
    // show Popup (Dialog)
    double scrore = 0;
    private void showPopupRate(){
        // Views on Dialog: txtClose, a button, ratingbar.
        TextView txtClose;
        Button btnRateNow;
        RatingBar ratingBar;

        // set Content for myDialog by popr_up_rating
        myDialog.setContentView(R.layout.pop_up_rating);
        myDialog.setTitle("Give your star ! ");

        // linked object from myDialog
//        txtClose = myDialog.findViewById(R.id.txtClose);
        btnRateNow = myDialog.findViewById(R.id.btnRateInDialog);
        ratingBar = myDialog.findViewById(R.id.ratingBar);


        // Click X to close Dialog.
//        txtClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myDialog.dismiss();
//            }
//        });

        // Click btn to RATINGBAR.
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                // UPDATE RATING.
                scrore = 2*v;
            }
        });

        btnRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double ibmScore =  currentMovie.getRate();
                int numRate = currentMovie.getNumRate();
                double newIbmScore = (ibmScore*numRate + scrore)/(numRate+1);

                currentMovie.setNumRate(++numRate);
                currentMovie.setRate(newIbmScore);

                Movie.mMovieTable.update(currentMovie);
                Toast.makeText(DetailMovieActivity.this, "Thank you for your rating!", Toast.LENGTH_SHORT).show();


                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    private void setViewPager() {
        mViewPagerAdapter.addFragment(new DetailMovieFragment(), DetailMovieFragment.TITLE);
        mViewPagerAdapter.addFragment(new CommentFragment(), CommentFragment.TITLE);
        mViewPager.setAdapter(mViewPagerAdapter);
    }


    Bitmap bitmap;
    public  class GetImageFromURL extends AsyncTask<String,Void,Bitmap> {
        ImageView image;
        public  GetImageFromURL(ImageView imageView){
            this.image = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                InputStream inputStream = new URL(strings[0]).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image.setImageBitmap(bitmap);
        }
    }
}
