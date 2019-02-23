package com.example.sipln.movieapp.Presenters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sipln.movieapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.sipln.movieapp.Models.Comment;
import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.Views.Activities.DetailMovieActivity;

public class RVCommentAdapter extends RecyclerView.Adapter<RVCommentAdapter.CommentViewHolder> {
    List<Comment> commentList = new ArrayList<>();


    // set
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    // add comment
    public void add(Comment comment){
        commentList.add(comment);
    }


    public RVCommentAdapter(List<Comment> commentList){
        this.commentList = commentList;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        RVCommentAdapter.CommentViewHolder commentViewHolder = new RVCommentAdapter.CommentViewHolder(view);
        return commentViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Get user by id, then get NAME OF USER.
        int userIndex = commentList.get(position).getUserIndex();
        // get name?????
        String userName = Reviewer.getUserName(userIndex);
        holder.txtUserName.setText(userName);
        holder.txtComment.setText(commentList.get(position).getContent());
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String commentTime = df.format(commentList.get(position).getDateComment());
        holder.txtDate.setText(commentTime);


        // set avatar:
        //holder.photoUser............
        new RVCommentAdapter.GetImageFromURL(holder.photoUser).execute(Reviewer.loggingReviewer.getAvatar());
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtDate;
        TextView txtComment;
        TextView txtUserName;
        ImageView photoUser;

        CommentViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_comment_item);
            txtDate = (TextView) itemView.findViewById(R.id.txtDateComment);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            photoUser = (ImageView) itemView.findViewById(R.id.userPhoto);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
        }
    }



}
