package com.amrita.amail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.amrita.amail.PhonenumberVerification.pwd_updt;
import static com.amrita.amail.afterLogin.logout;


public class MainActivity extends AppCompatActivity {
private EditText username,passwd;
private Button login;
private DatabaseReference firebaseDatabase;
private String usernm,pass;
private int count=0;
private TextView signup,forgotpasswd;
private String currentuid,curramail,curruname;
private ProgressBar progress;
private Handler handler;
   static SharedPreferences pref;
  static   SharedPreferences.Editor editor;
public static final String Name="name";
    public static final String Username="username";
    public static final String Uid="uid";
    public static final String isLoggged="isLogged";
public static boolean fromLogin=false,onresume=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putBoolean(isLoggged,false);
        if(pref.getBoolean(isLoggged,false))
        {
            onresume=true;
            Intent ia=new Intent(this,afterLogin.class);
            ia.putExtra("cuid",pref.getString(Uid,null));
            ia.putExtra("camail",pref.getString(Username,null));
            ia.putExtra("cuname",pref.getString(Name,null));
            ia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(ia);
        }
        if(logout)
        {
            logout=false;
            Snackbar.make(findViewById(R.id.login_lay),"Logged out sucessfully!!",Snackbar.LENGTH_LONG).show();
        }
        else if(pwd_updt)
        {
            pwd_updt=false;
            Snackbar.make(findViewById(R.id.login_lay),"Password updated sucessfully!!",Snackbar.LENGTH_LONG).show();
        }

        signup=(TextView)findViewById(R.id.signup);
        forgotpasswd=(TextView)findViewById(R.id.tv_frgt);
        forgotpasswd.setPaintFlags(forgotpasswd.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        signup.setPaintFlags(signup.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        username=(EditText)findViewById(R.id.et_username);
        passwd=(EditText)findViewById(R.id.et_password);
        login=(Button)findViewById(R.id.bt_login);
        progress=(ProgressBar)findViewById(R.id.spin);
        progress.setVisibility(View.INVISIBLE);
        firebaseDatabase=FirebaseDatabase.getInstance().getReference("users");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setEnabled(false);
                passwd.setEnabled(false);
                login.setClickable(false);
                signup.setClickable(false);
                progress.setVisibility(View.VISIBLE);
                    loginUser();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIntent();
            }
        });
        forgotpasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVerification();
            }
        });
    }
    private void openLogin()
    {fromLogin=true;

        Intent i=new Intent(this,afterLogin.class);
        i.putExtra("cuid",currentuid);
        i.putExtra("camail",curramail);
        i.putExtra("cuname",curruname);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        //Toast.makeText(this, ""+currentuid, Toast.LENGTH_SHORT).show();
    }
    private void openIntent()
    {   fromLogin=true;
        Intent intent=new Intent(this,Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void openVerification()
    {
        Intent intent1=new Intent(this,Forgotpasswd.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }
    private void loginUser()
    {

          usernm=username.getText().toString().trim();
          pass=passwd.getText().toString().trim();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if((snapshot.child("username").getValue().toString().compareTo(usernm)==0)&&(snapshot.child("password").getValue().toString().compareTo(pass)==0))
                    {
                        currentuid=snapshot.child("uid").getValue().toString();
                        curruname=snapshot.child("name").getValue().toString();
                        curramail=snapshot.child("username").getValue().toString()+"@amail.com";
                        count++;
                        editor.putBoolean(isLoggged,true);
                        editor.putString(Uid,currentuid);
                        editor.putString(Name,curruname);
                        editor.putString(Username,curramail);
                        editor.commit();
                    }
                }

                showdialog1();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showDialog()
    {
        Snackbar.make(findViewById(R.id.login_lay),"Invalid Username or Password",Snackbar.LENGTH_LONG).show();
    }

    private  void  showdialog1()
    {
// Create a Handler instance on the main thread
         handler = new Handler();

// Create and start a new Thread
       new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(3000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        progress.setVisibility(View.INVISIBLE);
                        if(count==1)
                        {
                            openLogin();
                        }
                        else {
                            username.setEnabled(true);
                            passwd.setEnabled(true);
                            login.setClickable(true);
                            signup.setClickable(true);
                            showDialog();
                        }
                    }
                });
               // handler.removeCallbacksAndMessages(null);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    private void cancelDialog()
    {

        progress.setVisibility(View.INVISIBLE);
    }
}
