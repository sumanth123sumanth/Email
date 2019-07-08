package com.amrita.amail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Unead extends AppCompatActivity {
String f1,s1,m1,d1,t1;
TextView uf,ud,ut,us,um;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unead);
        f1=getIntent().getStringExtra("from");
        s1=getIntent().getStringExtra("sub");
        m1=getIntent().getStringExtra("msg");
        d1=getIntent().getStringExtra("date");
        t1=getIntent().getStringExtra("time");
        uf=(TextView)findViewById(R.id.un_from);
        ud=(TextView)findViewById(R.id.un_date);
        ut=(TextView)findViewById(R.id.un_time);
        us=(TextView)findViewById(R.id.un_sub);
        um=(TextView)findViewById(R.id.un_msg);
        uf.setText(f1);
        us.setText(s1);
        um.setText(m1);
        ud.setText(d1);
        ut.setText(t1);
    }

    @Override
    public void onBackPressed() {
        Intent a=new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
