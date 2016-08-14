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
import android.widget.EditText;
import android.widget.ProgressBar;

import com.talkramer.finalproject.R;
import com.talkramer.finalproject.activities.MainActivity;
import com.talkramer.finalproject.dialogs.ForgotPasswordDialog;
import com.talkramer.finalproject.model.Model;
import com.talkramer.finalproject.model.Utils.Helper;

public class SignInFragment extends Fragment {

    private View view;
    private EditText emailext, passwordText;
    private Button signInButton, registerButton, forgotPasswordButton;
    private ProgressBar progressBar;

    public SignInFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().hide();

        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        signInButton = (Button) view.findViewById(R.id.signInButton_signIn_fragment);
        registerButton = (Button) view.findViewById(R.id.registerButton_signIn_fragment);
        forgotPasswordButton = (Button) view.findViewById(R.id.forgotPassword_signIn_fragment);

        emailext = (EditText) view.findViewById(R.id.email_signIn_fragment);
        passwordText = (EditText) view.findViewById(R.id.password_signIn_fragment);
        progressBar = (ProgressBar) view.findViewById(R.id.signin_progressbar);

        emailext.setHint(getResources().getString(R.string.email));
        passwordText.setHint(getResources().getString(R.string.password));

        //emailext.clearFocus();
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

                if(email.compareTo("") == 0) {
                    showAlertDialog("Please type an email", false);
                    return;
                }
                if(password.compareTo("") == 0)
                {
                    showAlertDialog("Please type a valid password", false);
                    return;
                }

                signIn(email, password);
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordDialog dialog = new ForgotPasswordDialog();
                dialog.setDelegate(new ForgotPasswordDialog.ForgetPasswordLisener() {
                    @Override
                    public void ok(String email) {
                        progressBar.setVisibility(View.VISIBLE);
                        Model.getInstance().resetPassword(email, new Model.OperationListener() {
                            @Override
                            public void success() {
                                progressBar.setVisibility(View.GONE);
                                showAlertDialog("Please check your email", false);
                            }

                            @Override
                            public void fail(String msg) {
                                progressBar.setVisibility(View.GONE);
                                showAlertDialog("Could not reset Password. " + msg, false);
                            }
                        });
                    }

                    @Override
                    public void cancle() {
                        return;
                    }
                });
                dialog.show(getFragmentManager(), "GGG");
            }
        });

        return view;
    }

    private void signIn(final String email, final String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        setButtonsEnable(false);
        Model.getInstance().login(email, password, createAuthListener(email, password));
    }

    private void signUp(final String email, final String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        setButtonsEnable(false);
        Model.getInstance().signUp(email, password, new Model.OperationListener() {
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

    private Model.AuthListener createAuthListener(final String email, final String password)
    {
        Model.AuthListener listener = new Model.AuthListener() {
            @Override
            public void onDone(String userId, Exception e) {
                if (e == null){
                    Log.d("TAG", "login success");
                    progressBar.setVisibility(View.GONE);
                    openProductsFragment();
                }else{
                    Log.d("TAG",e.getMessage());
                    showAlertDialog("Could not sign in. "+ e.getMessage(), false);
                }
            }
        };

        return listener;
    }

    private void showAlertDialog(String message, final boolean successSignin)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //if successfully logged in, open products fragment
                if(successSignin)
                    openProductsFragment();
                else //otherwise, enable sign in/register
                    setButtonsEnable(true);

                return;
            }
        });

        progressBar.setVisibility(View.GONE);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openProductsFragment()
    {
        GridViewFragment frag = new GridViewFragment();
        frag.setFilter(Helper.GridProductFilter.ALL_PRODUCTS);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.main_frag_container, frag, Helper.GRID_VIEW_NO_FILTER_TAG);
        // Commit the transaction
        transaction.commit();
    }

    private void setButtonsEnable(boolean enable)
    {
        emailext.setEnabled(enable);
        passwordText.setEnabled(enable);
        signInButton.setEnabled(enable);
        registerButton.setEnabled(enable);
        forgotPasswordButton.setEnabled(enable);
    }

}
