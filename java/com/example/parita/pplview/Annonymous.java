package com.example.parita.pplview;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class Annonymous extends AppCompatActivity {

    String senddevid;
    TextView tvdid, dateset, loginlink, date;
    EditText place,password, retypepassword;
    Spinner gender, education_details;
    Button register;
    String agender, aedudetails, adateset, aplace, apassword, aretypepass, adeviceid;
    DatePickerDialog.OnDateSetListener datePickerListener;
    private Calendar myCalendar = Calendar.getInstance();
    Boolean CheckEditText,passwordmatch ;
    ProgressDialog progressDialog;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String HttpURL = "https://paritasampa95.000webhostapp.com/PeopleView/anonymous.php";
    String chk_gender="Choose Gender", chk_details="Choose Educational Details";
    Context apContext, ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonymous);
        init();
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        //displaying id in textview
        TextView deviceid=(TextView)findViewById(R.id.deviceid);
        deviceid.setText(id);
        adeviceid=deviceid.getText().toString().trim();
        deviceid.setVisibility(View.INVISIBLE);

        tvdid=(TextView)findViewById(R.id.textViewdeviceid);
        tvdid.setVisibility(View.INVISIBLE);

        datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateset.setText(sdf.format(myCalendar.getTime()));
            }

        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Annonymous.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(Annonymous.this, AnonymousLogin.class);
                startActivity(login);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                Checkpassword();
                if (CheckEditText && passwordmatch) {

                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    UpdateAnonymousFunction(adeviceid,adateset, agender, aplace, aedudetails, apassword);


                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(Annonymous.this, "Please fill all form fields correctly.",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    //if any Anonymous User wants to update his details
    public void UpdateAnonymousFunction(final String deviceid,  final String dob, final String gender,final String address,
                                     final String details, final String password){

        class UpdateAnonymousFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(Annonymous.this,"Updating...",null,true,true);
            }



            @Override
            protected String doInBackground(String... params) {

                hashMap.put("deviceid",params[0]);

                hashMap.put("dob",params[1]);

                hashMap.put("gender",params[2]);

                hashMap.put("address", params[3]);

                hashMap.put("details", params[4]);

                hashMap.put("password",params[5]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                isInternet(ctx);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server Result: "+httpResponseMsg, Toast.LENGTH_LONG).show();
                clearTextFields();
            }
        }

        UpdateAnonymousFunctionClass anonymousFunctionClass = new UpdateAnonymousFunctionClass();

        anonymousFunctionClass.execute(deviceid,dob,gender, address,details,password);
    }


    public void CheckEditTextIsEmptyOrNot(){
        agender=gender.getSelectedItem().toString().trim();
        aedudetails=education_details.getSelectedItem().toString().trim();
        adateset=dateset.getText().toString().trim();
        aplace=place.getText().toString().trim();
        apassword=password.getText().toString().trim();
        aretypepass=retypepassword.getText().toString().trim();

        if(TextUtils.isEmpty(adateset) || TextUtils.isEmpty(agender) || TextUtils.isEmpty(aplace)
                || TextUtils.isEmpty(aedudetails) || TextUtils.isEmpty(apassword) || TextUtils.isEmpty(aretypepass))
        {
            CheckEditText = false;
        }
        else {
            if(agender.equals(chk_gender) || aedudetails.equals(chk_details)){
                CheckEditText = false ;
            }
            else {
                CheckEditText = true;
            }
        }
    }
    public void Checkpassword(){
        if(password.getText().toString().equals(retypepassword.getText().toString()) && password.length()>0 && retypepassword.length()>0){
            passwordmatch=true;
            //Toast is the pop up message
            Toast.makeText(getApplicationContext(), "Password match",
                    Toast.LENGTH_LONG).show();
        }
        else{
            passwordmatch = false;
            //Toast is the pop up message
            Toast.makeText(getApplicationContext(), "Password does not match!",
                    Toast.LENGTH_LONG).show();

        }
    }
    //check whether the application is connected to the internet or not
    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
            Toast.makeText(Annonymous.this,"Connect to the Wifi/Mobile Data",Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;

    }

    private boolean isInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(getApplicationContext(), "Updatation of Anonymous Details cannot be possible without Internet",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else{
            return true;
        }


    }
    public void clearTextFields(){
        dateset.setText("");
        place.setText("");
        password.setText("");
        retypepassword.setText("");
        education_details.setSelection(0);
        gender.setSelection(0);
    }
    public void init(){
        date=(TextView)findViewById(R.id.date);
        dateset=(TextView)findViewById(R.id.dateset);
        loginlink=(TextView)findViewById(R.id.loginlink);
        place=(EditText)findViewById(R.id.place);
        password=(EditText)findViewById(R.id.password);
        retypepassword=(EditText)findViewById(R.id.retypepassword);
        register=(Button)findViewById(R.id.register);

        education_details=(Spinner)findViewById(R.id.education_details);
        ArrayAdapter<CharSequence> detailsadaptar = ArrayAdapter.createFromResource(this, R.array.edu_details,
                android.R.layout.simple_spinner_item);
        detailsadaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education_details.setAdapter(detailsadaptar);

        gender=(Spinner)findViewById(R.id.gender);
        ArrayAdapter<CharSequence> genadaptar = ArrayAdapter.createFromResource(this, R.array.gen,
                android.R.layout.simple_spinner_item);
        genadaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genadaptar);
    }
}
