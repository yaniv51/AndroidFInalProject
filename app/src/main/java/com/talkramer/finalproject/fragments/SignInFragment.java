package com.talkramer.finalproject.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.talkramer.finalproject.ApplicationStartup;
import com.talkramer.finalproject.R;
import com.talkramer.finalproject.model.Model;
import com.talkramer.finalproject.model.Utils.Helper;

public class SignInFragment extends Fragment {

    private View view;
    private EditText emailext, passwordText;

    public SignInFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        Button signInButton = (Button) view.findViewById(R.id.signInButton_signIn_fragment);
        final Button registerButton = (Button) view.findViewById(R.id.registerButton_signIn_fragment);
        emailext = (EditText) view.findViewById(R.id.email_signIn_fragment);
        passwordText = (EditText) view.findViewById(R.id.password_signIn_fragment);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = emailext.getText().toString();
                password = passwordText.getText().toString();

                Log.d("TAG", "register pressed: email: " + email + ", Password: "+ password);
                signUp(email, password);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = emailext.getText().toString();
                password = passwordText.getText().toString();

                Log.d("TAG", "sign in pressed: email: " + email + ", Password: "+ password);
                signIn(email, password);
            }
        });
        return view;
    }

    private void signIn(final String email, final String password)
    {
        Model.getInstance().login(email, password, createAuthListener(email, password));
    }

    private Model.AuthListener createAuthListener(final String email, final String password)
    {
        Model.AuthListener listener = new Model.AuthListener() {
            @Override
            public void onDone(String userId, Exception e) {
                if (e == null){
                    Log.d("TAG", "login success");
                    signInSeccessfully();
                }else{
                    Log.d("TAG",e.getMessage());
                    showAlertDialog("Could not sign in. "+ e.getMessage(), false);
                }
            }
        };

        return listener;
    }

    private void signUp(final String email, final String password)
    {
        Model.getInstance().signUp(email, password, new Model.SignupListener() {
            @Override
            public void success() {
                showAlertDialog("You have been signed up successfully", true);
            }

            @Override
            public void fail(String msg) {
                showAlertDialog(msg, false);
            }
        });
    }

    private void showAlertDialog(String message, final boolean successSignin)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if(successSignin)
                    signInSeccessfully();

                return;
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void signInSeccessfully()
    {
        GridViewFragment frag = new GridViewFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.main_frag_container, frag);
        //TODO: check what happens if prees back after list loaded
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

}
