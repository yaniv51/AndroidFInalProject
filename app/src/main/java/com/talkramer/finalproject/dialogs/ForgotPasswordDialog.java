package com.talkramer.finalproject.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.talkramer.finalproject.R;

/**
 * Created by Yaniv on 12/08/2016.
 */
public class ForgotPasswordDialog extends DialogFragment {

    EditText emailText;
    ForgetPasswordLisener delegate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forget_password, null);

        emailText = (EditText) view.findViewById(R.id.email_forget_password_dialog);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
        // Add action buttons
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String email = emailText.getText().toString();
                Log.d("TAG", "send password reset mail to: "+ email);
                delegate.ok(email);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("TAG", "Cancel reset password operation");
                delegate.cancle();
            }
        });
        return builder.create();
    }

    public void setDelegate(ForgetPasswordLisener delegate) {
        this.delegate = delegate;
    }

    public interface ForgetPasswordLisener{
        public void ok(String email);
        public void cancle();
    }
}
