package com.amrita.amail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.amrita.amail.afterLogin.cuid;

public class addcont_tab extends Fragment {

    private EditText email;
    private Button add;
    private DatabaseReference fire;
   private String i,ni,username4,p,n;
   private int a=0;
   private  boolean b=false,cb=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.addcont_tab,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       // super.onViewCreated(view, savedInstanceState);
        username4="";i="";ni="";
        email=(EditText)view.findViewById(R.id.et_email);
        fire=FirebaseDatabase.getInstance().getReference("users");
        add=(Button)view.findViewById(R.id.bt_addcon);
        add.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(final View view) {
                a = 0;b=false;cb=false;
                username4 = email.getText().toString().trim();
                if (username4.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter username!", Toast.LENGTH_LONG).show();
                } else {
                    fire.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot sh : dataSnapshot.getChildren()) {
                                if (sh.child("username").getValue().toString().equals(username4)) {
                                    b = true;
                                    i = sh.child("uid").getValue().toString();
                                    ni = sh.child("username").getValue().toString();
                                    p=sh.child("phno").getValue().toString();
                                    n=sh.child("name").getValue().toString();
                                    if (!i.isEmpty() && !ni.isEmpty()) {
                                        if (!cuid.equals(i)) {

                                            fire.child(cuid).child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                        if (d.child("username").getValue().toString().equals(username4)) {
                                                            a = 1;
                                                            break;
                                                        }
                                                    }
                                                    if (a == 0) {
                                                        String k = fire.child(cuid).child("contacts").push().getKey();
                                                        HashMap<String, Object> s = new HashMap<>();
                                                        s.put("username", ni);
                                                        s.put("uid", i);
                                                        s.put("name",n);
                                                        s.put("phno",p);
                                                        fire.child(cuid).child("contacts").child(k).setValue(s);
                                                        Toast.makeText(getContext(), "Contact added successfully!", Toast.LENGTH_LONG).show();
                                                        email.setText("");
                                                    }
                                                    else if(a==1) Toast.makeText(getContext(), "Contact already exists!", Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                        else{
                                              cb=true;

                                        }

                                    }
                                    break;
                                }
                            }
                            if (b == false) {
                                Toast.makeText(getContext(), "User doesn't exist!", Toast.LENGTH_LONG).show();
                            }

                            if(cb==true){Toast.makeText(getContext(),"Cannot add yourself",Toast.LENGTH_LONG).show();}
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

            }


        }

