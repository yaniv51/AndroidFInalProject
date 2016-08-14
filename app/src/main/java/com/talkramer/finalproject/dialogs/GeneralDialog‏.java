package com.talkramer.finalproject.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.talkramer.finalproject.R;

/**
 * Created by TAL on 14/08/2016.
 */
public class GeneralDialog‏ extends DialogFragment {

    GeneralDialogdLisener delegate;
    public String title, msg;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.getTitle());
        builder.setMessage(this.getMsg());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Log.d("TAG", "general dialog - OK");
                delegate.ok();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("TAG", "general dialog - CANCEL");
                        delegate.cancle();
                    }
                });
        return builder.create();
    }

    public void setDelegate(GeneralDialogdLisener delegate) {
        this.delegate = delegate;
    }

    public interface GeneralDialogdLisener{
        void ok();
        void cancle();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }
}