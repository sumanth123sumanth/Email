package com.amrita.amail;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class kyctest extends AppCompatActivity {
    private Button scan,kycberbt;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static  String uid="",name="",gender="",yearOfBirth="",careOf="",villageTehsil="",postOffice="",district="",state="",postCode="";
    private TextView tvuid,tvname,tvdob,tvstate,tvpm;
    DatabaseReference kcr;
    String or_name="",or_addr="",or_dob="";
    ProgressDialog progress;
    Handler handler;
    Intent i;
    static boolean fromkyc=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyctest);
        scan=(Button)findViewById(R.id.bt_scan);
        tvdob=(TextView)findViewById(R.id.tv_scan_dob);tvdob.setVisibility(View.INVISIBLE);
        tvname=(TextView)findViewById(R.id.tv_scan_name);tvname.setVisibility(View.INVISIBLE);
        tvstate=(TextView)findViewById(R.id.tv_scan_state);tvstate.setVisibility(View.INVISIBLE);
        tvuid=(TextView)findViewById(R.id.tv_scan_uid);tvuid.setVisibility(View.INVISIBLE);
        tvpm=(TextView)findViewById(R.id.tv_scan_prompt);
        tvpm.setText("Scan aadhar QR code to display details");
        kycberbt=(Button)findViewById(R.id.bt_verifykycdetails);
        kycberbt.setVisibility(View.INVISIBLE);
        kcr=FirebaseDatabase.getInstance().getReference("users").child(cuid);
        i=new Intent(this,afterLogin.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    public void scannow( View view){
        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a Aadharcard QR Code");
        integrator.setResultDisplayDuration(500);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        scan.setVisibility(View.INVISIBLE);
        kycberbt.setVisibility(View.VISIBLE);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // process received data
            if(scanContent != null && !scanContent.isEmpty()){
                tvpm.setText("Scanned data from aadhar card");
                processScannedData(scanContent);
            }else{
                tvpm.setText("Scan aadhar QR code to display details");
                Toast toast = Toast.makeText(getApplicationContext(),"Scan Cancelled", Toast.LENGTH_SHORT);
                toast.show();
                kycberbt.setVisibility(View.INVISIBLE);
                scan.setVisibility(View.VISIBLE);
            }

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
            tvpm.setText("Scan aadhar QR code to display details");
            kycberbt.setVisibility(View.INVISIBLE);
            scan.setVisibility(View.VISIBLE);
        }
    }
    protected void processScannedData(String scanData){
        Log.d("Rajdeol",scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol","Start document");
                } else if(eventType == XmlPullParser.START_TAG && "PrintLetterBarcodeData".equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,"uid");
                    //name
                    name = parser.getAttributeValue(null,"name");
                    //gender
                    gender = parser.getAttributeValue(null,"gender");
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null,"dob");
                    // care of
                    careOf = parser.getAttributeValue(null,"co");
                    // village Tehsil
                    villageTehsil = parser.getAttributeValue(null,"vtc");
                    // Post Office
                    postOffice = parser.getAttributeValue(null,"po");
                    // district
                    district = parser.getAttributeValue(null,"dist");
                    // state
                    state = parser.getAttributeValue(null,"state");
                    // Post Code
                    postCode = parser.getAttributeValue(null,"pc");

                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol","End tag "+parser.getName());

                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol","Text "+parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

            // display the data on screen
            displayScannedData();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void  displayScannedData()
    {   tvuid.setVisibility(View.VISIBLE);
        tvstate.setVisibility(View.VISIBLE);
        tvname.setVisibility(View.VISIBLE);
        tvdob.setVisibility(View.VISIBLE);
        tvstate.setText(state);
        tvuid.setText(uid);
        tvname.setText(name);
        tvdob.setText(yearOfBirth);

    }
    public void verifykycdet(View view)
    {   showdialog();
        kcr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        or_name=dataSnapshot.child("name").getValue().toString();
                        or_dob=dataSnapshot.child("dob").getValue().toString();
                        or_addr=dataSnapshot.child("address").getValue().toString();
                if(name.equals(or_name) && state.equals(or_addr) && yearOfBirth.equals(or_dob))
                {   canceldialog();
                    kcr.child("is_kyc").setValue("true");
                    kcr.child("kyc").child("name").setValue(name);
                    kcr.child("kyc").child("uid").setValue(uid);
                    kcr.child("kyc").child("dob").setValue(yearOfBirth);
                    kcr.child("kyc").child("state").setValue(state);
                    fromkyc=true;
                   startActivity(i);
                }
                else {
                    canceldialog();
                    Toast.makeText(getApplicationContext(),"Details not matching",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private  void  showdialog()
    {    progress = new ProgressDialog(this);

        progress.setTitle("Verifying KYC details...");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    private  void  canceldialog(){
        progress.dismiss();
    }
    private  void  open()
    {
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        fromkyc=true;
        startActivity(i);
    }
}
