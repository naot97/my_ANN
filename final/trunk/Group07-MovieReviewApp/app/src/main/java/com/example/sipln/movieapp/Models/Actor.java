package com.example.sipln.movieapp.Models;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;


public class Actor {
    private String id;
    private int actorId;
    private String actorImage;
    private String actorName;
    private String desc;
    private int photoId;
    public static List<Actor> actorList;
    public static MobileServiceTable<Actor> mActorTable;



    public static List<Actor> actorsList;
    public static List<Actor> currentActorList;
    //public static  List<Actor> listActor;
    public Actor(int id, String name, String desc, String actorImage) {
        this.actorName = name;
        this.actorId = id;
        this.actorImage = actorImage;
        this.desc = desc;
    }
    public String getActorName() {
        return actorName;
    }
    public void setActorName(String name) {
        this.actorName = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public void setActorImage(String actorImage) {
        this.actorImage = actorImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActorImage() {
        return actorImage;
    }

    public int getActorId() {
        return actorId;
    }

    public String getId() {
        return id;
    }


    public static void getActorTable(MobileServiceClient mClient){
        try {
            mActorTable = mClient.getTable(Actor.class);
            mActorTable.execute(new TableQueryCallback<Actor>() {
                @Override
                public void onCompleted(List<Actor> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                    actorsList = list;
                    reNew();
                }
            });
        } catch (MobileServiceException e) {
            e.printStackTrace();
        }
    }

    public static  void reNew(){
        currentActorList = new ArrayList<Actor>(actorsList);
    }
    public static Actor getActorBy(int actorId){
        for(Actor actor : actorList){
            if(actor.id.equals(actorId))
                return actor;
        }
        return null;
    }
//    public static void getActorTable(MobileServiceClient mClient){
//        mActorTable = mClient.getTable(Actor.class);
//        try {
//            mActorTable.execute(new TableQueryCallback<Movie>() {
//                @Override
//                public void onCompleted(List<Actor> list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
//                    actorList = list;
//                    reNew();
////                    for (int k = 0; k <topRatedListMovie.size(); k++){
////                        Log.e("DLD","" + topRatedListMovie.get(k).rate + "   " + topRatedListMovie.get(k).movieId);
////                    }
////                    for (int k = 0; k <popularListMovie.size(); k++){
////                        Log.e("DLD","" + popularListMovie.get(k).numRate + "   " + popularListMovie.get(k).movieId);
////                    }
//                }
//            });
//        } catch (MobileServiceException e) {
//            e.printStackTrace();
//        }
//    }



}
