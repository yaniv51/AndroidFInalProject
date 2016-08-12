package com.talkramer.finalproject.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.talkramer.finalproject.ApplicationStartup;
import com.talkramer.finalproject.R;
import com.talkramer.finalproject.fragments.GridViewFragment;
import com.talkramer.finalproject.fragments.SignInFragment;
import com.talkramer.finalproject.model.Model;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationStartup.setActivity(this);

        FirebaseUser user = Model.getInstance().getFirebaseUser();

        if(user == null)
        {
            SignInFragment frag = new SignInFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.main_frag_container, frag, "signInFragment");
            transaction.commit();
        }
        else {
            GridViewFragment frag = new GridViewFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frag_container, frag);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_button, menu);
        //getMenuInflater().inflate(R.menu.menu_home_button, menu);
        //getMenuInflater().inflate(R.menu.menu_profile_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: {
//                NewStudentFragment newStudentFragment = new NewStudentFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_frag_container, newStudentFragment, "newStudentFragment");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
            }

            case R.id.home: {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Fragment page = getSupportFragmentManager().findFragmentByTag("studentDetailsFragment");
//                page.onOptionsItemSelected(item);
                break;
            }

            case R.id.profile: {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Fragment page = getSupportFragmentManager().findFragmentByTag("studentDetailsFragment");
//                page.onOptionsItemSelected(item);
                break;
            }

            default:
                return false;
        }
        return  false;
    }

}
