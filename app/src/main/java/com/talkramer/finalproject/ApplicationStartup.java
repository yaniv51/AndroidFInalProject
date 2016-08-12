package com.talkramer.finalproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.talkramer.finalproject.model.Model;

/**
 * Created by Yaniv on 29/06/2016.
 */
public class ApplicationStartup extends Application {
    private static Activity activity;

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
        //Model.getInstance().SetLocalBitmap(defaultImage);
    }

    public static void setActivity(Activity myActivity)
    {
        activity = myActivity;
    }

    public static String readPreferenceString(String tag)
    {
        String action;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        action = sharedPref.getString(tag, null);

        return action;
    }

    public static Boolean readPreferenceBoolean(String tag)
    {
        Boolean action;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        action = sharedPref.getBoolean(tag, false);

        return action;
    }

    public static void setPreferenceString(String tag, String value)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putString(tag, value);

        //commits your edits
        editor.commit();
    }

    public static void setPreferenceBoolean(String tag, Boolean value)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putBoolean(tag, value);

        //commits your edits
        editor.commit();

    }

    public static Context getAppContext(){return context;}

    public static Activity getAppActivity(){
        return activity;
    }
}
