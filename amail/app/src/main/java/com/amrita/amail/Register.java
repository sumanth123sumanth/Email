package com.amrita.amail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;import android.app.DatePickerDialog;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.amrita.amail.Forgotpasswd.ph_toVerify;

public class Register extends AppCompatActivity {
private EditText username2,password2,name2,address,phno,dob;
private Button reg,datebt;
DatabaseReference usersdatabase;
private TextView login;
private ProgressBar progressBar3;
private Handler handler;
private  String username="",  password="",name="",adrs="", db="", ph="",id="",mon="";
public static String err="";
private user use;
private boolean userexists=false,phexists=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar3=(ProgressBar)findViewById(R.id.spinreg);
        progressBar3.setVisibility(View.INVISIBLE);
        login=(TextView)findViewById(R.id.login);
        login.setPaintFlags(login.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        username2=(EditText)findViewById(R.id.et_username2);
        password2=(EditText)findViewById(R.id.et_password2);
        name2=(EditText)findViewById(R.id.et_name);
        address=(EditText)findViewById(R.id.et_address);
        phno=(EditText)findViewById(R.id.et_phno);
        dob=(EditText)findViewById(R.id.et_dob);
        dob.setEnabled(false);
        reg=(Button)findViewById(R.id.bt_register2);
        usersdatabase=FirebaseDatabase.getInstance().getReference("users");
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIntent();
            }
        });
        datebt=(Button)findViewById(R.id.bt_date);
        datebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                mon="";
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if((month+1)<10)
                                    mon="0"+(month+1);
                                dob.setText(year+"-"+mon+"-"+day);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
    }
    private void addUser()
    {
          username= username2.getText().toString().trim();
         password=password2.getText().toString().trim();
        name=name2.getText().toString().trim();
         adrs=address.getText().toString().trim();
         dob.setEnabled(false);
         db=dob.getText().toString().trim();
         ph=phno.getText().toString().trim();
         userexists=false;
         phexists=false;
         err="";
        if(!checkUsername(username))
        {
            err+="- Username must start with alphabet\n";
        }
        if(!checkPass(password))
        {
            err+="- Password length must be atleast 6 characters\n";
        }
        if(!checkPhone(ph))
        {
            err+="- Phone number must contain exactly 10 digits\n";
        }
        if(!checkName(name))
        {
            err+="- Name must contain alphabet only ";
        }
        if(!checkCity(adrs))
        {
            err+="- City must contain alphabet only";
        }
        if(!checkUsername(username)|| !checkName(name) || !checkPhone(ph) || !checkPass(password))
        {
            AlertDialog a=new AlertDialog();
            a.show(getSupportFragmentManager(),"alert");
        }

       else if(!username.isEmpty() && !password.isEmpty() && !name.isEmpty() && !adrs.isEmpty() && !db.isEmpty() && !ph.isEmpty()) {

            usersdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        if(ds.child("username").getValue().toString().equals(username))
                        {
                            userexists=true;
                            break;
                        }
                        if(ds.child("phno").getValue().toString().equals(ph))
                        {
                            phexists=true;
                            break;
                        }
                    }
                    if(!userexists && !phexists)
                    {
                         id = usersdatabase.push().getKey();
                         use = new user(username, password, db, adrs, ph, name, id);

                        AuthUI.IdpConfig phoneConfigWithDefaultNumber = new AuthUI.IdpConfig.PhoneBuilder()
                                .setDefaultNumber("+91"+ph)
                                .build();
                        FirebaseAuth auth=FirebaseAuth.getInstance();
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(Arrays.asList(phoneConfigWithDefaultNumber))
                                        .setTheme(R.style.FirebaseUI_TextInputEditText_PhoneField)
                                        .setIsSmartLockEnabled(false)
                                        .build(),
                                1);
                        }
                        else{
                        username2.setEnabled(false);
                        password2.setEnabled(false);
                        name2.setEnabled(false);
                        address.setEnabled(false);
                        dob.setEnabled(false);
                        phno.setEnabled(false);
                        showdialog2();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        else
        {
            Snackbar.make(findViewById(R.id.reg_layout),"Please fill all the deatils",Snackbar.LENGTH_LONG).show();
        }

       // usersdatabase.child(id).setValue(cntcs);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthUI.getInstance()
                        .delete(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ..
                                usersdatabase.child(id).setValue(use);
                                usersdatabase.child(id).child("inbox").setValue("");
                                usersdatabase.child(id).child("contacts").setValue("");
                                usersdatabase.child(id).child("is_kyc").setValue("false");
                                username2.setText("");
                                password2.setText("");
                                name2.setText("");
                                address.setText("");
                                dob.setText("");
                                phno.setText("");
                                showdialog1();
                            }
                        });
                // ...
            } else {
                if(response==null)
                {
                    Intent q=new Intent(getApplicationContext(),Register.class);
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
    private void openIntent()
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }
    private  void  showdialog1() {
        progressBar3.setVisibility(View.VISIBLE);

// Create a Handler instance on the main thread
        handler = new Handler();

// Create and start a new Thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3500);
                } catch (Exception e) {
                } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        progressBar3.setVisibility(View.INVISIBLE);
                        Snackbar.make(findViewById(R.id.reg_layout), "User registered sucessfully!!", Snackbar.LENGTH_LONG).show();
                    }
                });
                // handler.removeCallbacksAndMessages(null);
            }
        }).start();
    }
        private  void  showdialog2()
        {
            progressBar3.setVisibility(View.VISIBLE);
// Create a Handler instance on the main thread
            handler = new Handler();

// Create and start a new Thread
            new Thread(new Runnable() {
                public void run() {
                    try{
                        Thread.sleep(3500);
                    }
                    catch (Exception e) { } // Just catch the InterruptedException

                    // Now we use the Handler to post back to the main thread
                    handler.post(new Runnable() {
                        public void run() {
                            // Set the View's visibility back on the main UI Thread
                            progressBar3.setVisibility(View.INVISIBLE);
                            username2.setEnabled(true);
                            password2.setEnabled(true);
                            name2.setEnabled(true);
                            address.setEnabled(true);
                            dob.setEnabled(true);
                            phno.setEnabled(true);
                            if(userexists)
                            Snackbar.make(findViewById(R.id.reg_layout),"This username is already taken!",Snackbar.LENGTH_LONG).show();
                            else if(phexists)
                                Snackbar.make(findViewById(R.id.reg_layout),"This phone number is already linked to other account",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    // handler.removeCallbacksAndMessages(null);
                }
            }).start();
    }
    private boolean checkUsername(String user)
    {  boolean isValid = false;

        String expression = "^[A-Za-z][A-Za-z0-9]*([_]?[A-Za-z0-9]+)$";
       // CharSequence inputStr = user;

      //  Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
      //  Matcher matcher = pattern.matcher(inputStr);
        if (user.matches(expression)) {
            isValid = true;
        }
        return isValid;
    }
    private boolean checkPhone(String p)
    {
        boolean isValid = false;

        String expression = "^[7-9][0-9]{9}$";
        // CharSequence inputStr = user;

        //  Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        //  Matcher matcher = pattern.matcher(inputStr);
        if (p.matches(expression)) {
            isValid = true;
        }
        return isValid;
    }
    private boolean checkPass(String ps)
    {boolean isValid=false;
        if(ps.length()>=6)
        {
            isValid=true;
        }
        return isValid;
    }
    private boolean checkName(String n)
    {
        boolean isValid = false;

        String expression = "^([A-Za-z]*[' ']*)*$";
        // CharSequence inputStr = user;

        //  Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        //  Matcher matcher = pattern.matcher(inputStr);
        if (n.matches(expression)) {
            isValid = true;
        }
        return isValid;
    }
    private boolean checkCity(String c)
    {
        boolean isValid = false;

        String expression = "^([A-Za-z]*[' ']*)*$";
        // CharSequence inputStr = user;

        //  Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        //  Matcher matcher = pattern.matcher(inputStr);
        if (c.matches(expression)) {
            isValid = true;
        }
        return isValid;
    }

}
