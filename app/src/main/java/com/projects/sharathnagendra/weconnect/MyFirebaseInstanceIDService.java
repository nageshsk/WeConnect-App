package com.projects.sharathnagendra.weconnect;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Sharath Nagendra on 10/23/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh(){


        String token= FirebaseInstanceId.getInstance().getToken();
        registerToken(token);

    }
    private void registerToken(String token)
    {

        OkHttpClient client= new OkHttpClient();
        String text= Utility.getValueFromSP(MyFirebaseInstanceIDService.this,"email");
        System.out.println("---------------------------------------"+text);
        Log.d("token",token);
        RequestBody body=new FormBody.Builder().add("Token",token).build();
        Log.d("body", String.valueOf(body));
        Request request=new Request.Builder().url(" https://weconnect-imnikhil.c9users.io/api/token/").post(body).build();

        try {
            client.newCall(request).execute();

            Log.d("request", String.valueOf(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
