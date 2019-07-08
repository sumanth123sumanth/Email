package com.amrita.amail;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.amrita.amail.afterLogin.cml;
import static com.amrita.amail.afterLogin.cuid;
import static com.amrita.amail.afterLogin.cunm;
import static com.amrita.amail.allmails.fromallmails;
import static com.amrita.amail.cont_tab.fromconttab;


public class Main2Activity extends AppCompatActivity {
    ChipsInput chipsInput;
    private TextView from;
    private ArrayList<String> sendto=new ArrayList<String>();
    private EditText sub,msg;
    DatabaseReference d;
    private String to,toid,s,m;
    String ufromall;
    boolean draf=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ufromall=getIntent().getStringExtra("user");
        Toolbar sb=(Toolbar)findViewById(R.id.send);
        sb.setTitle("Send");
        sb.setTitleTextColor(Color.rgb(255,255,255));
        setSupportActionBar(sb);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chipsInput = (ChipsInput) findViewById(R.id.chips_input);
        from=(TextView)findViewById(R.id.from);
        from.setText(cunm);
        sub=(EditText)findViewById(R.id.sub);
        msg=(EditText)findViewById(R.id.message);

        d= FirebaseDatabase.getInstance().getReference("users");
        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                // chip added
                // newSize is the size of the updated selected chip list
                sendto.add(chip.getLabel());

            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                // chip removed
                // newSize is the size of the updated selected chip list
                sendto.remove(chip.getLabel());
            }

            @Override
            public void onTextChanged(CharSequence text) {
                // text changed
                if(text.toString().matches("^[A-Za-z][A-Za-z0-9]*@amail.com$"))
                {
                    chipsInput.addChip(text.toString().split("@")[0], text.toString());
                }
            }

        });
        chipsInput.setShowChipDetailed(false);
        if(fromallmails)
        {
            chipsInput.addChip(ufromall,ufromall+"@amail.com");
            fromallmails=false;
        }
        if(fromconttab)
        {
            chipsInput.addChip(ufromall,ufromall+"@amail.com");
            fromconttab=false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId())
            { case android.R.id.home:
                    this.finish();
                case R.id.sendmail:
                    sendmail();
                   // Toast.makeText(getApplicationContext(),sendto.get(0),Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
    }
    private void sendmail()
    {
         s=sub.getText().toString().trim();
         m=msg.getText().toString().trim();
        for(int i=0;i<sendto.size();i++)
        {
             to=sendto.get(i);


                   d.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           draf=true;
                           for(DataSnapshot s:dataSnapshot.getChildren())
                           {
                               if(to.equals(cml))
                               {
                                   draf=false;
                                   break;
                               }
                           }

                                   for(DataSnapshot ds:dataSnapshot.getChildren())
                                   {
                                       if(to.equals(cml.split("@")[0]) || draf)
                                       {
                                           // Toast.makeText(getApplicationContext(),cml.split("@")[0]+to,Toast.LENGTH_SHORT).show();
                                           toid=ds.child("uid").getValue().toString();
                                           String k = d.child(cuid).child("drafts").push().getKey();
                                           d.child(cuid).child("drafts").child(k).child("from").setValue(cml.split("@")[0]);
                                           d.child(cuid).child("drafts").child(k).child("to").setValue(to);
                                           d.child(cuid).child("drafts").child(k).child("isread").setValue("false");
                                           d.child(cuid).child("drafts").child(k).child("sub").setValue(s);
                                           d.child(cuid).child("drafts").child(k).child("msg").setValue(m);
                                           Calendar cal = Calendar.getInstance();
                                           Date currentLocalTime = cal.getTime();
                                           DateFormat date = new SimpleDateFormat("hh:mm:ss a");
// you can get seconds by adding  "...:ss" to it
                                           date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                                           DateFormat da=new SimpleDateFormat("dd/MM/yyyy");
                                           String dae=da.format(currentLocalTime);
                                           String localTime = date.format(currentLocalTime);
                                           d.child(cuid).child("drafts").child(k).child("time").setValue(localTime);
                                           d.child(cuid).child("drafts").child(k).child("date").setValue(dae);
                                           Toast.makeText(getApplicationContext(),"Mail saved as draft",Toast.LENGTH_SHORT).show();
                                           break;
                                       }
                                       else if(ds.child("username").getValue().toString().equals(to))
                                       {

                                           toid=ds.child("uid").getValue().toString();
                                           String k = d.child(toid).child("inbox").push().getKey();
                                           d.child(toid).child("inbox").child(k).child("from").setValue(cml.split("@")[0]);
                                           d.child(toid).child("inbox").child(k).child("to").setValue(to);
                                           d.child(toid).child("inbox").child(k).child("isread").setValue("false");
                                           d.child(toid).child("inbox").child(k).child("sub").setValue(s);
                                           d.child(toid).child("inbox").child(k).child("msg").setValue(m);
                                           Calendar cal = Calendar.getInstance();
                                           Date currentLocalTime = cal.getTime();
                                           DateFormat date = new SimpleDateFormat("hh:mm:ss a");
// you can get seconds by adding  "...:ss" to it
                                           date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                                           DateFormat da=new SimpleDateFormat("dd/MM/yyyy");
                                           String dae=da.format(currentLocalTime);
                                           String localTime = date.format(currentLocalTime);
                                           d.child(toid).child("inbox").child(k).child("time").setValue(localTime);
                                           d.child(toid).child("inbox").child(k).child("date").setValue(dae);
                                           Toast.makeText(getApplicationContext(),"Mail sent",Toast.LENGTH_SHORT).show();
                                           break;
                                       }
                                   }
                                   sub.setText("");
                                   msg.setText("");
                                   for(int i=0;i<sendto.size();i++)
                                   {
                                       chipsInput.removeChipByLabel(sendto.get(i));
                                   }


                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

                }


        }
    }

