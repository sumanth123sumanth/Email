package com.amrita.amail;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.amrita.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialOverlayLayout;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.Collections;

import static com.amrita.amail.afterLogin.cuid;
import static com.amrita.amail.drafts_fragment.fromdrafts;

public class allmails extends AppCompatActivity {
private ArrayList<Message> m;
private RecyclerView recyclerView;
private RecyclerViewAdapter ra;
private String s,f2,d2,t2,s2,m2;
static  boolean fromallmails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmails);
        fromallmails=false;
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView=findViewById(R.id.list_recview);
        m=new ArrayList<>();
        s=getIntent().getStringExtra("user");
     //   Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference a= FirebaseDatabase.getInstance().getReference("users").child(cuid).child("inbox");
        if(fromdrafts)
        {
             a= FirebaseDatabase.getInstance().getReference("users").child(cuid).child("drafts");
            fromdrafts=false;
        }
                a.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                m.clear();
                for(DataSnapshot f:dataSnapshot.getChildren()){
                    if(f.child("from").getValue().toString().equals(s))
                    {
                        f2=f.child("from").getValue().toString();
                        d2=f.child("date").getValue().toString();
                        t2=f.child("time").getValue().toString();
                        s2=f.child("sub").getValue().toString();
                        m2=f.child("msg").getValue().toString();
                        Message ms=new Message();
                        ms.setFrom(f2);
                        ms.setDate(d2);
                        ms.setTimestamp(t2);
                        ms.setSubject(s2);
                        ms.setMessage(m2);
                        m.add(ms);
                    }

                }
                Collections.sort(m);
                ra=new RecyclerViewAdapter(m,getApplicationContext());
                recyclerView.setAdapter(ra);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         final SpeedDialView mSpeedDialView=findViewById(R.id.speedDial_all);
        SpeedDialOverlayLayout overlayLayout = findViewById(R.id.overlay_all);
        mSpeedDialView.setOverlayLayout(overlayLayout);
        mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.compose_all,R.drawable.ic_edit_blue_24dp)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()))
                .setLabel("Compose")
                .setLabelColor(Color.rgb(255,255,255))
                .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.label_back,getTheme()))
                .setLabelClickable(false)
                .create()
        );
        mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch(actionItem.getId())
                {
                    case R.id.compose_all:
                        mSpeedDialView.close();
                        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                        i.putExtra("user",s);
                        fromallmails=true;
                        startActivity(i);
                        break;
                }
                return  true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent r=new Intent(this,MainActivity.class);
        r.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(r);
    }
}
