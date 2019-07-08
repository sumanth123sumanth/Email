package com.amrita.amail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.amrita.amail.Register.err;
import static com.amrita.amail.afterLogin.cuid;

public class Personal_info_fragment extends Fragment {
    private EditText ad,nm,dob,ph,pwd;
    private Button ed,sv,dob2;
    DatabaseReference set;
    private String sad,snm,sdob,sph,spwd;
    private String password,phn,adrs,name;
    private Handler handler;
    private ProgressBar progress1;
    private String ea,eph,epwd,edob,enm;
    public static boolean pexists=false;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_info,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ad=(EditText)view.findViewById(R.id.et_add_pinfo);
        nm=(EditText)view.findViewById(R.id.et_name_pinfo);
        dob=(EditText)view.findViewById(R.id.et_dob_pinfo);
        ph=(EditText)view.findViewById(R.id.et_phone_pinfo);
        pwd=(EditText)view.findViewById(R.id.et_pass_pinfo);
        dob2=(Button)view.findViewById(R.id.perinfo_dob);
        progress1=(ProgressBar)view.findViewById(R.id.profilespin);
        progress1.setVisibility(View.INVISIBLE);
        setFalse();
        set=FirebaseDatabase.getInstance().getReference("users");
        dob.setEnabled(false);
        dob2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dob.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });



        set.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot x:dataSnapshot.getChildren())
                {
                    if(x.child("uid").getValue().toString().compareTo(cuid)==0)
                    {
                        sad=x.child("address").getValue().toString();
                        snm=x.child("name").getValue().toString();
                        sdob=x.child("dob").getValue().toString();
                        sph=x.child("phno").getValue().toString();
                        spwd=x.child("password").getValue().toString();
                        ad.setText(sad);
                        nm.setText(snm);
                        dob.setText(sdob);
                        ph.setText(sph);
                        pwd.setText(spwd);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ed=(Button)view.findViewById(R.id.bt_edit);
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTrue();
            }
        });
        sv=(Button)view.findViewById(R.id.bt_save);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v=view;
                password=pwd.getText().toString().trim();
                phn=ph.getText().toString().trim();
                name=nm.getText().toString().trim();
                adrs=ad.getText().toString().trim();
                err="";pexists=false;
                if(!checkPass(password))
                {
                    err+="- Password length must be atleast 6 characters\n";
                }
                if(!checkPhone(phn))
                {
                    err+="- Phone number must contain exactly 10 digits\n";
                }
                if(!checkName(name))
                {
                    err+="- Name must contain alphabet only\n ";
                }
                if(!checkCity(adrs))
                {
                    err+="- City must contain alphabet only\n";
                }
                if( !checkName(name) || !checkPhone(phn) || !checkPass(password)|| !checkCity(adrs))
                {
                    AlertDialog a=new AlertDialog();
                    a.show(getFragmentManager(),"alert");
                }
                else if(!password.isEmpty() && !phn.isEmpty() && !name.isEmpty() && !adrs.isEmpty()){
                    setFalse();
                    ea = ad.getText().toString().trim();
                     eph = ph.getText().toString().trim();
                     epwd = pwd.getText().toString().trim();
                     edob = dob.getText().toString().trim();
                     enm = nm.getText().toString().trim();
                    if (!ea.isEmpty() && !eph.isEmpty() && !epwd.isEmpty() && !edob.isEmpty() && !enm.isEmpty()) {

                            set.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds:dataSnapshot.getChildren())
                                    {
                                        if(ds.child("phno").getValue().toString().equals(eph))
                                        {
                                            pexists=true;
                                            break;
                                        }
                                    }

                                    if(pexists && !eph.equals(sph)) {
                                        ph.setText(sph);
                                        Snackbar.make(getActivity().findViewById(R.id.per_info_lay), "This phone number is already linked to other account", Snackbar.LENGTH_LONG).show();
                                    }
                                    else if(!eph.equals(sph)) {
                                        AuthUI.IdpConfig phoneConfigWithDefaultNumber = new AuthUI.IdpConfig.PhoneBuilder()
                                                .setDefaultNumber("+91" + eph)
                                                .build();
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        startActivityForResult(
                                                AuthUI.getInstance()
                                                        .createSignInIntentBuilder()
                                                        .setAvailableProviders(Arrays.asList(phoneConfigWithDefaultNumber))
                                                        .setTheme(R.style.FirebaseUI_TextInputEditText_PhoneField)
                                                        .setIsSmartLockEnabled(false)
                                                        .build(),
                                                1);


                                    }
                                    else if((eph.equals(sph))&& (!enm.equals(snm) || !edob.equals(sdob) || !ea.equals(sad))){
                                        set.child(cuid).child("is_kyc").setValue("false");
                                        set.child(cuid).child("address").setValue(ea);
                                        set.child(cuid).child("password").setValue(epwd);
                                        set.child(cuid).child("dob").setValue(edob);
                                        set.child(cuid).child("name").setValue(enm);
                                        progress1.setVisibility(View.VISIBLE);
                                        handler = new Handler();

// Create and start a new Thread
                                        new Thread(new Runnable() {
                                            public void run() {
                                                try{
                                                    Thread.sleep(2000);
                                                }
                                                catch (Exception e) { } // Just catch the InterruptedException

                                                // Now we use the Handler to post back to the main thread
                                                handler.post(new Runnable() {
                                                    public void run() {
                                                        // Set the View's visibility back on the main UI Thread
                                                        progress1.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(getContext(),"Details saved successfully!",Toast.LENGTH_SHORT).show();
                                                        Snackbar snackbar=Snackbar.make(getActivity().findViewById(R.id.fragment_container),"Details changed\nPlease update your KYC!",Snackbar.LENGTH_INDEFINITE).setAction("UPDATE", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new kyc_fragment()).commit();
                                                            }
                                                        });
                                                        snackbar.setActionTextColor(Color.argb(255,86,120,248));
                                                        snackbar.setDuration(2000);
                                                        snackbar.show();
                                                    }
                                                });
                                                // handler.removeCallbacksAndMessages(null);
                                            }
                                        }).start();

                                    }
                                    else if(eph.equals(sph)){
                                        set.child(cuid).child("address").setValue(ea);
                                        set.child(cuid).child("password").setValue(epwd);
                                        set.child(cuid).child("dob").setValue(edob);
                                        set.child(cuid).child("name").setValue(enm);
                                        showdialog1();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    } else {
                        Toast.makeText(getContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar.make(view.findViewById(R.id.per_info_lay),"Please fill all the deatils",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthUI.getInstance()
                        .delete(getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!ea.equals(sad)|| !edob.equals(sdob) || !enm.equals(snm)) {
                                    set.child(cuid).child("is_kyc").setValue("false");
                                    set.child(cuid).child("address").setValue(ea);
                                    set.child(cuid).child("phno").setValue(eph);
                                    set.child(cuid).child("password").setValue(epwd);
                                    set.child(cuid).child("dob").setValue(edob);
                                    set.child(cuid).child("name").setValue(enm);
                                    progress1.setVisibility(View.VISIBLE);
                                    handler = new Handler();

// Create and start a new Thread
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try{
                                                Thread.sleep(2000);
                                            }
                                            catch (Exception e) { } // Just catch the InterruptedException

                                            // Now we use the Handler to post back to the main thread
                                            handler.post(new Runnable() {
                                                public void run() {
                                                    // Set the View's visibility back on the main UI Thread
                                                    progress1.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getContext(),"Details saved successfully!",Toast.LENGTH_SHORT).show();
                                                    Snackbar snackbar=Snackbar.make(getActivity().findViewById(R.id.fragment_container),"Update KYC details!",Snackbar.LENGTH_INDEFINITE).setAction("UPDATE", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new kyc_fragment()).commit();
                                                        }
                                                    });
                                                    snackbar.setActionTextColor(Color.argb(255,86,120,248));
                                                    snackbar.setDuration(2000);
                                                    snackbar.show();
                                                }
                                            });
                                            // handler.removeCallbacksAndMessages(null);
                                        }
                                    }).start();

                                }
                                else{
                                    set.child(cuid).child("address").setValue(ea);
                                    set.child(cuid).child("phno").setValue(eph);
                                    set.child(cuid).child("password").setValue(epwd);
                                    set.child(cuid).child("dob").setValue(edob);
                                    set.child(cuid).child("name").setValue(enm);
                                    showdialog1();
                                }
                            }
                        });
                // ...
            } else {
                if(response==null)
                {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,new Personal_info_fragment()).commit();
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void setFalse()
    {   dob2.setEnabled(false);
        ad.setEnabled(false);
        nm.setEnabled(false);
        dob.setEnabled(false);
        ph.setEnabled(false);
        pwd.setEnabled(false);
    }
    private void setTrue()
    {   dob2.setEnabled(true);
        ad.setEnabled(true);
        nm.setEnabled(true);
        ph.setEnabled(true);
        pwd.setEnabled(true);
    }

    private  void  showdialog1()
    {
        progress1.setVisibility(View.VISIBLE);
// Create a Handler instance on the main thread
        handler = new Handler();

// Create and start a new Thread
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(2000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        progress1.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(),"Details saved successfully!",Toast.LENGTH_SHORT).show();

                    }
                });
                // handler.removeCallbacksAndMessages(null);
            }
        }).start();
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
