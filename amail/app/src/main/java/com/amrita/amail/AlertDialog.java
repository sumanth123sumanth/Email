package com.amrita.amail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import static com.amrita.amail.Register.err;

public class AlertDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder ad=new android.app.AlertDialog.Builder(getActivity());
        ad.setTitle("Validity criteria")
                .setMessage(err)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                return ad.create();
    }
}
