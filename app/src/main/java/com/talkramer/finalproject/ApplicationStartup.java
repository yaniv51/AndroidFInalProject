package com.talkramer.finalproject;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import model.Model;

/**
 * Created by Yaniv on 29/06/2016.
 */
public class ApplicationStartup extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "first exec");

        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        if(defaultImage == null)
            Log.d("TAG", "null default image");
        Model.getInstance().SetLocalBitmap(defaultImage);
    }
}
