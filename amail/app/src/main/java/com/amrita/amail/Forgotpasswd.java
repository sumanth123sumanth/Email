package com.amrita.amail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Forgotpasswd extends AppCompatActivity {
private EditText user;
private Button verify;
    private FirebaseAuth fbAuth;
    DatabaseReference dref;
    public static String mail,ph_toVerify;
    ProgressDialog progress;
    Intent nxt;
    private boolean exists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpasswd);
        user=(EditText)findViewById(R.id.et_usermailcheck);
        verify = (Button) findViewById(R.id.bt_verify);
        fbAuth = FirebaseAuth.getInstance();
        dref=FirebaseDatabase.getInstance().getReference("users");
        nxt=new Intent(this,PhonenumberVerification.class);
        nxt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail="";ph_toVerify="";
                exists=false;
                showdialog();
              mail=user.getText().toString().trim();
                dref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d:dataSnapshot.getChildren())
                        {
                            if(d.child("username").getValue().toString().equals(mail))
                            {
                                ph_toVerify=d.child("phno").getValue().toString();
                                mail=d.child("username").getValue().toString();
                                exists=true;
                                user.setEnabled(false);
                                canceldialog();
                                startActivity(nxt);
                                break;
                            }
                        }
                        if(exists) {



                        }
                        else
                        {canceldialog();
                            Toast.makeText(getApplicationContext(),"There is no such user",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }



  private  void  showdialog()
    {    progress = new ProgressDialog(this);

        progress.setTitle("Validating user...");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    private  void  canceldialog()
    {
        progress.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent w=new Intent(this,MainActivity.class);
        w.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(w);
    }
}
