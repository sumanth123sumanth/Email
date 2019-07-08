package com.amrita.amail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import static com.amrita.amail.Forgotpasswd.mail;
import static com.amrita.amail.Forgotpasswd.ph_toVerify;

public class PhonenumberVerification extends AppCompatActivity {
private String vphone,vuser;
 private final  int RC_SIGN_IN=1;
 private TextView userid;
 private EditText pwd,cnf_pwd;
 private Button updt_pwd;
 private String tempid,p1,p2;
 DatabaseReference ref;
 private Intent i;
 static boolean pwd_updt=false;
ProgressDialog progress;
Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber_verification);
        pwd_updt=false;
        vuser=mail;
       // Toast.makeText(getApplicationContext(),vuser,Toast.LENGTH_SHORT).show();
        AuthUI.IdpConfig phoneConfigWithDefaultNumber = new AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultNumber("+91"+ph_toVerify)
                .build();
       FirebaseAuth auth=FirebaseAuth.getInstance();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(phoneConfigWithDefaultNumber))
                        .setTheme(R.style.FirebaseUI_TextInputEditText_PhoneField)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
        //
        //
        i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        userid=(TextView)findViewById(R.id.tv_userchngmail);
        pwd=(EditText)findViewById(R.id.et_enter_password);
        cnf_pwd=(EditText)findViewById(R.id.et_cnf_password);
        userid.setText(vuser+"@amail.com");
        updt_pwd=(Button)findViewById(R.id.bt_passwd_update);
        updt_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 p1=pwd.getText().toString().trim();
                p2=cnf_pwd.getText().toString().trim();
                if(p1.equals(p2))
                {
                    if(p1.length()>=6)
                    {
                        ref=FirebaseDatabase.getInstance().getReference("users");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot s:dataSnapshot.getChildren())
                                {
                                    if(s.child("username").getValue().toString().equals(vuser))
                                    {
                                        tempid=s.child("uid").getValue().toString();pwd_updt=true;
                                    }

                                }
                                ref.child(tempid).child("password").setValue(p1);
                                startActivity(i);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password length must be atleast 6 charecters",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthUI.getInstance()
                        .delete(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ..
                            }
                        });
                // ...
            } else {
                if(response==null)
                {
                    Intent q=new Intent(getApplicationContext(),Forgotpasswd.class);
                    q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(q);
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    public void onBackPressed() {

            Intent q=new Intent(this,Forgotpasswd.class);
            q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(q);

    }



}
