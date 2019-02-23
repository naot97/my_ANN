package com.example.sipln.movieapp.Views.Fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipln.movieapp.Models.Comment;
import com.example.sipln.movieapp.Models.Movie;
import com.example.sipln.movieapp.Models.Reviewer;
import com.example.sipln.movieapp.Presenters.RVCommentAdapter;
import com.example.sipln.movieapp.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommentFragment extends android.support.v4.app.Fragment {

    public MobileServiceTable<Comment> mCommentTable ;
    public static final String TITLE = "Comment";
    public static Movie movie = new Movie();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RVCommentAdapter rvCommentAdapter;
    List<Comment> commentList = new ArrayList<>();

    // button post + textfield comment
    private Button btnPost;
    private TextView txtCommentText;




    public CommentFragment(){
    }

    public static CommentFragment newInstance(MobileServiceTable<Comment> mCommentTable) {
        CommentFragment fragment = new CommentFragment();
        fragment.mCommentTable = mCommentTable;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Comment.mCommentTable.where().field("movieid").eq(movie.getMovieId()).execute(new TableQueryCallback<Comment>() {
            @Override
            public void onCompleted(List<Comment> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                commentList = list;
                rvCommentAdapter.setCommentList(list);
                rvCommentAdapter.notifyDataSetChanged();

            }
        });


       // txtCommentText.setOnEditorActionListener(editorActionListener);
    }



    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i){
                case EditorInfo.IME_ACTION_DONE:
                    Log.d("ADN","cmnr");
                    break;
            }
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_now, container, false);

        View rootHomeView = inflater.inflate(R.layout.fragment_comment, container, false);
        txtCommentText = rootHomeView.findViewById(R.id.txtCommentBox);
        btnPost = rootHomeView.findViewById(R.id.btnPostComment);
        recyclerView = (RecyclerView) rootHomeView.findViewById(R.id.recycler_view_comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        rvCommentAdapter = new RVCommentAdapter(commentList);
        recyclerView.setAdapter(rvCommentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Reviewer.loggingReviewer.getUerIndex() == 0) {
                    Toast.makeText(getActivity(), "Please sign up in profile", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = (String) txtCommentText.getText().toString();
                if (content.equals("")) {
                    Toast.makeText(getContext(), "Comment is empty! Please type something!", Toast.LENGTH_SHORT).show();
                }
                else {
                    txtCommentText.setText("");
                    // new Comment:
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date today = Calendar.getInstance().getTime();
                    String commentTime = df.format(today);


                    Comment comment = new Comment(Comment.commentList.size()+1,content,Reviewer.loggingReviewer.getUerIndex(),movie.getMovieId(),new Date(),new Date());
                    Comment.commentList.add(comment);

                    //
                    Comment.mCommentTable.insert(comment);
                    rvCommentAdapter.add(comment);
                    rvCommentAdapter.notifyDataSetChanged();
                }


            }
        });

        return rootHomeView;
    }

}
