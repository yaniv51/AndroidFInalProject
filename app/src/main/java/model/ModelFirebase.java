package model;

import android.content.Context;
import android.util.Log;

import com.firebase.client.Firebase;

/**
 * Created by Yaniv on 30/06/2016.
 */
public class ModelFirebase {

    private Firebase myFirebase;

    ModelFirebase(Context context) {
        try {
            //Firebase.setAndroidContext(context);
            myFirebase = new Firebase("https://finalproject-23ce7.firebaseio.com/");
            //myFirebase.setAndroidContext(context);
            myFirebase.child("message").setValue("Do you have data? You'll love Firebase.");
        }
        catch (Exception ex) {
            Log.d("TAG", ex.getMessage());
        }
    }
}
