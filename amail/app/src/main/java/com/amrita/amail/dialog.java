package com.amrita.amail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;




public class dialog extends AppCompatActivity {
private TextView nametv,email,phone;
private Button send;
private String u,n,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        nametv=(TextView)findViewById(R.id.tv_cont_dialog_name);
        email=(TextView)findViewById(R.id.tv_cont_dialog_email);
        phone=(TextView)findViewById(R.id.tv_cont_dialog_phone);
        send=(Button)findViewById(R.id.bt_cont_dialog_send);
        u=getIntent().getStringExtra("uname");
        n=getIntent().getStringExtra("name");
        p=getIntent().getStringExtra("phno");
        email.setText(u+"@amail.com");
        nametv.setText(n);
        phone.setText("+91"+p);
    }


}
