package com.example.sipln.movieapp.Models;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

public class MobileService {
    public MobileServiceClient client ;
    public MobileService(Context c){
        try {
            client = new MobileServiceClient(
                    "https://ttcnpm.azurewebsites.net",
                    c);
        }
        catch (Exception e){}
    }
}
