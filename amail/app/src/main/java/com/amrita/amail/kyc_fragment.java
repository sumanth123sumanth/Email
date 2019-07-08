package com.amrita.amail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import static com.amrita.amail.afterLogin.cuid;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public  class kyc_fragment extends Fragment {
    private Button sc;
    private TextView tvver;
    static String kyc_ver="";
   DatabaseReference kr;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.kyc_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       sc=(Button)view.findViewById(R.id.bt_go);
       tvver=(TextView)view.findViewById(R.id.tv_isverified);
       tvver.setVisibility(View.VISIBLE);
       sc.setVisibility(View.INVISIBLE);
        kr=FirebaseDatabase.getInstance().getReference("users").child(cuid);
       kr.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               kyc_ver=dataSnapshot.child("is_kyc").getValue().toString();
               if(kyc_ver.equals("true")) {
                   tvver.setTextColor(Color.rgb(0,255,0));
                   tvver.setText("Verified");
               }
               else
               {    sc.setVisibility(View.VISIBLE);
                   tvver.setTextColor(Color.rgb(255,0,0));
                   tvver.setText("Not verified");
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       sc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent a=new Intent(getActivity(),kyctest.class);
               startActivity(a);
           }
       });
    }



}
