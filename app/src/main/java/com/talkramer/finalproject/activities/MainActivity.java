package com.talkramer.finalproject.activities;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.talkramer.finalproject.ApplicationStartup;
import com.talkramer.finalproject.R;
import com.talkramer.finalproject.fragments.ProfileFragment;
import com.talkramer.finalproject.fragments.GridViewFragment;
import com.talkramer.finalproject.fragments.SignInFragment;
import com.talkramer.finalproject.model.Model;

public class MainActivity extends ActionBarActivity {

    private FragmentManager supportFragmentManager;

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
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: {
                break;
            }

            case R.id.home: {
                GridViewFragment frag = new GridViewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //clear fragment back stack
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.main_frag_container, frag, "GridViewFragment");
                transaction.commit();
                break;
            }

            case R.id.profile: {
                ProfileFragment frag = new ProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //clear fragment back stack
                transaction.add(R.id.main_frag_container, frag, "ProfileFragment");
                transaction.commit();
                break;
            }

            case R.id.logout: {
                Model.getInstance().logout();
                SignInFragment frag = new SignInFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //clear fragment back stack
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.main_frag_container, frag, "signInFragment");
                transaction.commit();
                break;
            }

            default:
                return false;
        }
        return  false;
    }
}
