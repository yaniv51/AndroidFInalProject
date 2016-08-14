package com.talkramer.finalproject.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Yaniv on 08/06/16.
 */
public class SaveOperationDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Save Operation");
        builder.setMessage("Your product has been updated");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("TAG", "OK");
                delegate.ok();
            }
        });

        return builder.create();
    }

    public interface Delegate{
        public void ok();
    }

    Delegate delegate;


    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }
}
