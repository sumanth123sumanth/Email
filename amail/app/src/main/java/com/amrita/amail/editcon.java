package com.amrita.amail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.amrita.amail.afterLogin.cuid;

public class editcon extends AppCompatActivity {
private TextView m;
private EditText n,p;
private Button b,d;
private String eu,en,ep,nn,pp,key;
DatabaseReference g;
boolean isn=false,isp=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcon);
        m=(TextView)findViewById(R.id.edmail);
        p=(EditText) findViewById(R.id.edphone);
        n=(EditText)findViewById(R.id.edname);
        b=(Button)findViewById(R.id.esave);
        d=(Button)findViewById(R.id.eedit);
        g= FirebaseDatabase.getInstance().getReference("users").child(cuid).child("contacts");
        eu=getIntent().getStringExtra("username");
        en=getIntent().getStringExtra("name");
        ep=getIntent().getStringExtra("phno");
        m.setText(eu);
        p.setText(ep);
        n.setText(en);
        p.setEnabled(false);n.setEnabled(false);
        b.setEnabled(false);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setEnabled(true);
                n.setEnabled(true);
                b.setEnabled(true);
                d.setEnabled(false);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setEnabled(true);
                p.setEnabled(false);
                n.setEnabled(false);
                pp=p.getText().toString().trim();
                nn=n.getText().toString().trim();
                Register.err="";
                if(!checkName(nn))
                {
                    Register.err+="- Name must contain alphabet only ";
                }
                if(!checkPhone(pp))
                {
                    Register.err+="- Phone number must contain exactly 10 digits\n";
                }
                if(!checkPhone(pp) || !checkName(nn))
                {
                    AlertDialog a1=new AlertDialog();
                    a1.show(getSupportFragmentManager(),"alert");
                    p.setText(ep);n.setText(en);
                    Toast.makeText(getApplicationContext(),pp+nn,Toast.LENGTH_SHORT).show();
                }
                else if(!pp.isEmpty() && !nn.isEmpty())
                {
                    g.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot m:dataSnapshot.getChildren())
                            {
                                if(m.child("name").getValue().toString().equals(nn))
                                {
                                    isn=true;
                                }
                                if(m.child("phno").getValue().toString().equals(nn))
                                {
                                    isp=true;
                                }

                            }
                            if(isn){ //Toast.makeText(getApplicationContext(),"Contact name alredy exists",Toast.LENGTH_SHORT).show(); p.setText(ep);
                                n.setText(en);isn=false;}
                            if(isp){ //Toast.makeText(getApplicationContext(),"Contact phone alredy exists",Toast.LENGTH_SHORT).show();p.setText(ep);
                                n.setText(en);isp=false;}
                            if(isn && isp) {Toast.makeText(getApplicationContext(),"Contact name and phone alredy exists",Toast.LENGTH_SHORT).show();p.setText(ep);
                                n.setText(en);isn=false;isp=false;}
                            else if(!isn && !isp){
                                g.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot f:dataSnapshot.getChildren())
                                        {
                                            if(f.child("name").getValue().toString().equals(en))
                                            {
                                                key=f.getKey();
                                                g.child(key).child("name").setValue(nn);
                                                g.child(key).child("phno").setValue(pp);
                                                Toast.makeText(getApplicationContext(),"Contact saved",Toast.LENGTH_SHORT).show();
                                                p.setText(pp);
                                                n.setText(nn);
                                                break;
                                            }
                                        }
                                       // Toast.makeText(getApplicationContext(),eu,Toast.LENGTH_SHORT).show();
                                       // g.child(key).child("name").setValue(nn);
                                        //g.child(key).child("phno").setValue(pp);
                                        //Toast.makeText(getApplicationContext(),"Contact saved",Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
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
}
