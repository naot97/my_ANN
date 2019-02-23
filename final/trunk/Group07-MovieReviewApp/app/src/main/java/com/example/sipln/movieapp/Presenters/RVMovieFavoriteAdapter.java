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
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class RVMovieFavoriteAdapter extends RecyclerView.Adapter<RVMovieFavoriteAdapter.MovieViewHolder> {

    List<Movie> movies;
    public int userIndex;
    public RVMovieFavoriteAdapter(List<Movie> movies) {
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
    public RVMovieFavoriteAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_favor_item, parent, false);
        RVMovieFavoriteAdapter.MovieViewHolder movieViewHolder = new RVMovieFavoriteAdapter.MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVMovieFavoriteAdapter.MovieViewHolder holder, int position) {

        new RVMovieFavoriteAdapter.GetImageFromURL(holder.moviePhoto).execute(movies.get(position).getLinkImage());


        double rate = movies.get(position).getRate();
        rate = (double) Math.round(rate * 10) / 10;

        holder.txtScoreIbm.setText(rate+"");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
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


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView moviePhoto;
        TextView txtScoreIbm;

        MovieViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_movie_favor_item);
            moviePhoto = (ImageView) itemView.findViewById(R.id.movie_favor_item_photo);
            txtScoreIbm = (TextView) itemView.findViewById(R.id.ibmScoreFavorite);
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
