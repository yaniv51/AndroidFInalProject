package com.talkramer.finalproject.model;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.talkramer.finalproject.model.Domain.Product;

import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {

    private Firebase myFirebase;

    ModelFirebase(Context context) {
            Firebase.setAndroidContext(context);
            myFirebase = new Firebase("https://console.firebase.google.com/project/finalproject-678c9/");
    }

    public void add(Product pr, Model.AddProductListener listener) {
        Firebase prRef = myFirebase.child("product").child(getUserId()).child(pr.getId());
        prRef.setValue(pr);
    }

    public void getProduct(String id, final Model.GetProductListener listener) {
        Firebase prRef = myFirebase.child("product").child(getUserId()).child(id);
        // Attach a listener to read the data at our posts reference
        prRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                listener.done(product);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.done(null);
            }
        });

    }

    public void getProducts(final Model.GetProductsListener listener) {
        Firebase prRef = myFirebase.child("product").child(getUserId());
        // Attach a listener to read the data at our posts reference
        prRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Product> prList = new LinkedList<Product>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    prList.add(product);
                }
                listener.done(prList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.done(null);
            }
        });
    }

    public String getUserId(){
        AuthData authData = myFirebase.getAuth();
        if (authData != null) {
            return authData.getUid();
        }
        return null;
    }

    public void signeup(String email, String pwd, final SignupListener listener) {
        myFirebase.createUser(email, pwd, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                listener.success();
            }

            @Override
            public void onError(FirebaseError firebaseError) {

                listener.fail(firebaseError.getMessage());
            }
        });

    }

    public void login(String email, String pwd, final SignupListener listener) {
        myFirebase.authWithPassword(email, pwd, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                listener.success();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.fail(firebaseError.getMessage());
            }
        });

    }

    public void logout() {
        myFirebase.unauth();
    }
}
