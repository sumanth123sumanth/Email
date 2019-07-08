package com.amrita.amail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amrita.model.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialOverlayLayout;
import com.leinardi.android.speeddial.SpeedDialView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.amrita.amail.MainActivity.Name;
import static com.amrita.amail.MainActivity.Uid;
import static com.amrita.amail.MainActivity.Username;
import static com.amrita.amail.MainActivity.editor;
import static com.amrita.amail.MainActivity.fromLogin;
import static com.amrita.amail.MainActivity.isLoggged;
import static com.amrita.amail.MainActivity.onresume;
import static com.amrita.amail.kyctest.fromkyc;


public class afterLogin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawer;
private TextView uname,amail;
private String cuname,camail,id;
 public static String cuid,cunm,cml;
 public static boolean logout=false;
View headerView;
private DatabaseReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        if(!fromkyc) {
            cuid = getIntent().getStringExtra("cuid");
            cuname = getIntent().getStringExtra("cuname");
            cunm=cuname;
            camail = getIntent().getStringExtra("camail");
            cml=camail;
        }
        if(onresume)
        {   onresume=false;
            Snackbar.make(findViewById(R.id.fragment_container),"Welcome back "+cuname+" !",Snackbar.LENGTH_LONG).show();
        }
        if(fromLogin)
        {   fromLogin=false;
            Snackbar.make(findViewById(R.id.fragment_container),"Welcome "+cuname+" !",Snackbar.LENGTH_LONG).show();
        }
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);
        dref=FirebaseDatabase.getInstance().getReference("users");
        NavigationView navigationView=findViewById(R.id.nav_view);
         headerView = navigationView.getHeaderView(0);
        uname=(TextView)headerView.findViewById(R.id.nav_username);
        amail=(TextView)headerView.findViewById(R.id.nav_amail);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            uname.setText(cuname);
            amail.setText(camail);
            if(fromkyc)
            {   fromkyc=false;
                Snackbar snackbar=Snackbar.make(findViewById(R.id.drawer_layout),"KYC status updated",Snackbar.LENGTH_INDEFINITE).setAction("VIEW", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new kyc_fragment()).commit();
                    }
                });
                snackbar.setActionTextColor(Color.argb(255,86,120,248));
                snackbar.setDuration(4000);
                snackbar.show();

            }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new inbox_fragment()).commit();

    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
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

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        { case R.id.compose:
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new send_fragment()).commit();
            break;
            case R.id.drafts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new drafts_fragment()).commit();
                break;
            case R.id.inbox:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new inbox_fragment()).commit();
                break;
            case R.id.personal:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Personal_info_fragment()).commit();
                break;
            case R.id.logout:
                logout=true;
                openIntent();
                break;
            case R.id.contacts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Contacts_fragment()).commit();
                break;
            case R.id.kyc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new kyc_fragment(),"kyc").commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private  void openIntent()
    {
        editor.remove(Username);
        editor.remove(Name);
        editor.remove(Uid);
        editor.putBoolean(isLoggged,false);
        editor.commit();
        Intent a=new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


}
