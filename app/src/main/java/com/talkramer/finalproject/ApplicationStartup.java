package com.talkramer.finalproject;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import model.Model;

/**
 * Created by Yaniv on 29/06/2016.
 */
public class ApplicationStartup extends Application {

    private  static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "first exec");
        context = getApplicationContext();

        //initialize model
        Model.getInstance();

        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        if(defaultImage == null)
            Log.d("TAG", "null default image");
        Model.getInstance().SetLocalBitmap(defaultImage);
    }

    public static Context getAppContext(){return context;}
}
