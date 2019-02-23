package com.example.sipln.movieapp.Models;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Reviewer   {

    private String id;
    private int userIndex;
    private String userName;
    private String passWord;
    private String email;

    public Boolean getMan() {
        return isMan;
    }

    public void setMan(Boolean man) {
        isMan = man;
    }

    private Boolean isMan;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String avatar;

    public Reviewer(int userIndex, String userName, String passWord, String email, Boolean isMan, String firstName, String lastName, String birthDay){
        this.userIndex = userIndex;
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.isMan = isMan;
        this.firstName = firstName;
        this.lastName =lastName;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.birthDay = df.parse(birthDay);
        }
        catch (Exception e){}
    }

    // User is using app.
    public static Reviewer loggingReviewer;
    // Table from database
    public static MobileServiceTable<Reviewer> mReviewerTable;
    // all Reviewers.
    public static List<Reviewer> reviewerList;


    public Reviewer(int userIndex, String email, String firstName, String lastName) {
        this.userIndex = userIndex;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static void getTableReviewer(MobileServiceClient mClient){
        mReviewerTable = mClient.getTable(Reviewer.class);
        try {
            mReviewerTable.execute(new TableQueryCallback<Reviewer>() {
                @Override
                public void onCompleted(List<Reviewer> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                    reviewerList = list;

                }
            });
        } catch (MobileServiceException e) {
            e.printStackTrace();
        }


    }
    // get user with userIndex.
    public static String getUserName(int index){
        for(Reviewer r: reviewerList){
            if(r.userIndex == index)
                return r.userName;
        }
        return  null;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName(){
        return userName;
    }

    public  String getPassWord(){
        return  passWord;
    }

    public String getEmail(){
        return email;
    }

    public int getUerIndex(){
        return userIndex;
    }

    public  void setPassWord(String p){
        this.passWord = p;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }
}


