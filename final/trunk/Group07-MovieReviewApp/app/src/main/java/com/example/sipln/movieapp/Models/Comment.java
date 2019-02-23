package com.example.sipln.movieapp.Models;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.Date;
import java.util.List;


public class Comment {
    private String id;
    private int commentId;
    private String content;
    private int userIndex;
    private int movieId;
    private Date dateComment;
    private Date updateDat;

    public Date getDateComment() {
        return dateComment;
    }
    public int getUserIndex() {
        return userIndex;
    }
    public String getContent() {
        return content;
    }

    public Comment(String text, Date time, int userIndex){
        this.content  = text;
        this.updateDat = time;
        this.userIndex = userIndex;
    }
    public Comment(int commentId, String content, int userIndex, int movieId, Date dateComment, Date updateDat) {

        this.commentId = commentId;
        this.content = content;
        this.userIndex = userIndex;
        this.movieId = movieId;
        this.dateComment = dateComment;
        this.updateDat = updateDat;
    }

    public static MobileServiceTable<Comment> mCommentTable;
    public static List<Comment> commentList;
    public static void getCommentTable(MobileServiceClient mClient) throws MobileServiceException {
        mCommentTable = mClient.getTable(Comment.class);
        mCommentTable.execute(new TableQueryCallback<Comment>() {
                @Override
            public void onCompleted(List<Comment> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                commentList = list;

            }
        });
    }
}
