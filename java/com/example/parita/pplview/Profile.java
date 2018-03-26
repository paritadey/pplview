package com.example.parita.pplview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    String actualmail;
    String finalResult ;
    ProgressDialog progressDialog;
    String HttpURL="https://paritasampa95.000webhostapp.com/PeopleView/UserpostfromApp.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    EditText heading,description;
    TextView etUsername,share, date, anoname;
    CheckBox support, against;
    String EmailHolder, myemail, frominternational, fromnational, myview;
    Button post;
    String postheading, postdescription, postdate;
    boolean checkedittext, checkemail;
    String checkmydevice, anonymous, deviceid;
    View view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        etUsername=(TextView)findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra("email");
        etUsername.setText(EmailHolder);
        myemail= etUsername.getText().toString().trim();


        anoname=(TextView)findViewById(R.id.anoname);
        anonymous = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        anoname.setText(anonymous);
        deviceid=anoname.getText().toString().trim();
        checkmydevice=deviceid;


        frominternational=intent.getStringExtra("email");
    //    fromworld.setText(frominternational);
        fromnational=intent.getStringExtra("email");
        //fromcon.setText(fromnational);

        date=(TextView)findViewById(R.id.date);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        date.setText(filename);

        support=(CheckBox)findViewById(R.id.support);
        support.setOnClickListener(this);
        against=(CheckBox)findViewById(R.id.against);
        against.setOnClickListener(this);

        //Bottom Navigation Bar and its functionalities
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.international:
                                Intent international=new Intent(Profile.this, International.class);
                                startActivity(international);
                                return true;
                            case R.id.national:
                                Intent nation=new Intent(Profile.this, National.class);
                                startActivity(nation);
                                return true;
                            case R.id.profile:
                                item.setIcon(R.drawable.pressme);
                        }
                        return true;
                    }
                });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                CheckUser();
                if(checkedittext || checkemail){
                    UserAppPostFunction(actualmail,postheading,postdescription,myview,postdate);
                }
                else {
                    Toast.makeText(Profile.this, "Please fill all form fields correctly.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(checkedittext) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, postheading + "\n" + postdescription + "\n" + myview);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void checkemail(){
        if(myemail.length()>0) {

            checkemail=true;
            actualmail=etUsername.getText().toString().trim();
         //   Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_SHORT).show();
        }
        else if(frominternational.length()>0){
            checkemail=true;
            actualmail=frominternational;
           // Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_SHORT).show();
        }
        else if(fromnational.length()>0 ){
            checkemail=true;
            actualmail=fromnational;
            //Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_SHORT).show();
        }
        else checkemail=false;
    }

    public void CheckUser(){
        if(myemail.length()==0 && frominternational.length()==0 && fromnational.length()==0 && deviceid.length()>1){
            actualmail=checkmydevice;
            //Toast.makeText(getApplicationContext(), actualmail+"\n"+checkmydevice,Toast.LENGTH_LONG).show();
        }
        else if(myemail.length()>1 || fromnational.length()==0 || frominternational.length()==0 && deviceid.length()==0) {
            actualmail = myemail;
          //  Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_LONG).show();
        }
        else if(myemail.length()==0 || frominternational.length()>1 || fromnational.length()==0 && deviceid.length()==0) {
            actualmail = frominternational;
            //Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_LONG).show();
        }
       else if(myemail.length()==0 || frominternational.length()==0 || fromnational.length()>1 && deviceid.length()==0) {
            actualmail = fromnational;
            //Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_LONG).show();
        }
        else if(myemail.length()>1 || frominternational.length()==0 || fromnational.length()==0 && deviceid.length()>1){
            actualmail=myemail;
           // Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_LONG).show();
        }
        else if(myemail.length()==0 ||fromnational.length()==0 || frominternational.length()>1 && deviceid.length()>1){
            actualmail=frominternational;
           // Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_LONG).show();
        }
        else if(myemail.length()==0 || fromnational.length()>1 || frominternational.length()==0 && deviceid.length()>1){
            actualmail=fromnational;
           // Toast.makeText(getApplicationContext(), actualmail,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(support.isChecked()){
            myview=support.getText().toString().trim();
          //  Toast.makeText(getApplicationContext(), myview, Toast.LENGTH_LONG).show();
            against.setEnabled(false);
        }
        if(against.isChecked()){
            myview=against.getText().toString().trim();
            //Toast.makeText(getApplicationContext(), myview, Toast.LENGTH_LONG).show();
            support.setEnabled(false);
        }
    }


    public void CheckEditTextIsEmptyOrNot(){
        postheading=heading.getText().toString().trim();
        postdescription=description.getText().toString().trim();
        postdate=date.getText().toString().trim();

        checkemail();
        onClick(view1);
        if(TextUtils.isEmpty(postheading)|| TextUtils.isEmpty(postdescription) || TextUtils.isEmpty(postdate)){
            checkedittext=false;
        }
        else
            checkedittext=true;
    }
    public void clearTextFields(){
        heading.setText("");
        description.setText("");
        support.setChecked(false);
        against.setChecked(false);

    }
    public void init(){
        heading=(EditText)findViewById(R.id.heading);
        description=(EditText)findViewById(R.id.description);

        share=(TextView)findViewById(R.id.share);

        post=(Button)findViewById(R.id.post);
    }
    public void UserAppPostFunction(final String email,  final String heading, final String description,
                                    final String view, final String date){

        class UserAppPostFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(Profile.this,"Uploading...",null,true,true);
            }



            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("heading",params[1]);

                hashMap.put("description",params[2]);

                hashMap.put("view", params[3]);

                hashMap.put("date",params[4]);


                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), httpResponseMsg, Toast.LENGTH_LONG).show();

            }
        }

        UserAppPostFunctionClass userAppPostFunctionClass = new UserAppPostFunctionClass();

        userAppPostFunctionClass.execute(email,heading, description, view, date);
    }
}
