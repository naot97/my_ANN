package com.example.sipln.movieapp.Presenters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sipln.movieapp.Models.Movie;
import com.example.sipln.movieapp.R;
import com.example.sipln.movieapp.Views.Activities.DetailMovieActivity;
import com.example.sipln.movieapp.Views.Fragments.CommentFragment;
import com.example.sipln.movieapp.Views.Fragments.DetailMovieFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class RVMovieAdapter extends RecyclerView.Adapter<RVMovieAdapter.MovieViewHolder> {

    List<Movie> movies;

    public  static int numCurrentMovies = 4;

    public int userIndex;
    public RVMovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }
    public void setMovies(List<Movie> movies){
        this.movies = movies;
    }



    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 30;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    @NonNull
    @Override
    public  MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, final int position) {
        movieViewHolder.movieName.setText(movies.get(position).getName());
        movieViewHolder.movieDesc.setText(movies.get(position).getDesc());
        movieViewHolder.ratingBar.setRating((float) movies.get(position).getRate()/2);

        LayerDrawable stars = (LayerDrawable) movieViewHolder.ratingBar.getProgressDrawable();


        // set image
        new GetImageFromURL(movieViewHolder.moviePhoto).execute(movies.get(position).getLinkImage());

        movieViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailMovieActivity.class);

                DetailMovieFragment.movie = movies.get(position);
                CommentFragment.movie =movies.get(position);
                intent.putExtra("movieId",movies.get(position).getMovieId());

                intent.putExtra("userIndex",userIndex);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView movieName;
        TextView movieDesc;
        ImageView moviePhoto;
        RatingBar ratingBar;

        MovieViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_movie_item);
            movieName = (TextView) itemView.findViewById(R.id.tv_name);
            movieDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            moviePhoto = (ImageView) itemView.findViewById(R.id.iv_movie_photo);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        }
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

            bitmap = getRoundedCornerBitmap(bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image.setImageBitmap(bitmap);
        }
    }
}
